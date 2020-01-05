package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Arzt;
import de.kvb.eps.domain.Systemnutzung;
import de.kvb.eps.repository.ArztRepository;
import de.kvb.eps.repository.search.ArztSearchRepository;
import de.kvb.eps.service.ArztService;
import de.kvb.eps.service.dto.ArztDTO;
import de.kvb.eps.service.mapper.ArztMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.ArztCriteria;
import de.kvb.eps.service.ArztQueryService;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
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

    private static final String DEFAULT_LANR = "1234567";
    private static final String UPDATED_LANR = "7654321";

    private static final String DEFAULT_TITEL = "Dr. med.";
    private static final String UPDATED_TITEL = "Prof.";

    private static final String DEFAULT_VORNAME = "Martin";
    private static final String UPDATED_VORNAME = "Michael";

    private static final String DEFAULT_NACHNAME = "Schmitz";
    private static final String UPDATED_NACHNAME = "Meier";

    private static final String DEFAULT_BEZEICHNUNG = DEFAULT_LANR + " " + DEFAULT_TITEL + " " + DEFAULT_VORNAME + " " + DEFAULT_NACHNAME;
    private static final String UPDATED_BEZEICHNUNG = UPDATED_LANR + " " + UPDATED_TITEL + " " + UPDATED_VORNAME + " " + UPDATED_NACHNAME;

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
    private ArztQueryService arztQueryService;

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
        final ArztResource arztResource = new ArztResource(arztService, arztQueryService);
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
        assertThat(testArzt.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);

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
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)));
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
            .andExpect(jsonPath("$.nachname").value(DEFAULT_NACHNAME))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG));
    }


    @Test
    @Transactional
    public void getArztsByIdFiltering() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        Long id = arzt.getId();

        defaultArztShouldBeFound("id.equals=" + id);
        defaultArztShouldNotBeFound("id.notEquals=" + id);

        defaultArztShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultArztShouldNotBeFound("id.greaterThan=" + id);

        defaultArztShouldBeFound("id.lessThanOrEqual=" + id);
        defaultArztShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllArztsByLanrIsEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where lanr equals to DEFAULT_LANR
        defaultArztShouldBeFound("lanr.equals=" + DEFAULT_LANR);

        // Get all the arztList where lanr equals to UPDATED_LANR
        defaultArztShouldNotBeFound("lanr.equals=" + UPDATED_LANR);
    }

    @Test
    @Transactional
    public void getAllArztsByLanrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where lanr not equals to DEFAULT_LANR
        defaultArztShouldNotBeFound("lanr.notEquals=" + DEFAULT_LANR);

        // Get all the arztList where lanr not equals to UPDATED_LANR
        defaultArztShouldBeFound("lanr.notEquals=" + UPDATED_LANR);
    }

    @Test
    @Transactional
    public void getAllArztsByLanrIsInShouldWork() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where lanr in DEFAULT_LANR or UPDATED_LANR
        defaultArztShouldBeFound("lanr.in=" + DEFAULT_LANR + "," + UPDATED_LANR);

        // Get all the arztList where lanr equals to UPDATED_LANR
        defaultArztShouldNotBeFound("lanr.in=" + UPDATED_LANR);
    }

    @Test
    @Transactional
    public void getAllArztsByLanrIsNullOrNotNull() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where lanr is not null
        defaultArztShouldBeFound("lanr.specified=true");

        // Get all the arztList where lanr is null
        defaultArztShouldNotBeFound("lanr.specified=false");
    }
                @Test
    @Transactional
    public void getAllArztsByLanrContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where lanr contains DEFAULT_LANR
        defaultArztShouldBeFound("lanr.contains=" + DEFAULT_LANR);

        // Get all the arztList where lanr contains UPDATED_LANR
        defaultArztShouldNotBeFound("lanr.contains=" + UPDATED_LANR);
    }

    @Test
    @Transactional
    public void getAllArztsByLanrNotContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where lanr does not contain DEFAULT_LANR
        defaultArztShouldNotBeFound("lanr.doesNotContain=" + DEFAULT_LANR);

        // Get all the arztList where lanr does not contain UPDATED_LANR
        defaultArztShouldBeFound("lanr.doesNotContain=" + UPDATED_LANR);
    }


    @Test
    @Transactional
    public void getAllArztsByTitelIsEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where titel equals to DEFAULT_TITEL
        defaultArztShouldBeFound("titel.equals=" + DEFAULT_TITEL);

        // Get all the arztList where titel equals to UPDATED_TITEL
        defaultArztShouldNotBeFound("titel.equals=" + UPDATED_TITEL);
    }

    @Test
    @Transactional
    public void getAllArztsByTitelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where titel not equals to DEFAULT_TITEL
        defaultArztShouldNotBeFound("titel.notEquals=" + DEFAULT_TITEL);

        // Get all the arztList where titel not equals to UPDATED_TITEL
        defaultArztShouldBeFound("titel.notEquals=" + UPDATED_TITEL);
    }

    @Test
    @Transactional
    public void getAllArztsByTitelIsInShouldWork() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where titel in DEFAULT_TITEL or UPDATED_TITEL
        defaultArztShouldBeFound("titel.in=" + DEFAULT_TITEL + "," + UPDATED_TITEL);

        // Get all the arztList where titel equals to UPDATED_TITEL
        defaultArztShouldNotBeFound("titel.in=" + UPDATED_TITEL);
    }

    @Test
    @Transactional
    public void getAllArztsByTitelIsNullOrNotNull() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where titel is not null
        defaultArztShouldBeFound("titel.specified=true");

        // Get all the arztList where titel is null
        defaultArztShouldNotBeFound("titel.specified=false");
    }
                @Test
    @Transactional
    public void getAllArztsByTitelContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where titel contains DEFAULT_TITEL
        defaultArztShouldBeFound("titel.contains=" + DEFAULT_TITEL);

        // Get all the arztList where titel contains UPDATED_TITEL
        defaultArztShouldNotBeFound("titel.contains=" + UPDATED_TITEL);
    }

    @Test
    @Transactional
    public void getAllArztsByTitelNotContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where titel does not contain DEFAULT_TITEL
        defaultArztShouldNotBeFound("titel.doesNotContain=" + DEFAULT_TITEL);

        // Get all the arztList where titel does not contain UPDATED_TITEL
        defaultArztShouldBeFound("titel.doesNotContain=" + UPDATED_TITEL);
    }


    @Test
    @Transactional
    public void getAllArztsByVornameIsEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where vorname equals to DEFAULT_VORNAME
        defaultArztShouldBeFound("vorname.equals=" + DEFAULT_VORNAME);

        // Get all the arztList where vorname equals to UPDATED_VORNAME
        defaultArztShouldNotBeFound("vorname.equals=" + UPDATED_VORNAME);
    }

    @Test
    @Transactional
    public void getAllArztsByVornameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where vorname not equals to DEFAULT_VORNAME
        defaultArztShouldNotBeFound("vorname.notEquals=" + DEFAULT_VORNAME);

        // Get all the arztList where vorname not equals to UPDATED_VORNAME
        defaultArztShouldBeFound("vorname.notEquals=" + UPDATED_VORNAME);
    }

    @Test
    @Transactional
    public void getAllArztsByVornameIsInShouldWork() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where vorname in DEFAULT_VORNAME or UPDATED_VORNAME
        defaultArztShouldBeFound("vorname.in=" + DEFAULT_VORNAME + "," + UPDATED_VORNAME);

        // Get all the arztList where vorname equals to UPDATED_VORNAME
        defaultArztShouldNotBeFound("vorname.in=" + UPDATED_VORNAME);
    }

    @Test
    @Transactional
    public void getAllArztsByVornameIsNullOrNotNull() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where vorname is not null
        defaultArztShouldBeFound("vorname.specified=true");

        // Get all the arztList where vorname is null
        defaultArztShouldNotBeFound("vorname.specified=false");
    }
                @Test
    @Transactional
    public void getAllArztsByVornameContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where vorname contains DEFAULT_VORNAME
        defaultArztShouldBeFound("vorname.contains=" + DEFAULT_VORNAME);

        // Get all the arztList where vorname contains UPDATED_VORNAME
        defaultArztShouldNotBeFound("vorname.contains=" + UPDATED_VORNAME);
    }

    @Test
    @Transactional
    public void getAllArztsByVornameNotContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where vorname does not contain DEFAULT_VORNAME
        defaultArztShouldNotBeFound("vorname.doesNotContain=" + DEFAULT_VORNAME);

        // Get all the arztList where vorname does not contain UPDATED_VORNAME
        defaultArztShouldBeFound("vorname.doesNotContain=" + UPDATED_VORNAME);
    }


    @Test
    @Transactional
    public void getAllArztsByNachnameIsEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where nachname equals to DEFAULT_NACHNAME
        defaultArztShouldBeFound("nachname.equals=" + DEFAULT_NACHNAME);

        // Get all the arztList where nachname equals to UPDATED_NACHNAME
        defaultArztShouldNotBeFound("nachname.equals=" + UPDATED_NACHNAME);
    }

    @Test
    @Transactional
    public void getAllArztsByNachnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where nachname not equals to DEFAULT_NACHNAME
        defaultArztShouldNotBeFound("nachname.notEquals=" + DEFAULT_NACHNAME);

        // Get all the arztList where nachname not equals to UPDATED_NACHNAME
        defaultArztShouldBeFound("nachname.notEquals=" + UPDATED_NACHNAME);
    }

    @Test
    @Transactional
    public void getAllArztsByNachnameIsInShouldWork() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where nachname in DEFAULT_NACHNAME or UPDATED_NACHNAME
        defaultArztShouldBeFound("nachname.in=" + DEFAULT_NACHNAME + "," + UPDATED_NACHNAME);

        // Get all the arztList where nachname equals to UPDATED_NACHNAME
        defaultArztShouldNotBeFound("nachname.in=" + UPDATED_NACHNAME);
    }

    @Test
    @Transactional
    public void getAllArztsByNachnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where nachname is not null
        defaultArztShouldBeFound("nachname.specified=true");

        // Get all the arztList where nachname is null
        defaultArztShouldNotBeFound("nachname.specified=false");
    }
                @Test
    @Transactional
    public void getAllArztsByNachnameContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where nachname contains DEFAULT_NACHNAME
        defaultArztShouldBeFound("nachname.contains=" + DEFAULT_NACHNAME);

        // Get all the arztList where nachname contains UPDATED_NACHNAME
        defaultArztShouldNotBeFound("nachname.contains=" + UPDATED_NACHNAME);
    }

    @Test
    @Transactional
    public void getAllArztsByNachnameNotContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where nachname does not contain DEFAULT_NACHNAME
        defaultArztShouldNotBeFound("nachname.doesNotContain=" + DEFAULT_NACHNAME);

        // Get all the arztList where nachname does not contain UPDATED_NACHNAME
        defaultArztShouldBeFound("nachname.doesNotContain=" + UPDATED_NACHNAME);
    }

    @Transactional
    public void getAllArztsByBezeichnungIsEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where bezeichnung equals to DEFAULT_BEZEICHNUNG
        defaultArztShouldBeFound("bezeichnung.equals=" + DEFAULT_BEZEICHNUNG);

        // Get all the arztList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultArztShouldNotBeFound("bezeichnung.equals=" + UPDATED_BEZEICHNUNG);
    }


    @Transactional
    public void getAllArztsByBezeichnungIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where bezeichnung not equals to DEFAULT_BEZEICHNUNG
        defaultArztShouldNotBeFound("bezeichnung.notEquals=" + DEFAULT_BEZEICHNUNG);

        // Get all the arztList where bezeichnung not equals to UPDATED_BEZEICHNUNG
        defaultArztShouldBeFound("bezeichnung.notEquals=" + UPDATED_BEZEICHNUNG);
    }


    @Transactional
    public void getAllArztsByBezeichnungIsInShouldWork() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where bezeichnung in DEFAULT_BEZEICHNUNG or UPDATED_BEZEICHNUNG
        defaultArztShouldBeFound("bezeichnung.in=" + DEFAULT_BEZEICHNUNG + "," + UPDATED_BEZEICHNUNG);

        // Get all the arztList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultArztShouldNotBeFound("bezeichnung.in=" + UPDATED_BEZEICHNUNG);
    }


    @Transactional
    public void getAllArztsByBezeichnungIsNullOrNotNull() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where bezeichnung is not null
        defaultArztShouldBeFound("bezeichnung.specified=true");

        // Get all the arztList where bezeichnung is null
        defaultArztShouldNotBeFound("bezeichnung.specified=false");
    }


    @Transactional
    public void getAllArztsByBezeichnungContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where bezeichnung contains DEFAULT_BEZEICHNUNG
        defaultArztShouldBeFound("bezeichnung.contains=" + DEFAULT_BEZEICHNUNG);

        // Get all the arztList where bezeichnung contains UPDATED_BEZEICHNUNG
        defaultArztShouldNotBeFound("bezeichnung.contains=" + UPDATED_BEZEICHNUNG);
    }


    @Transactional
    public void getAllArztsByBezeichnungNotContainsSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);

        // Get all the arztList where bezeichnung does not contain DEFAULT_BEZEICHNUNG
        defaultArztShouldNotBeFound("bezeichnung.doesNotContain=" + DEFAULT_BEZEICHNUNG);

        // Get all the arztList where bezeichnung does not contain UPDATED_BEZEICHNUNG
        defaultArztShouldBeFound("bezeichnung.doesNotContain=" + UPDATED_BEZEICHNUNG);
    }


    @Test
    @Transactional
    public void getAllArztsBySystemnutzungIsEqualToSomething() throws Exception {
        // Initialize the database
        arztRepository.saveAndFlush(arzt);
        Systemnutzung systemnutzung = SystemnutzungResourceIT.createEntity(em);
        em.persist(systemnutzung);
        em.flush();
        arzt.addSystemnutzung(systemnutzung);
        arztRepository.saveAndFlush(arzt);
        Long systemnutzungId = systemnutzung.getId();

        // Get all the arztList where systemnutzung equals to systemnutzungId
        defaultArztShouldBeFound("systemnutzungId.equals=" + systemnutzungId);

        // Get all the arztList where systemnutzung equals to systemnutzungId + 1
        defaultArztShouldNotBeFound("systemnutzungId.equals=" + (systemnutzungId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArztShouldBeFound(String filter) throws Exception {
        restArztMockMvc.perform(get("/api/arzts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").exists())
            .andExpect(jsonPath("$.[*].id").value(hasItem(arzt.getId().intValue())))
            .andExpect(jsonPath("$.[*].lanr").value(hasItem(DEFAULT_LANR)))
            .andExpect(jsonPath("$.[*].titel").value(hasItem(DEFAULT_TITEL)))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)));

        // Check, that the count call also returns 1
        restArztMockMvc.perform(get("/api/arzts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArztShouldNotBeFound(String filter) throws Exception {
        restArztMockMvc.perform(get("/api/arzts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArztMockMvc.perform(get("/api/arzts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
            .nachname(UPDATED_NACHNAME)
            .bezeichnung(UPDATED_BEZEICHNUNG);
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
        assertThat(testArzt.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);

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
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)));
    }
}
