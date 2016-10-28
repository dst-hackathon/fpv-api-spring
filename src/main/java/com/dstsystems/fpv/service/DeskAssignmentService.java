package com.dstsystems.fpv.service;

import com.dstsystems.fpv.domain.DeskAssignment;
import com.dstsystems.fpv.repository.DeskAssignmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing DeskAssignment.
 */
@Service
@Transactional
public class DeskAssignmentService {

    private final Logger log = LoggerFactory.getLogger(DeskAssignmentService.class);
    
    @Inject
    private DeskAssignmentRepository deskAssignmentRepository;

    /**
     * Save a deskAssignment.
     *
     * @param deskAssignment the entity to save
     * @return the persisted entity
     */
    public DeskAssignment save(DeskAssignment deskAssignment) {
        log.debug("Request to save DeskAssignment : {}", deskAssignment);
        DeskAssignment result = deskAssignmentRepository.save(deskAssignment);
        return result;
    }

    /**
     *  Get all the deskAssignments.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DeskAssignment> findAll(Pageable pageable) {
        log.debug("Request to get all DeskAssignments");
        Page<DeskAssignment> result = deskAssignmentRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one deskAssignment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DeskAssignment findOne(Long id) {
        log.debug("Request to get DeskAssignment : {}", id);
        DeskAssignment deskAssignment = deskAssignmentRepository.findOne(id);
        return deskAssignment;
    }

    /**
     *  Delete the  deskAssignment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DeskAssignment : {}", id);
        deskAssignmentRepository.delete(id);
    }
}
