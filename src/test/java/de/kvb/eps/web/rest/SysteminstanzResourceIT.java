package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Systeminstanz;
import de.kvb.eps.domain.Systemtyp;
import de.kvb.eps.repository.SysteminstanzRepository;
import de.kvb.eps.repository.search.SysteminstanzSearchRepository;
import de.kvb.eps.service.SysteminstanzService;
import de.kvb.eps.service.dto.SysteminstanzDTO;
import de.kvb.eps.service.mapper.SysteminstanzMapper;
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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link SysteminstanzResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class SysteminstanzResourceIT {

    private static final String DEFAULT_BEZEICHNUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEZEICHNUNG = "BBBBBBBBBB";

    private static final String DEFAULT_GERAET_NUMMER = "AAAAAAAAAA";
    private static final String UPDATED_GERAET_NUMMER = "BBBBBBBBBB";

    private static final String DEFAULT_GERAET_BAUJAHR = "AAAA";
    private static final String UPDATED_GERAET_BAUJAHR = "BBBB";

    private static final LocalDate DEFAULT_GUELTIG_BIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUELTIG_BIS = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_GWE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_GWE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_GWE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_GWE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_BEMERKUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEMERKUNG = "BBBBBBBBBB";

    @Autowired
    private SysteminstanzRepository systeminstanzRepository;

    @Autowired
    private SysteminstanzMapper systeminstanzMapper;

    @Autowired
    private SysteminstanzService systeminstanzService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.SysteminstanzSearchRepositoryMockConfiguration
     */
    @Autowired
    private SysteminstanzSearchRepository mockSysteminstanzSearchRepository;

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

    private MockMvc restSysteminstanzMockMvc;

    private Systeminstanz systeminstanz;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SysteminstanzResource systeminstanzResource = new SysteminstanzResource(systeminstanzService);
        this.restSysteminstanzMockMvc = MockMvcBuilders.standaloneSetup(systeminstanzResource)
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
    public static Systeminstanz createEntity(EntityManager em) {
        Systeminstanz systeminstanz = new Systeminstanz()
            .bezeichnung(DEFAULT_BEZEICHNUNG)
            .geraetNummer(DEFAULT_GERAET_NUMMER)
            .geraetBaujahr(DEFAULT_GERAET_BAUJAHR)
            .gueltigBis(DEFAULT_GUELTIG_BIS)
            .gwe(DEFAULT_GWE)
            .gweContentType(DEFAULT_GWE_CONTENT_TYPE)
            .bemerkung(DEFAULT_BEMERKUNG);
        // Add required entity
        Systemtyp systemtyp;
        if (TestUtil.findAll(em, Systemtyp.class).isEmpty()) {
            systemtyp = SystemtypResourceIT.createEntity(em);
            em.persist(systemtyp);
            em.flush();
        } else {
            systemtyp = TestUtil.findAll(em, Systemtyp.class).get(0);
        }
        systeminstanz.setSystemtyp(systemtyp);
        return systeminstanz;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Systeminstanz createUpdatedEntity(EntityManager em) {
        Systeminstanz systeminstanz = new Systeminstanz()
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .geraetNummer(UPDATED_GERAET_NUMMER)
            .geraetBaujahr(UPDATED_GERAET_BAUJAHR)
            .gueltigBis(UPDATED_GUELTIG_BIS)
            .gwe(UPDATED_GWE)
            .gweContentType(UPDATED_GWE_CONTENT_TYPE)
            .bemerkung(UPDATED_BEMERKUNG);
        // Add required entity
        Systemtyp systemtyp;
        if (TestUtil.findAll(em, Systemtyp.class).isEmpty()) {
            systemtyp = SystemtypResourceIT.createUpdatedEntity(em);
            em.persist(systemtyp);
            em.flush();
        } else {
            systemtyp = TestUtil.findAll(em, Systemtyp.class).get(0);
        }
        systeminstanz.setSystemtyp(systemtyp);
        return systeminstanz;
    }

    @BeforeEach
    public void initTest() {
        systeminstanz = createEntity(em);
    }

    @Test
    @Transactional
    public void createSysteminstanz() throws Exception {
        int databaseSizeBeforeCreate = systeminstanzRepository.findAll().size();

        // Create the Systeminstanz
        SysteminstanzDTO systeminstanzDTO = systeminstanzMapper.toDto(systeminstanz);
        restSysteminstanzMockMvc.perform(post("/api/systeminstanzs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systeminstanzDTO)))
            .andExpect(status().isCreated());

        // Validate the Systeminstanz in the database
        List<Systeminstanz> systeminstanzList = systeminstanzRepository.findAll();
        assertThat(systeminstanzList).hasSize(databaseSizeBeforeCreate + 1);
        Systeminstanz testSysteminstanz = systeminstanzList.get(systeminstanzList.size() - 1);
        assertThat(testSysteminstanz.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);
        assertThat(testSysteminstanz.getGeraetNummer()).isEqualTo(DEFAULT_GERAET_NUMMER);
        assertThat(testSysteminstanz.getGeraetBaujahr()).isEqualTo(DEFAULT_GERAET_BAUJAHR);
        assertThat(testSysteminstanz.getGueltigBis()).isEqualTo(DEFAULT_GUELTIG_BIS);
        assertThat(testSysteminstanz.getGwe()).isEqualTo(DEFAULT_GWE);
        assertThat(testSysteminstanz.getGweContentType()).isEqualTo(DEFAULT_GWE_CONTENT_TYPE);
        assertThat(testSysteminstanz.getBemerkung()).isEqualTo(DEFAULT_BEMERKUNG);

        // Validate the Systeminstanz in Elasticsearch
        verify(mockSysteminstanzSearchRepository, times(1)).save(testSysteminstanz);
    }

    @Test
    @Transactional
    public void createSysteminstanzWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systeminstanzRepository.findAll().size();

        // Create the Systeminstanz with an existing ID
        systeminstanz.setId(1L);
        SysteminstanzDTO systeminstanzDTO = systeminstanzMapper.toDto(systeminstanz);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysteminstanzMockMvc.perform(post("/api/systeminstanzs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systeminstanzDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Systeminstanz in the database
        List<Systeminstanz> systeminstanzList = systeminstanzRepository.findAll();
        assertThat(systeminstanzList).hasSize(databaseSizeBeforeCreate);

        // Validate the Systeminstanz in Elasticsearch
        verify(mockSysteminstanzSearchRepository, times(0)).save(systeminstanz);
    }


    @Test
    @Transactional
    public void checkBezeichnungIsRequired() throws Exception {
        int databaseSizeBeforeTest = systeminstanzRepository.findAll().size();
        // set the field null
        systeminstanz.setBezeichnung(null);

        // Create the Systeminstanz, which fails.
        SysteminstanzDTO systeminstanzDTO = systeminstanzMapper.toDto(systeminstanz);

        restSysteminstanzMockMvc.perform(post("/api/systeminstanzs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systeminstanzDTO)))
            .andExpect(status().isBadRequest());

        List<Systeminstanz> systeminstanzList = systeminstanzRepository.findAll();
        assertThat(systeminstanzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGeraetNummerIsRequired() throws Exception {
        int databaseSizeBeforeTest = systeminstanzRepository.findAll().size();
        // set the field null
        systeminstanz.setGeraetNummer(null);

        // Create the Systeminstanz, which fails.
        SysteminstanzDTO systeminstanzDTO = systeminstanzMapper.toDto(systeminstanz);

        restSysteminstanzMockMvc.perform(post("/api/systeminstanzs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systeminstanzDTO)))
            .andExpect(status().isBadRequest());

        List<Systeminstanz> systeminstanzList = systeminstanzRepository.findAll();
        assertThat(systeminstanzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGeraetBaujahrIsRequired() throws Exception {
        int databaseSizeBeforeTest = systeminstanzRepository.findAll().size();
        // set the field null
        systeminstanz.setGeraetBaujahr(null);

        // Create the Systeminstanz, which fails.
        SysteminstanzDTO systeminstanzDTO = systeminstanzMapper.toDto(systeminstanz);

        restSysteminstanzMockMvc.perform(post("/api/systeminstanzs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systeminstanzDTO)))
            .andExpect(status().isBadRequest());

        List<Systeminstanz> systeminstanzList = systeminstanzRepository.findAll();
        assertThat(systeminstanzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzs() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList
        restSysteminstanzMockMvc.perform(get("/api/systeminstanzs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systeminstanz.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].geraetNummer").value(hasItem(DEFAULT_GERAET_NUMMER)))
            .andExpect(jsonPath("$.[*].geraetBaujahr").value(hasItem(DEFAULT_GERAET_BAUJAHR)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].gweContentType").value(hasItem(DEFAULT_GWE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].gwe").value(hasItem(Base64Utils.encodeToString(DEFAULT_GWE))))
            .andExpect(jsonPath("$.[*].bemerkung").value(hasItem(DEFAULT_BEMERKUNG.toString())));
    }
    
    @Test
    @Transactional
    public void getSysteminstanz() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get the systeminstanz
        restSysteminstanzMockMvc.perform(get("/api/systeminstanzs/{id}", systeminstanz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systeminstanz.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.geraetNummer").value(DEFAULT_GERAET_NUMMER))
            .andExpect(jsonPath("$.geraetBaujahr").value(DEFAULT_GERAET_BAUJAHR))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()))
            .andExpect(jsonPath("$.gweContentType").value(DEFAULT_GWE_CONTENT_TYPE))
            .andExpect(jsonPath("$.gwe").value(Base64Utils.encodeToString(DEFAULT_GWE)))
            .andExpect(jsonPath("$.bemerkung").value(DEFAULT_BEMERKUNG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSysteminstanz() throws Exception {
        // Get the systeminstanz
        restSysteminstanzMockMvc.perform(get("/api/systeminstanzs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSysteminstanz() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        int databaseSizeBeforeUpdate = systeminstanzRepository.findAll().size();

        // Update the systeminstanz
        Systeminstanz updatedSysteminstanz = systeminstanzRepository.findById(systeminstanz.getId()).get();
        // Disconnect from session so that the updates on updatedSysteminstanz are not directly saved in db
        em.detach(updatedSysteminstanz);
        updatedSysteminstanz
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .geraetNummer(UPDATED_GERAET_NUMMER)
            .geraetBaujahr(UPDATED_GERAET_BAUJAHR)
            .gueltigBis(UPDATED_GUELTIG_BIS)
            .gwe(UPDATED_GWE)
            .gweContentType(UPDATED_GWE_CONTENT_TYPE)
            .bemerkung(UPDATED_BEMERKUNG);
        SysteminstanzDTO systeminstanzDTO = systeminstanzMapper.toDto(updatedSysteminstanz);

        restSysteminstanzMockMvc.perform(put("/api/systeminstanzs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systeminstanzDTO)))
            .andExpect(status().isOk());

        // Validate the Systeminstanz in the database
        List<Systeminstanz> systeminstanzList = systeminstanzRepository.findAll();
        assertThat(systeminstanzList).hasSize(databaseSizeBeforeUpdate);
        Systeminstanz testSysteminstanz = systeminstanzList.get(systeminstanzList.size() - 1);
        assertThat(testSysteminstanz.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);
        assertThat(testSysteminstanz.getGeraetNummer()).isEqualTo(UPDATED_GERAET_NUMMER);
        assertThat(testSysteminstanz.getGeraetBaujahr()).isEqualTo(UPDATED_GERAET_BAUJAHR);
        assertThat(testSysteminstanz.getGueltigBis()).isEqualTo(UPDATED_GUELTIG_BIS);
        assertThat(testSysteminstanz.getGwe()).isEqualTo(UPDATED_GWE);
        assertThat(testSysteminstanz.getGweContentType()).isEqualTo(UPDATED_GWE_CONTENT_TYPE);
        assertThat(testSysteminstanz.getBemerkung()).isEqualTo(UPDATED_BEMERKUNG);

        // Validate the Systeminstanz in Elasticsearch
        verify(mockSysteminstanzSearchRepository, times(1)).save(testSysteminstanz);
    }

    @Test
    @Transactional
    public void updateNonExistingSysteminstanz() throws Exception {
        int databaseSizeBeforeUpdate = systeminstanzRepository.findAll().size();

        // Create the Systeminstanz
        SysteminstanzDTO systeminstanzDTO = systeminstanzMapper.toDto(systeminstanz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysteminstanzMockMvc.perform(put("/api/systeminstanzs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systeminstanzDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Systeminstanz in the database
        List<Systeminstanz> systeminstanzList = systeminstanzRepository.findAll();
        assertThat(systeminstanzList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Systeminstanz in Elasticsearch
        verify(mockSysteminstanzSearchRepository, times(0)).save(systeminstanz);
    }

    @Test
    @Transactional
    public void deleteSysteminstanz() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        int databaseSizeBeforeDelete = systeminstanzRepository.findAll().size();

        // Delete the systeminstanz
        restSysteminstanzMockMvc.perform(delete("/api/systeminstanzs/{id}", systeminstanz.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Systeminstanz> systeminstanzList = systeminstanzRepository.findAll();
        assertThat(systeminstanzList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Systeminstanz in Elasticsearch
        verify(mockSysteminstanzSearchRepository, times(1)).deleteById(systeminstanz.getId());
    }

    @Test
    @Transactional
    public void searchSysteminstanz() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);
        when(mockSysteminstanzSearchRepository.search(queryStringQuery("id:" + systeminstanz.getId())))
            .thenReturn(Collections.singletonList(systeminstanz));
        // Search the systeminstanz
        restSysteminstanzMockMvc.perform(get("/api/_search/systeminstanzs?query=id:" + systeminstanz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systeminstanz.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].geraetNummer").value(hasItem(DEFAULT_GERAET_NUMMER)))
            .andExpect(jsonPath("$.[*].geraetBaujahr").value(hasItem(DEFAULT_GERAET_BAUJAHR)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].gweContentType").value(hasItem(DEFAULT_GWE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].gwe").value(hasItem(Base64Utils.encodeToString(DEFAULT_GWE))))
            .andExpect(jsonPath("$.[*].bemerkung").value(hasItem(DEFAULT_BEMERKUNG.toString())));
    }
}
