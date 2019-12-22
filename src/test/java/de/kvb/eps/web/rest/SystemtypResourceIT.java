package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Systemtyp;
import de.kvb.eps.domain.Geraet;
import de.kvb.eps.repository.SystemtypRepository;
import de.kvb.eps.repository.search.SystemtypSearchRepository;
import de.kvb.eps.service.SystemtypService;
import de.kvb.eps.service.dto.SystemtypDTO;
import de.kvb.eps.service.mapper.SystemtypMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SystemtypResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class SystemtypResourceIT {

    private static final String DEFAULT_BEZEICHNUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEZEICHNUNG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GUELTIG_BIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUELTIG_BIS = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SystemtypRepository systemtypRepository;

    @Autowired
    private SystemtypMapper systemtypMapper;

    @Autowired
    private SystemtypService systemtypService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.SystemtypSearchRepositoryMockConfiguration
     */
    @Autowired
    private SystemtypSearchRepository mockSystemtypSearchRepository;

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

    private MockMvc restSystemtypMockMvc;

    private Systemtyp systemtyp;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SystemtypResource systemtypResource = new SystemtypResource(systemtypService);
        this.restSystemtypMockMvc = MockMvcBuilders.standaloneSetup(systemtypResource)
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
    public static Systemtyp createEntity(EntityManager em) {
        Systemtyp systemtyp = new Systemtyp()
            .bezeichnung(DEFAULT_BEZEICHNUNG)
            .gueltigBis(DEFAULT_GUELTIG_BIS);
        // Add required entity
        Geraet geraet;
        if (TestUtil.findAll(em, Geraet.class).isEmpty()) {
            geraet = GeraetResourceIT.createEntity(em);
            em.persist(geraet);
            em.flush();
        } else {
            geraet = TestUtil.findAll(em, Geraet.class).get(0);
        }
        systemtyp.setGeraet(geraet);
        return systemtyp;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Systemtyp createUpdatedEntity(EntityManager em) {
        Systemtyp systemtyp = new Systemtyp()
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS);
        // Add required entity
        Geraet geraet;
        if (TestUtil.findAll(em, Geraet.class).isEmpty()) {
            geraet = GeraetResourceIT.createUpdatedEntity(em);
            em.persist(geraet);
            em.flush();
        } else {
            geraet = TestUtil.findAll(em, Geraet.class).get(0);
        }
        systemtyp.setGeraet(geraet);
        return systemtyp;
    }

    @BeforeEach
    public void initTest() {
        systemtyp = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemtyp() throws Exception {
        int databaseSizeBeforeCreate = systemtypRepository.findAll().size();

        // Create the Systemtyp
        SystemtypDTO systemtypDTO = systemtypMapper.toDto(systemtyp);
        restSystemtypMockMvc.perform(post("/api/systemtyps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemtypDTO)))
            .andExpect(status().isCreated());

        // Validate the Systemtyp in the database
        List<Systemtyp> systemtypList = systemtypRepository.findAll();
        assertThat(systemtypList).hasSize(databaseSizeBeforeCreate + 1);
        Systemtyp testSystemtyp = systemtypList.get(systemtypList.size() - 1);
        assertThat(testSystemtyp.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);
        assertThat(testSystemtyp.getGueltigBis()).isEqualTo(DEFAULT_GUELTIG_BIS);

        // Validate the Systemtyp in Elasticsearch
        verify(mockSystemtypSearchRepository, times(1)).save(testSystemtyp);
    }

    @Test
    @Transactional
    public void createSystemtypWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemtypRepository.findAll().size();

        // Create the Systemtyp with an existing ID
        systemtyp.setId(1L);
        SystemtypDTO systemtypDTO = systemtypMapper.toDto(systemtyp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemtypMockMvc.perform(post("/api/systemtyps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemtypDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Systemtyp in the database
        List<Systemtyp> systemtypList = systemtypRepository.findAll();
        assertThat(systemtypList).hasSize(databaseSizeBeforeCreate);

        // Validate the Systemtyp in Elasticsearch
        verify(mockSystemtypSearchRepository, times(0)).save(systemtyp);
    }


    @Test
    @Transactional
    public void checkBezeichnungIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemtypRepository.findAll().size();
        // set the field null
        systemtyp.setBezeichnung(null);

        // Create the Systemtyp, which fails.
        SystemtypDTO systemtypDTO = systemtypMapper.toDto(systemtyp);

        restSystemtypMockMvc.perform(post("/api/systemtyps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemtypDTO)))
            .andExpect(status().isBadRequest());

        List<Systemtyp> systemtypList = systemtypRepository.findAll();
        assertThat(systemtypList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSystemtyps() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList
        restSystemtypMockMvc.perform(get("/api/systemtyps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemtyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));
    }
    
    @Test
    @Transactional
    public void getSystemtyp() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get the systemtyp
        restSystemtypMockMvc.perform(get("/api/systemtyps/{id}", systemtyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systemtyp.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSystemtyp() throws Exception {
        // Get the systemtyp
        restSystemtypMockMvc.perform(get("/api/systemtyps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemtyp() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        int databaseSizeBeforeUpdate = systemtypRepository.findAll().size();

        // Update the systemtyp
        Systemtyp updatedSystemtyp = systemtypRepository.findById(systemtyp.getId()).get();
        // Disconnect from session so that the updates on updatedSystemtyp are not directly saved in db
        em.detach(updatedSystemtyp);
        updatedSystemtyp
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS);
        SystemtypDTO systemtypDTO = systemtypMapper.toDto(updatedSystemtyp);

        restSystemtypMockMvc.perform(put("/api/systemtyps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemtypDTO)))
            .andExpect(status().isOk());

        // Validate the Systemtyp in the database
        List<Systemtyp> systemtypList = systemtypRepository.findAll();
        assertThat(systemtypList).hasSize(databaseSizeBeforeUpdate);
        Systemtyp testSystemtyp = systemtypList.get(systemtypList.size() - 1);
        assertThat(testSystemtyp.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);
        assertThat(testSystemtyp.getGueltigBis()).isEqualTo(UPDATED_GUELTIG_BIS);

        // Validate the Systemtyp in Elasticsearch
        verify(mockSystemtypSearchRepository, times(1)).save(testSystemtyp);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemtyp() throws Exception {
        int databaseSizeBeforeUpdate = systemtypRepository.findAll().size();

        // Create the Systemtyp
        SystemtypDTO systemtypDTO = systemtypMapper.toDto(systemtyp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemtypMockMvc.perform(put("/api/systemtyps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemtypDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Systemtyp in the database
        List<Systemtyp> systemtypList = systemtypRepository.findAll();
        assertThat(systemtypList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Systemtyp in Elasticsearch
        verify(mockSystemtypSearchRepository, times(0)).save(systemtyp);
    }

    @Test
    @Transactional
    public void deleteSystemtyp() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        int databaseSizeBeforeDelete = systemtypRepository.findAll().size();

        // Delete the systemtyp
        restSystemtypMockMvc.perform(delete("/api/systemtyps/{id}", systemtyp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Systemtyp> systemtypList = systemtypRepository.findAll();
        assertThat(systemtypList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Systemtyp in Elasticsearch
        verify(mockSystemtypSearchRepository, times(1)).deleteById(systemtyp.getId());
    }

    @Test
    @Transactional
    public void searchSystemtyp() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);
        when(mockSystemtypSearchRepository.search(queryStringQuery("id:" + systemtyp.getId())))
            .thenReturn(Collections.singletonList(systemtyp));
        // Search the systemtyp
        restSystemtypMockMvc.perform(get("/api/_search/systemtyps?query=id:" + systemtyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemtyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));
    }
}
