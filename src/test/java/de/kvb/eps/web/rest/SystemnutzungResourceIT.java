package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Systemnutzung;
import de.kvb.eps.repository.SystemnutzungRepository;
import de.kvb.eps.repository.search.SystemnutzungSearchRepository;
import de.kvb.eps.service.SystemnutzungService;
import de.kvb.eps.service.dto.SystemnutzungDTO;
import de.kvb.eps.service.mapper.SystemnutzungMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static de.kvb.eps.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SystemnutzungResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class SystemnutzungResourceIT {

    @Autowired
    private SystemnutzungRepository systemnutzungRepository;

    @Autowired
    private SystemnutzungMapper systemnutzungMapper;

    @Autowired
    private SystemnutzungService systemnutzungService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.SystemnutzungSearchRepositoryMockConfiguration
     */
    @Autowired
    private SystemnutzungSearchRepository mockSystemnutzungSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSystemnutzungMockMvc;

    private Systemnutzung systemnutzung;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SystemnutzungResource systemnutzungResource = new SystemnutzungResource(systemnutzungService);
        this.restSystemnutzungMockMvc = MockMvcBuilders.standaloneSetup(systemnutzungResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Systemnutzung createEntity(EntityManager em) {
        Systemnutzung systemnutzung = new Systemnutzung();
        return systemnutzung;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Systemnutzung createUpdatedEntity(EntityManager em) {
        Systemnutzung systemnutzung = new Systemnutzung();
        return systemnutzung;
    }

    @BeforeEach
    public void initTest() {
        systemnutzung = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemnutzung() throws Exception {
        int databaseSizeBeforeCreate = systemnutzungRepository.findAll().size();

        // Create the Systemnutzung
        SystemnutzungDTO systemnutzungDTO = systemnutzungMapper.toDto(systemnutzung);
        restSystemnutzungMockMvc.perform(post("/api/systemnutzungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemnutzungDTO)))
            .andExpect(status().isCreated());

        // Validate the Systemnutzung in the database
        List<Systemnutzung> systemnutzungList = systemnutzungRepository.findAll();
        assertThat(systemnutzungList).hasSize(databaseSizeBeforeCreate + 1);
        Systemnutzung testSystemnutzung = systemnutzungList.get(systemnutzungList.size() - 1);

        // Validate the Systemnutzung in Elasticsearch
        verify(mockSystemnutzungSearchRepository, times(1)).save(testSystemnutzung);
    }

    @Test
    @Transactional
    public void createSystemnutzungWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemnutzungRepository.findAll().size();

        // Create the Systemnutzung with an existing ID
        systemnutzung.setId(1L);
        SystemnutzungDTO systemnutzungDTO = systemnutzungMapper.toDto(systemnutzung);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemnutzungMockMvc.perform(post("/api/systemnutzungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemnutzungDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Systemnutzung in the database
        List<Systemnutzung> systemnutzungList = systemnutzungRepository.findAll();
        assertThat(systemnutzungList).hasSize(databaseSizeBeforeCreate);

        // Validate the Systemnutzung in Elasticsearch
        verify(mockSystemnutzungSearchRepository, times(0)).save(systemnutzung);
    }


    @Test
    @Transactional
    public void getAllSystemnutzungs() throws Exception {
        // Initialize the database
        systemnutzungRepository.saveAndFlush(systemnutzung);

        // Get all the systemnutzungList
        restSystemnutzungMockMvc.perform(get("/api/systemnutzungs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemnutzung.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getSystemnutzung() throws Exception {
        // Initialize the database
        systemnutzungRepository.saveAndFlush(systemnutzung);

        // Get the systemnutzung
        restSystemnutzungMockMvc.perform(get("/api/systemnutzungs/{id}", systemnutzung.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systemnutzung.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSystemnutzung() throws Exception {
        // Get the systemnutzung
        restSystemnutzungMockMvc.perform(get("/api/systemnutzungs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemnutzung() throws Exception {
        // Initialize the database
        systemnutzungRepository.saveAndFlush(systemnutzung);

        int databaseSizeBeforeUpdate = systemnutzungRepository.findAll().size();

        // Update the systemnutzung
        Systemnutzung updatedSystemnutzung = systemnutzungRepository.findById(systemnutzung.getId()).get();
        // Disconnect from session so that the updates on updatedSystemnutzung are not directly saved in db
        em.detach(updatedSystemnutzung);
        SystemnutzungDTO systemnutzungDTO = systemnutzungMapper.toDto(updatedSystemnutzung);

        restSystemnutzungMockMvc.perform(put("/api/systemnutzungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemnutzungDTO)))
            .andExpect(status().isOk());

        // Validate the Systemnutzung in the database
        List<Systemnutzung> systemnutzungList = systemnutzungRepository.findAll();
        assertThat(systemnutzungList).hasSize(databaseSizeBeforeUpdate);
        Systemnutzung testSystemnutzung = systemnutzungList.get(systemnutzungList.size() - 1);

        // Validate the Systemnutzung in Elasticsearch
        verify(mockSystemnutzungSearchRepository, times(1)).save(testSystemnutzung);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemnutzung() throws Exception {
        int databaseSizeBeforeUpdate = systemnutzungRepository.findAll().size();

        // Create the Systemnutzung
        SystemnutzungDTO systemnutzungDTO = systemnutzungMapper.toDto(systemnutzung);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemnutzungMockMvc.perform(put("/api/systemnutzungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemnutzungDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Systemnutzung in the database
        List<Systemnutzung> systemnutzungList = systemnutzungRepository.findAll();
        assertThat(systemnutzungList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Systemnutzung in Elasticsearch
        verify(mockSystemnutzungSearchRepository, times(0)).save(systemnutzung);
    }

    @Test
    @Transactional
    public void deleteSystemnutzung() throws Exception {
        // Initialize the database
        systemnutzungRepository.saveAndFlush(systemnutzung);

        int databaseSizeBeforeDelete = systemnutzungRepository.findAll().size();

        // Delete the systemnutzung
        restSystemnutzungMockMvc.perform(delete("/api/systemnutzungs/{id}", systemnutzung.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Systemnutzung> systemnutzungList = systemnutzungRepository.findAll();
        assertThat(systemnutzungList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Systemnutzung in Elasticsearch
        verify(mockSystemnutzungSearchRepository, times(1)).deleteById(systemnutzung.getId());
    }

    @Test
    @Transactional
    public void searchSystemnutzung() throws Exception {
        // Initialize the database
        systemnutzungRepository.saveAndFlush(systemnutzung);
        when(mockSystemnutzungSearchRepository.search(queryStringQuery("id:" + systemnutzung.getId())))
            .thenReturn(Collections.singletonList(systemnutzung));
        // Search the systemnutzung
        restSystemnutzungMockMvc.perform(get("/api/_search/systemnutzungs?query=id:" + systemnutzung.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemnutzung.getId().intValue())));
    }
}
