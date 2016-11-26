package com.dstsystems.fpv.domain;


import com.dstsystems.fpv.domain.enumeration.ChangesetItemStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * A ChangesetItem - Spring Data Projection.
 */
@Projection(name = "simple", types = {ChangesetItem.class})
interface ChangesetItemProjection {

    Long getId();

    @Enumerated(EnumType.STRING)
    ChangesetItemStatus getStatus();

    @Value("#{target.employee.id}")
    Long getEmployee();

    @Value("#{target.fromDesk.id}")
    Long getFromDesk();

    @Value("#{target.toDesk.id}")
    Long getToDesk();

    @Value("#{target.changeset.id}")
    Long getChangeset();
}
