package com.example.springbootmultidatasource.entity.Product;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
//@Data: This annotation combines the functionality of @Getter, @Setter, @ToString, @EqualsAndHashCode, and @RequiredArgsConstructor. It is typically used at the class level
@Table(name="Product_TB")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;


}
