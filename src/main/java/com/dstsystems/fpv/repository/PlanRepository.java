package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Plan;

import com.dstsystems.fpv.domain.enumeration.PlanStatus;
import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Plan entity.
 */
@SuppressWarnings("unused")
public interface PlanRepository extends JpaRepository<Plan,Long> {
    Plan findFirstByStatusAndEffectiveDateIsLessThanOrderByEffectiveDateDesc(PlanStatus status, LocalDate effectiveDate);
}
