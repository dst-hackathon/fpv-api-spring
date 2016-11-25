package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Floor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Floor entity.
 */
@SuppressWarnings("unused")
public interface FloorRepository extends JpaRepository<Floor,Long> {

    @Query("from Floor f where f.building.id = :buildingId")
    Page<Floor> findByBuilding(Pageable pageable, @Param("buildingId") Long buildingId);
}
