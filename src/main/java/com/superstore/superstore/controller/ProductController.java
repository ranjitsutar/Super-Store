package com.superstore.superstore.controller;

import com.superstore.superstore.dto.ProductDto;
import com.superstore.superstore.model.Product;
import com.superstore.superstore.repository.ProductRepo;
import com.superstore.superstore.service.ProductService;
import com.superstore.superstore.service.ProductServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepo productRepo;


    @GetMapping({"", "/"})
    public String getAllProduct(Model model){
        List<Product> product= productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products",product);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model){
        ProductDto  productDto= new ProductDto();
        model.addAttribute("productDto",productDto);
        return "products/createProduct";

    }
    @PostMapping("/create")
    public String createProduct(
        @Valid @ModelAttribute ProductDto productDto,
        BindingResult result
    ){

        if(productDto.getImageName().isEmpty()){
            result.addError(new FieldError("productDto", "imageFile",
                    "The image is not supported "));
        }
        if (result.hasErrors()){
            return "products/createProduct";
        }

        // save image file
        MultipartFile image= productDto.getImageName();
        Date createAt= new Date();
        String storageFileName = createAt.getTime()+" "+image.getOriginalFilename();

        try {
            String uploadDir= "public/image/";
            Path uploadPath= Paths.get(uploadDir);

            if (Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir+storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);

            }
        }
        catch (Exception e){
            System.out.println("Exception: "+ e.getMessage());
        }

        Product product= new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createAt);
        product.setImageName(storageFileName);

        productRepo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(
            Model model, @RequestParam int id
    ){
        try {
            Product product= productRepo.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto= new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto",productDto);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/products";
        }
        return "redirect:/editProduct";

    }

    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ){

        try {
            Product product = productRepo.findById(id).get();
            model.addAttribute("product",product);

            if(result.hasErrors()){
                return "redirect:/editProduct";
            }

            if(! productDto.getImageName().isEmpty()){
                //delete old image
                String uploadDir= "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageName());

                try {
                    Files.delete(oldImagePath);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                //Save new Image File
                MultipartFile image= productDto.getImageName();
                Date createdAt= new Date();
                String storageFileName= createdAt.getTime()+"_"+ image.getOriginalFilename();

                try (InputStream inputStream =image.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);

                }
                product.setImageName(storageFileName);
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            productRepo.save(product);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return "redirect:/products";

    }


    @DeleteMapping("/delete")
    public String deleteProduct(
            @RequestParam int id
    ){
        try {
            Product product= productRepo.findById(id).get();
            Path path = Paths.get("public/images/" + product.getImageName());
            try {
                Files.delete(path);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            productRepo.delete(product);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "redirect:/products";
    }
}
