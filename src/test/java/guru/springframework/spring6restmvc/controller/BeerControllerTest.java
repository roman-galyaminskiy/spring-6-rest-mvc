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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
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
class BeerControllerTest {

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
                .upc("486728976186178")
                .price(BigDecimal.ONE)
                .build());
        beerDTOList.add(BeerDTO.builder()
                .id(beerId2)
                .beerName("beer2")
                .beerStyle(BeerStyle.LAGER)
                .upc("4153752453375637")
                .price(BigDecimal.ONE)
                .build());

        objectMapper.findAndRegisterModules();
    }

    @Test
    void listBeers() throws Exception {
        // when
        when(beerService.listBeers(any(), any(), any(), any())).thenReturn(new PageImpl(beerDTOList));

        // then
        mockMvc.perform(
                        get(BEER_API_URL)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].id", is(beerDTOList.get(0).getId().toString())))
                .andExpect(jsonPath("$.content.[1].id", is(beerDTOList.get(1).getId().toString())));
    }

    @Test
    void getBeerById() throws Exception {

        // when
        when(beerService.getBeerById(any())).thenReturn(Optional.of(beerDTOList.get(0)));

        // then
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

        // then
        mockMvc.perform(
                        post(BEER_API_URL + "/new")
                                .content(objectMapper.writeValueAsString(beerDTOList.get(0)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BEER_API_URL + "/" + beerDTOList.get(0).getId().toString()));
    }

    @Test
    void createNewBeerNullName() throws Exception {
        BeerDTO dto = BeerDTO.builder()
                .beerStyle(BeerStyle.LAGER)
                .upc("4153752453375637")
                .price(BigDecimal.ONE)
                .build();

        // then
        MvcResult result = mockMvc.perform(
                        post(BEER_API_URL + "/new")
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void createNewBeerBlankName() throws Exception {
        BeerDTO dto = BeerDTO.builder()
                .beerName("")
                .beerStyle(BeerStyle.LAGER)
                .upc("4153752453375637")
                .price(BigDecimal.ONE)
                .build();

        // then
        MvcResult result = mockMvc.perform(
                        post(BEER_API_URL + "/new")
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }



    @Test
    void updateBeerById() throws Exception {
        // when
        when(beerService.updateBeerById(any(), any())).thenReturn(beerDTOList.get(0));

        // then
        mockMvc.perform(
                        patch(BY_BEER_ID_URL, beerDTOList.get(0).getId())
                                .content(objectMapper.writeValueAsString(beerDTOList.get(0)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
        // .andExpect(jsonPath("$.beerName", is(beerDTOList.get(0).getBeerName())))
        // .andExpect(jsonPath("$.beerStyle", is(beerDTOList.get(0).getBeerStyle().toString())));
    }

    @Test
    void replaceBeerById() throws Exception {
        // when
        when(beerService.replaceBeerById(any(), any())).thenReturn(beerDTOList.get(0));

        // then
        mockMvc.perform(
                        put(BY_BEER_ID_URL, beerDTOList.get(0).getId())
                                .content(objectMapper.writeValueAsString(beerDTOList.get(0)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
        // .andExpect(jsonPath("$.beerName", is(beerDTOList.get(0).getBeerName())))
        // .andExpect(jsonPath("$.beerStyle", is(beerDTOList.get(0).getBeerStyle().toString())));
    }

    @Test
    void deleteBeerById() throws Exception {
        // then
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

    // @Test
    // void deleteByBody() throws Exception {
    //     mockMvc.perform(delete(BEER_API_URL)
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(objectMapper.writeValueAsString(BeerDTO.builder().id(UUID.randomUUID()).build())))
    //             .andExpect(status().isOk());
    // }
}