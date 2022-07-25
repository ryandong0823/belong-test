//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.belong.telecom.repository;

import com.belong.telecom.entity.Customer;
import com.belong.telecom.entity.PhoneNumbers;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT
)
@ActiveProfiles({"dev"})
public class PhoneNumberRepositoryTest {
    @Autowired
    IPhoneNumberRepository phoneNumberRepository;
    @Autowired
    ICustomerRepository customerRepository;

    public PhoneNumberRepositoryTest() {
    }

    @BeforeEach
    public void before() {
    }

    @Test
    public void saveTest() {
        Customer customer = Customer.builder().id(2L).build();
        PhoneNumbers phoneNumbers = PhoneNumbers.builder().phoneNumber("0451111122").activity(0).customer(customer).build();
        this.phoneNumberRepository.save(phoneNumbers);
        Assertions.assertNotNull(phoneNumbers.getId());
    }

    @Test
    public void searchAllPaginationTest() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<PhoneNumbers> phoneNumbersPage = this.phoneNumberRepository.findAll(pageable);
        Assertions.assertEquals(2, phoneNumbersPage.getContent().size());
    }

    @Test
    public void searchByCustomerPaginationTest() {
        Pageable pageable = PageRequest.of(1, 4);
        String firstName = "Test";
        String lastName = "Belong";
        LocalDate dob = LocalDate.of(2022, 7, 1);
        Page<PhoneNumbers> phoneNumbersPage = this.phoneNumberRepository.findByCustomer_FirstNameAndCustomer_LastNameAndCustomer_Dob(firstName, lastName, dob, pageable);
        Assertions.assertEquals(1, phoneNumbersPage.getContent().size());
        phoneNumbersPage = this.phoneNumberRepository.findByCustomer_Id(1L, pageable);
        Assertions.assertEquals(1, phoneNumbersPage.getContent().size());
    }

    @Test
    public void searchByPhoneNumberTest() {
        String phoneNumber = "0451111111";
        Assertions.assertTrue(this.phoneNumberRepository.findByPhoneNumber(phoneNumber).isPresent());
    }
}
