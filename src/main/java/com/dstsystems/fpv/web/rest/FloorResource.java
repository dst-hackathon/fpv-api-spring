package com.dstsystems.fpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dstsystems.fpv.domain.Floor;
import com.dstsystems.fpv.service.FloorService;
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
 * REST controller for managing Floor.
 */
@RestController
@RequestMapping("/api")
public class FloorResource {

    private final Logger log = LoggerFactory.getLogger(FloorResource.class);
        
    @Inject
    private FloorService floorService;

    /**
     * POST  /floors : Create a new floor.
     *
     * @param floor the floor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new floor, or with status 400 (Bad Request) if the floor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/floors")
    @Timed
    public ResponseEntity<Floor> createFloor(@Valid @RequestBody Floor floor) throws URISyntaxException {
        log.debug("REST request to save Floor : {}", floor);
        if (floor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("floor", "idexists", "A new floor cannot already have an ID")).body(null);
        }
        Floor result = floorService.save(floor);
        return ResponseEntity.created(new URI("/api/floors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("floor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /floors : Updates an existing floor.
     *
     * @param floor the floor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated floor,
     * or with status 400 (Bad Request) if the floor is not valid,
     * or with status 500 (Internal Server Error) if the floor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/floors")
    @Timed
    public ResponseEntity<Floor> updateFloor(@Valid @RequestBody Floor floor) throws URISyntaxException {
        log.debug("REST request to update Floor : {}", floor);
        if (floor.getId() == null) {
            return createFloor(floor);
        }
        Floor result = floorService.save(floor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("floor", floor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /floors : get all the floors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of floors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/floors")
    @Timed
    public ResponseEntity<List<Floor>> getAllFloors(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Floors");
        Page<Floor> page = floorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/floors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /floors/:id : get the "id" floor.
     *
     * @param id the id of the floor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the floor, or with status 404 (Not Found)
     */
    @GetMapping("/floors/{id}")
    @Timed
    public ResponseEntity<Floor> getFloor(@PathVariable Long id) {
        log.debug("REST request to get Floor : {}", id);
        Floor floor = floorService.findOne(id);
        return Optional.ofNullable(floor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /floors/:id : delete the "id" floor.
     *
     * @param id the id of the floor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/floors/{id}")
    @Timed
    public ResponseEntity<Void> deleteFloor(@PathVariable Long id) {
        log.debug("REST request to delete Floor : {}", id);
        floorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("floor", id.toString())).build();
    }

}
