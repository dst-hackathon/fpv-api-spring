package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.DeskAssignment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DeskAssignment entity.
 */
@SuppressWarnings("unused")
public interface DeskAssignmentRepository extends JpaRepository<DeskAssignment,Long> {

}
