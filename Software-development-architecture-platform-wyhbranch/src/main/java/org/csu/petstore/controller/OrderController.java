package org.csu.petstore.controller;

import jakarta.servlet.http.HttpSession;
import org.csu.petstore.common.CommonResponse;
import org.csu.petstore.entity.Item;
import org.csu.petstore.entity.LineItem;
import org.csu.petstore.entity.Order;
import org.csu.petstore.entity.OrderStatus;
import org.csu.petstore.service.CartService;
import org.csu.petstore.service.CatalogService;
import org.csu.petstore.service.OrderService;
import org.csu.petstore.vo.AccountVO;
import org.csu.petstore.vo.CartItemListMapper;
import org.csu.petstore.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private HttpSession session;

    // 创建新订单
    @GetMapping("/orders")
    public CommonResponse<Order> createOrder() {
        Order order = (Order) session.getAttribute("order");
        return CommonResponse.createForSuccess("Order Initialized Successfully", order);
    }

    // 确认订单
    @PostMapping("/orders")
    public CommonResponse<Order> confirmOrder(@RequestBody Order order) {
        AccountVO account = (AccountVO) session.getAttribute("account");
        LocalDate localDate = LocalDate.now();
        String userId = account.getUsername();
        order.setUsername(userId);
        order.setTotalPrice((BigDecimal) session.getAttribute("subTotal"));
        order.setOrderDate(localDate);

        session.setAttribute("order", order);
        cartService.clearCart(userId);

        CartItemListMapper cartItemListMapper = (CartItemListMapper) session.getAttribute("cartItemListMapper");
        int ret = orderService.insertOrder(order, account);
        if (ret != 1) {
            return CommonResponse.createForSuccessMessage("Order Created Failed");
        }
        orderService.finishOrder(order, cartItemListMapper);

        return CommonResponse.createForSuccess("Order Confirmed Successfully", order);
    }

    // 查看用户所有订单
    @GetMapping("/users/orders")
    public CommonResponse<List<Order>> listOrder(@RequestParam String username) {
        List<Order> orderList = orderService.getOrdersByUsername(username);

        if (orderList.size() == 0) {
            return CommonResponse.createForError("User has no orders");
        }

        return CommonResponse.createForSuccess(orderList);
    }

    // 查看特定的单个订单
    @GetMapping("/orders/{id}")
    public CommonResponse<Map<String, Object>> viewTheOrder(@PathVariable int id) {
        Order order = orderService.getOrderByOrderId(id);

        if (order == null || order.getOrderId() != id) {
            return CommonResponse.createForError("Order not found or ID mismatch");
        }

        List<OrderStatus> orderStatus = orderService.getOrderStatusByOrderId(id);
        List<LineItem> lineItemList = orderService.getOrderLineItemByOrderId(id);
        List<ItemVO> itemList = new ArrayList<>();
        for (LineItem lineItem : lineItemList) {
            itemList.add(catalogService.getItem(lineItem.getItemId()));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderStatus", orderStatus);
        result.put("lineItems", lineItemList);
        result.put("itemDetails", itemList);

        return CommonResponse.createForSuccess(result);
    }
}
