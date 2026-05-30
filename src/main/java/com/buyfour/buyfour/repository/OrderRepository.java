package com.buyfour.buyfour.repository;


import com.buyfour.buyfour.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order ,Long> {

    List<Order> findByUserUserId(Long userId);
}
