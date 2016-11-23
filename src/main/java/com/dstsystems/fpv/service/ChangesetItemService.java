package com.dstsystems.fpv.service;

import com.dstsystems.fpv.domain.ChangesetItem;
import com.dstsystems.fpv.repository.ChangesetItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ChangesetItem.
 */
@Service
@Transactional
public class ChangesetItemService {

    private final Logger log = LoggerFactory.getLogger(ChangesetItemService.class);
    
    @Inject
    private ChangesetItemRepository changesetItemRepository;

    /**
     * Save a changesetItem.
     *
     * @param changesetItem the entity to save
     * @return the persisted entity
     */
    public ChangesetItem save(ChangesetItem changesetItem) {
        log.debug("Request to save ChangesetItem : {}", changesetItem);
        ChangesetItem result = changesetItemRepository.save(changesetItem);
        return result;
    }

    /**
     *  Get all the changesetItems.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ChangesetItem> findAll(Pageable pageable) {
        log.debug("Request to get all ChangesetItems");
        Page<ChangesetItem> result = changesetItemRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one changesetItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ChangesetItem findOne(Long id) {
        log.debug("Request to get ChangesetItem : {}", id);
        ChangesetItem changesetItem = changesetItemRepository.findOne(id);
        return changesetItem;
    }

    /**
     *  Delete the  changesetItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ChangesetItem : {}", id);
        changesetItemRepository.delete(id);
    }
}
