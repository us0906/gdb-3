package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Systeminstanz;
import de.kvb.eps.domain.Systemnutzung;
import de.kvb.eps.domain.Systemtyp;
import de.kvb.eps.domain.Betriebsstaette;
import de.kvb.eps.domain.Betreiber;
import de.kvb.eps.repository.SysteminstanzRepository;
import de.kvb.eps.repository.search.SysteminstanzSearchRepository;
import de.kvb.eps.service.SysteminstanzService;
import de.kvb.eps.service.dto.SysteminstanzDTO;
import de.kvb.eps.service.mapper.SysteminstanzMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.SysteminstanzCriteria;
import de.kvb.eps.service.SysteminstanzQueryService;

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
    private static final LocalDate SMALLER_GUELTIG_BIS = LocalDate.ofEpochDay(-1L);

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
    private SysteminstanzQueryService systeminstanzQueryService;

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
        final SysteminstanzResource systeminstanzResource = new SysteminstanzResource(systeminstanzService, systeminstanzQueryService);
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
        // Add required entity
        Betriebsstaette betriebsstaette;
        if (TestUtil.findAll(em, Betriebsstaette.class).isEmpty()) {
            betriebsstaette = BetriebsstaetteResourceIT.createEntity(em);
            em.persist(betriebsstaette);
            em.flush();
        } else {
            betriebsstaette = TestUtil.findAll(em, Betriebsstaette.class).get(0);
        }
        systeminstanz.setBetriebsstaette(betriebsstaette);
        // Add required entity
        Betreiber betreiber;
        if (TestUtil.findAll(em, Betreiber.class).isEmpty()) {
            betreiber = BetreiberResourceIT.createEntity(em);
            em.persist(betreiber);
            em.flush();
        } else {
            betreiber = TestUtil.findAll(em, Betreiber.class).get(0);
        }
        systeminstanz.setBetreiber(betreiber);
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
        // Add required entity
        Betriebsstaette betriebsstaette;
        if (TestUtil.findAll(em, Betriebsstaette.class).isEmpty()) {
            betriebsstaette = BetriebsstaetteResourceIT.createUpdatedEntity(em);
            em.persist(betriebsstaette);
            em.flush();
        } else {
            betriebsstaette = TestUtil.findAll(em, Betriebsstaette.class).get(0);
        }
        systeminstanz.setBetriebsstaette(betriebsstaette);
        // Add required entity
        Betreiber betreiber;
        if (TestUtil.findAll(em, Betreiber.class).isEmpty()) {
            betreiber = BetreiberResourceIT.createUpdatedEntity(em);
            em.persist(betreiber);
            em.flush();
        } else {
            betreiber = TestUtil.findAll(em, Betreiber.class).get(0);
        }
        systeminstanz.setBetreiber(betreiber);
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
    public void getSysteminstanzsByIdFiltering() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        Long id = systeminstanz.getId();

        defaultSysteminstanzShouldBeFound("id.equals=" + id);
        defaultSysteminstanzShouldNotBeFound("id.notEquals=" + id);

        defaultSysteminstanzShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSysteminstanzShouldNotBeFound("id.greaterThan=" + id);

        defaultSysteminstanzShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSysteminstanzShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSysteminstanzsByBezeichnungIsEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where bezeichnung equals to DEFAULT_BEZEICHNUNG
        defaultSysteminstanzShouldBeFound("bezeichnung.equals=" + DEFAULT_BEZEICHNUNG);

        // Get all the systeminstanzList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultSysteminstanzShouldNotBeFound("bezeichnung.equals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByBezeichnungIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where bezeichnung not equals to DEFAULT_BEZEICHNUNG
        defaultSysteminstanzShouldNotBeFound("bezeichnung.notEquals=" + DEFAULT_BEZEICHNUNG);

        // Get all the systeminstanzList where bezeichnung not equals to UPDATED_BEZEICHNUNG
        defaultSysteminstanzShouldBeFound("bezeichnung.notEquals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByBezeichnungIsInShouldWork() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where bezeichnung in DEFAULT_BEZEICHNUNG or UPDATED_BEZEICHNUNG
        defaultSysteminstanzShouldBeFound("bezeichnung.in=" + DEFAULT_BEZEICHNUNG + "," + UPDATED_BEZEICHNUNG);

        // Get all the systeminstanzList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultSysteminstanzShouldNotBeFound("bezeichnung.in=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByBezeichnungIsNullOrNotNull() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where bezeichnung is not null
        defaultSysteminstanzShouldBeFound("bezeichnung.specified=true");

        // Get all the systeminstanzList where bezeichnung is null
        defaultSysteminstanzShouldNotBeFound("bezeichnung.specified=false");
    }
                @Test
    @Transactional
    public void getAllSysteminstanzsByBezeichnungContainsSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where bezeichnung contains DEFAULT_BEZEICHNUNG
        defaultSysteminstanzShouldBeFound("bezeichnung.contains=" + DEFAULT_BEZEICHNUNG);

        // Get all the systeminstanzList where bezeichnung contains UPDATED_BEZEICHNUNG
        defaultSysteminstanzShouldNotBeFound("bezeichnung.contains=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByBezeichnungNotContainsSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where bezeichnung does not contain DEFAULT_BEZEICHNUNG
        defaultSysteminstanzShouldNotBeFound("bezeichnung.doesNotContain=" + DEFAULT_BEZEICHNUNG);

        // Get all the systeminstanzList where bezeichnung does not contain UPDATED_BEZEICHNUNG
        defaultSysteminstanzShouldBeFound("bezeichnung.doesNotContain=" + UPDATED_BEZEICHNUNG);
    }


    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetNummerIsEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetNummer equals to DEFAULT_GERAET_NUMMER
        defaultSysteminstanzShouldBeFound("geraetNummer.equals=" + DEFAULT_GERAET_NUMMER);

        // Get all the systeminstanzList where geraetNummer equals to UPDATED_GERAET_NUMMER
        defaultSysteminstanzShouldNotBeFound("geraetNummer.equals=" + UPDATED_GERAET_NUMMER);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetNummerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetNummer not equals to DEFAULT_GERAET_NUMMER
        defaultSysteminstanzShouldNotBeFound("geraetNummer.notEquals=" + DEFAULT_GERAET_NUMMER);

        // Get all the systeminstanzList where geraetNummer not equals to UPDATED_GERAET_NUMMER
        defaultSysteminstanzShouldBeFound("geraetNummer.notEquals=" + UPDATED_GERAET_NUMMER);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetNummerIsInShouldWork() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetNummer in DEFAULT_GERAET_NUMMER or UPDATED_GERAET_NUMMER
        defaultSysteminstanzShouldBeFound("geraetNummer.in=" + DEFAULT_GERAET_NUMMER + "," + UPDATED_GERAET_NUMMER);

        // Get all the systeminstanzList where geraetNummer equals to UPDATED_GERAET_NUMMER
        defaultSysteminstanzShouldNotBeFound("geraetNummer.in=" + UPDATED_GERAET_NUMMER);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetNummerIsNullOrNotNull() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetNummer is not null
        defaultSysteminstanzShouldBeFound("geraetNummer.specified=true");

        // Get all the systeminstanzList where geraetNummer is null
        defaultSysteminstanzShouldNotBeFound("geraetNummer.specified=false");
    }
                @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetNummerContainsSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetNummer contains DEFAULT_GERAET_NUMMER
        defaultSysteminstanzShouldBeFound("geraetNummer.contains=" + DEFAULT_GERAET_NUMMER);

        // Get all the systeminstanzList where geraetNummer contains UPDATED_GERAET_NUMMER
        defaultSysteminstanzShouldNotBeFound("geraetNummer.contains=" + UPDATED_GERAET_NUMMER);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetNummerNotContainsSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetNummer does not contain DEFAULT_GERAET_NUMMER
        defaultSysteminstanzShouldNotBeFound("geraetNummer.doesNotContain=" + DEFAULT_GERAET_NUMMER);

        // Get all the systeminstanzList where geraetNummer does not contain UPDATED_GERAET_NUMMER
        defaultSysteminstanzShouldBeFound("geraetNummer.doesNotContain=" + UPDATED_GERAET_NUMMER);
    }


    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetBaujahrIsEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetBaujahr equals to DEFAULT_GERAET_BAUJAHR
        defaultSysteminstanzShouldBeFound("geraetBaujahr.equals=" + DEFAULT_GERAET_BAUJAHR);

        // Get all the systeminstanzList where geraetBaujahr equals to UPDATED_GERAET_BAUJAHR
        defaultSysteminstanzShouldNotBeFound("geraetBaujahr.equals=" + UPDATED_GERAET_BAUJAHR);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetBaujahrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetBaujahr not equals to DEFAULT_GERAET_BAUJAHR
        defaultSysteminstanzShouldNotBeFound("geraetBaujahr.notEquals=" + DEFAULT_GERAET_BAUJAHR);

        // Get all the systeminstanzList where geraetBaujahr not equals to UPDATED_GERAET_BAUJAHR
        defaultSysteminstanzShouldBeFound("geraetBaujahr.notEquals=" + UPDATED_GERAET_BAUJAHR);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetBaujahrIsInShouldWork() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetBaujahr in DEFAULT_GERAET_BAUJAHR or UPDATED_GERAET_BAUJAHR
        defaultSysteminstanzShouldBeFound("geraetBaujahr.in=" + DEFAULT_GERAET_BAUJAHR + "," + UPDATED_GERAET_BAUJAHR);

        // Get all the systeminstanzList where geraetBaujahr equals to UPDATED_GERAET_BAUJAHR
        defaultSysteminstanzShouldNotBeFound("geraetBaujahr.in=" + UPDATED_GERAET_BAUJAHR);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetBaujahrIsNullOrNotNull() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetBaujahr is not null
        defaultSysteminstanzShouldBeFound("geraetBaujahr.specified=true");

        // Get all the systeminstanzList where geraetBaujahr is null
        defaultSysteminstanzShouldNotBeFound("geraetBaujahr.specified=false");
    }
                @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetBaujahrContainsSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetBaujahr contains DEFAULT_GERAET_BAUJAHR
        defaultSysteminstanzShouldBeFound("geraetBaujahr.contains=" + DEFAULT_GERAET_BAUJAHR);

        // Get all the systeminstanzList where geraetBaujahr contains UPDATED_GERAET_BAUJAHR
        defaultSysteminstanzShouldNotBeFound("geraetBaujahr.contains=" + UPDATED_GERAET_BAUJAHR);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGeraetBaujahrNotContainsSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where geraetBaujahr does not contain DEFAULT_GERAET_BAUJAHR
        defaultSysteminstanzShouldNotBeFound("geraetBaujahr.doesNotContain=" + DEFAULT_GERAET_BAUJAHR);

        // Get all the systeminstanzList where geraetBaujahr does not contain UPDATED_GERAET_BAUJAHR
        defaultSysteminstanzShouldBeFound("geraetBaujahr.doesNotContain=" + UPDATED_GERAET_BAUJAHR);
    }


    @Test
    @Transactional
    public void getAllSysteminstanzsByGueltigBisIsEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where gueltigBis equals to DEFAULT_GUELTIG_BIS
        defaultSysteminstanzShouldBeFound("gueltigBis.equals=" + DEFAULT_GUELTIG_BIS);

        // Get all the systeminstanzList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultSysteminstanzShouldNotBeFound("gueltigBis.equals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGueltigBisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where gueltigBis not equals to DEFAULT_GUELTIG_BIS
        defaultSysteminstanzShouldNotBeFound("gueltigBis.notEquals=" + DEFAULT_GUELTIG_BIS);

        // Get all the systeminstanzList where gueltigBis not equals to UPDATED_GUELTIG_BIS
        defaultSysteminstanzShouldBeFound("gueltigBis.notEquals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGueltigBisIsInShouldWork() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where gueltigBis in DEFAULT_GUELTIG_BIS or UPDATED_GUELTIG_BIS
        defaultSysteminstanzShouldBeFound("gueltigBis.in=" + DEFAULT_GUELTIG_BIS + "," + UPDATED_GUELTIG_BIS);

        // Get all the systeminstanzList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultSysteminstanzShouldNotBeFound("gueltigBis.in=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGueltigBisIsNullOrNotNull() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where gueltigBis is not null
        defaultSysteminstanzShouldBeFound("gueltigBis.specified=true");

        // Get all the systeminstanzList where gueltigBis is null
        defaultSysteminstanzShouldNotBeFound("gueltigBis.specified=false");
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGueltigBisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where gueltigBis is greater than or equal to DEFAULT_GUELTIG_BIS
        defaultSysteminstanzShouldBeFound("gueltigBis.greaterThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the systeminstanzList where gueltigBis is greater than or equal to UPDATED_GUELTIG_BIS
        defaultSysteminstanzShouldNotBeFound("gueltigBis.greaterThanOrEqual=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGueltigBisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where gueltigBis is less than or equal to DEFAULT_GUELTIG_BIS
        defaultSysteminstanzShouldBeFound("gueltigBis.lessThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the systeminstanzList where gueltigBis is less than or equal to SMALLER_GUELTIG_BIS
        defaultSysteminstanzShouldNotBeFound("gueltigBis.lessThanOrEqual=" + SMALLER_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGueltigBisIsLessThanSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where gueltigBis is less than DEFAULT_GUELTIG_BIS
        defaultSysteminstanzShouldNotBeFound("gueltigBis.lessThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the systeminstanzList where gueltigBis is less than UPDATED_GUELTIG_BIS
        defaultSysteminstanzShouldBeFound("gueltigBis.lessThan=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSysteminstanzsByGueltigBisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);

        // Get all the systeminstanzList where gueltigBis is greater than DEFAULT_GUELTIG_BIS
        defaultSysteminstanzShouldNotBeFound("gueltigBis.greaterThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the systeminstanzList where gueltigBis is greater than SMALLER_GUELTIG_BIS
        defaultSysteminstanzShouldBeFound("gueltigBis.greaterThan=" + SMALLER_GUELTIG_BIS);
    }


    @Test
    @Transactional
    public void getAllSysteminstanzsBySystemnutzungIsEqualToSomething() throws Exception {
        // Initialize the database
        systeminstanzRepository.saveAndFlush(systeminstanz);
        Systemnutzung systemnutzung = SystemnutzungResourceIT.createEntity(em);
        em.persist(systemnutzung);
        em.flush();
        systeminstanz.addSystemnutzung(systemnutzung);
        systeminstanzRepository.saveAndFlush(systeminstanz);
        Long systemnutzungId = systemnutzung.getId();

        // Get all the systeminstanzList where systemnutzung equals to systemnutzungId
        defaultSysteminstanzShouldBeFound("systemnutzungId.equals=" + systemnutzungId);

        // Get all the systeminstanzList where systemnutzung equals to systemnutzungId + 1
        defaultSysteminstanzShouldNotBeFound("systemnutzungId.equals=" + (systemnutzungId + 1));
    }


    @Test
    @Transactional
    public void getAllSysteminstanzsBySystemtypIsEqualToSomething() throws Exception {
        // Get already existing entity
        Systemtyp systemtyp = systeminstanz.getSystemtyp();
        systeminstanzRepository.saveAndFlush(systeminstanz);
        Long systemtypId = systemtyp.getId();

        // Get all the systeminstanzList where systemtyp equals to systemtypId
        defaultSysteminstanzShouldBeFound("systemtypId.equals=" + systemtypId);

        // Get all the systeminstanzList where systemtyp equals to systemtypId + 1
        defaultSysteminstanzShouldNotBeFound("systemtypId.equals=" + (systemtypId + 1));
    }


    @Test
    @Transactional
    public void getAllSysteminstanzsByBetriebsstaetteIsEqualToSomething() throws Exception {
        // Get already existing entity
        Betriebsstaette betriebsstaette = systeminstanz.getBetriebsstaette();
        systeminstanzRepository.saveAndFlush(systeminstanz);
        Long betriebsstaetteId = betriebsstaette.getId();

        // Get all the systeminstanzList where betriebsstaette equals to betriebsstaetteId
        defaultSysteminstanzShouldBeFound("betriebsstaetteId.equals=" + betriebsstaetteId);

        // Get all the systeminstanzList where betriebsstaette equals to betriebsstaetteId + 1
        defaultSysteminstanzShouldNotBeFound("betriebsstaetteId.equals=" + (betriebsstaetteId + 1));
    }


    @Test
    @Transactional
    public void getAllSysteminstanzsByBetreiberIsEqualToSomething() throws Exception {
        // Get already existing entity
        Betreiber betreiber = systeminstanz.getBetreiber();
        systeminstanzRepository.saveAndFlush(systeminstanz);
        Long betreiberId = betreiber.getId();

        // Get all the systeminstanzList where betreiber equals to betreiberId
        defaultSysteminstanzShouldBeFound("betreiberId.equals=" + betreiberId);

        // Get all the systeminstanzList where betreiber equals to betreiberId + 1
        defaultSysteminstanzShouldNotBeFound("betreiberId.equals=" + (betreiberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSysteminstanzShouldBeFound(String filter) throws Exception {
        restSysteminstanzMockMvc.perform(get("/api/systeminstanzs?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restSysteminstanzMockMvc.perform(get("/api/systeminstanzs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSysteminstanzShouldNotBeFound(String filter) throws Exception {
        restSysteminstanzMockMvc.perform(get("/api/systeminstanzs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSysteminstanzMockMvc.perform(get("/api/systeminstanzs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
