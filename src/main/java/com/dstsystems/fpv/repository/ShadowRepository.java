package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Shadow;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Shadow entity.
 */
@SuppressWarnings("unused")
public interface ShadowRepository extends JpaRepository<Shadow,Long> {

}
