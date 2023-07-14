package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository repository;

    Beer beer;

    @BeforeEach
    void setUp() {
        beer = repository.save(Beer.builder()
                .beerName("test")
                .beerStyle(BeerStyle.LAGER)
                .upc("1234123412341")
                .price(BigDecimal.ONE)
                .build());
    }

    @Test
    void listBeersWithNameLike() {
        Set<Beer> beer = repository.findAllByBeerNameLike("t%");

        assertThat(beer.size()).isEqualTo(1);
    }

    @Test
    void listBeersWithNameLikeAndStyle() {
        Set<Beer> beer = repository.findAllByBeerNameLikeAndBeerStyle("t%", BeerStyle.LAGER);

        assertThat(beer.size()).isEqualTo(1);
    }

    @Test
    void noBeersWithNameLikeAndStyle() {
        Set<Beer> beer = repository.findAllByBeerNameLikeAndBeerStyle("taaa%", BeerStyle.LAGER);

        assertThat(beer.size()).isEqualTo(0);
    }

    @Transactional
    @Rollback
    @Test
    void testSaveBeer() {
        Beer saved = repository.save(beer);

        repository.flush();

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedDate()).isNotNull();
        assertThat(saved.getUpdateDate()).isNotNull();
        assertThat(saved.getVersion()).isEqualTo(0);
    }

    @Rollback
    @Test
    void updateBeer() throws InterruptedException {
        Beer saved = repository.save(beer);
        repository.flush();
        LocalDateTime firstUpdated = LocalDateTime.from(saved.getUpdateDate());

        beer.setBeerName("updated");
        Thread.sleep(1000);
        repository.save(beer);
        repository.flush();

        Beer updated = repository.findById(beer.getId()).get();
        assertThat(updated.getVersion()).isEqualTo(1);
        assertThat(updated.getUpdateDate()).isAfter(firstUpdated);
    }

    @Transactional
    @Rollback
    @Test
    void testSaveBeerNullName() {
        assertThrows(ConstraintViolationException.class, () -> {
            beer.setBeerName(null);
            Beer saved = repository.save(beer);
            repository.flush();
        });
    }
}