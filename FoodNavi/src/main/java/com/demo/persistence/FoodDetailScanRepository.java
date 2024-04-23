package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.FoodDetail;

public interface FoodDetailScanRepository extends JpaRepository<FoodDetail, Integer> {

}
