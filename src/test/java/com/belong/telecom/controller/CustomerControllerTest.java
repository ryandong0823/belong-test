package com.belong.telecom.controller;

import com.belong.telecom.biz.PageInfo;
import com.belong.telecom.biz.ResultInfo;
import com.belong.telecom.filter.AuthenticationFilter;
import com.belong.telecom.service.IPhoneNumberService;
import com.belong.telecom.service.ISecurityCheckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerManagementController.class)
@Import(AuthenticationFilter.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IPhoneNumberService phoneNumberService;
    @MockBean
    private ISecurityCheckService securityCheckService;

    @Test
    public void givenCustomerId_whenGetPhoneNumbers_thenResultStatus401() throws Exception {
        long customerId = 1l;
        Mockito.when(this.securityCheckService.authenticated(Mockito.anyString())).thenReturn(true);
        Mockito.when(this.phoneNumberService.getAllPhoneNumbers(PageInfo.builder().page(0).size(3).build(), customerId)).thenReturn(ResultInfo.builder().success(true).build());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/customer/" + customerId + "/phone_number"))
                .andExpect(MockMvcResultMatchers.status().is(401));
    }

    @Test
    public void givenCustomerId_whenGetPhoneNumbers_thenResultStatus403() throws Exception {
        long customerId = 1L;
        Mockito.when(this.securityCheckService.authenticated(Mockito.anyString())).thenReturn(false);
        Mockito.when(this.phoneNumberService.getAllPhoneNumbers(PageInfo.builder().page(0).size(3).build(), customerId)).thenReturn(ResultInfo.builder().success(true).build());
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/customer/" + customerId + "/phone_number")
                .header("authToken", new Object[]{"test"}))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    public void givenCustomerId_whenGetPhoneNumbers_thenResultStatus200() throws Exception {
        long customerId = 1L;
        Mockito.when(this.securityCheckService.authenticated(Mockito.anyString())).thenReturn(true);
        Mockito.when(this.phoneNumberService.getAllPhoneNumbers(PageInfo.builder().page(0).size(3).build(), customerId)).thenReturn(ResultInfo.builder().success(true).build());
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/customer/" + customerId + "/phone_number")
                .header("authToken", new Object[]{"test"}))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
