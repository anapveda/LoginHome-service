package com.players.LoginHome_service.repository;

import com.players.LoginHome_service.model.Entity.User;
import com.players.LoginHome_service.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

   Optional<User> findFirstByEmail(String email);
   Optional<User> findByUserRole(UserRole userRole);

}
