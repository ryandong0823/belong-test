package com.belong.telecom.service.impl;

import com.belong.telecom.biz.CustomerInfo;
import com.belong.telecom.biz.PageInfo;
import com.belong.telecom.biz.ResultInfo;
import com.belong.telecom.entity.Customer;
import com.belong.telecom.entity.PhoneNumbers;
import com.belong.telecom.repository.ICustomerRepository;
import com.belong.telecom.repository.IPhoneNumberRepository;
import com.belong.telecom.service.IPhoneNumberService;
import com.belong.telecom.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
public class PhoneNumberServiceImpl implements IPhoneNumberService {
    @Autowired
    private IPhoneNumberRepository phoneNumberRepository;
    @Autowired
    private ICustomerRepository customerRepository;
    private static final int _INACTIVE_STATUS = 0;
    private static final int _ACTIVE_STATUS = 1;

    public PhoneNumberServiceImpl() {
    }

    public ResultInfo getAllPhoneNumbers(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), Sort.by(Direction.DESC, "phoneNumber"));
        Page page = null;

        try {
            page = this.phoneNumberRepository.findAll(pageable);
            pageInfo.setTotal(page.getTotalElements());
            pageInfo.setPage(pageInfo.getPage() + 1);
        } catch (Exception ex) {
            log.error("search phone number exception ", ex);
            return ResultInfo.builder().success(false).message("search phone number failed").pageInfo(pageInfo).build();
        }

        log.info("search phone numbers page {} size {} and result {}", pageInfo.getPage(), pageInfo.getSize(), page.getContent());
        return ResultInfo.builder()
                .success(true)
                .phoneNumbers(page.getContent())
                .pageInfo(pageInfo)
                .build();
    }

    public ResultInfo getAllPhoneNumbers(CustomerInfo customerInfo) {
        Assert.notNull(customerInfo.getCustomer(), "Customer's Information should not be EMPTY");
        Assert.notNull(customerInfo.getCustomer().getFirstName(), "customer's first name should not be EMPTY");
        Assert.notNull(customerInfo.getCustomer().getLastName(), "customer's last name should not be EMPTY");
        Assert.notNull(customerInfo.getCustomer().getDob(), "customer's date of birth should not be EMPTY");
        if (customerInfo.getPageInfo() == null) {
            customerInfo.setPageInfo(PageInfo.builder().page(0).size(Constant.DEFAULT_SIZE).build());
        }

        if (customerInfo.getPageInfo().getPage() == null) {
            customerInfo.getPageInfo().setPage(0);
        }

        if (customerInfo.getPageInfo().getSize() == null) {
            customerInfo.getPageInfo().setSize(Constant.DEFAULT_SIZE);
        }

        Pageable pageable = PageRequest.of(customerInfo.getPageInfo().getPage(), customerInfo.getPageInfo().getSize(), Sort.by(Direction.DESC, "phoneNumber"));
        Page page = null;

        try {
            page = this.phoneNumberRepository
                    .findByCustomer_FirstNameAndCustomer_LastNameAndCustomer_Dob(
                            customerInfo.getCustomer().getFirstName(),
                            customerInfo.getCustomer().getLastName(),
                            customerInfo.getCustomer().getDob(),
                            pageable);
        } catch (Exception ex) {
            log.error("search phone number by customer [{}] exception ", customerInfo.getCustomer(), ex);
            return ResultInfo.builder()
                    .success(false)
                    .message("search phone number failed")
                    .customer(customerInfo.getCustomer())
                    .pageInfo(customerInfo.getPageInfo())
                    .build();
        }

        ResultInfo resultInfo = ResultInfo.builder()
                .success(true)
                .phoneNumbers(page.getContent())
                .pageInfo(PageInfo.builder()
                        .page(customerInfo.getPageInfo().getPage() + 1)
                        .size(customerInfo.getPageInfo().getSize())
                        .build())
                .build();

        try {
            resultInfo.setCustomer(this.customerRepository.findByFirstNameAndLastNameAndDob(customerInfo.getCustomer().getFirstName(), customerInfo.getCustomer().getLastName(), customerInfo.getCustomer().getDob()).orElse(customerInfo.getCustomer()));
        } catch (Exception ex) {
            log.error("search customer {} exception ", customerInfo.getCustomer(), ex);
            resultInfo.setCustomer(customerInfo.getCustomer());
        }

        log.info("search phone number by customer [{}] done [{}]", customerInfo.getCustomer(), resultInfo.getPhoneNumbers());
        return resultInfo;
    }

    public ResultInfo getAllPhoneNumbers(PageInfo pageInfo, Long customerId) {
        Assert.notNull(customerId, "customer's ID should not be EMPTY");
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), Sort.by(Direction.DESC, new String[]{"phoneNumber"}));
        Page<PhoneNumbers> page = this.phoneNumberRepository.findByCustomer_Id(customerId, pageable);
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setPage(pageInfo.getPage() + 1);
        log.info("search customer {} phone numbers page {} size {} and result {}", customerId, pageInfo.getPage(), pageInfo.getSize(), page.getContent());
        return ResultInfo.builder().success(true).phoneNumbers(page.getContent()).pageInfo(pageInfo).customer(this.customerRepository.findById(customerId).orElse(Customer.builder().id(customerId).build())).build();
    }

    public ResultInfo activePhoneNumber(CustomerInfo customerInfo) {
        Assert.notNull(customerInfo.getCustomer(), "Customer's Information should not be EMPTY");
        Assert.notNull(customerInfo.getCustomer().getFirstName(), "customer's first name should not be EMPTY");
        Assert.notNull(customerInfo.getCustomer().getLastName(), "customer's last name should not be EMPTY");
        Assert.notNull(customerInfo.getCustomer().getDob(), "customer's date of birth should not be EMPTY");
        Assert.notNull(customerInfo.getPhoneNumber(), "Phone number should not be EMPTY");
        ResultInfo resultInfo = ResultInfo.builder().phoneNumber(customerInfo.getPhoneNumber()).customer(customerInfo.getCustomer()).build();
        phoneNumberRepository.findByCustomer_FirstNameAndCustomer_LastNameAndCustomer_DobAndPhoneNumberAndActivity(
                        customerInfo.getCustomer().getFirstName(),
                        customerInfo.getCustomer().getLastName(),
                        customerInfo.getCustomer().getDob(), customerInfo.getPhoneNumber(), _INACTIVE_STATUS)
                .ifPresentOrElse((phoneNumbers) -> {
                    phoneNumbers.setActivity(_ACTIVE_STATUS);
                    try {
                        this.phoneNumberRepository.save(phoneNumbers);
                        resultInfo.setSuccess(true);
                        log.info("active customer {} phone number {} success", customerInfo.getCustomer(), customerInfo.getPhoneNumber());
                    } catch (Exception var5) {
                        resultInfo.setSuccess(false);
                        resultInfo.setMessage("active phone number failed");
                        log.error("Active phone number {} exception ", customerInfo.getPhoneNumber(), var5);
                    }

                }, () -> {
                    log.info("phone number {} was active or cannot find", customerInfo.getPhoneNumber());
                    resultInfo.setSuccess(false);
                    resultInfo.setMessage("could not find phone number or active phone number failed");
                });
        return resultInfo;
    }
}
