package com.example.demo.service;

import com.example.demo.entity.LeaveMes;

import java.util.List;

public interface LeaveMesService {
    int addLeaveMes(LeaveMes leaveMes);

    List<LeaveMes> selectLeaveMesByProductId(Integer productId);

    int deleteLeaveMes(LeaveMes mes);

    int deleteAllLeaveMesByPid(Integer pid);
}
