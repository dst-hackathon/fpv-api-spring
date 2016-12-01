package com.dstsystems.fpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dstsystems.fpv.domain.Desk;
import com.dstsystems.fpv.domain.DeskAssignment;
import com.dstsystems.fpv.repository.DeskAssignmentRepository;
import com.dstsystems.fpv.service.DeskAssignmentService;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DeskAssignment.
 */
@RestController
@RequestMapping("/api")
public class DeskAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(DeskAssignmentResource.class);

    @Inject
    private DeskAssignmentService deskAssignmentService;
    
    @Inject
    private DeskAssignmentRepository deskAssignmentRepository;

    /**
     * POST  /desk-assignments : Create a new deskAssignment.
     *
     * @param deskAssignment the deskAssignment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deskAssignment, or with status 400 (Bad Request) if the deskAssignment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/desk-assignments")
    @Timed
    public ResponseEntity<DeskAssignment> createDeskAssignment(@RequestBody DeskAssignment deskAssignment) throws URISyntaxException {
        log.debug("REST request to save DeskAssignment : {}", deskAssignment);
        if (deskAssignment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("deskAssignment", "idexists", "A new deskAssignment cannot already have an ID")).body(null);
        }
        DeskAssignment result = deskAssignmentService.save(deskAssignment);
        return ResponseEntity.created(new URI("/api/desk-assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("deskAssignment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /desk-assignments : Updates an existing deskAssignment.
     *
     * @param deskAssignment the deskAssignment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deskAssignment,
     * or with status 400 (Bad Request) if the deskAssignment is not valid,
     * or with status 500 (Internal Server Error) if the deskAssignment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/desk-assignments")
    @Timed
    public ResponseEntity<DeskAssignment> updateDeskAssignment(@RequestBody DeskAssignment deskAssignment) throws URISyntaxException {
        log.debug("REST request to update DeskAssignment : {}", deskAssignment);
        if (deskAssignment.getId() == null) {
            return createDeskAssignment(deskAssignment);
        }
        DeskAssignment result = deskAssignmentService.save(deskAssignment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("deskAssignment", deskAssignment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /desk-assignments : get all the deskAssignments.
     *
     * @param pageable the pagination information
     * @param floorId the floorId information
     * @return the ResponseEntity with status 200 (OK) and the list of deskAssignments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/desk-assignments")
    @Timed
    public ResponseEntity<List<DeskAssignment>> getAllDeskAssignments(Pageable pageable,@RequestParam(required = false) Long floorId)
        throws URISyntaxException {
        log.debug("REST request to get a page of DeskAssignments");
        Page<DeskAssignment> page = deskAssignmentService.findAll(pageable,floorId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desk-assignments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desk-assignments/:id : get the "id" deskAssignment.
     *
     * @param id the id of the deskAssignment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deskAssignment, or with status 404 (Not Found)
     */
    @GetMapping("/desk-assignments/{id}")
    @Timed
    public ResponseEntity<DeskAssignment> getDeskAssignment(@PathVariable Long id) {
        log.debug("REST request to get DeskAssignment : {}", id);
        DeskAssignment deskAssignment = deskAssignmentService.findOne(id);
        return Optional.ofNullable(deskAssignment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /desk-assignments/:id : delete the "id" deskAssignment.
     *
     * @param id the id of the deskAssignment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desk-assignments/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeskAssignment(@PathVariable Long id) {
        log.debug("REST request to delete DeskAssignment : {}", id);
        deskAssignmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deskAssignment", id.toString())).build();
    }
    
    @GetMapping("/desk-assignments/search/desk")
    @Timed
    public ResponseEntity<Desk> getCurrentDeskAssignment(@RequestParam Long employeeId, @RequestParam Long planId) {
    	DeskAssignment deskAssignment = deskAssignmentRepository.findByEmployeeAndPlan(employeeId, planId);
    	
    	return new ResponseEntity<>(
			Optional.ofNullable(deskAssignment)
				.map(result -> result.getDesk())
				.orElse(null),
			HttpStatus.OK);
    }
}
