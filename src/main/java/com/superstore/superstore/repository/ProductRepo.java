package com.superstore.superstore.repository;

import com.superstore.superstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {

}
