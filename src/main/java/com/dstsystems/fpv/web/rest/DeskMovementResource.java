package com.dstsystems.fpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dstsystems.fpv.domain.DeskMovement;
import com.dstsystems.fpv.service.DeskMovementService;
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
 * REST controller for managing DeskMovement.
 */
@RestController
@RequestMapping("/api")
public class DeskMovementResource {

    private final Logger log = LoggerFactory.getLogger(DeskMovementResource.class);
        
    @Inject
    private DeskMovementService deskMovementService;

    /**
     * POST  /desk-movements : Create a new deskMovement.
     *
     * @param deskMovement the deskMovement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deskMovement, or with status 400 (Bad Request) if the deskMovement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/desk-movements")
    @Timed
    public ResponseEntity<DeskMovement> createDeskMovement(@RequestBody DeskMovement deskMovement) throws URISyntaxException {
        log.debug("REST request to save DeskMovement : {}", deskMovement);
        if (deskMovement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("deskMovement", "idexists", "A new deskMovement cannot already have an ID")).body(null);
        }
        DeskMovement result = deskMovementService.save(deskMovement);
        return ResponseEntity.created(new URI("/api/desk-movements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("deskMovement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /desk-movements : Updates an existing deskMovement.
     *
     * @param deskMovement the deskMovement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deskMovement,
     * or with status 400 (Bad Request) if the deskMovement is not valid,
     * or with status 500 (Internal Server Error) if the deskMovement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/desk-movements")
    @Timed
    public ResponseEntity<DeskMovement> updateDeskMovement(@RequestBody DeskMovement deskMovement) throws URISyntaxException {
        log.debug("REST request to update DeskMovement : {}", deskMovement);
        if (deskMovement.getId() == null) {
            return createDeskMovement(deskMovement);
        }
        DeskMovement result = deskMovementService.save(deskMovement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("deskMovement", deskMovement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /desk-movements : get all the deskMovements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deskMovements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/desk-movements")
    @Timed
    public ResponseEntity<List<DeskMovement>> getAllDeskMovements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DeskMovements");
        Page<DeskMovement> page = deskMovementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desk-movements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desk-movements/:id : get the "id" deskMovement.
     *
     * @param id the id of the deskMovement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deskMovement, or with status 404 (Not Found)
     */
    @GetMapping("/desk-movements/{id}")
    @Timed
    public ResponseEntity<DeskMovement> getDeskMovement(@PathVariable Long id) {
        log.debug("REST request to get DeskMovement : {}", id);
        DeskMovement deskMovement = deskMovementService.findOne(id);
        return Optional.ofNullable(deskMovement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /desk-movements/:id : delete the "id" deskMovement.
     *
     * @param id the id of the deskMovement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desk-movements/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeskMovement(@PathVariable Long id) {
        log.debug("REST request to delete DeskMovement : {}", id);
        deskMovementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deskMovement", id.toString())).build();
    }

}
