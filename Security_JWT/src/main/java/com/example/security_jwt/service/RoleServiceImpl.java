package com.example.security_jwt.service;

import com.example.security_jwt.entity.Role;
import com.example.security_jwt.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleServiceImpl implements IRoleService{
    @Autowired
    private IRoleRepository iRoleRepository;

    @Override
    public Role findByRoleName(String roleName) {
        return iRoleRepository.findByRoleName(roleName);
    }
}
