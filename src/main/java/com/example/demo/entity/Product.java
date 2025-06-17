package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    Integer pid;
    String uname;
    String pname;
    String depict;
    String image;
    Double price;
    Date publish;
}
