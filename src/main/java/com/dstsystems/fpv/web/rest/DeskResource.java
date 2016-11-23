package com.dstsystems.fpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dstsystems.fpv.domain.Desk;
import com.dstsystems.fpv.service.DeskService;
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
 * REST controller for managing Desk.
 */
@RestController
@RequestMapping("/api")
public class DeskResource {

    private final Logger log = LoggerFactory.getLogger(DeskResource.class);

    @Inject
    private DeskService deskService;

    /**
     * POST  /desks : Create a new desk.
     *
     * @param desk the desk to create
     * @return the ResponseEntity with status 201 (Created) and with body the new desk, or with status 400 (Bad Request) if the desk has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/desks")
    @Timed
    public ResponseEntity<Desk> createDesk(@Valid @RequestBody Desk desk) throws URISyntaxException {
        log.debug("REST request to save Desk : {}", desk);
        if (desk.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("desk", "idexists", "A new desk cannot already have an ID")).body(null);
        }
        Desk result = deskService.save(desk);
        return ResponseEntity.created(new URI("/api/desks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("desk", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /desks : Updates an existing desk.
     *
     * @param desk the desk to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated desk,
     * or with status 400 (Bad Request) if the desk is not valid,
     * or with status 500 (Internal Server Error) if the desk couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/desks")
    @Timed
    public ResponseEntity<Desk> updateDesk(@Valid @RequestBody Desk desk) throws URISyntaxException {
        log.debug("REST request to update Desk : {}", desk);
        if (desk.getId() == null) {
            return createDesk(desk);
        }
        Desk result = deskService.save(desk);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("desk", desk.getId().toString()))
            .body(result);
    }

    /**
     * GET  /desks : get all the desks.
     *
     * @param pageable the pagination information
     * @param floorId the floorId information
     * @return the ResponseEntity with status 200 (OK) and the list of desks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/desks")
    @Timed
    public ResponseEntity<List<Desk>> getAllDesks(Pageable pageable,Long floorId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Desks");
        Page<Desk> page = deskService.findAll(pageable,floorId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desks/:id : get the "id" desk.
     *
     * @param id the id of the desk to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the desk, or with status 404 (Not Found)
     */
    @GetMapping("/desks/{id}")
    @Timed
    public ResponseEntity<Desk> getDesk(@PathVariable Long id) {
        log.debug("REST request to get Desk : {}", id);
        Desk desk = deskService.findOne(id);
        return Optional.ofNullable(desk)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /desks/:id : delete the "id" desk.
     *
     * @param id the id of the desk to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desks/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesk(@PathVariable Long id) {
        log.debug("REST request to delete Desk : {}", id);
        deskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("desk", id.toString())).build();
    }

}
