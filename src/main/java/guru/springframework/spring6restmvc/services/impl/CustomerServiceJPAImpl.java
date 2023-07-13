package guru.springframework.spring6restmvc.services.impl;

import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@RequiredArgsConstructor
@Service
public class CustomerServiceJPAImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.ofNullable(
                customerMapper.entityToDto(customerRepository.findById(uuid).orElse(null))
        );
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::entityToDto)
                .toList();
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        return customerMapper.entityToDto(customerRepository.save(customerMapper.dtoToEntity(customer)));
    }

    @Override
    public CustomerDTO replaceCustomerById(UUID id, CustomerDTO replacement) {
        return customerRepository.findById(id).map(
                customer -> {
                    customer.setName(replacement.getName());
                    customerRepository.save(customer);
                    return customerMapper.entityToDto(customer);
                }).orElseThrow(NotFoundException::new);
    }

    @Override
    public void deleteCustomerById(UUID id) {
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDTO updateCustomerById(UUID id, CustomerDTO patch) {
        return customerRepository.findById(id).map(customer -> {
            if (patch.getName() != null) {
                customer.setName(patch.getName());
            }
            customerRepository.save(customer);
            return customerMapper.entityToDto(customer);
        }).orElseThrow(NotFoundException::new);
    }
}
