package com.rcn.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rcn.order.entities.OrderData;

import java.util.List;


@Repository
public interface OrderDataRepository extends JpaRepository<OrderData, Object> {

    public List<OrderData> findByFarmId(String farmId);
}
