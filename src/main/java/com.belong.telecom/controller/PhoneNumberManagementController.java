package com.belong.telecom.controller;

import com.belong.telecom.biz.CustomerInfo;
import com.belong.telecom.biz.PageInfo;
import com.belong.telecom.biz.ResultInfo;
import com.belong.telecom.service.IPhoneNumberService;
import com.belong.telecom.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/phone_numbers"})
@Slf4j
public class PhoneNumberManagementController {
    @Autowired
    IPhoneNumberService phoneNumberService;

    @GetMapping({"/list"})
    public ResponseEntity<ResultInfo> findAllPhoneNumbers(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        log.info("find phone numbers [page:{},size:{}]", page, size);
        return ResponseEntity.ok(this.phoneNumberService.getAllPhoneNumbers(PageInfo.builder().page(page == null ? 0 : page).size(size == null ? Constant.DEFAULT_SIZE : size).build()));
    }

    @PostMapping({"/list"})
    public ResponseEntity<ResultInfo> findAllPhoneNumber(@RequestBody CustomerInfo info) {
        log.info("find customer {} phone numbers page {}]", info.getCustomer(), info.getPageInfo());
        return ResponseEntity.ok(this.phoneNumberService.getAllPhoneNumbers(info));
    }

    @PostMapping({"/active"})
    public ResponseEntity<ResultInfo> activePhoneNumber(@RequestBody CustomerInfo info) {
        log.info("active customer {} phone numbers []", info.getCustomer(), info.getPhoneNumber());
        return ResponseEntity.ok(this.phoneNumberService.activePhoneNumber(info));
    }
}
