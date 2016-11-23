package com.dstsystems.fpv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dstsystems.fpv.domain.ChangesetItem;
import com.dstsystems.fpv.service.ChangesetItemService;
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
 * REST controller for managing ChangesetItem.
 */
@RestController
@RequestMapping("/api")
public class ChangesetItemResource {

    private final Logger log = LoggerFactory.getLogger(ChangesetItemResource.class);
        
    @Inject
    private ChangesetItemService changesetItemService;

    /**
     * POST  /changeset-items : Create a new changesetItem.
     *
     * @param changesetItem the changesetItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new changesetItem, or with status 400 (Bad Request) if the changesetItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/changeset-items")
    @Timed
    public ResponseEntity<ChangesetItem> createChangesetItem(@Valid @RequestBody ChangesetItem changesetItem) throws URISyntaxException {
        log.debug("REST request to save ChangesetItem : {}", changesetItem);
        if (changesetItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("changesetItem", "idexists", "A new changesetItem cannot already have an ID")).body(null);
        }
        ChangesetItem result = changesetItemService.save(changesetItem);
        return ResponseEntity.created(new URI("/api/changeset-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("changesetItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /changeset-items : Updates an existing changesetItem.
     *
     * @param changesetItem the changesetItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated changesetItem,
     * or with status 400 (Bad Request) if the changesetItem is not valid,
     * or with status 500 (Internal Server Error) if the changesetItem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/changeset-items")
    @Timed
    public ResponseEntity<ChangesetItem> updateChangesetItem(@Valid @RequestBody ChangesetItem changesetItem) throws URISyntaxException {
        log.debug("REST request to update ChangesetItem : {}", changesetItem);
        if (changesetItem.getId() == null) {
            return createChangesetItem(changesetItem);
        }
        ChangesetItem result = changesetItemService.save(changesetItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("changesetItem", changesetItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /changeset-items : get all the changesetItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of changesetItems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/changeset-items")
    @Timed
    public ResponseEntity<List<ChangesetItem>> getAllChangesetItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChangesetItems");
        Page<ChangesetItem> page = changesetItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/changeset-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /changeset-items/:id : get the "id" changesetItem.
     *
     * @param id the id of the changesetItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the changesetItem, or with status 404 (Not Found)
     */
    @GetMapping("/changeset-items/{id}")
    @Timed
    public ResponseEntity<ChangesetItem> getChangesetItem(@PathVariable Long id) {
        log.debug("REST request to get ChangesetItem : {}", id);
        ChangesetItem changesetItem = changesetItemService.findOne(id);
        return Optional.ofNullable(changesetItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /changeset-items/:id : delete the "id" changesetItem.
     *
     * @param id the id of the changesetItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/changeset-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteChangesetItem(@PathVariable Long id) {
        log.debug("REST request to delete ChangesetItem : {}", id);
        changesetItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("changesetItem", id.toString())).build();
    }

}
