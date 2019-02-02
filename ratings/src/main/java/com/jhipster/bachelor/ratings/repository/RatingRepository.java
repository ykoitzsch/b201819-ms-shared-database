package com.jhipster.bachelor.ratings.repository;

import com.jhipster.bachelor.ratings.domain.Rating;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

	List<Rating> findByProductId(long productId);
}
