package com.danaga.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danaga.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByOptionSets_Id(Long optionSetId);
}
