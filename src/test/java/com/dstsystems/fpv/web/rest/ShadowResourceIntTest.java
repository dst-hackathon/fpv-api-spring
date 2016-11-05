package com.dstsystems.fpv.web.rest;

import com.dstsystems.fpv.FpvApp;

import com.dstsystems.fpv.domain.Shadow;
import com.dstsystems.fpv.repository.ShadowRepository;
import com.dstsystems.fpv.service.ShadowService;

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
 * Test class for the ShadowResource REST controller.
 *
 * @see ShadowResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpvApp.class)
public class ShadowResourceIntTest {

    @Inject
    private ShadowRepository shadowRepository;

    @Inject
    private ShadowService shadowService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restShadowMockMvc;

    private Shadow shadow;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShadowResource shadowResource = new ShadowResource();
        ReflectionTestUtils.setField(shadowResource, "shadowService", shadowService);
        this.restShadowMockMvc = MockMvcBuilders.standaloneSetup(shadowResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shadow createEntity(EntityManager em) {
        Shadow shadow = new Shadow();
        return shadow;
    }

    @Before
    public void initTest() {
        shadow = createEntity(em);
    }

    @Test
    @Transactional
    public void createShadow() throws Exception {
        int databaseSizeBeforeCreate = shadowRepository.findAll().size();

        // Create the Shadow

        restShadowMockMvc.perform(post("/api/shadows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shadow)))
                .andExpect(status().isCreated());

        // Validate the Shadow in the database
        List<Shadow> shadows = shadowRepository.findAll();
        assertThat(shadows).hasSize(databaseSizeBeforeCreate + 1);
        Shadow testShadow = shadows.get(shadows.size() - 1);
    }

    @Test
    @Transactional
    public void getAllShadows() throws Exception {
        // Initialize the database
        shadowRepository.saveAndFlush(shadow);

        // Get all the shadows
        restShadowMockMvc.perform(get("/api/shadows?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shadow.getId().intValue())));
    }

    @Test
    @Transactional
    public void getShadow() throws Exception {
        // Initialize the database
        shadowRepository.saveAndFlush(shadow);

        // Get the shadow
        restShadowMockMvc.perform(get("/api/shadows/{id}", shadow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shadow.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingShadow() throws Exception {
        // Get the shadow
        restShadowMockMvc.perform(get("/api/shadows/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShadow() throws Exception {
        // Initialize the database
        shadowService.save(shadow);

        int databaseSizeBeforeUpdate = shadowRepository.findAll().size();

        // Update the shadow
        Shadow updatedShadow = shadowRepository.findOne(shadow.getId());

        restShadowMockMvc.perform(put("/api/shadows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedShadow)))
                .andExpect(status().isOk());

        // Validate the Shadow in the database
        List<Shadow> shadows = shadowRepository.findAll();
        assertThat(shadows).hasSize(databaseSizeBeforeUpdate);
        Shadow testShadow = shadows.get(shadows.size() - 1);
    }

    @Test
    @Transactional
    public void deleteShadow() throws Exception {
        // Initialize the database
        shadowService.save(shadow);

        int databaseSizeBeforeDelete = shadowRepository.findAll().size();

        // Get the shadow
        restShadowMockMvc.perform(delete("/api/shadows/{id}", shadow.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Shadow> shadows = shadowRepository.findAll();
        assertThat(shadows).hasSize(databaseSizeBeforeDelete - 1);
    }
}
