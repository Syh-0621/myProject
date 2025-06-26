package com.example.demo.service;

import com.example.demo.entity.Product;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<Product> showAllProduct();

    //返回图片路径 上传失败则返回空
    String uploadImages(MultipartFile[] images, String username);

    boolean deleteImages(Product product);

    String addProduct(Product product, MultipartFile[] images, String username);

    List<Product> searchProduct(String keyword);

    List<Product> showMyProduct(String username);

    Product showProductById(int id);

    String editProduct(Product product, MultipartFile[] images, String username);

    int deleteProduct(Integer id);

    List<Product> showProductByCategory(String category);

    List<String> showAllCategory();
}
