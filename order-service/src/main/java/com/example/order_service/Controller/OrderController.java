package com.example.order_service.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.client.ProductClient;
import com.example.order_service.client.UserClient;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.dto.User;
import com.example.order_service.dto.Product;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @PostMapping
    public String createOrder(@RequestBody Order order) {
        orderRepository.save(order);
        return "Order created successfully" + order.getId();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow();

        // Fetch user and product
        User user = userClient.getUserById(order.getUserId());
        Product product = productClient.getProductById(order.getProductId());

        // Return combined response
        return new OrderResponse(
                order.getId(),
                user.getName(),
                user.getEmail(),
                product.getName());
    }

}
