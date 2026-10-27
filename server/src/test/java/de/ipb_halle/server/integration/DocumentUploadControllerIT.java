package de.ipb_halle.server.integration;

import de.ipb_halle.server.postgre.models.DocumentRecord;
import de.ipb_halle.server.postgre.models.DocumentRecordStatus;
import de.ipb_halle.server.postgre.repositories.DocumentRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DisplayName("DocumentUploadController Integration Tests")
class DocumentUploadControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.flyway.enabled", () -> "false");
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4j::getAdminPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentRecordRepository documentRecordRepository;

    @BeforeEach
    void setUp() {
        // Clean up database records before each test
        documentRecordRepository.deleteAll();
    }

    @Test
    @DisplayName("Should upload valid PDF file and return 202 Accepted")
    @Transactional
    void uploadDocument_validPdf_returns202Accepted() throws Exception {
        // Arrange
        MockMultipartFile pdfFile = new MockMultipartFile(
            "file",
            "document.pdf",
            "application/pdf",
            "PDF content here".getBytes()
        );

        // Act
        MvcResult result = mockMvc.perform(multipart("/api/documents/upload")
                .file(pdfFile)
                .with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.documentId", notNullValue()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.message").value(containsString("uploaded successfully")))
                .andReturn();

        // Assert - Verify database record was created
        Long documentId = Long.parseLong(result.getResponse().getContentAsString()
                .replaceAll(".*\"documentId\":(\\d+).*", "$1"));
        
        DocumentRecord record = documentRecordRepository.findById(documentId).orElse(null);
        assertThat(record).isNotNull();
        assertThat(record.getOriginalFilename()).isEqualTo("document.pdf");
        assertThat(record.getStatus()).isEqualTo(DocumentRecordStatus.PENDING);
        assertThat(record.getFilePath()).isNotNull();
    }

    @Test
    @DisplayName("Should upload valid CSV file and return 202 Accepted")
    @Transactional
    void uploadDocument_validCsv_returns202Accepted() throws Exception {
        // Arrange
        MockMultipartFile csvFile = new MockMultipartFile(
            "file",
            "data.csv",
            "text/csv",
            "col1,col2\nval1,val2".getBytes()
        );

        // Act & Assert
        MvcResult result = mockMvc.perform(multipart("/api/documents/upload")
                .file(csvFile)
                .with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.documentId", notNullValue()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();

        // Verify file was written to disk
        Long documentId = Long.parseLong(result.getResponse().getContentAsString()
                .replaceAll(".*\"documentId\":(\\d+).*", "$1"));
        
        DocumentRecord record = documentRecordRepository.findById(documentId).orElse(null);
        assertThat(record).isNotNull();
        assertThat(record.getFilePath()).isNotNull();
        
        File tempFile = new File(record.getFilePath());
        assertThat(tempFile).exists();
        assertThat(Files.readAllBytes(tempFile.toPath())).contains("col1,col2".getBytes());
    }

    @Test
    @DisplayName("Should upload valid TXT file and return 202 Accepted")
    @Transactional
    void uploadDocument_validTxt_returns202Accepted() throws Exception {
        // Arrange
        MockMultipartFile txtFile = new MockMultipartFile(
            "file",
            "notes.txt",
            "text/plain",
            "Plain text content".getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/documents/upload")
                .file(txtFile)
                .with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.documentId", notNullValue()))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for empty file")
    void uploadDocument_emptyFile_returns400BadRequest() throws Exception {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file",
            "empty.pdf",
            "application/pdf",
            new byte[0]
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/documents/upload")
                .file(emptyFile)
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.documentId").doesNotExist())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value(containsString("empty")));

        // Verify no record was created
        assertThat(documentRecordRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Should return 400 Bad Request for unsupported file type (DOCX)")
    void uploadDocument_invalidFileType_returns400BadRequest() throws Exception {
        // Arrange
        MockMultipartFile docxFile = new MockMultipartFile(
            "file",
            "document.docx",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "DOCX content".getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/documents/upload")
                .file(docxFile)
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value(containsString("File type not allowed")))
                .andExpect(jsonPath("$.message").value(containsString("PDF, CSV, TXT")));

        // Verify no record was created
        assertThat(documentRecordRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Should return 400 Bad Request for JSON file")
    void uploadDocument_jsonFile_returns400BadRequest() throws Exception {
        // Arrange
        MockMultipartFile jsonFile = new MockMultipartFile(
            "file",
            "data.json",
            "application/json",
            "{\"key\": \"value\"}".getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/documents/upload")
                .file(jsonFile)
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"));

        assertThat(documentRecordRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Should return 400 Bad Request when file parameter is missing")
    void uploadDocument_missingFileParameter_returns400BadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(multipart("/api/documents/upload")
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should create multiple document records independently")
    @Transactional
    void uploadDocument_multipleFiles_createsIndependentRecords() throws Exception {
        // Arrange
        MockMultipartFile file1 = new MockMultipartFile(
            "file", "doc1.pdf", "application/pdf", "Content 1".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
            "file", "doc2.csv", "text/csv", "Content 2".getBytes()
        );

        // Act
        mockMvc.perform(multipart("/api/documents/upload").file(file1).with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.documentId", notNullValue()));

        mockMvc.perform(multipart("/api/documents/upload").file(file2).with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.documentId", notNullValue()));

        // Assert
        assertThat(documentRecordRepository.findAll())
                .hasSize(2)
                .allMatch(r -> r.getStatus().equals(DocumentRecordStatus.PENDING));

        assertThat(documentRecordRepository.findByStatus(DocumentRecordStatus.PENDING))
                .hasSize(2);
    }

    @Test
    @DisplayName("Should preserve original filename in database record")
    @Transactional
    void uploadDocument_preservesOriginalFilename() throws Exception {
        // Arrange
        String originalFilename = "my-important-document.pdf";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            originalFilename,
            "application/pdf",
            "Content".getBytes()
        );

        // Act
        mockMvc.perform(multipart("/api/documents/upload").file(file).with(csrf()))
                .andExpect(status().isAccepted());

        // Assert
        DocumentRecord record = documentRecordRepository.findAll().get(0);
        assertThat(record.getOriginalFilename()).isEqualTo(originalFilename);
    }

    @Test
    @DisplayName("Should write file to temporary directory on disk")
    @Transactional
    void uploadDocument_writesFileToDisk() throws Exception {
        // Arrange
        byte[] fileContent = "Test file content for disk verification".getBytes();
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            fileContent
        );

        // Act
        MvcResult result = mockMvc.perform(multipart("/api/documents/upload")
                .file(file)
                .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        // Extract document ID from response
        Long documentId = Long.parseLong(result.getResponse().getContentAsString()
                .replaceAll(".*\"documentId\":(\\d+).*", "$1"));

        // Assert - Verify file exists on disk with correct content
        DocumentRecord record = documentRecordRepository.findById(documentId).orElse(null);
        assertThat(record).isNotNull();
        assertThat(record.getFilePath()).isNotNull();

        Path filePath = Paths.get(record.getFilePath());
        assertThat(filePath).exists();
        assertThat(Files.readAllBytes(filePath)).isEqualTo(fileContent);

        // Cleanup
        Files.delete(filePath);
    }

    @Test
    @DisplayName("Should set correct timestamps on record creation")
    @Transactional
    void uploadDocument_setsTimestamps() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.pdf",
            "application/pdf",
            "Content".getBytes()
        );

        // Act
        mockMvc.perform(multipart("/api/documents/upload").file(file).with(csrf()))
                .andExpect(status().isAccepted());

        // Assert
        DocumentRecord record = documentRecordRepository.findAll().get(0);
        assertThat(record.getCreatedAt()).isNotNull();
        assertThat(record.getUpdatedAt()).isNotNull();
        assertThat(record.getCreatedAt()).isBeforeOrEqualTo(record.getUpdatedAt());
    }

    @Test
    @DisplayName("Should return proper error response structure for validation errors")
    void uploadDocument_validationError_returnsProperErrorStructure() throws Exception {
        // Arrange
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file",
            "test.exe",
            "application/x-msdownload",
            "Invalid".getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/documents/upload").file(invalidFile).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.documentId").doesNotExist())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").isString());
    }
}
