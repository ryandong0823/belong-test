package com.belong.telecom.biz;

import com.belong.telecom.entity.Customer;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerInfo {

    private Customer customer;
    private String phoneNumber;
    private PageInfo pageInfo;

}
