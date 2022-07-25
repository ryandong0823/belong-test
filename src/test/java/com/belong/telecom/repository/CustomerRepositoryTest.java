package com.belong.telecom.repository;

import com.belong.telecom.entity.Customer;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"dev"})
public class CustomerRepositoryTest {
    @Autowired
    ICustomerRepository customerRepository;

    private int recordCount = 5;

    @Test
    public void saveTest() {
        Customer customer = Customer.builder().firstName("Test4").lastName("Belong").dob(LocalDate.of(2022, 7, 4)).build();
        this.customerRepository.save(customer);
        Assertions.assertNotNull(customer.getId(), "customer id should not be null");
    }

    @Test
    public void searchAllTest() {
        List<Customer> customerList = this.customerRepository.findAll();
        Assertions.assertEquals(this.recordCount, customerList.size());
    }

    @Test
    public void searchAllPaginationTest() {
        Page<Customer> customers = this.customerRepository.findAll(PageRequest.of(2, 2));
        Assertions.assertEquals(1, customers.getContent().size());
    }

    @Test
    public void searchByConditionsTest() {
        String firstName = "Test";
        String lastName = "Belong";
        LocalDate dob = LocalDate.of(2022, 7, 1);
        Assertions.assertTrue(this.customerRepository.findByFirstNameAndLastNameAndDob(firstName, lastName, dob).isPresent());
    }
}
