package com.dstsystems.fpv.service;

import com.dstsystems.fpv.domain.Plan;
import com.dstsystems.fpv.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Plan.
 */
@Service
@Transactional
public class PlanService {

    private final Logger log = LoggerFactory.getLogger(PlanService.class);
    
    @Inject
    private PlanRepository planRepository;

    /**
     * Save a plan.
     *
     * @param plan the entity to save
     * @return the persisted entity
     */
    public Plan save(Plan plan) {
        log.debug("Request to save Plan : {}", plan);
        Plan result = planRepository.save(plan);
        return result;
    }

    /**
     *  Get all the plans.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Plan> findAll(Pageable pageable) {
        log.debug("Request to get all Plans");
        Page<Plan> result = planRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one plan by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Plan findOne(Long id) {
        log.debug("Request to get Plan : {}", id);
        Plan plan = planRepository.findOne(id);
        return plan;
    }

    /**
     *  Delete the  plan by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Plan : {}", id);
        planRepository.delete(id);
    }
}
