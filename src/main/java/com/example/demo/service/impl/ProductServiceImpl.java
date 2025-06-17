package com.example.demo.service.impl;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> showAllProduct() {
        return productMapper.selectAllProduct();
    }

    @Override
    public String uploadImages(MultipartFile images, String username) {
        if (images.isEmpty())
            return null;
        String folderpath = "/home/syh/Pictures/img/";
        String filepath = username + "/" + new Random().nextInt(1000) + images.getOriginalFilename();
        File wholepath = new File(folderpath + filepath);
        if (!wholepath.getParentFile().exists()) {
            wholepath.getParentFile().mkdirs();
        }
        try {
            images.transferTo(wholepath);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return "/images/" + filepath;
    }

    @Override
    public boolean deleteImages(Product product){
        String imgpath = showProductById(product.getPid()).getImage();
        if (imgpath == null || imgpath.equals(""))
            return true;
        File file = new File("/home/syh/Pictures/img/" + imgpath.substring(8));
        if (file.exists())
            return file.delete();
        return false;
    }

    @Override
    public String addProduct(Product product, MultipartFile images, String username){
        String imgpath = uploadImages(images, username);
        product.setUname(username);
        product.setImage(imgpath);
        productMapper.insertProduct(product);
        return "添加成功";
    }

    @Override
    public List<Product> searchProduct(String keyword){
        return productMapper.selectProductByKeyword(keyword);
    }

    @Override
    public List<Product> showMyProduct(String username){
        return productMapper.selectProductByUname(username);
    }

    @Override
    public Product showProductById(int id){
        return productMapper.selectProductById(id);
    }

    @Override
    public String editProduct(Product product, MultipartFile images, String username){
        String imgpath = null;
        log.info(String.valueOf(images.isEmpty()));
        if (!images.isEmpty()) {
            deleteImages(product);
            imgpath = uploadImages(images, username);
        }
        else {
            imgpath = showProductById(product.getPid()).getImage();
        }
        product.setImage(imgpath);
        product.setUname(username);
        productMapper.updateProduct(product);
        return "修改成功";
    }

    @Override
    public int deleteProduct(int id){
        deleteImages(productMapper.selectProductById(id));
        return productMapper.deleteProductById(id);
    }
}
