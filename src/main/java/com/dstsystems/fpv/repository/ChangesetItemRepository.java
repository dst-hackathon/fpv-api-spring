package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.ChangesetItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChangesetItem entity.
 */
@SuppressWarnings("unused")
public interface ChangesetItemRepository extends JpaRepository<ChangesetItem,Long> {

}
