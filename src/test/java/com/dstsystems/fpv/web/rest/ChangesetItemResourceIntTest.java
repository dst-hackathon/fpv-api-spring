package com.dstsystems.fpv.web.rest;

import com.dstsystems.fpv.FpvApp;

import com.dstsystems.fpv.domain.ChangesetItem;
import com.dstsystems.fpv.domain.Employee;
import com.dstsystems.fpv.domain.Changeset;
import com.dstsystems.fpv.repository.ChangesetItemRepository;
import com.dstsystems.fpv.service.ChangesetItemService;

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

import com.dstsystems.fpv.domain.enumeration.ChangesetItemStatus;
/**
 * Test class for the ChangesetItemResource REST controller.
 *
 * @see ChangesetItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpvApp.class)
public class ChangesetItemResourceIntTest {

    private static final ChangesetItemStatus DEFAULT_STATUS = ChangesetItemStatus.ACCEPT;
    private static final ChangesetItemStatus UPDATED_STATUS = ChangesetItemStatus.DENY;

    @Inject
    private ChangesetItemRepository changesetItemRepository;

    @Inject
    private ChangesetItemService changesetItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChangesetItemMockMvc;

    private ChangesetItem changesetItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChangesetItemResource changesetItemResource = new ChangesetItemResource();
        ReflectionTestUtils.setField(changesetItemResource, "changesetItemService", changesetItemService);
        this.restChangesetItemMockMvc = MockMvcBuilders.standaloneSetup(changesetItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChangesetItem createEntity(EntityManager em) {
        ChangesetItem changesetItem = new ChangesetItem()
                .status(DEFAULT_STATUS);
        // Add required entity
        Employee employee = EmployeeResourceIntTest.createEntity(em);
        em.persist(employee);
        em.flush();
        changesetItem.setEmployee(employee);
        // Add required entity
        Changeset changeset = ChangesetResourceIntTest.createEntity(em);
        em.persist(changeset);
        em.flush();
        changesetItem.setChangeset(changeset);
        return changesetItem;
    }

    @Before
    public void initTest() {
        changesetItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createChangesetItem() throws Exception {
        int databaseSizeBeforeCreate = changesetItemRepository.findAll().size();

        // Create the ChangesetItem

        restChangesetItemMockMvc.perform(post("/api/changeset-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(changesetItem)))
                .andExpect(status().isCreated());

        // Validate the ChangesetItem in the database
        List<ChangesetItem> changesetItems = changesetItemRepository.findAll();
        assertThat(changesetItems).hasSize(databaseSizeBeforeCreate + 1);
        ChangesetItem testChangesetItem = changesetItems.get(changesetItems.size() - 1);
        assertThat(testChangesetItem.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllChangesetItems() throws Exception {
        // Initialize the database
        changesetItemRepository.saveAndFlush(changesetItem);

        // Get all the changesetItems
        restChangesetItemMockMvc.perform(get("/api/changeset-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(changesetItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getChangesetItem() throws Exception {
        // Initialize the database
        changesetItemRepository.saveAndFlush(changesetItem);

        // Get the changesetItem
        restChangesetItemMockMvc.perform(get("/api/changeset-items/{id}", changesetItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(changesetItem.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChangesetItem() throws Exception {
        // Get the changesetItem
        restChangesetItemMockMvc.perform(get("/api/changeset-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChangesetItem() throws Exception {
        // Initialize the database
        changesetItemService.save(changesetItem);

        int databaseSizeBeforeUpdate = changesetItemRepository.findAll().size();

        // Update the changesetItem
        ChangesetItem updatedChangesetItem = changesetItemRepository.findOne(changesetItem.getId());
        updatedChangesetItem
                .status(UPDATED_STATUS);

        restChangesetItemMockMvc.perform(put("/api/changeset-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChangesetItem)))
                .andExpect(status().isOk());

        // Validate the ChangesetItem in the database
        List<ChangesetItem> changesetItems = changesetItemRepository.findAll();
        assertThat(changesetItems).hasSize(databaseSizeBeforeUpdate);
        ChangesetItem testChangesetItem = changesetItems.get(changesetItems.size() - 1);
        assertThat(testChangesetItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteChangesetItem() throws Exception {
        // Initialize the database
        changesetItemService.save(changesetItem);

        int databaseSizeBeforeDelete = changesetItemRepository.findAll().size();

        // Get the changesetItem
        restChangesetItemMockMvc.perform(delete("/api/changeset-items/{id}", changesetItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChangesetItem> changesetItems = changesetItemRepository.findAll();
        assertThat(changesetItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
