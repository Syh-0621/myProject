package com.example.demo.service.impl;

import com.example.demo.entity.LeaveMes;
import com.example.demo.mapper.LeaveMesMapper;
import com.example.demo.service.LeaveMesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveMesServiceImpl implements LeaveMesService {

    @Autowired
    private LeaveMesMapper leaveMesMapper;

    @Override
    public int addLeaveMes(LeaveMes leaveMes) {
        return leaveMesMapper.addLeaveMes(leaveMes);
    }

    @Override
    public List<LeaveMes> selectLeaveMesByProductId(Integer productId) {
        return leaveMesMapper.selectLeaveMesByPId(productId);
    }

    @Override
    public int deleteLeaveMes(LeaveMes mes){
        return leaveMesMapper.deleteLeaveMes(mes);
    }

    @Override
    public int deleteAllLeaveMesByPid(Integer pid){
        selectLeaveMesByProductId(pid).forEach(leaveMesMapper::deleteLeaveMes);
        return 1;
    }
}
