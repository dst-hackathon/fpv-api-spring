package com.dstsystems.fpv.web.rest;

import com.dstsystems.fpv.FpvApp;

import com.dstsystems.fpv.domain.Desk;
import com.dstsystems.fpv.repository.DeskRepository;
import com.dstsystems.fpv.service.DeskService;

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
 * Test class for the DeskResource REST controller.
 *
 * @see DeskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpvApp.class)
public class DeskResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;

    @Inject
    private DeskRepository deskRepository;

    @Inject
    private DeskService deskService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDeskMockMvc;

    private Desk desk;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeskResource deskResource = new DeskResource();
        ReflectionTestUtils.setField(deskResource, "deskService", deskService);
        this.restDeskMockMvc = MockMvcBuilders.standaloneSetup(deskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Desk createEntity(EntityManager em) {
        Desk desk = new Desk()
                .code(DEFAULT_CODE)
                .x(DEFAULT_X)
                .y(DEFAULT_Y);
        return desk;
    }

    @Before
    public void initTest() {
        desk = createEntity(em);
    }

    @Test
    @Transactional
    public void createDesk() throws Exception {
        int databaseSizeBeforeCreate = deskRepository.findAll().size();

        // Create the Desk

        restDeskMockMvc.perform(post("/api/desks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(desk)))
                .andExpect(status().isCreated());

        // Validate the Desk in the database
        List<Desk> desks = deskRepository.findAll();
        assertThat(desks).hasSize(databaseSizeBeforeCreate + 1);
        Desk testDesk = desks.get(desks.size() - 1);
        assertThat(testDesk.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDesk.getX()).isEqualTo(DEFAULT_X);
        assertThat(testDesk.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = deskRepository.findAll().size();
        // set the field null
        desk.setCode(null);

        // Create the Desk, which fails.

        restDeskMockMvc.perform(post("/api/desks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(desk)))
                .andExpect(status().isBadRequest());

        List<Desk> desks = deskRepository.findAll();
        assertThat(desks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDesks() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the desks
        restDeskMockMvc.perform(get("/api/desks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(desk.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
                .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())));
    }

    @Test
    @Transactional
    public void getDesk() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get the desk
        restDeskMockMvc.perform(get("/api/desks/{id}", desk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(desk.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDesk() throws Exception {
        // Get the desk
        restDeskMockMvc.perform(get("/api/desks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDesk() throws Exception {
        // Initialize the database
        deskService.save(desk);

        int databaseSizeBeforeUpdate = deskRepository.findAll().size();

        // Update the desk
        Desk updatedDesk = deskRepository.findOne(desk.getId());
        updatedDesk
                .code(UPDATED_CODE)
                .x(UPDATED_X)
                .y(UPDATED_Y);

        restDeskMockMvc.perform(put("/api/desks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDesk)))
                .andExpect(status().isOk());

        // Validate the Desk in the database
        List<Desk> desks = deskRepository.findAll();
        assertThat(desks).hasSize(databaseSizeBeforeUpdate);
        Desk testDesk = desks.get(desks.size() - 1);
        assertThat(testDesk.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDesk.getX()).isEqualTo(UPDATED_X);
        assertThat(testDesk.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    public void deleteDesk() throws Exception {
        // Initialize the database
        deskService.save(desk);

        int databaseSizeBeforeDelete = deskRepository.findAll().size();

        // Get the desk
        restDeskMockMvc.perform(delete("/api/desks/{id}", desk.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Desk> desks = deskRepository.findAll();
        assertThat(desks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
