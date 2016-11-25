package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.DeskAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the DeskAssignment entity.
 */
@SuppressWarnings("unused")
public interface DeskAssignmentRepository extends JpaRepository<DeskAssignment,Long> {

    @Query("from DeskAssignment d where d.desk.floor.id = :floorId")
    Page<DeskAssignment> findByFloor(Pageable pageable, @Param("floorId") Long floorId);
}
