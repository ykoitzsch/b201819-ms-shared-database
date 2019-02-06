package com.jhipster.bachelor.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhipster.bachelor.inventory.domain.Rating;

/**
 * Spring Data repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

  List<Rating> findByProductId(long productId);
}
