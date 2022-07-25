package com.belong.telecom.service;

import com.belong.telecom.biz.CustomerInfo;
import com.belong.telecom.biz.PageInfo;
import com.belong.telecom.entity.Customer;
import com.belong.telecom.entity.PhoneNumbers;
import com.belong.telecom.repository.IPhoneNumberRepository;
import com.belong.telecom.service.impl.PhoneNumberServiceImpl;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith({MockitoExtension.class})
public class PhoneNumberServiceTest {
    @InjectMocks
    private PhoneNumberServiceImpl phoneNumberService;
    @Mock
    private IPhoneNumberRepository phoneNumberRepository;

    @Test
    public void findAllPhoneNumberTest() {
        PageInfo pageInfo = PageInfo.builder().page(0).size(2).build();
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), Sort.by(Direction.DESC, "phoneNumber"));
        PageImpl<PhoneNumbers> page = new PageImpl(Arrays.asList(PhoneNumbers.builder().phoneNumber("0451111111").build(), PhoneNumbers.builder().phoneNumber("0451111112").build()), pageable, 5L);
        Mockito.when(this.phoneNumberRepository.findAll(pageable)).thenReturn(page);
        List<PhoneNumbers> phoneNumbersList = this.phoneNumberService.getAllPhoneNumbers(pageInfo).getPhoneNumbers();
        Assertions.assertNotNull(phoneNumbersList);
    }

    @Test
    public void activePhoneNumberTest() {
        Customer customer = Customer.builder().firstName("test").lastName("belong").dob(LocalDate.now()).build();
        String phoneNumber = "0451111111";
        PhoneNumbers phoneNumbers = PhoneNumbers.builder().phoneNumber(phoneNumber).build();
        Mockito.when(this.phoneNumberRepository.findByCustomer_FirstNameAndCustomer_LastNameAndCustomer_DobAndPhoneNumberAndActivity(customer.getFirstName(), customer.getLastName(), customer.getDob(), phoneNumber, 0)).thenReturn(Optional.ofNullable(phoneNumbers));
        Mockito.when(this.phoneNumberRepository.save(Mockito.any(PhoneNumbers.class))).thenReturn(phoneNumbers);
        CustomerInfo info = new CustomerInfo();
        info.setCustomer(customer);
        info.setPhoneNumber(phoneNumber);
        Assertions.assertTrue(this.phoneNumberService.activePhoneNumber(info).isSuccess());
    }
}
