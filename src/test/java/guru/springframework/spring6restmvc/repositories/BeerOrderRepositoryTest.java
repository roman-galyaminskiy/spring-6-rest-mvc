package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.bootstrap.BootstrapData;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.BeerOrderShipment;
import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testBeer = beerRepository.findAll().get(0);
    }

    @Test
    void empty() {
        assertEquals(beerOrderRepository.count(), 0);
    }

    @Transactional
    @Test
    void testBeerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test customer ref")
                .build();

        beerOrder.setBeerOrderShipment(BeerOrderShipment.builder().trackingNumber("test").build());


        beerOrder.setCustomer(testCustomer);

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder); // saveAndFlush

        assertNotNull(savedBeerOrder.getId());
        assertNotNull(savedBeerOrder.getCustomer());
        assertEquals(savedBeerOrder.getCustomerRef(), beerOrder.getCustomerRef());
        assertEquals(savedBeerOrder.getCustomer().getBeerOrders().size(), 1);
        assertNotNull(savedBeerOrder.getBeerOrderShipment().getId());
        assertNotNull(savedBeerOrder.getBeerOrderShipment().getBeerOrder().getId());
    }
}