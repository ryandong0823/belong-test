package com.belong.telecom.repository;

import com.belong.telecom.entity.PhoneNumbers;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPhoneNumberRepository extends JpaRepository<PhoneNumbers, Long> {
    Page<PhoneNumbers> findByCustomer_FirstNameAndCustomer_LastNameAndCustomer_Dob(String firstName, String lastName, LocalDate dob, Pageable pageable);

    Page<PhoneNumbers> findByCustomer_Id(Long customerId, Pageable pageable);

    Optional<PhoneNumbers> findByPhoneNumber(String phoneNumber);

    Optional<PhoneNumbers> findByCustomer_FirstNameAndCustomer_LastNameAndCustomer_DobAndPhoneNumberAndActivity(String firstName, String lastName, LocalDate dob, String phoneName, int activity);
}
