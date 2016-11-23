package com.dstsystems.fpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dstsystems.fpv.domain.Building;
import com.dstsystems.fpv.service.BuildingService;
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
 * REST controller for managing Building.
 */
@RestController
@RequestMapping("/api")
public class BuildingResource {

    private final Logger log = LoggerFactory.getLogger(BuildingResource.class);

    @Inject
    private BuildingService buildingService;

    /**
     * POST  /buildings : Create a new building.
     *
     * @param building the building to create
     * @return the ResponseEntity with status 201 (Created) and with body the new building, or with status 400 (Bad Request) if the building has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/buildings")
    @Timed
    public ResponseEntity<Building> createBuilding(@Valid @RequestBody Building building) throws URISyntaxException {
        log.debug("REST request to save Building : {}", building);
        if (building.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("building", "idexists", "A new building cannot already have an ID")).body(null);
        }
        Building result = buildingService.save(building);
        return ResponseEntity.created(new URI("/api/buildings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("building", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /buildings : Updates an existing building.
     *
     * @param building the building to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated building,
     * or with status 400 (Bad Request) if the building is not valid,
     * or with status 500 (Internal Server Error) if the building couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/buildings")
    @Timed
    public ResponseEntity<Building> updateBuilding(@Valid @RequestBody Building building) throws URISyntaxException {
        log.debug("REST request to update Building : {}", building);
        if (building.getId() == null) {
            return createBuilding(building);
        }
        Building result = buildingService.save(building);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("building", building.getId().toString()))
            .body(result);
    }

    /**
     * GET  /buildings : get all the buildings.
     *
     * @param pageable the pagination information
     * @param planId the planId information
     * @return the ResponseEntity with status 200 (OK) and the list of buildings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/buildings")
    @Timed
    public ResponseEntity<List<Building>> getAllBuildings(Pageable pageable,Long planId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Buildings");
        Page<Building> page = buildingService.findAll(pageable, planId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/buildings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /buildings/:id : get the "id" building.
     *
     * @param id the id of the building to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the building, or with status 404 (Not Found)
     */
    @GetMapping("/buildings/{id}")
    @Timed
    public ResponseEntity<Building> getBuilding(@PathVariable Long id) {
        log.debug("REST request to get Building : {}", id);
        Building building = buildingService.findOne(id);
        return Optional.ofNullable(building)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /buildings/:id : delete the "id" building.
     *
     * @param id the id of the building to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/buildings/{id}")
    @Timed
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        log.debug("REST request to delete Building : {}", id);
        buildingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("building", id.toString())).build();
    }

}
