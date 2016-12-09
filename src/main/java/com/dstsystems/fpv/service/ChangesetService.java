package com.dstsystems.fpv.service;

import com.dstsystems.fpv.domain.Changeset;
import com.dstsystems.fpv.domain.ChangesetItem;
import com.dstsystems.fpv.domain.DeskAssignment;
import com.dstsystems.fpv.domain.enumeration.ChangesetStatus;
import com.dstsystems.fpv.repository.ChangesetRepository;
import com.dstsystems.fpv.repository.DeskAssignmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing Changeset.
 */
@Service
@Transactional
public class ChangesetService {

    private final Logger log = LoggerFactory.getLogger(ChangesetService.class);

    @Inject
    private ChangesetRepository changesetRepository;
    @Inject
    private DeskAssignmentRepository deskAssignmentRepository;

    /**
     * Save a changeset.
     *
     * @param changeset the entity to save
     * @return the persisted entity
     */
    public Changeset save(Changeset changeset) {
        log.debug("Request to save Changeset : {}", changeset);
        Changeset result = changesetRepository.save(changeset);
        return result;
    }

    /**
     *  Get all the changesets.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Changeset> findAll(Pageable pageable,Long planId) {
        log.debug("Request to get all Changesets");

        if (planId!=null){
            return changesetRepository.findByPlanId(pageable,planId);
        }

        Page<Changeset> result = changesetRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one changeset by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Changeset findOne(Long id) {
        log.debug("Request to get Changeset : {}", id);
        Changeset changeset = changesetRepository.findOne(id);
        return changeset;
    }

    /**
     *  Delete the  changeset by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Changeset : {}", id);
        changesetRepository.delete(id);
    }

    @Transactional
    public Changeset approve(Long id) {
        Changeset changeset = changesetRepository.findOne(id);

        for (ChangesetItem item : changeset.getChangesetItems()) {
            DeskAssignment existingAssign = deskAssignmentRepository.findByDeskAndEmployee(item.getFromDesk(),item.getEmployee());

            if (existingAssign==null){
                existingAssign = new DeskAssignment();
                existingAssign.setEmployee(item.getEmployee());
                existingAssign.setPlan(item.getChangeset().getPlan());
            }

            existingAssign.setDesk(item.getToDesk());
            deskAssignmentRepository.save(existingAssign);
        };

        changeset.setStatus(ChangesetStatus.COMPLETE);
        changesetRepository.save(changeset);

        return changeset;
    }
}
