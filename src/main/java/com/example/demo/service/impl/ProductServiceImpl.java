package com.example.demo.service.impl;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Getter
    private static final List<String> categories = new ArrayList<String>() {{
        add("电子产品");
        add("生活用品");
        add("运动器材");
        add("书籍");
        add("学习用品");
        add("化妆品");
        add("服装");
        add("食品");
        add("其他");
    }};

    @Override
    public List<Product> showAllProduct() {
        return productMapper.selectAllProduct();
    }

    /**
     *
     * @param images
     * @param username
     * @return
     * 上传图片，返回图片路径，以#分隔
     */
    @Override
    public String uploadImages(MultipartFile[] images, String username) {
        if (images == null || images.length == 0)
            return null;
        String folderpath = "/home/syh/Pictures/img/";
        String[] suffixs = new String[]{"jpg", "png", "jpeg", "bmp", "gif"};
        List<String> imagespath = new ArrayList<>();

        for (MultipartFile image : images){
            if (image.isEmpty())
                continue;
            if (image.getSize() > 1024 * 1024 * 5) {
                throw new RuntimeException("图片大小不能超过5MB");
            }
            String filepath = username + "/" + new Random().nextInt(1000) + image.getOriginalFilename();
            File wholepath = new File(folderpath + filepath);
            if (!wholepath.getParentFile().exists()) {
                wholepath.getParentFile().mkdirs();
            }
            try {
                image.transferTo(wholepath);
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            imagespath.add("/images/" + filepath);
        }
        return String.join(",", imagespath);
    }

    @Override
    public boolean deleteImages(Product product){
        String imgpath = showProductById(product.getPid()).getImage();
        if (imgpath == null || imgpath.isEmpty())
            return true;
        String[] imgpaths = imgpath.split(",");
        for (String path : imgpaths) {
            File file = new File("/home/syh/Pictures/img/" + path.substring(8));
            if (file.exists())
                return file.delete();
        }
        return false;
    }

    @Override
    public String addProduct(Product product, MultipartFile[] images, String username){
        String imgpath = uploadImages(images, username);
        product.setUname(username);
        product.setImage(imgpath);
        System.out.println(product);
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
    public String editProduct(Product product, MultipartFile[] images, String username){
        String imgpath = null;
        if (!images[0].isEmpty()) {
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

    @Override
    public List<Product> showProductByCategory(String category){
        return productMapper.selectProductByCategory(category);
    }

    @Override
    public List<String> showAllCategory() {
        return categories;
    }
}
