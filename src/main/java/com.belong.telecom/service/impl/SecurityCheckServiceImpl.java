package com.belong.telecom.service.impl;

import com.belong.telecom.service.ISecurityCheckService;
import org.springframework.stereotype.Service;

@Service
public class SecurityCheckServiceImpl implements ISecurityCheckService {

    public boolean authenticated(String token) {
        return true;
    }

}
