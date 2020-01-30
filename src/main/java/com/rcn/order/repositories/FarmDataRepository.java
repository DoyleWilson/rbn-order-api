package com.rcn.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rcn.order.entities.FarmData;


@Repository
public interface FarmDataRepository extends JpaRepository<FarmData, Object> {
}
