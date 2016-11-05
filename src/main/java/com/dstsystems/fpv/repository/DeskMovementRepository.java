package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.DeskMovement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DeskMovement entity.
 */
@SuppressWarnings("unused")
public interface DeskMovementRepository extends JpaRepository<DeskMovement,Long> {

}
