package com.example.springbootmultidatasource.repository.User;

import com.example.springbootmultidatasource.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
