package com.superstore.superstore.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @NotEmpty(message = "the name is required")
    private String name;
    @NotEmpty(message = "the brand is required")
    private String brand;
    @NotEmpty(message = "the category is required")
    private String category;
    @Min(0)
    private double price;
    @Size(min = 10, message = "it should be 10 characters")
    @Size(max = 200,message = "less the 200 charecters")
    private String description;

    private MultipartFile imageName;


}
