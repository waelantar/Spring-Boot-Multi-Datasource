package com.example.springbootmultidatasource.repository.Product;

import com.example.springbootmultidatasource.entity.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
