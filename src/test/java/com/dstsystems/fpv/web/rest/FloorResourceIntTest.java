package com.dstsystems.fpv.web.rest;

import com.dstsystems.fpv.FpvApp;

import com.dstsystems.fpv.domain.Floor;
import com.dstsystems.fpv.repository.FloorRepository;
import com.dstsystems.fpv.service.FloorService;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FloorResource REST controller.
 *
 * @see FloorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpvApp.class)
public class FloorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Inject
    private FloorRepository floorRepository;

    @Inject
    private FloorService floorService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFloorMockMvc;

    private Floor floor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FloorResource floorResource = new FloorResource();
        ReflectionTestUtils.setField(floorResource, "floorService", floorService);
        this.restFloorMockMvc = MockMvcBuilders.standaloneSetup(floorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Floor createEntity(EntityManager em) {
        Floor floor = new Floor()
                .name(DEFAULT_NAME)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return floor;
    }

    @Before
    public void initTest() {
        floor = createEntity(em);
    }

    @Test
    @Transactional
    public void createFloor() throws Exception {
        int databaseSizeBeforeCreate = floorRepository.findAll().size();

        // Create the Floor

        restFloorMockMvc.perform(post("/api/floors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(floor)))
                .andExpect(status().isCreated());

        // Validate the Floor in the database
        List<Floor> floors = floorRepository.findAll();
        assertThat(floors).hasSize(databaseSizeBeforeCreate + 1);
        Floor testFloor = floors.get(floors.size() - 1);
        assertThat(testFloor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFloor.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFloor.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = floorRepository.findAll().size();
        // set the field null
        floor.setName(null);

        // Create the Floor, which fails.

        restFloorMockMvc.perform(post("/api/floors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(floor)))
                .andExpect(status().isBadRequest());

        List<Floor> floors = floorRepository.findAll();
        assertThat(floors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFloors() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        // Get all the floors
        restFloorMockMvc.perform(get("/api/floors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(floor.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void getFloor() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        // Get the floor
        restFloorMockMvc.perform(get("/api/floors/{id}", floor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(floor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingFloor() throws Exception {
        // Get the floor
        restFloorMockMvc.perform(get("/api/floors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFloor() throws Exception {
        // Initialize the database
        floorService.save(floor);

        int databaseSizeBeforeUpdate = floorRepository.findAll().size();

        // Update the floor
        Floor updatedFloor = floorRepository.findOne(floor.getId());
        updatedFloor
                .name(UPDATED_NAME)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restFloorMockMvc.perform(put("/api/floors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFloor)))
                .andExpect(status().isOk());

        // Validate the Floor in the database
        List<Floor> floors = floorRepository.findAll();
        assertThat(floors).hasSize(databaseSizeBeforeUpdate);
        Floor testFloor = floors.get(floors.size() - 1);
        assertThat(testFloor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFloor.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFloor.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteFloor() throws Exception {
        // Initialize the database
        floorService.save(floor);

        int databaseSizeBeforeDelete = floorRepository.findAll().size();

        // Get the floor
        restFloorMockMvc.perform(delete("/api/floors/{id}", floor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Floor> floors = floorRepository.findAll();
        assertThat(floors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
