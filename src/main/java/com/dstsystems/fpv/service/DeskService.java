package com.dstsystems.fpv.service;

import com.dstsystems.fpv.domain.Desk;
import com.dstsystems.fpv.repository.DeskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing Desk.
 */
@Service
@Transactional
public class DeskService {

    private final Logger log = LoggerFactory.getLogger(DeskService.class);

    @Inject
    private DeskRepository deskRepository;

    /**
     * Save a desk.
     *
     * @param desk the entity to save
     * @return the persisted entity
     */
    public Desk save(Desk desk) {
        log.debug("Request to save Desk : {}", desk);
        Desk result = deskRepository.save(desk);
        return result;
    }

    /**
     *  Get all the desks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Desk> findAll(Pageable pageable,Long floorId) {
        log.debug("Request to get all Desks");

        if(floorId!=null){
            return deskRepository.findByFloorId(pageable, floorId);
        }

        Page<Desk> result = deskRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one desk by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Desk findOne(Long id) {
        log.debug("Request to get Desk : {}", id);
        Desk desk = deskRepository.findOne(id);
        return desk;
    }

    /**
     *  Delete the  desk by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Desk : {}", id);
        deskRepository.delete(id);
    }
}
