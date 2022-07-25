package com.belong.telecom.biz;

import com.belong.telecom.entity.Customer;
import com.belong.telecom.entity.PhoneNumbers;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResultInfo {

    private boolean success;
    private String message;
    private PageInfo pageInfo;
    private Customer customer;
    private List<PhoneNumbers> phoneNumbers;
    private String phoneNumber;

}
