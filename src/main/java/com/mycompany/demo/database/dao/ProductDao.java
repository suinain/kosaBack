package com.mycompany.demo.database.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.demo.database.dto.Pager;
import com.mycompany.demo.database.entity.Product;

@Mapper 
public interface ProductDao {
    // Insert: 데이터를 저장하는 메소드(C)
    int insert(Product product);

    // Select: 기본키로 조회하는 메소드(R)
    Product selectById(String productId);

    // 행수를 반환하는 메소드
    int selectCount(@Param("where") Map<String, Object> where);

    // Select: 전체 데이터를 가지고 오는 메소드(R)
    List<Product> selectPage(@Param("pager") Pager pager, @Param("where") Map<String, Object> where);

    // Update: 주어진 값으로 수정하는 메소드(U)
    int update(Product product);
    
    // Delete: 기본키로 삭제하는 메소드(D)
    int deleteById(String productId);
}
