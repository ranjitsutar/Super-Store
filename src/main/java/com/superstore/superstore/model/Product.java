package com.superstore.superstore.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String brand;
    private String category;
    private double price;
    @Column(columnDefinition = "text")
    private String description;
    private Date createdAt;
    private String imageName;





}
