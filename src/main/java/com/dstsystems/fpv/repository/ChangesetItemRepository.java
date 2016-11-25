package com.dstsystems.fpv.repository;

import com.dstsystems.fpv.domain.Changeset;
import com.dstsystems.fpv.domain.ChangesetItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ChangesetItem entity.
 */
@SuppressWarnings("unused")
public interface ChangesetItemRepository extends JpaRepository<ChangesetItem,Long> {

    @Query("from ChangesetItem c where c.changeset.id = :changesetId")
    Page<ChangesetItem> findByChangesetId(Pageable pageable, @Param("changesetId") Long changesetId);
}
