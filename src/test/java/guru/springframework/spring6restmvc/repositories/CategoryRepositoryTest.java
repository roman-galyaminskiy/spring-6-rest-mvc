package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer testBeer;

    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll().get(0);
    }

    @Test
    void empty() {
        assertEquals(categoryRepository.count(), 0);
    }

    @Transactional
    @Test
    void testAddCategory() {
        Category savedCategory = categoryRepository.save(
                Category.builder().description("test").build()
        );

        testBeer.addCategory(savedCategory);

        Beer savedBeer = beerRepository.save(testBeer);

        assertEquals(savedBeer.getCategories().size(), 1);
        assertEquals(savedCategory.getBeers().size(), 1);
    }
}