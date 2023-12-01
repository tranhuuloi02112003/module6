package com.example.security_jwt.security;

import com.example.security_jwt.entity.Users;
import com.example.security_jwt.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipleService implements UserDetailsService {
    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = iUserRepository.findByUserName(username);
        if (users == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return UserPrinciple.mapUserToUserPrinciple(users);
    }
}
