package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Changeset;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Changeset entity.
 */
@SuppressWarnings("unused")
public interface ChangesetRepository extends JpaRepository<Changeset,Long> {

}
