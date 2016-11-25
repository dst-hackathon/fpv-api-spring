package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Changeset;
import com.dstsystems.fpv.domain.Desk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Desk entity.
 */
@SuppressWarnings("unused")
public interface DeskRepository extends JpaRepository<Desk,Long> {

    @Query("from Desk d where d.floor.id = :floorId")
    Page<Desk> findByFloorId(Pageable pageable, @Param("floorId") Long floorId);
}
