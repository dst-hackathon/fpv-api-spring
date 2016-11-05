package com.dstsystems.fpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dstsystems.fpv.domain.Shadow;
import com.dstsystems.fpv.service.ShadowService;
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
 * REST controller for managing Shadow.
 */
@RestController
@RequestMapping("/api")
public class ShadowResource {

    private final Logger log = LoggerFactory.getLogger(ShadowResource.class);
        
    @Inject
    private ShadowService shadowService;

    /**
     * POST  /shadows : Create a new shadow.
     *
     * @param shadow the shadow to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shadow, or with status 400 (Bad Request) if the shadow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shadows")
    @Timed
    public ResponseEntity<Shadow> createShadow(@RequestBody Shadow shadow) throws URISyntaxException {
        log.debug("REST request to save Shadow : {}", shadow);
        if (shadow.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shadow", "idexists", "A new shadow cannot already have an ID")).body(null);
        }
        Shadow result = shadowService.save(shadow);
        return ResponseEntity.created(new URI("/api/shadows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shadow", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shadows : Updates an existing shadow.
     *
     * @param shadow the shadow to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shadow,
     * or with status 400 (Bad Request) if the shadow is not valid,
     * or with status 500 (Internal Server Error) if the shadow couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shadows")
    @Timed
    public ResponseEntity<Shadow> updateShadow(@RequestBody Shadow shadow) throws URISyntaxException {
        log.debug("REST request to update Shadow : {}", shadow);
        if (shadow.getId() == null) {
            return createShadow(shadow);
        }
        Shadow result = shadowService.save(shadow);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shadow", shadow.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shadows : get all the shadows.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of shadows in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/shadows")
    @Timed
    public ResponseEntity<List<Shadow>> getAllShadows(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Shadows");
        Page<Shadow> page = shadowService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shadows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /shadows/:id : get the "id" shadow.
     *
     * @param id the id of the shadow to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shadow, or with status 404 (Not Found)
     */
    @GetMapping("/shadows/{id}")
    @Timed
    public ResponseEntity<Shadow> getShadow(@PathVariable Long id) {
        log.debug("REST request to get Shadow : {}", id);
        Shadow shadow = shadowService.findOne(id);
        return Optional.ofNullable(shadow)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shadows/:id : delete the "id" shadow.
     *
     * @param id the id of the shadow to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shadows/{id}")
    @Timed
    public ResponseEntity<Void> deleteShadow(@PathVariable Long id) {
        log.debug("REST request to delete Shadow : {}", id);
        shadowService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shadow", id.toString())).build();
    }

}
