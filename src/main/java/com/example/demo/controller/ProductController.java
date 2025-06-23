package com.example.demo.controller;

import com.alibaba.fastjson2.JSON;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/")
    public String index1(Model model) {
        model.addAttribute("products", productService.showAllProduct());
        model.addAttribute("categories", productService.showAllCategory());
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("categories", productService.showAllCategory());
        model.addAttribute("products", productService.showAllProduct());
        return "index";
    }

    @GetMapping("/addproduct")
    public String add(Model model) {
        model.addAttribute("categories", productService.showAllCategory());
        return "addproduct";
    }

    @PostMapping("/addproduct")
    public String add(Product product, @RequestParam("file") MultipartFile[] file, Authentication authentication) {
        productService.addProduct(product, file, authentication.getName());
        return "redirect:/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("products", productService.searchProduct(keyword));
        model.addAttribute("categories", productService.showAllCategory());
        return "index";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable("id") int id, Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("product", productService.showProductById(id));
        return "product";
    }

    @GetMapping("/editproduct/{id}")
    public String edit(@PathVariable("id") int id, Model model, Authentication authentication) {
        if (!Objects.equals(productService.showProductById(id).getUname(), authentication.getName())){
            return "redirect:/home";
        }
        model.addAttribute("product", productService.showProductById(id));
        model.addAttribute("categories", productService.showAllCategory());
        return "editproduct";
    }

    @PostMapping("/editproduct")
    public String edit(Product product, @RequestParam(value = "file", required = false) MultipartFile[] images, Authentication authentication) {
        productService.editProduct(product, images, authentication.getName());
        return "redirect:/home";
    }

    @GetMapping("/deleteproduct/{id}")
    public String delete(@PathVariable("id") int id, Authentication authentication) {
        if (!Objects.equals(productService.showProductById(id).getUname(), authentication.getName())){
            return "redirect:/home";
        }
        productService.deleteProduct(id);
        return "redirect:/home";
    }

    @PostMapping("/getproduct")
    @ResponseBody
    public String getproduct(@RequestParam("pid") int pid) {
        return JSON.toJSONString(productService.showProductById(pid));
    }

    @PostMapping("/getproducts")
    @ResponseBody
    public String getproducts() {
        return JSON.toJSONString(productService.showAllProduct());
    }

    @GetMapping("/category")
    public String category(@RequestParam("category") String category, Model model) {
        model.addAttribute("products", productService.showProductByCategory(category));
        model.addAttribute("categories", productService.showAllCategory());
        return "index";
    }
}
