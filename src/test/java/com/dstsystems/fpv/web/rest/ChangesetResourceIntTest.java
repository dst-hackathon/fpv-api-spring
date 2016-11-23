package com.dstsystems.fpv.web.rest;

import com.dstsystems.fpv.FpvApp;

import com.dstsystems.fpv.domain.Changeset;
import com.dstsystems.fpv.domain.Plan;
import com.dstsystems.fpv.repository.ChangesetRepository;
import com.dstsystems.fpv.service.ChangesetService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dstsystems.fpv.domain.enumeration.ChangesetStatus;
/**
 * Test class for the ChangesetResource REST controller.
 *
 * @see ChangesetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpvApp.class)
public class ChangesetResourceIntTest {

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ChangesetStatus DEFAULT_STATUS = ChangesetStatus.IN_PROGRESS;
    private static final ChangesetStatus UPDATED_STATUS = ChangesetStatus.COMPLETE;

    @Inject
    private ChangesetRepository changesetRepository;

    @Inject
    private ChangesetService changesetService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChangesetMockMvc;

    private Changeset changeset;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChangesetResource changesetResource = new ChangesetResource();
        ReflectionTestUtils.setField(changesetResource, "changesetService", changesetService);
        this.restChangesetMockMvc = MockMvcBuilders.standaloneSetup(changesetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Changeset createEntity(EntityManager em) {
        Changeset changeset = new Changeset()
                .effectiveDate(DEFAULT_EFFECTIVE_DATE)
                .status(DEFAULT_STATUS);
        // Add required entity
        Plan plan = PlanResourceIntTest.createEntity(em);
        em.persist(plan);
        em.flush();
        changeset.setPlan(plan);
        return changeset;
    }

    @Before
    public void initTest() {
        changeset = createEntity(em);
    }

    @Test
    @Transactional
    public void createChangeset() throws Exception {
        int databaseSizeBeforeCreate = changesetRepository.findAll().size();

        // Create the Changeset

        restChangesetMockMvc.perform(post("/api/changesets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(changeset)))
                .andExpect(status().isCreated());

        // Validate the Changeset in the database
        List<Changeset> changesets = changesetRepository.findAll();
        assertThat(changesets).hasSize(databaseSizeBeforeCreate + 1);
        Changeset testChangeset = changesets.get(changesets.size() - 1);
        assertThat(testChangeset.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testChangeset.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkEffectiveDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = changesetRepository.findAll().size();
        // set the field null
        changeset.setEffectiveDate(null);

        // Create the Changeset, which fails.

        restChangesetMockMvc.perform(post("/api/changesets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(changeset)))
                .andExpect(status().isBadRequest());

        List<Changeset> changesets = changesetRepository.findAll();
        assertThat(changesets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = changesetRepository.findAll().size();
        // set the field null
        changeset.setStatus(null);

        // Create the Changeset, which fails.

        restChangesetMockMvc.perform(post("/api/changesets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(changeset)))
                .andExpect(status().isBadRequest());

        List<Changeset> changesets = changesetRepository.findAll();
        assertThat(changesets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChangesets() throws Exception {
        // Initialize the database
        changesetRepository.saveAndFlush(changeset);

        // Get all the changesets
        restChangesetMockMvc.perform(get("/api/changesets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(changeset.getId().intValue())))
                .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getChangeset() throws Exception {
        // Initialize the database
        changesetRepository.saveAndFlush(changeset);

        // Get the changeset
        restChangesetMockMvc.perform(get("/api/changesets/{id}", changeset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(changeset.getId().intValue()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChangeset() throws Exception {
        // Get the changeset
        restChangesetMockMvc.perform(get("/api/changesets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChangeset() throws Exception {
        // Initialize the database
        changesetService.save(changeset);

        int databaseSizeBeforeUpdate = changesetRepository.findAll().size();

        // Update the changeset
        Changeset updatedChangeset = changesetRepository.findOne(changeset.getId());
        updatedChangeset
                .effectiveDate(UPDATED_EFFECTIVE_DATE)
                .status(UPDATED_STATUS);

        restChangesetMockMvc.perform(put("/api/changesets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChangeset)))
                .andExpect(status().isOk());

        // Validate the Changeset in the database
        List<Changeset> changesets = changesetRepository.findAll();
        assertThat(changesets).hasSize(databaseSizeBeforeUpdate);
        Changeset testChangeset = changesets.get(changesets.size() - 1);
        assertThat(testChangeset.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testChangeset.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteChangeset() throws Exception {
        // Initialize the database
        changesetService.save(changeset);

        int databaseSizeBeforeDelete = changesetRepository.findAll().size();

        // Get the changeset
        restChangesetMockMvc.perform(delete("/api/changesets/{id}", changeset.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Changeset> changesets = changesetRepository.findAll();
        assertThat(changesets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
