package com.jhipster.bachelor.orders.repository;

import com.jhipster.bachelor.orders.domain.CompleteOrder;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompleteOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompleteOrderRepository extends JpaRepository<CompleteOrder, Long> {
	
	List<CompleteOrder> findByCustomerId(long customerId);
}
