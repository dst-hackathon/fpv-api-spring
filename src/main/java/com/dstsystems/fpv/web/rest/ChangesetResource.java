package com.dstsystems.fpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dstsystems.fpv.domain.Changeset;
import com.dstsystems.fpv.service.ChangesetService;
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
 * REST controller for managing Changeset.
 */
@RestController
@RequestMapping("/api")
public class ChangesetResource {

    private final Logger log = LoggerFactory.getLogger(ChangesetResource.class);

    @Inject
    private ChangesetService changesetService;

    /**
     * POST  /changesets : Create a new changeset.
     *
     * @param changeset the changeset to create
     * @return the ResponseEntity with status 201 (Created) and with body the new changeset, or with status 400 (Bad Request) if the changeset has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/changesets")
    @Timed
    public ResponseEntity<Changeset> createChangeset(@Valid @RequestBody Changeset changeset) throws URISyntaxException {
        log.debug("REST request to save Changeset : {}", changeset);
        if (changeset.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("changeset", "idexists", "A new changeset cannot already have an ID")).body(null);
        }
        Changeset result = changesetService.save(changeset);
        return ResponseEntity.created(new URI("/api/changesets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("changeset", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /changesets : Updates an existing changeset.
     *
     * @param changeset the changeset to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated changeset,
     * or with status 400 (Bad Request) if the changeset is not valid,
     * or with status 500 (Internal Server Error) if the changeset couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/changesets")
    @Timed
    public ResponseEntity<Changeset> updateChangeset(@Valid @RequestBody Changeset changeset) throws URISyntaxException {
        log.debug("REST request to update Changeset : {}", changeset);
        if (changeset.getId() == null) {
            return createChangeset(changeset);
        }
        Changeset result = changesetService.save(changeset);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("changeset", changeset.getId().toString()))
            .body(result);
    }

    /**
     * GET  /changesets : get all the changesets.
     *
     * @param pageable the pagination information
     * @param planId the planId information
     * @return the ResponseEntity with status 200 (OK) and the list of changesets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/changesets")
    @Timed
    public ResponseEntity<List<Changeset>> getAllChangesets(Pageable pageable,@RequestParam(required = false) Long planId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Changesets");
        Page<Changeset> page = changesetService.findAll(pageable,planId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/changesets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /changesets/:id : get the "id" changeset.
     *
     * @param id the id of the changeset to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the changeset, or with status 404 (Not Found)
     */
    @GetMapping("/changesets/{id}")
    @Timed
    public ResponseEntity<Changeset> getChangeset(@PathVariable Long id) {
        log.debug("REST request to get Changeset : {}", id);
        Changeset changeset = changesetService.findOne(id);
        return Optional.ofNullable(changeset)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /changesets/:id : delete the "id" changeset.
     *
     * @param id the id of the changeset to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/changesets/{id}")
    @Timed
    public ResponseEntity<Void> deleteChangeset(@PathVariable Long id) {
        log.debug("REST request to delete Changeset : {}", id);
        changesetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("changeset", id.toString())).build();
    }

}
