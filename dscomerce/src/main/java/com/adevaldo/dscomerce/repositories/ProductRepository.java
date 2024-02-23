package com.adevaldo.dscomerce.repositories;

import com.adevaldo.dscomerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
