package com.dstsystems.fpv.web.rest;

import com.dstsystems.fpv.FpvApp;

import com.dstsystems.fpv.domain.DeskMovement;
import com.dstsystems.fpv.repository.DeskMovementRepository;
import com.dstsystems.fpv.service.DeskMovementService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DeskMovementResource REST controller.
 *
 * @see DeskMovementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpvApp.class)
public class DeskMovementResourceIntTest {

    @Inject
    private DeskMovementRepository deskMovementRepository;

    @Inject
    private DeskMovementService deskMovementService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDeskMovementMockMvc;

    private DeskMovement deskMovement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeskMovementResource deskMovementResource = new DeskMovementResource();
        ReflectionTestUtils.setField(deskMovementResource, "deskMovementService", deskMovementService);
        this.restDeskMovementMockMvc = MockMvcBuilders.standaloneSetup(deskMovementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeskMovement createEntity(EntityManager em) {
        DeskMovement deskMovement = new DeskMovement();
        return deskMovement;
    }

    @Before
    public void initTest() {
        deskMovement = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeskMovement() throws Exception {
        int databaseSizeBeforeCreate = deskMovementRepository.findAll().size();

        // Create the DeskMovement

        restDeskMovementMockMvc.perform(post("/api/desk-movements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deskMovement)))
                .andExpect(status().isCreated());

        // Validate the DeskMovement in the database
        List<DeskMovement> deskMovements = deskMovementRepository.findAll();
        assertThat(deskMovements).hasSize(databaseSizeBeforeCreate + 1);
        DeskMovement testDeskMovement = deskMovements.get(deskMovements.size() - 1);
    }

    @Test
    @Transactional
    public void getAllDeskMovements() throws Exception {
        // Initialize the database
        deskMovementRepository.saveAndFlush(deskMovement);

        // Get all the deskMovements
        restDeskMovementMockMvc.perform(get("/api/desk-movements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deskMovement.getId().intValue())));
    }

    @Test
    @Transactional
    public void getDeskMovement() throws Exception {
        // Initialize the database
        deskMovementRepository.saveAndFlush(deskMovement);

        // Get the deskMovement
        restDeskMovementMockMvc.perform(get("/api/desk-movements/{id}", deskMovement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deskMovement.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDeskMovement() throws Exception {
        // Get the deskMovement
        restDeskMovementMockMvc.perform(get("/api/desk-movements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeskMovement() throws Exception {
        // Initialize the database
        deskMovementService.save(deskMovement);

        int databaseSizeBeforeUpdate = deskMovementRepository.findAll().size();

        // Update the deskMovement
        DeskMovement updatedDeskMovement = deskMovementRepository.findOne(deskMovement.getId());

        restDeskMovementMockMvc.perform(put("/api/desk-movements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDeskMovement)))
                .andExpect(status().isOk());

        // Validate the DeskMovement in the database
        List<DeskMovement> deskMovements = deskMovementRepository.findAll();
        assertThat(deskMovements).hasSize(databaseSizeBeforeUpdate);
        DeskMovement testDeskMovement = deskMovements.get(deskMovements.size() - 1);
    }

    @Test
    @Transactional
    public void deleteDeskMovement() throws Exception {
        // Initialize the database
        deskMovementService.save(deskMovement);

        int databaseSizeBeforeDelete = deskMovementRepository.findAll().size();

        // Get the deskMovement
        restDeskMovementMockMvc.perform(delete("/api/desk-movements/{id}", deskMovement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DeskMovement> deskMovements = deskMovementRepository.findAll();
        assertThat(deskMovements).hasSize(databaseSizeBeforeDelete - 1);
    }
}
