package com.dstsystems.fpv.web.rest;

import com.dstsystems.fpv.FpvApp;
import com.dstsystems.fpv.domain.Plan;
import com.dstsystems.fpv.domain.enumeration.PlanStatus;
import com.dstsystems.fpv.repository.PlanRepository;
import com.dstsystems.fpv.service.PlanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the PlanResource REST controller.
 *
 * @see PlanResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpvApp.class)
public class PlanResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_APPROVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPROVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final PlanStatus DEFAULT_STATUS = PlanStatus.DRAFT;
    private static final PlanStatus UPDATED_STATUS = PlanStatus.PENDING;

    private static final Boolean DEFAULT_CLONED = false;
    private static final Boolean UPDATED_CLONED = true;

    private static final Long DEFAULT_CLONE_FROM_PLAN_ID = 1L;
    private static final Long UPDATED_CLONE_FROM_PLAN_ID = 2L;

    @Inject
    private PlanRepository planRepository;

    @Inject
    private PlanService planService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPlanMockMvc;

    private Plan plan;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlanResource planResource = new PlanResource();
        ReflectionTestUtils.setField(planResource, "planService", planService);
        this.restPlanMockMvc = MockMvcBuilders.standaloneSetup(planResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plan createEntity(EntityManager em) {
        Plan plan = new Plan()
                .name(DEFAULT_NAME)
                .effectiveDate(DEFAULT_EFFECTIVE_DATE)
                .approveDate(DEFAULT_APPROVE_DATE)
                .status(DEFAULT_STATUS)
                .cloned(DEFAULT_CLONED)
                .cloneFromPlanId(DEFAULT_CLONE_FROM_PLAN_ID);
        return plan;
    }

    @Before
    public void initTest() {
        plan = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlan() throws Exception {
        int databaseSizeBeforeCreate = planRepository.findAll().size();

        // Create the Plan

        restPlanMockMvc.perform(post("/api/plans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plan)))
                .andExpect(status().isCreated());

        // Validate the Plan in the database
        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeCreate + 1);
        Plan testPlan = plans.get(plans.size() - 1);
        assertThat(testPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlan.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testPlan.getApproveDate()).isEqualTo(DEFAULT_APPROVE_DATE);
        assertThat(testPlan.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPlan.isCloned()).isEqualTo(DEFAULT_CLONED);
        assertThat(testPlan.getCloneFromPlanId()).isEqualTo(DEFAULT_CLONE_FROM_PLAN_ID);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = planRepository.findAll().size();
        // set the field null
        plan.setName(null);

        // Create the Plan, which fails.

        restPlanMockMvc.perform(post("/api/plans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plan)))
                .andExpect(status().isBadRequest());

        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = planRepository.findAll().size();
        // set the field null
        plan.setStatus(null);

        // Create the Plan, which fails.

        restPlanMockMvc.perform(post("/api/plans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plan)))
                .andExpect(status().isBadRequest());

        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlans() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the plans
        restPlanMockMvc.perform(get("/api/plans?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(plan.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].approveDate").value(hasItem(DEFAULT_APPROVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].cloned").value(hasItem(DEFAULT_CLONED.booleanValue())))
                .andExpect(jsonPath("$.[*].cloneFromPlanId").value(hasItem(DEFAULT_CLONE_FROM_PLAN_ID.intValue())));
    }

    @Test
    @Transactional
    public void getPlan() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get the plan
        restPlanMockMvc.perform(get("/api/plans/{id}", plan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(plan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.approveDate").value(DEFAULT_APPROVE_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.cloned").value(DEFAULT_CLONED.booleanValue()))
            .andExpect(jsonPath("$.cloneFromPlanId").value(DEFAULT_CLONE_FROM_PLAN_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlan() throws Exception {
        // Get the plan
        restPlanMockMvc.perform(get("/api/plans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlan() throws Exception {
        // Initialize the database
        planService.save(plan);

        int databaseSizeBeforeUpdate = planRepository.findAll().size();

        // Update the plan
        Plan updatedPlan = planRepository.findOne(plan.getId());
        updatedPlan
                .name(UPDATED_NAME)
                .effectiveDate(UPDATED_EFFECTIVE_DATE)
                .approveDate(UPDATED_APPROVE_DATE)
                .status(UPDATED_STATUS)
                .cloned(UPDATED_CLONED)
                .cloneFromPlanId(UPDATED_CLONE_FROM_PLAN_ID);

        restPlanMockMvc.perform(put("/api/plans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPlan)))
                .andExpect(status().isOk());

        // Validate the Plan in the database
        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeUpdate);
        Plan testPlan = plans.get(plans.size() - 1);
        assertThat(testPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlan.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testPlan.getApproveDate()).isEqualTo(UPDATED_APPROVE_DATE);
        assertThat(testPlan.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPlan.isCloned()).isEqualTo(UPDATED_CLONED);
        assertThat(testPlan.getCloneFromPlanId()).isEqualTo(UPDATED_CLONE_FROM_PLAN_ID);
    }

    @Test
    @Transactional
    public void deletePlan() throws Exception {
        // Initialize the database
        planService.save(plan);

        int databaseSizeBeforeDelete = planRepository.findAll().size();

        // Get the plan
        restPlanMockMvc.perform(delete("/api/plans/{id}", plan.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeDelete - 1);
    }
}
