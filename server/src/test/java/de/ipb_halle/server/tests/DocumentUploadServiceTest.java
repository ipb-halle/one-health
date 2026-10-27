package de.ipb_halle.server.tests;

import de.ipb_halle.server.postgre.models.DocumentRecord;
import de.ipb_halle.server.postgre.models.DocumentRecordStatus;
import de.ipb_halle.server.postgre.repositories.DocumentRecordRepository;
import de.ipb_halle.server.services.DocumentUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DocumentUploadService Unit Tests")
class DocumentUploadServiceTest {

    @Mock
    private DocumentRecordRepository documentRecordRepository;

    @InjectMocks
    private DocumentUploadService documentUploadService;

    private MultipartFile validPdfFile;
    private MultipartFile validCsvFile;
    private MultipartFile validTxtFile;
    private MultipartFile emptyFile;
    private MultipartFile invalidMimeTypeFile;

    @BeforeEach
    void setUp() {
        validPdfFile = createMockMultipartFile("test.pdf", "application/pdf", "PDF content");
        validCsvFile = createMockMultipartFile("data.csv", "text/csv", "col1,col2\nval1,val2");
        validTxtFile = createMockMultipartFile("notes.txt", "text/plain", "Plain text content");
        emptyFile = createMockMultipartFile("empty.pdf", "application/pdf", "");
        invalidMimeTypeFile = createMockMultipartFile("document.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "DOCX content");
    }

    @Test
    @DisplayName("Should successfully upload valid PDF file and create PENDING record")
    void uploadFile_validPdf_createsRecordWithPendingStatus() throws IOException {
        // Arrange
        DocumentRecord savedRecord = new DocumentRecord("test.pdf", "/tmp/doc_abc123.pdf", DocumentRecordStatus.PENDING);
        savedRecord.setId(1L);
        when(documentRecordRepository.save(any(DocumentRecord.class))).thenReturn(savedRecord);

        // Act
        DocumentRecord result = documentUploadService.uploadFile(validPdfFile);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getOriginalFilename()).isEqualTo("test.pdf");
        assertThat(result.getStatus()).isEqualTo(DocumentRecordStatus.PENDING);
        assertThat(result.getFilePath()).isNotNull();
        
        ArgumentCaptor<DocumentRecord> captor = ArgumentCaptor.forClass(DocumentRecord.class);
        verify(documentRecordRepository, times(1)).save(captor.capture());
        
        DocumentRecord capturedRecord = captor.getValue();
        assertThat(capturedRecord.getStatus()).isEqualTo(DocumentRecordStatus.PENDING);
        assertThat(capturedRecord.getOriginalFilename()).isEqualTo("test.pdf");
    }

    @Test
    @DisplayName("Should successfully upload valid CSV file")
    void uploadFile_validCsv_createsRecord() throws IOException {
        // Arrange
        DocumentRecord savedRecord = new DocumentRecord("data.csv", "/tmp/doc_def456.csv", DocumentRecordStatus.PENDING);
        savedRecord.setId(2L);
        when(documentRecordRepository.save(any(DocumentRecord.class))).thenReturn(savedRecord);

        // Act
        DocumentRecord result = documentUploadService.uploadFile(validCsvFile);

        // Assert
        assertThat(result.getOriginalFilename()).isEqualTo("data.csv");
        assertThat(result.getStatus()).isEqualTo(DocumentRecordStatus.PENDING);
        verify(documentRecordRepository).save(any(DocumentRecord.class));
    }

    @Test
    @DisplayName("Should successfully upload valid TXT file")
    void uploadFile_validTxt_createsRecord() throws IOException {
        // Arrange
        DocumentRecord savedRecord = new DocumentRecord("notes.txt", "/tmp/doc_ghi789.txt", DocumentRecordStatus.PENDING);
        savedRecord.setId(3L);
        when(documentRecordRepository.save(any(DocumentRecord.class))).thenReturn(savedRecord);

        // Act
        DocumentRecord result = documentUploadService.uploadFile(validTxtFile);

        // Assert
        assertThat(result.getOriginalFilename()).isEqualTo("notes.txt");
        verify(documentRecordRepository).save(any(DocumentRecord.class));
    }

    @Test
    @DisplayName("Should reject empty file with IllegalArgumentException")
    void uploadFile_emptyFile_throwsIllegalArgumentException() {
        // Act & Assert
        assertThatThrownBy(() -> documentUploadService.uploadFile(emptyFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("File cannot be empty");

        verify(documentRecordRepository, never()).save(any(DocumentRecord.class));
    }

    @Test
    @DisplayName("Should reject file with invalid MIME type")
    void uploadFile_invalidMimeType_throwsIllegalArgumentException() {
        // Act & Assert
        assertThatThrownBy(() -> documentUploadService.uploadFile(invalidMimeTypeFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("File type not allowed")
                .hasMessageContaining("PDF, CSV, TXT");

        verify(documentRecordRepository, never()).save(any(DocumentRecord.class));
    }

    @Test
    @DisplayName("Should reject file with null MIME type")
    void uploadFile_nullMimeType_throwsIllegalArgumentException() {
        // Arrange
        MultipartFile nullMimeTypeFile = createMockMultipartFile("test.pdf", null, "content");

        // Act & Assert
        assertThatThrownBy(() -> documentUploadService.uploadFile(nullMimeTypeFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("File type not allowed");

        verify(documentRecordRepository, never()).save(any(DocumentRecord.class));
    }

    @Test
    @DisplayName("Should create and validate temporary file path")
    void uploadFile_streamsFileToTempDirectory() throws IOException {
        // Arrange
        DocumentRecord savedRecord = new DocumentRecord("test.pdf", "/tmp/doc_xyz999.pdf", DocumentRecordStatus.PENDING);
        savedRecord.setId(5L);
        when(documentRecordRepository.save(any(DocumentRecord.class))).thenReturn(savedRecord);

        // Act
        DocumentRecord result = documentUploadService.uploadFile(validPdfFile);

        // Assert
        assertThat(result.getFilePath()).isNotNull();
        assertThat(result.getFilePath()).isNotEmpty();
        
        ArgumentCaptor<DocumentRecord> captor = ArgumentCaptor.forClass(DocumentRecord.class);
        verify(documentRecordRepository).save(captor.capture());
        
        String filePath = captor.getValue().getFilePath();
        assertThat(filePath).isNotNull();
        assertThat(filePath).contains("doc_"); // Verify temp file prefix is used
    }

    @Test
    @DisplayName("Should preserve file extension in temporary file path")
    void uploadFile_preservesFileExtension() throws IOException {
        // Arrange
        DocumentRecord savedPdfRecord = new DocumentRecord("test.pdf", "/tmp/doc_abc.pdf", DocumentRecordStatus.PENDING);
        savedPdfRecord.setId(6L);
        DocumentRecord savedCsvRecord = new DocumentRecord("data.csv", "/tmp/doc_def.csv", DocumentRecordStatus.PENDING);
        savedCsvRecord.setId(7L);
        
        when(documentRecordRepository.save(any(DocumentRecord.class)))
                .thenReturn(savedPdfRecord)
                .thenReturn(savedCsvRecord);

        // Act
        DocumentRecord pdfResult = documentUploadService.uploadFile(validPdfFile);
        DocumentRecord csvResult = documentUploadService.uploadFile(validCsvFile);

        // Assert
        assertThat(pdfResult.getFilePath()).endsWith(".pdf");
        assertThat(csvResult.getFilePath()).endsWith(".csv");
    }

    @Test
    @DisplayName("Should handle IO exception during file streaming")
    void uploadFile_ioExceptionDuringStreaming_propagatesException() throws IOException {
        // Arrange
        MultipartFile ioExceptionFile = mock(MultipartFile.class);
        when(ioExceptionFile.isEmpty()).thenReturn(false);
        when(ioExceptionFile.getContentType()).thenReturn("application/pdf");
        when(ioExceptionFile.getOriginalFilename()).thenReturn("test.pdf");
        doThrow(new IOException("Disk write failed"))
                .when(ioExceptionFile).transferTo(any(java.io.File.class));

        // Act & Assert
        assertThatThrownBy(() -> documentUploadService.uploadFile(ioExceptionFile))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Disk write failed");

        verify(documentRecordRepository, never()).save(any(DocumentRecord.class));
    }

    @Test
    @DisplayName("Should not save record if repository throws exception")
    void uploadFile_repositorySaveThrows_propagatesException() throws IOException {
        // Arrange
        when(documentRecordRepository.save(any(DocumentRecord.class)))
                .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        assertThatThrownBy(() -> documentUploadService.uploadFile(validPdfFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database connection error");
    }

    // Helper method to create real MockMultipartFile
    private MultipartFile createMockMultipartFile(String filename, String contentType, String content) {
        return new MockMultipartFile(
            "file",
            filename,
            contentType,
            content.getBytes(StandardCharsets.UTF_8)
        );
    }
}
