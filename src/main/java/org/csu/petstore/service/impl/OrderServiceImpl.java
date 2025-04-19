package org.csu.petstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.petstore.entity.*;
import org.csu.petstore.persistence.*;
import org.csu.petstore.service.OrderService;
import org.csu.petstore.vo.AccountVO;
import org.csu.petstore.vo.CartItem;
import org.csu.petstore.vo.CartItemListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private LineItemMapper lineItemMapper;

    @Autowired
    private ItemQuantityMapper itemQuantityMapper;


    @Override
    public List<Order> getOrdersByUsername(String username) {
        return orderMapper.selectList(new QueryWrapper<Order>().eq("userid", username));
    }

    public Order getOrderByOrderId(int orderId) {
        return orderMapper.selectOne(new QueryWrapper<Order>().eq("orderid", orderId));  // 使用 selectOne 来按其他字段查询
    }


    public List<OrderStatus> getOrderStatusByOrderId(int orderId) {
        return orderStatusMapper.selectList(new QueryWrapper<OrderStatus>().eq("orderid", orderId));
    }

    public List<LineItem> getOrderLineItemByOrderId(int orderId) {
        return lineItemMapper.selectList(new QueryWrapper<LineItem>().eq("orderid", orderId));
    }

    @Override
    public int insertOrder(Order order, AccountVO account) {
        LocalDate dateNow = LocalDate.now();
        order.setOrderDate(dateNow);
        if (order.getShipAddress1() == "") {
            order.setShipAddress1(account.getAddress1());
        }
        if (order.getShipCity() == "") {
            order.setShipCity(account.getCity());
        }
        if (order.getShipState() == "") {
            order.setShipState(account.getState());
        }
        if (order.getShipZip() == "") {
            order.setShipZip(account.getZip());
        }
        if (order.getShipCountry() == "") {
            order.setShipZip(account.getZip());
        }
        if (order.getShipToFirstName() == "") {
            order.setShipToFirstName(account.getFirstName());
        }
        if (order.getShipToLastName() == "") {
            order.setShipToLastName(account.getLastName());
        }
        order.setCreditCard("999 9999 9999 9999");

        order.setCourier("UPS");
        order.setLocale("CA");
        int result = orderMapper.insert(order);
        return result;
    }

    @Override
    public void finishOrder(Order order, CartItemListMapper cartItemListMapper) {
        OrderStatus orderStatus = new OrderStatus();

        List<CartItem> cartItemList = cartItemListMapper.getCartItemList();

        int orderId = order.getOrderId();
        orderStatus.setOrderId(orderId);
        orderStatus.setLineNum(cartItemList.size());
        orderStatus.setTimeStamp(LocalDate.now());
        orderStatus.setStatus("P");
        orderStatusMapper.insert(orderStatus);


        for (CartItem cartItem : cartItemList) {
            LineItem lineItem = new LineItem();
            ItemQuantity itemQuantity = new ItemQuantity();
            int index = cartItemList.indexOf(cartItem) + 1;
            int quantity = cartItem.getCart().getQuantity();
            String itemId = cartItem.getCart().getItemId();
            lineItem.setOrderId(orderId);
            lineItem.setItemId(itemId);
            lineItem.setLineNumber(index);
            lineItem.setQuantity(quantity);
            lineItem.setUnitPrice(cartItem.getItemVO().getListPrice());
            lineItemMapper.insert(lineItem);
            itemQuantity.setItemId(itemId);
            itemQuantity.setQuantity(itemQuantityMapper.selectById(itemId).getQuantity() - quantity);
            itemQuantityMapper.updateById(itemQuantity);
        }
    }
}
