package guru.springframework.spring6restmvc.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerOrderLineRepositoryTest {

    @Autowired
    BeerOrderLineRepository beerOrderLineRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void empty() {
        assertEquals(beerOrderLineRepository.count(), 0);
    }
}