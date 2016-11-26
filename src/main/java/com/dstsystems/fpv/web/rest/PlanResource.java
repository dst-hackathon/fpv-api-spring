package com.dstsystems.fpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dstsystems.fpv.domain.Plan;
import com.dstsystems.fpv.domain.enumeration.PlanStatus;
import com.dstsystems.fpv.service.PlanService;
import com.dstsystems.fpv.web.rest.util.HeaderUtil;
import com.dstsystems.fpv.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Plan.
 */
@RestController
@RequestMapping("/api")
public class PlanResource {

    private final Logger log = LoggerFactory.getLogger(PlanResource.class);

    @Inject
    private PlanService planService;

    /**
     * POST  /plans : Create a new plan.
     *
     * @param plan the plan to create
     * @return the ResponseEntity with status 201 (Created) and with body the new plan, or with status 400 (Bad Request) if the plan has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/plans")
    @Timed
    public ResponseEntity<Plan> createPlan(@Valid @RequestBody Plan plan) throws URISyntaxException {
        log.debug("REST request to save Plan : {}", plan);
        if (plan.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("plan", "idexists", "A new plan cannot already have an ID")).body(null);
        }

        Plan result;

        if(plan.isCloned() != null && plan.isCloned() == true) {
            result = planService.cloneLatestApprovedPlan(plan);
        } else {
            result = planService.save(plan);
        }
        return ResponseEntity.created(new URI("/api/plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("plan", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /plans : Updates an existing plan.
     *
     * @param plan the plan to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated plan,
     * or with status 400 (Bad Request) if the plan is not valid,
     * or with status 500 (Internal Server Error) if the plan couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/plans")
    @Timed
    public ResponseEntity<Plan> updatePlan(@Valid @RequestBody Plan plan) throws URISyntaxException {
        log.debug("REST request to update Plan : {}", plan);
        if (plan.getId() == null) {
            return createPlan(plan);
        }
        Plan result = planService.save(plan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("plan", plan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /plans : get all the plans.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of plans in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/plans")
    @Timed
    public ResponseEntity<List<Plan>> getAllPlans(Pageable pageable, @RequestParam(required = false) PlanStatus status)
        throws URISyntaxException {
        log.debug("REST request to get a page of Plans");
        
        Page<Plan> page;
        if (status != null) {
        	page = planService.findByStatus(status, pageable);
        } else {
        	page = planService.findAll(pageable);
        }
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/plans");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /plans/:id : get the "id" plan.
     *
     * @param id the id of the plan to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the plan, or with status 404 (Not Found)
     */
    @GetMapping("/plans/{id}")
    @Timed
    public ResponseEntity<Plan> getPlan(@PathVariable Long id) {
        log.debug("REST request to get Plan : {}", id);
        Plan plan = planService.findOne(id);
        return Optional.ofNullable(plan)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /plans/:id : delete the "id" plan.
     *
     * @param id the id of the plan to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/plans/{id}")
    @Timed
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        log.debug("REST request to delete Plan : {}", id);
        planService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("plan", id.toString())).build();
    }
    
    @GetMapping("/plans/master")
    @Timed
    public ResponseEntity<Plan> getMasterPlan() {
    	log.debug("REST request to get a master plan");
    	Plan plan = planService.getMasterPlan();
    	
    	return Optional.ofNullable(plan)
    		.map(result -> new ResponseEntity<>(
				result,
				HttpStatus.OK))
    		.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
