package com.mycompany.demo.database.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mycompany.demo.database.dao.ReviewDao;
import com.mycompany.demo.database.dao.ProductDao;
import com.mycompany.demo.database.dao.SellerDao;
import com.mycompany.demo.database.dto.Pager;
import com.mycompany.demo.database.entity.Review;
import com.mycompany.demo.database.entity.Product;
import com.mycompany.demo.database.entity.Seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service  
@RequiredArgsConstructor 
@Slf4j 
public class ProductService {
    private final ProductDao productDao;
    private final SellerDao sellerDao;
    private final ReviewDao reviewDao;

    // Seller CRUD 메소드 ---------------------------------------
    public int createSeller(Seller seller) {
        return sellerDao.insert(seller);
    }

    public Seller getSeller(long sellerId) {
        return sellerDao.selectById(sellerId);
    }

    public int updateSeller(Seller seller) {
        return sellerDao.update(seller);
    }

    public int deleteSeller(long sellerId) {
        return sellerDao.deleteById(sellerId);
    }

    public List<Seller> listSeller(Pager pager, Map<String, Object> where) {
        return sellerDao.selectPage(pager, where);
    }

    public int pagingTotalRows(Map<String, Object> where) {
        return sellerDao.selectCount(where);
    }

    // Product CRUD 메소드 ---------------------------------------
    public int createProduct(Product product) {
        return productDao.insert(product);
    }

    public Product getProduct(String productId) {
        return productDao.selectById(productId);
    }

    public int updateProduct(Product product) {
        return productDao.update(product);
    }

    public int deleteProduct(String productId) {
        return productDao.deleteById(productId);
    }

    public List<Product> listProduct(Pager pager, Map<String, Object> where) {
        return productDao.selectPage(pager, where);
    }

    public int pagingProductTotalRows(Map<String, Object> where) {
        return productDao.selectCount(where);
    }

    // Review CRUD 메소드 ---------------------------------------
    public int createBoard(Review review) {
        return reviewDao.insert(review);
    }

    public Review getBoard(long boardId) {
        return reviewDao.selectById(boardId);
    }

    public int updateBoard(Review review) {
        return reviewDao.update(review);
    }

    public int deleteBoard(String boardId) {
        return reviewDao.deleteById(boardId);
    }

    public List<Review> listBoard(Pager pager, Map<String, Object> where) {
        return reviewDao.selectPage(pager, where);
    }

    public int pagingBoardTotalRows(Map<String, Object> where) {
        return reviewDao.selectCount(where);
    }
}
