package com.belong.telecom.controller;

import com.belong.telecom.biz.PageInfo;
import com.belong.telecom.biz.ResultInfo;
import com.belong.telecom.service.IPhoneNumberService;
import com.belong.telecom.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/customer"})
@Slf4j
public class CustomerManagementController {

    @Autowired
    private IPhoneNumberService phoneNumberService;

    @GetMapping({"/{id}/phone_number"})
    public ResponseEntity<ResultInfo> findCustomerPhoneNumber(@PathVariable Long id, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        log.info("find customer {} phone numbers [page:{},size:{}]", new Object[]{id, page, size});
        return ResponseEntity.ok(this.phoneNumberService.getAllPhoneNumbers(PageInfo.builder().page(page == null ? 0 : page).size(size == null ? Constant.DEFAULT_SIZE : size).build(), id));
    }
}
