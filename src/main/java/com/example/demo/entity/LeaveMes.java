package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveMes {
    private Integer pid;
    private String luser;
    private String lcontent;
    private String ltime;
}
