package com.example.demo.mapper;

import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("select * from product")
    List<Product> selectAllProduct();

    @Select("""
        <script>
            select * from product
            <where>
                <if test="keyword != null and keyword != ''">
                    and pname like concat('%', #{keyword}, '%')
                </if>
            </where>
        </script>
    """)
    List<Product> selectProductByKeyword(String keyword);

    @Select("select * from product where pid = #{pid}")
    Product selectProductById(int pid);

    @Insert("insert into product(uname, pname, depict, image, price, publish) values(#{uname}, #{pname}, #{depict}, #{image}, #{price}, NOW())")
    int insertProduct(Product product);

    @Select("select * from product where uname = #{uname}")
    List<Product> selectProductByUname(String uname);

    @Update("update product set pname = #{pname}, depict = #{depict}, image = #{image}, price = #{price} where pid = #{pid}")
    int updateProduct(Product product);

    @Update("delete from product where pid = #{pid}")
    int deleteProductById(int pid);
}
