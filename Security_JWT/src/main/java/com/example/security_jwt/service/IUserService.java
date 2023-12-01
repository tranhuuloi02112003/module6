package com.example.security_jwt.service;

import com.example.security_jwt.entity.Users;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public interface IUserService {
    Users findByUserName(String userName);
    boolean existsByUserName(String userName);
    void changePassword(Users users);
}
