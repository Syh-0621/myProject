package com.example.demo.mapper;

import com.example.demo.entity.LeaveMes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LeaveMesMapper {

    @Select("select * from leave_mes where pid = #{pid}")
    List<LeaveMes> selectLeaveMesByPId(Integer pid);

    @Insert("insert into leave_mes (pid, luser, lcontent, ltime) values (#{pid}, #{luser}, #{lcontent}, NOW())")
    int addLeaveMes(LeaveMes leaveMes);

    @Delete("delete from leave_mes where pid = #{pid} and luser = #{luser} and lcontent = #{lcontent} and ltime = #{ltime}")
    int deleteLeaveMes(LeaveMes leaveMes);
}
