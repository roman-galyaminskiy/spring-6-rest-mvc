package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CustomerControllerIT {

    public static final int NUMBER_OF_CUSTOMERS = 3;
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void getCustomerList() {
        List<CustomerDTO> dtos = customerController.listAllCustomers();

        assertEquals(dtos.size(), NUMBER_OF_CUSTOMERS);
    }

    @Transactional
    @Rollback
    @Test
    void getEmptyCustomerList() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.listAllCustomers();

        assertEquals(dtos.size(), 0);
    }

    @Test
    void getCustomerById() {
        var customer = customerRepository.findAll().get(0);

        var dto = customerController.getCustomerById(customer.getId());

        assertNotNull(dto);
    }

    @Test
    void getCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void createNewCustomer() {
        var dto = CustomerDTO.builder().name("test").build();

        ResponseEntity response = customerController.createNewCustomer(dto);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getHeaders().getLocation());

        UUID savedUuid = UUID.fromString(response.getHeaders().getLocation().getPath().split("/")[4]);

        Customer savedCustomer = customerRepository.findById(savedUuid).get();

        assertNotNull(savedCustomer);
    }

    @Transactional
    @Rollback
    @Test
    void updateCustomer() {
        var customer = customerRepository.findAll().get(0);
        var dto = customerMapper.entityToDto(customer);
        dto.setName(null);

        customerController.updateCustomerById(customer.getId(), dto);

        Customer savedCustomer = customerRepository.findById(customer.getId()).get();

        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getName());
    }

    @Transactional
    @Rollback
    @Test
    void replaceCustomer() {
        var customer = customerRepository.findAll().get(0);
        var dto = customerMapper.entityToDto(customer);
        dto.setName("replaced");

        customerController.replaceCustomerById(customer.getId(), dto);

        Customer savedCustomer = customerRepository.findById(customer.getId()).get();

        assertNotNull(savedCustomer);
        assertEquals(savedCustomer.getName(), dto.getName());
    }

    @Transactional
    @Rollback
    @Test
    void deleteCustomer() {
        var customer = customerRepository.findAll().get(0);
        customerController.deleteCustomerById(customer.getId());

        Optional<Customer> optionalBeer = customerRepository.findById(customer.getId());

        assertTrue(optionalBeer.isEmpty());
    }
}