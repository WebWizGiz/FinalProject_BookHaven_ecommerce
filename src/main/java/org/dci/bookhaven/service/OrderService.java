package org.dci.bookhaven.service;

import jakarta.transaction.Transactional;
import org.dci.bookhaven.model.Order;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    @Modifying
    @Transactional
    void create(Order order);
}
