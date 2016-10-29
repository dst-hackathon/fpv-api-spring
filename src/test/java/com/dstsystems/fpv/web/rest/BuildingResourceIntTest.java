package com.dstsystems.fpv.web.rest;

import com.dstsystems.fpv.FpvApp;

import com.dstsystems.fpv.domain.Building;
import com.dstsystems.fpv.repository.BuildingRepository;
import com.dstsystems.fpv.service.BuildingService;

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
 * Test class for the BuildingResource REST controller.
 *
 * @see BuildingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpvApp.class)
public class BuildingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private BuildingRepository buildingRepository;

    @Inject
    private BuildingService buildingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBuildingMockMvc;

    private Building building;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BuildingResource buildingResource = new BuildingResource();
        ReflectionTestUtils.setField(buildingResource, "buildingService", buildingService);
        this.restBuildingMockMvc = MockMvcBuilders.standaloneSetup(buildingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createEntity(EntityManager em) {
        Building building = new Building()
                .name(DEFAULT_NAME);
        return building;
    }

    @Before
    public void initTest() {
        building = createEntity(em);
    }

    @Test
    @Transactional
    public void createBuilding() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // Create the Building

        restBuildingMockMvc.perform(post("/api/buildings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(building)))
                .andExpect(status().isCreated());

        // Validate the Building in the database
        List<Building> buildings = buildingRepository.findAll();
        assertThat(buildings).hasSize(databaseSizeBeforeCreate + 1);
        Building testBuilding = buildings.get(buildings.size() - 1);
        assertThat(testBuilding.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingRepository.findAll().size();
        // set the field null
        building.setName(null);

        // Create the Building, which fails.

        restBuildingMockMvc.perform(post("/api/buildings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(building)))
                .andExpect(status().isBadRequest());

        List<Building> buildings = buildingRepository.findAll();
        assertThat(buildings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBuildings() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildings
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(building.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", building.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(building.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBuilding() throws Exception {
        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuilding() throws Exception {
        // Initialize the database
        buildingService.save(building);

        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Update the building
        Building updatedBuilding = buildingRepository.findOne(building.getId());
        updatedBuilding
                .name(UPDATED_NAME);

        restBuildingMockMvc.perform(put("/api/buildings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBuilding)))
                .andExpect(status().isOk());

        // Validate the Building in the database
        List<Building> buildings = buildingRepository.findAll();
        assertThat(buildings).hasSize(databaseSizeBeforeUpdate);
        Building testBuilding = buildings.get(buildings.size() - 1);
        assertThat(testBuilding.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteBuilding() throws Exception {
        // Initialize the database
        buildingService.save(building);

        int databaseSizeBeforeDelete = buildingRepository.findAll().size();

        // Get the building
        restBuildingMockMvc.perform(delete("/api/buildings/{id}", building.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Building> buildings = buildingRepository.findAll();
        assertThat(buildings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
