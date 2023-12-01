package com.example.security_jwt.repository;

import com.example.security_jwt.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<Users,Integer> {
    Users findByUserName(String userName);
    boolean existsByUserName(String userName);

}
