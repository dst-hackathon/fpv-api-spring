package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Building entity.
 */
@SuppressWarnings("unused")
public interface BuildingRepository extends JpaRepository<Building,Long> {

}
