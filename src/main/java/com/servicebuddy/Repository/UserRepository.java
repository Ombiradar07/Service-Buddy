package com.servicebuddy.Repository;

import com.servicebuddy.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {

    Optional<Users>findByEmail(String email);
    Optional<Users>findByEmailOrPhone(String email,String phone);
}
