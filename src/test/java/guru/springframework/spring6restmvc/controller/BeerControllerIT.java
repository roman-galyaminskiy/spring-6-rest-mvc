package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.springframework.spring6restmvc.controller.BeerController.BEER_API_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class BeerControllerIT {

    public static final int NUMBER_OF_BEERS = 2413;
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void empty() {

    }

    @Test
    void testListBeers() {
        List<BeerDTO> dtos = beerController.listBeers();

        assertThat(dtos.size()).isEqualTo(NUMBER_OF_BEERS);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void getBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertNotNull(dto);
    }

    @Test
    void getBeerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void createNewBeer() {
        var dto = new BeerDTO();

        ResponseEntity response = beerController.createNewBeer(dto);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getHeaders().getLocation());

        UUID savedUuid = UUID.fromString(response.getHeaders().getLocation().getPath().split("/")[4]);

        Beer savedBeer = beerRepository.findById(savedUuid).get();

        assertNotNull(savedBeer);
    }

    @Transactional
    @Rollback
    @Test
    void updateBeer() {
        var beer = beerRepository.findAll().get(0);
        var dto = beerMapper.entityToDto(beer);
        dto.setBeerName(null);
        dto.setPrice(BigDecimal.ONE);

        beerController.updateBeerById(beer.getId(), dto);

        Beer savedBeer = beerRepository.findById(beer.getId()).get();

        assertNotNull(savedBeer);
        assertNotNull(savedBeer.getBeerName());
        assertEquals(savedBeer.getPrice(), dto.getPrice());


    }

    @Transactional
    @Rollback
    @Test
    void replaceBeer() {
        var beer = beerRepository.findAll().get(0);
        var dto = beerMapper.entityToDto(beer);
        dto.setBeerName("replaced");

        beerController.replaceBeerById(beer.getId(), dto);

        Beer savedBeer = beerRepository.findById(beer.getId()).get();

        assertNotNull(savedBeer);
        assertEquals(savedBeer.getBeerName(), dto.getBeerName());
    }

    @Transactional
    @Rollback
    @Test
    void deleteBeer() {
        var beer = beerRepository.findAll().get(0);
        beerController.deleteBeerById(beer.getId());

        Optional<Beer> optionalBeer = beerRepository.findById(beer.getId());

        assertTrue(optionalBeer.isEmpty());
    }

    // @Test
    void createNewBeerNotUniqueName() throws Exception {
        BeerDTO dto = BeerDTO.builder()
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.LAGER)
                .upc("4153752453375637")
                .price(BigDecimal.ONE)
                .build();

        // assert
        mockMvc.perform(
                        post(BEER_API_URL + "/new")
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }
}