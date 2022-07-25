package com.belong.telecom.service;

import com.belong.telecom.biz.CustomerInfo;
import com.belong.telecom.biz.PageInfo;
import com.belong.telecom.biz.ResultInfo;

public interface IPhoneNumberService {
    ResultInfo getAllPhoneNumbers(PageInfo pageInfo);

    ResultInfo getAllPhoneNumbers(CustomerInfo customerInfo);

    ResultInfo getAllPhoneNumbers(PageInfo pageInfo, Long customerId);

    ResultInfo activePhoneNumber(CustomerInfo customerInfo);
}
