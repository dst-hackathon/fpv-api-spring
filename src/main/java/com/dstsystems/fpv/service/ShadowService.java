package com.dstsystems.fpv.service;

import com.dstsystems.fpv.domain.Shadow;
import com.dstsystems.fpv.repository.ShadowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Shadow.
 */
@Service
@Transactional
public class ShadowService {

    private final Logger log = LoggerFactory.getLogger(ShadowService.class);
    
    @Inject
    private ShadowRepository shadowRepository;

    /**
     * Save a shadow.
     *
     * @param shadow the entity to save
     * @return the persisted entity
     */
    public Shadow save(Shadow shadow) {
        log.debug("Request to save Shadow : {}", shadow);
        Shadow result = shadowRepository.save(shadow);
        return result;
    }

    /**
     *  Get all the shadows.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Shadow> findAll(Pageable pageable) {
        log.debug("Request to get all Shadows");
        Page<Shadow> result = shadowRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one shadow by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Shadow findOne(Long id) {
        log.debug("Request to get Shadow : {}", id);
        Shadow shadow = shadowRepository.findOne(id);
        return shadow;
    }

    /**
     *  Delete the  shadow by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Shadow : {}", id);
        shadowRepository.delete(id);
    }
}
