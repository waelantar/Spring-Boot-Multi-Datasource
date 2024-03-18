package com.example.springbootmultidatasource.entity.User;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Data
//@Data: This annotation combines the functionality of @Getter, @Setter, @ToString, @EqualsAndHashCode, and @RequiredArgsConstructor. It is typically used at the class level
@Table(name="User_TB")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;
}