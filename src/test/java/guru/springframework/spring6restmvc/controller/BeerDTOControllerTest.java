package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.springframework.spring6restmvc.controller.BeerController.BEER_API_URL;
import static guru.springframework.spring6restmvc.controller.BeerController.BEER_ID;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerDTOControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BeerService beerService;

    private List<BeerDTO> beerDTOList;

    public static final String BY_BEER_ID_URL = BEER_API_URL + BEER_ID;

    @BeforeEach
    void setUp() {
        var beerId1 = UUID.randomUUID();
        var beerId2 = UUID.randomUUID();

        beerDTOList = new ArrayList<>();
        beerDTOList.add(BeerDTO.builder()
                .id(beerId1)
                .beerName("beer1")
                .beerStyle(BeerStyle.LAGER)
                .build());
        beerDTOList.add(BeerDTO.builder()
                .id(beerId2)
                .beerName("beer2")
                .beerStyle(BeerStyle.LAGER)
                .build());

        objectMapper.findAndRegisterModules();
    }

    @Test
    void listBeers() throws Exception {
        // when
        when(beerService.listBeers()).thenReturn(beerDTOList);

        // assert
        mockMvc.perform(
                        get(BEER_API_URL)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(beerDTOList.get(0).getId().toString())))
                .andExpect(jsonPath("$.[1].id", is(beerDTOList.get(1).getId().toString())));
    }

    @Test
    void getBeerById() throws Exception {

        // when
        when(beerService.getBeerById(any())).thenReturn(Optional.of(beerDTOList.get(0)));

        // assert
        mockMvc.perform(
                        get(BY_BEER_ID_URL, beerDTOList.get(0).getId())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(beerDTOList.get(0).getId().toString())));

    }

    @Test
    void createNewBeer() throws Exception {
        // when
        when(beerService.save(any())).thenReturn(beerDTOList.get(0));

        // assert
        mockMvc.perform(
                        post(BEER_API_URL + "/new")
                                .content(objectMapper.writeValueAsString(beerDTOList.get(0)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(is(beerDTOList.get(0).getId().toString())));
    }

    @Test
    void updateBeerById() throws Exception {
        // when
        when(beerService.updateBeerById(any(), any())).thenReturn(beerDTOList.get(0));

        // assert
        mockMvc.perform(
                        patch(BY_BEER_ID_URL, beerDTOList.get(0).getId())
                                .content(objectMapper.writeValueAsString(beerDTOList.get(0)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beerName", is(beerDTOList.get(0).getBeerName())))
                .andExpect(jsonPath("$.beerStyle", is(beerDTOList.get(0).getBeerStyle().toString())));
    }

    @Test
    void replaceBeerById() throws Exception {
        // when
        when(beerService.replaceBeerById(any(), any())).thenReturn(beerDTOList.get(0));

        // assert
        mockMvc.perform(
                        put(BY_BEER_ID_URL, beerDTOList.get(0).getId())
                                .content(objectMapper.writeValueAsString(beerDTOList.get(0)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beerName", is(beerDTOList.get(0).getBeerName())))
                .andExpect(jsonPath("$.beerStyle", is(beerDTOList.get(0).getBeerStyle().toString())));
    }

    @Test
    void deleteBeerById() throws Exception {
        // assert
        mockMvc.perform(delete(BY_BEER_ID_URL, beerDTOList.get(0).getId()))
                .andExpect(status().isNoContent());

        verify(beerService, times(1)).deleteById(any());
    }

    @Test
    void handleBeerNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get(BY_BEER_ID_URL, beerDTOList.get(0).getId()))
                .andExpect(status().isNotFound());
    }
}