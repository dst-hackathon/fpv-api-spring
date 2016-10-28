package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Desk;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Desk entity.
 */
@SuppressWarnings("unused")
public interface DeskRepository extends JpaRepository<Desk,Long> {

}
