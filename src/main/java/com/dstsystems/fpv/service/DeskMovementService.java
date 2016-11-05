package com.dstsystems.fpv.service;

import com.dstsystems.fpv.domain.DeskMovement;
import com.dstsystems.fpv.repository.DeskMovementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing DeskMovement.
 */
@Service
@Transactional
public class DeskMovementService {

    private final Logger log = LoggerFactory.getLogger(DeskMovementService.class);
    
    @Inject
    private DeskMovementRepository deskMovementRepository;

    /**
     * Save a deskMovement.
     *
     * @param deskMovement the entity to save
     * @return the persisted entity
     */
    public DeskMovement save(DeskMovement deskMovement) {
        log.debug("Request to save DeskMovement : {}", deskMovement);
        DeskMovement result = deskMovementRepository.save(deskMovement);
        return result;
    }

    /**
     *  Get all the deskMovements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DeskMovement> findAll(Pageable pageable) {
        log.debug("Request to get all DeskMovements");
        Page<DeskMovement> result = deskMovementRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one deskMovement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DeskMovement findOne(Long id) {
        log.debug("Request to get DeskMovement : {}", id);
        DeskMovement deskMovement = deskMovementRepository.findOne(id);
        return deskMovement;
    }

    /**
     *  Delete the  deskMovement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DeskMovement : {}", id);
        deskMovementRepository.delete(id);
    }
}
