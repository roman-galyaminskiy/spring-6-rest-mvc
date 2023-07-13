package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository repository;

    @Test
    void testSaveCustomer() {
        Customer saved = repository.save(Customer.builder().build());

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVersion()).isEqualTo(0);
    }
}