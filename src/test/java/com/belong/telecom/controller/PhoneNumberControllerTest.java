package com.belong.telecom.controller;

import com.belong.telecom.biz.CustomerInfo;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith({MockitoExtension.class})
@WebMvcTest({PhoneNumberManagementController.class})
@Import({AuthenticationFilter.class})
public class PhoneNumberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IPhoneNumberService phoneNumberService;
    @MockBean
    private ISecurityCheckService securityCheckService;

    @Test
    public void givenEmpty_whenGetPhoneNumbers_thenResultStatus200() throws Exception {
        Mockito.when(this.securityCheckService.authenticated(Mockito.anyString())).thenReturn(true);
        Mockito.when(this.phoneNumberService.getAllPhoneNumbers(Mockito.any(PageInfo.class)))
                .thenReturn(ResultInfo.builder().success(true).build());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/phone_numbers/list")
                .header("authToken", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenCustomerInfo_whenGetPhoneNumbers_thenResultStatus200() throws Exception {
        Mockito.when(this.securityCheckService.authenticated(Mockito.anyString())).thenReturn(true);
        Mockito.when(this.phoneNumberService.getAllPhoneNumbers(Mockito.any(CustomerInfo.class)))
                .thenReturn(ResultInfo.builder().success(true).build());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/phone_numbers/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customer\":{\"firstName\":\"test\", \"lastName\":\"belong\", \"dob\":\"2022-07-01\"}}")
                .header("authToken", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenCustomerInfo_PhoneNumber_whenActivePhoneNumbers_thenResultStatus200() throws Exception {
        Mockito.when(this.securityCheckService.authenticated(Mockito.anyString())).thenReturn(true);
        Mockito.when(this.phoneNumberService.activePhoneNumber(Mockito.any(CustomerInfo.class)))
                .thenReturn(ResultInfo.builder().success(true).build());
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/phone_numbers/active")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phoneNumber\":\"0451111111\", \"customer\":{\"firstName\":\"test\", \"lastName\":\"belong\", \"dob\": \"2022-07-01\"}}")
                .header("authToken", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", new Object[0]).value(true));
    }
}
