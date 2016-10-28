package com.dstsystems.fpv.web.rest;

import com.dstsystems.fpv.FpvApp;

import com.dstsystems.fpv.domain.DeskAssignment;
import com.dstsystems.fpv.repository.DeskAssignmentRepository;
import com.dstsystems.fpv.service.DeskAssignmentService;

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
 * Test class for the DeskAssignmentResource REST controller.
 *
 * @see DeskAssignmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpvApp.class)
public class DeskAssignmentResourceIntTest {

    @Inject
    private DeskAssignmentRepository deskAssignmentRepository;

    @Inject
    private DeskAssignmentService deskAssignmentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDeskAssignmentMockMvc;

    private DeskAssignment deskAssignment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeskAssignmentResource deskAssignmentResource = new DeskAssignmentResource();
        ReflectionTestUtils.setField(deskAssignmentResource, "deskAssignmentService", deskAssignmentService);
        this.restDeskAssignmentMockMvc = MockMvcBuilders.standaloneSetup(deskAssignmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeskAssignment createEntity(EntityManager em) {
        DeskAssignment deskAssignment = new DeskAssignment();
        return deskAssignment;
    }

    @Before
    public void initTest() {
        deskAssignment = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeskAssignment() throws Exception {
        int databaseSizeBeforeCreate = deskAssignmentRepository.findAll().size();

        // Create the DeskAssignment

        restDeskAssignmentMockMvc.perform(post("/api/desk-assignments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deskAssignment)))
                .andExpect(status().isCreated());

        // Validate the DeskAssignment in the database
        List<DeskAssignment> deskAssignments = deskAssignmentRepository.findAll();
        assertThat(deskAssignments).hasSize(databaseSizeBeforeCreate + 1);
        DeskAssignment testDeskAssignment = deskAssignments.get(deskAssignments.size() - 1);
    }

    @Test
    @Transactional
    public void getAllDeskAssignments() throws Exception {
        // Initialize the database
        deskAssignmentRepository.saveAndFlush(deskAssignment);

        // Get all the deskAssignments
        restDeskAssignmentMockMvc.perform(get("/api/desk-assignments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deskAssignment.getId().intValue())));
    }

    @Test
    @Transactional
    public void getDeskAssignment() throws Exception {
        // Initialize the database
        deskAssignmentRepository.saveAndFlush(deskAssignment);

        // Get the deskAssignment
        restDeskAssignmentMockMvc.perform(get("/api/desk-assignments/{id}", deskAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deskAssignment.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDeskAssignment() throws Exception {
        // Get the deskAssignment
        restDeskAssignmentMockMvc.perform(get("/api/desk-assignments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeskAssignment() throws Exception {
        // Initialize the database
        deskAssignmentService.save(deskAssignment);

        int databaseSizeBeforeUpdate = deskAssignmentRepository.findAll().size();

        // Update the deskAssignment
        DeskAssignment updatedDeskAssignment = deskAssignmentRepository.findOne(deskAssignment.getId());

        restDeskAssignmentMockMvc.perform(put("/api/desk-assignments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDeskAssignment)))
                .andExpect(status().isOk());

        // Validate the DeskAssignment in the database
        List<DeskAssignment> deskAssignments = deskAssignmentRepository.findAll();
        assertThat(deskAssignments).hasSize(databaseSizeBeforeUpdate);
        DeskAssignment testDeskAssignment = deskAssignments.get(deskAssignments.size() - 1);
    }

    @Test
    @Transactional
    public void deleteDeskAssignment() throws Exception {
        // Initialize the database
        deskAssignmentService.save(deskAssignment);

        int databaseSizeBeforeDelete = deskAssignmentRepository.findAll().size();

        // Get the deskAssignment
        restDeskAssignmentMockMvc.perform(delete("/api/desk-assignments/{id}", deskAssignment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DeskAssignment> deskAssignments = deskAssignmentRepository.findAll();
        assertThat(deskAssignments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
