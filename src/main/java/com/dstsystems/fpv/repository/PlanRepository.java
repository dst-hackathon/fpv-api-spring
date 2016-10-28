package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Plan;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Plan entity.
 */
@SuppressWarnings("unused")
public interface PlanRepository extends JpaRepository<Plan,Long> {

}
