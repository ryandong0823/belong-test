//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.belong.telecom.repository;

import com.belong.telecom.entity.Customer;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByFirstNameAndLastNameAndDob(String firstName, String lastName, LocalDate dob);
}
