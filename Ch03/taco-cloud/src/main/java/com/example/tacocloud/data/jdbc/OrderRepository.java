package com.example.tacocloud.data.jdbc;

import com.example.tacocloud.Order;

public interface OrderRepository {
    Order save(Order order);
}
