package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Building entity.
 */
@SuppressWarnings("unused")
public interface BuildingRepository extends JpaRepository<Building,Long> {

    @Query("from Building b where b.plan.id = :planId")
    Page<Building> findByPlanId(Pageable pageable, @Param("planId") Long planId);
}
