package quest.example.test;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import quest.example.test.Controller.FiremanController;
import quest.example.test.Entity.Fire;
import quest.example.test.Entity.Fireman;
import quest.example.test.Repository.FiremanRepository;

@WebMvcTest(FiremanController.class)
public class ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FiremanRepository firemanRepository;

    @Test
    void testGetVeteranSimple() throws Exception {
        var fireman = mock(Fireman.class);
        when(fireman.getId()).thenReturn(1L);
        when(fireman.getName()).thenReturn("champion");
        when(firemanRepository.getVeteran()).thenReturn(Optional.of(fireman));

        mockMvc.perform(get("/fireman/veteran"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("champion"));
    }

    @Test
    void testGetVeteranNotFound() throws Exception {

        mockMvc.perform(get("/fireman/veteran"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStatsSimple() throws Exception {
    List<Fireman> firemen = new ArrayList<>();
    
    for (int i = 0; i < 5; i++) {  
        Fireman mockFireman = mock(Fireman.class);
        firemen.add(mockFireman);

        List<Fire> fires = new ArrayList<>();
        for (int j = 0; j < 2 ; j++) {  
            Fire mockFire = mock(Fire.class);
            fires.add(mockFire);    
        }

    when(mockFireman.getFires()).thenReturn(fires);    
    }

    when(firemanRepository.findAll()).thenReturn(firemen);

        mockMvc.perform(get("/fireman/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firemenCount").value(5))
                .andExpect(jsonPath("$.firesCount").value(10));
    }

    @Test
    void testFiresNotCountedTwiceInGetStats() throws Exception {
    List<Fireman> firemen = new ArrayList<>();

    List<Fire> firesWithDuplicates = Arrays.asList(mock(Fire.class), mock(Fire.class), mock(Fire.class));
    
    for (int i = 0; i < 5; i++) {  
        Fireman mockFireman = mock(Fireman.class);
        firemen.add(mockFireman);
        when(mockFireman.getFires()).thenReturn(firesWithDuplicates);    
    }

    when(firemanRepository.findAll()).thenReturn(firemen);

        mockMvc.perform(get("/fireman/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firesCount").value(3));
    }
}
