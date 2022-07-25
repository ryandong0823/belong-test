package com.belong.telecom.service;

public interface ISecurityCheckService {
    boolean authenticated(String token);
}
