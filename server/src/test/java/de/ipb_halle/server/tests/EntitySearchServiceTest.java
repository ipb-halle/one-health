package de.ipb_halle.server.tests;
import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.interfaces.IEntityRepository;
import de.ipb_halle.server.data.interfaces.IEntitySearchRepository;
import de.ipb_halle.server.services.EntitySearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntitySearchServiceTest {


    @Mock
    private IEntitySearchRepository entitySearchRepository;

    @Mock
    private IEntityRepository entityRepository;

    @InjectMocks
    private EntitySearchService service;

    @Test
    void findEntities_sqlInjection_throwsRuntimeException() {
        String malicious = "' OR 1=1 --";
        assertThatThrownBy(() -> service.FindEntities(malicious))
                .isInstanceOf(RuntimeException.class);

        verifyNoInteractions(entitySearchRepository, entityRepository);
    }
    @Test
    void findEntities_normalQuery_delegatesToRepository(){
        String query = "glucose";
        List<String> mockIds = List.of("id_1","id_2");
        List<EntityDTO> mockResult = List.of(new EntityDTO("id_1",null,null), new EntityDTO("id_2",null,null));

        when(entitySearchRepository.FindMatchingEntityIds(query)).thenReturn(mockIds);
        when(entityRepository.GetNodes(mockIds)).thenReturn(mockResult);

        List<EntityDTO> result = service.FindEntities(query);
        assertThat(result).isEqualTo(mockResult);

        verify(entitySearchRepository, times(1)).FindMatchingEntityIds(query);
        verify(entityRepository).GetNodes(mockIds);
    }

    @Test
    void findEntities_validSmilesQuery_addsInchi(){

        String query = "CCO";
        String inchi = "InChI=1S/C2H6O/c1-2-3/h3H,2H2,1H3";

        List<String> mockIdsWithoutSmiles = new ArrayList<>(List.of("id_1"));
        List<String> mockIdsWithSmiles = new ArrayList<>(List.of("id_2"));
        List<EntityDTO> mockResult = List.of(new EntityDTO("id_1",null,null), new EntityDTO("id_2",null,null));

        when(entitySearchRepository.FindMatchingEntityIds(query)).thenReturn(mockIdsWithoutSmiles);
        when(entitySearchRepository.FindMatchingEntityIds(inchi)).thenReturn(mockIdsWithSmiles);
        when(entityRepository.GetNodes(List.of("id_1","id_2"))).thenReturn(mockResult);

        List<EntityDTO> result = service.FindEntities(query);
        assertThat(result).isEqualTo(mockResult);

        verify(entitySearchRepository).FindMatchingEntityIds(query);
        verify(entitySearchRepository).FindMatchingEntityIds(inchi);
        verify(entityRepository).GetNodes(List.of("id_1","id_2"));
        verifyNoMoreInteractions(entitySearchRepository);
    }
    @Test
    void findEntities_removesDistinctIds(){

        String query = "glucose";

        List<String> mockId = new ArrayList<>(List.of("id_1", "id_1"));
        List<EntityDTO> mockResult = List.of(new EntityDTO("id_1",null,null));


        when(entitySearchRepository.FindMatchingEntityIds(query)).thenReturn(mockId);
        when(entityRepository.GetNodes(List.of("id_1"))).thenReturn(mockResult);

        List<EntityDTO> result = service.FindEntities(query);
        assertThat(result).isEqualTo(mockResult);

        verify(entitySearchRepository, times(1)).FindMatchingEntityIds(any());
        verify(entityRepository).GetNodes(List.of("id_1"));
        verifyNoMoreInteractions(entitySearchRepository);
    }

    @Test
    void findEntities_emptyRepositoryResult_returnsEmptyList() {
        String query = "unknown";
        when(entitySearchRepository.FindMatchingEntityIds(query)).thenReturn(List.of());
        when(entityRepository.GetNodes(any())).thenReturn(List.of());
        assertThat(service.FindEntities(query)).isEmpty();
    }
}
