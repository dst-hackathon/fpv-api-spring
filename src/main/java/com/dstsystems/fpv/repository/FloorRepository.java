package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Floor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Floor entity.
 */
@SuppressWarnings("unused")
public interface FloorRepository extends JpaRepository<Floor,Long> {

}
