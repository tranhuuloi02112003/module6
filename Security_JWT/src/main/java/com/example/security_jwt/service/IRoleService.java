package com.example.security_jwt.service;

import com.example.security_jwt.entity.Role;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public interface IRoleService {
    Role findByRoleName(String roleName);

}
