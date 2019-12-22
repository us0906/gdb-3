package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Arzt;
import de.kvb.eps.repository.ArztRepository;
import de.kvb.eps.repository.search.ArztSearchRepository;
import de.kvb.eps.service.ArztService;
import de.kvb.eps.service.dto.ArztDTO;
import de.kvb.eps.service.mapper.ArztMapper;
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
 * Integration tests for the {@link ArztResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class ArztResourceIT {

    private static final String DEFAULT_LANR = "AAAAAAA";
    private static final String UPDATED_LANR = "BBBBBBB";

    private static final String DEFAULT_TITEL = "AAAAAAAAAA";
    private static final String UPDATED_TITEL = "BBBBBBBBBB";

    private static final String DEFAULT_VORNAME = "AAAAAAAAAA";
    private static final String UPDATED_VORNAME = "BBBBBBBBBB";

    private static final String DEFAULT_NACHNAME = "AAAAAAAAAA";
    private static final String UPDATED_NACHNAME = "BBBBBBBBBB";

    @Autowired
    private ArztRepository arztRepository;

    @Autowired
    private ArztMapper arztMapper;

    @Autowired
    private ArztService arztService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.ArztSearchRepositoryMockConfiguration
     */
    @Autowired
    private ArztSearchRepository mockArztSearchRepository;

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

    private MockMvc restArztMockMvc;

    private Arzt arzt;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArztResource arztResource = new ArztResource(arztService);
        this.restArztMockMvc = MockMvcBuilders.standaloneSetup(arztResource)
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
    public static Arzt createEntity(EntityManager em) {
        Arzt arzt = new Arzt()
            .lanr(DEFAULT_LANR)
            .titel(DEFAULT_TITEL)
            .vorname(DEFAULT_VORNAME)
            .nachname(DEFAULT_NACHNAME);
        return arzt;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arzt createUpdatedEntity(EntityManager em) {
        Arzt arzt = new Arzt()
            .lanr(UPDATED_LANR)
            .titel(UPDATED_TITEL)
            .vorname(UPDATED_VORNAME)
            .nachname(UPDATED_NACHNAME);
        return arzt;
    }

    @BeforeEach
    public void initTest() {
        arzt = createEntity(em);
    }

    @Test
    @Transactional
    public void createArzt() throws Exception {
        int databaseSizeBeforeCreate = arztRepository.findAll().size();

        // Create the Arzt
        ArztDTO arztDTO = arztMapper.toDto(arzt);
        restArztMockMvc.perform(post("/api/arzts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arztDTO)))
            .andExpect(status().isCreated());

        // Validate the Arzt in the database
        List<Arzt> arztList = arztRepository.findAll();
        assertThat(arztList).hasSize(databaseSizeBeforeCreate + 1);
        Arzt testArzt = arztList.get(arztList.size() - 1);
        assertThat(testArzt.getLanr()).isEqualTo(DEFAULT_LANR);
        assertThat(testArzt.getTitel()).isEqualTo(DEFAULT_TITEL);
        assertThat(testArzt.getVorname()).isEqualTo(DEFAULT_VORNAME);
        assertThat(testArzt.getNachname()).isEqualTo(DEFAULT_NACHNAME);

        // Validate the Arzt in Elasticsearch
        verify(mockArztSearchRepository, times(1)).save(testArzt);
    }

    @Test
    @Transactional
    public void createArztWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = arztRepository.findAll().size();

        // Create the Arzt with an existing ID
        arzt.setId(1L);
        ArztDTO arztDTO = arztMapper.toDto(arzt);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArztMockMvc.perform(post("/api/arzts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arztDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Arzt in the database
        List<Arzt> arztList = arztRepository.findAll();
        assertThat(arztList).hasSize(databaseSizeBeforeCreate);

        // Validate the Arzt in Elasticsearch
        verify(mockArztSearchRepository, times(0)).save(arzt);
    }


    @Test
    @Transactional
    public void checkLanrIsRequired() throws Exception {
        int databaseSizeBeforeTest = arztRepository.findAll().size();
        // set the field null
        arzt.setLanr(null);

        // Create the Arzt, which fails.
        ArztDTO arztDTO = arztMapper.toDto(arzt);

        restArztMockMvc.perform(post("/api/arzts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arztDTO)))
            .andExpect(status().isBadRequest());

        List<Arzt> arztList = arztRepository.findAll();
        assertThat(arztList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVornameIsRequired() throws Exception {
        int databaseSizeBeforeTest = arztRepository.findAll().size();
        // set the field null
        arzt.setVorname(null);

        // Create the Arzt, which fails.
        ArztDTO arztDTO = arztMapper.toDto(arzt);

        restArztMockMvc.perform(post("/api/arzts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arztDTO)))
            .andExpect(status().isBadRequest());

        List<Arzt> arztList = arztRepository.findAll();
        assertThat(arztList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNachnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = arztRepository.findAll().size();
        // set the field null
        arzt.setNachname(null);

        // Create the Arzt, which fails.
        ArztDTO arztDTO = arztMapper.toDto(arzt);

        restArztMockMvc.perform(post("/api/arzts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arztDTO)))
            .andExpect(status().isBadRequest());

        List<Arzt> arztList = arztRepository.findAll();
        assertThat(arztList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArzts() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList
        restArztMockMvc.perform(get("/api/arzts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arzt.getId().intValue())))
            .andExpect(jsonPath("$.[*].lanr").value(hasItem(DEFAULT_LANR)))
            .andExpect(jsonPath("$.[*].titel").value(hasItem(DEFAULT_TITEL)))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)));
    }
    
    @Test
    @Transactional
    public void getArzt() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get the arzt
        restArztMockMvc.perform(get("/api/arzts/{id}", arzt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(arzt.getId().intValue()))
            .andExpect(jsonPath("$.lanr").value(DEFAULT_LANR))
            .andExpect(jsonPath("$.titel").value(DEFAULT_TITEL))
            .andExpect(jsonPath("$.vorname").value(DEFAULT_VORNAME))
            .andExpect(jsonPath("$.nachname").value(DEFAULT_NACHNAME));
    }

    @Test
    @Transactional
    public void getNonExistingArzt() throws Exception {
        // Get the arzt
        restArztMockMvc.perform(get("/api/arzts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArzt() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        int databaseSizeBeforeUpdate = arztRepository.findAll().size();

        // Update the arzt
        Arzt updatedArzt = arztRepository.findById(arzt.getId()).get();
        // Disconnect from session so that the updates on updatedArzt are not directly saved in db
        em.detach(updatedArzt);
        updatedArzt
            .lanr(UPDATED_LANR)
            .titel(UPDATED_TITEL)
            .vorname(UPDATED_VORNAME)
            .nachname(UPDATED_NACHNAME);
        ArztDTO arztDTO = arztMapper.toDto(updatedArzt);

        restArztMockMvc.perform(put("/api/arzts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arztDTO)))
            .andExpect(status().isOk());

        // Validate the Arzt in the database
        List<Arzt> arztList = arztRepository.findAll();
        assertThat(arztList).hasSize(databaseSizeBeforeUpdate);
        Arzt testArzt = arztList.get(arztList.size() - 1);
        assertThat(testArzt.getLanr()).isEqualTo(UPDATED_LANR);
        assertThat(testArzt.getTitel()).isEqualTo(UPDATED_TITEL);
        assertThat(testArzt.getVorname()).isEqualTo(UPDATED_VORNAME);
        assertThat(testArzt.getNachname()).isEqualTo(UPDATED_NACHNAME);

        // Validate the Arzt in Elasticsearch
        verify(mockArztSearchRepository, times(1)).save(testArzt);
    }

    @Test
    @Transactional
    public void updateNonExistingArzt() throws Exception {
        int databaseSizeBeforeUpdate = arztRepository.findAll().size();

        // Create the Arzt
        ArztDTO arztDTO = arztMapper.toDto(arzt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArztMockMvc.perform(put("/api/arzts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arztDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Arzt in the database
        List<Arzt> arztList = arztRepository.findAll();
        assertThat(arztList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Arzt in Elasticsearch
        verify(mockArztSearchRepository, times(0)).save(arzt);
    }

    @Test
    @Transactional
    public void deleteArzt() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        int databaseSizeBeforeDelete = arztRepository.findAll().size();

        // Delete the arzt
        restArztMockMvc.perform(delete("/api/arzts/{id}", arzt.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Arzt> arztList = arztRepository.findAll();
        assertThat(arztList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Arzt in Elasticsearch
        verify(mockArztSearchRepository, times(1)).deleteById(arzt.getId());
    }

    @Test
    @Transactional
    public void searchArzt() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);
        when(mockArztSearchRepository.search(queryStringQuery("id:" + arzt.getId())))
            .thenReturn(Collections.singletonList(arzt));
        // Search the arzt
        restArztMockMvc.perform(get("/api/_search/arzts?query=id:" + arzt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arzt.getId().intValue())))
            .andExpect(jsonPath("$.[*].lanr").value(hasItem(DEFAULT_LANR)))
            .andExpect(jsonPath("$.[*].titel").value(hasItem(DEFAULT_TITEL)))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)));
    }
}
