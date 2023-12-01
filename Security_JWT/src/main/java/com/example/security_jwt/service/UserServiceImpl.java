package com.example.security_jwt.service;

import com.example.security_jwt.entity.Users;
import com.example.security_jwt.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements IUserService{
    @Autowired
    private IUserRepository iUserRepository;
    @Override
    public Users findByUserName(String userName) {
        return iUserRepository.findByUserName(userName);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return iUserRepository.existsByUserName(userName);
    }



    @Override
    public void changePassword(Users users) {
      iUserRepository.save(users);
    }
}
