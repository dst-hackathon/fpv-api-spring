package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Building;
import com.dstsystems.fpv.domain.Changeset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

/**
 * Spring Data JPA repository for the Changeset entity.
 */
@SuppressWarnings("unused")
public interface ChangesetRepository extends JpaRepository<Changeset,Long> {

    @Query("from Changeset c where c.plan.id = :planId")
    Page<Changeset> findByPlanId(Pageable pageable, @Param("planId") Long planId);

    Changeset findByEffectiveDate(LocalDate date);
}
