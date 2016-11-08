package com.dstsystems.fpv.service;

import com.dstsystems.fpv.domain.Building;
import com.dstsystems.fpv.domain.Desk;
import com.dstsystems.fpv.domain.Floor;
import com.dstsystems.fpv.domain.Plan;
import com.dstsystems.fpv.domain.enumeration.PlanStatus;
import com.dstsystems.fpv.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Service Implementation for managing Plan.
 */
@Service
@Transactional
public class PlanService {

    private final Logger log = LoggerFactory.getLogger(PlanService.class);

    @Inject
    private PlanRepository planRepository;

    /**
     * Save a plan.
     *
     * @param plan the entity to save
     * @return the persisted entity
     */
    public Plan save(Plan plan) {
        log.debug("Request to save Plan : {}", plan);
        Plan result = planRepository.save(plan);
        return result;
    }

    /**
     *  Get all the plans.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Plan> findAll(Pageable pageable) {
        log.debug("Request to get all Plans");
        Page<Plan> result = planRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one plan by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Plan findOne(Long id) {
        log.debug("Request to get Plan : {}", id);
        Plan plan = planRepository.findOne(id);
        return plan;
    }

    /**
     *  Delete the  plan by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Plan : {}", id);
        planRepository.delete(id);
    }

    public Plan cloneLatestApprovedPlan(Plan plan) {
        log.debug("Request to clone latest approved plan");
        Plan newPlan = new Plan();
        Plan latestPlan = planRepository.findFirstByStatusAndEffectiveDateIsLessThanOrderByEffectiveDateDesc(PlanStatus.APPROVE, LocalDate.now());

        log.debug("Latest approved plan id : {}, effective on : {}", latestPlan.getId(), latestPlan.getEffectiveDate());

        Set<Building> newBuildings = new HashSet<>();
        for(Building building : latestPlan.getBuildings()) {
            Building newBuilding = new Building();
            Set<Floor> newFloors = new HashSet<>();
            for(Floor floor : building.getFloors()) {
                Floor newFloor = new Floor();
                Set<Desk> newDesks = new HashSet<>();
                for(Desk desk  : floor.getDesks()) {
                    Desk newDesk = new Desk();
                    newDesk.setCode(desk.getCode());
                    newDesk.setX(desk.getX());
                    newDesk.setY(desk.getY());
                    newDesk.setFloor(newFloor);

                    newDesks.add(newDesk);
                }

                newFloor.setDesks(newDesks);
                newFloor.setName(floor.getName());
                newFloor.setImage(floor.getImage());
                newFloor.setImageContentType(floor.getImageContentType());
                newFloor.setBuilding(newBuilding);

                newFloors.add(newFloor);
            }

            newBuilding.setFloors(newFloors);
            newBuilding.setName(building.getName());
            newBuilding.setPlan(newPlan);
            newBuildings.add(newBuilding);
        }


        if( plan.getEffectiveDate() != null ) {
            newPlan.setEffectiveDate(plan.getEffectiveDate());
        } else {
            newPlan.setEffectiveDate(latestPlan.getEffectiveDate());
        }
        newPlan.setBuildings(newBuildings);
        newPlan.setStatus(PlanStatus.DRAFT);
        newPlan.setName(plan.getName());

        return this.save(newPlan);
    }
}
