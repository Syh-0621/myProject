package com.example.demo.controller;

import com.example.demo.entity.LeaveMes;
import com.example.demo.service.LeaveMesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leaveMes")
public class LeaveMesController {

    @Autowired
    private LeaveMesService leaveMesService;

    @PostMapping("/add")
    public String addLeaveMes(LeaveMes mes) {
        if (leaveMesService.addLeaveMes(mes) > 0) {
            return "success";
        }
        return "fail";
    }

    @PostMapping("/delete")
    public String deleteLeaveMes(LeaveMes mes) {
        if (leaveMesService.deleteLeaveMes(mes) > 0) {
            return "success";
        }
        return "fail";
    }
}
