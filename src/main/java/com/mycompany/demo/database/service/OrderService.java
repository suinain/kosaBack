package com.mycompany.demo.database.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mycompany.demo.database.dao.OrderItemDao;
import com.mycompany.demo.database.dao.OrdersDao;
import com.mycompany.demo.database.dto.Pager;
import com.mycompany.demo.database.entity.OrderItem;
import com.mycompany.demo.database.entity.Orders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor
@Slf4j 
public class OrderService {
    private final OrdersDao ordersDao;
    private final OrderItemDao orderItemDao;

    // Orders CRUD 메소드 ---------------------------------------
    public int createOrders(Orders orders) {
        return ordersDao.insert(orders);
    }

    public Orders getOrders(long orderId) {
        return ordersDao.selectById(orderId);
    }

    public int updateOrders(Orders orders) {
        return ordersDao.update(orders);
    }

    public int deleteOrders(long orderId) {
        return ordersDao.deleteById(orderId);
    }

    public List<Orders> listOrders(Pager pager, Map<String, Object> where) {
        return ordersDao.selectPage(pager, where);
    }

    public int pagingOrdersTotalRows(Map<String, Object> where) {
        return ordersDao.selectCount(where);
    }

    // OrderItem CRUD 메소드 ---------------------------------------
    public int createOrderItem(OrderItem orderItem) {
        return orderItemDao.insert(orderItem);
    }

    public OrderItem getOrderItem(OrderItem orderItem) {
        return orderItemDao.selectById(orderItem);
    }

    public int updateOrderItem(OrderItem orderItem) {
        return orderItemDao.update(orderItem);
    }

    public int deleteOrderItem(OrderItem orderItem) {
        return orderItemDao.deleteById(orderItem);
    }

    public List<OrderItem> listOrderItem(Pager pager, Map<String, Object> where) {
        return orderItemDao.selectPage(pager, where);
    }

    public int pagingOrderItemTotalRows(Map<String, Object> where) {
        return orderItemDao.selectCount(where);
    }
}
