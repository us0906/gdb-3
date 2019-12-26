package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Zubehoer;
import de.kvb.eps.domain.Systemtyp;
import de.kvb.eps.domain.Hersteller;
import de.kvb.eps.domain.ZubehoerTyp;
import de.kvb.eps.repository.ZubehoerRepository;
import de.kvb.eps.repository.search.ZubehoerSearchRepository;
import de.kvb.eps.service.ZubehoerService;
import de.kvb.eps.service.dto.ZubehoerDTO;
import de.kvb.eps.service.mapper.ZubehoerMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.ZubehoerCriteria;
import de.kvb.eps.service.ZubehoerQueryService;

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
 * Integration tests for the {@link ZubehoerResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class ZubehoerResourceIT {

    private static final String DEFAULT_BEZEICHNUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEZEICHNUNG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GUELTIG_BIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUELTIG_BIS = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_GUELTIG_BIS = LocalDate.ofEpochDay(-1L);

    @Autowired
    private ZubehoerRepository zubehoerRepository;

    @Autowired
    private ZubehoerMapper zubehoerMapper;

    @Autowired
    private ZubehoerService zubehoerService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.ZubehoerSearchRepositoryMockConfiguration
     */
    @Autowired
    private ZubehoerSearchRepository mockZubehoerSearchRepository;

    @Autowired
    private ZubehoerQueryService zubehoerQueryService;

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

    private MockMvc restZubehoerMockMvc;

    private Zubehoer zubehoer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ZubehoerResource zubehoerResource = new ZubehoerResource(zubehoerService, zubehoerQueryService);
        this.restZubehoerMockMvc = MockMvcBuilders.standaloneSetup(zubehoerResource)
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
    public static Zubehoer createEntity(EntityManager em) {
        Zubehoer zubehoer = new Zubehoer()
            .bezeichnung(DEFAULT_BEZEICHNUNG)
            .gueltigBis(DEFAULT_GUELTIG_BIS);
        // Add required entity
        Hersteller hersteller;
        if (TestUtil.findAll(em, Hersteller.class).isEmpty()) {
            hersteller = HerstellerResourceIT.createEntity(em);
            em.persist(hersteller);
            em.flush();
        } else {
            hersteller = TestUtil.findAll(em, Hersteller.class).get(0);
        }
        zubehoer.setHersteller(hersteller);
        // Add required entity
        ZubehoerTyp zubehoerTyp;
        if (TestUtil.findAll(em, ZubehoerTyp.class).isEmpty()) {
            zubehoerTyp = ZubehoerTypResourceIT.createEntity(em);
            em.persist(zubehoerTyp);
            em.flush();
        } else {
            zubehoerTyp = TestUtil.findAll(em, ZubehoerTyp.class).get(0);
        }
        zubehoer.setZubehoerTyp(zubehoerTyp);
        return zubehoer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zubehoer createUpdatedEntity(EntityManager em) {
        Zubehoer zubehoer = new Zubehoer()
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS);
        // Add required entity
        Hersteller hersteller;
        if (TestUtil.findAll(em, Hersteller.class).isEmpty()) {
            hersteller = HerstellerResourceIT.createUpdatedEntity(em);
            em.persist(hersteller);
            em.flush();
        } else {
            hersteller = TestUtil.findAll(em, Hersteller.class).get(0);
        }
        zubehoer.setHersteller(hersteller);
        // Add required entity
        ZubehoerTyp zubehoerTyp;
        if (TestUtil.findAll(em, ZubehoerTyp.class).isEmpty()) {
            zubehoerTyp = ZubehoerTypResourceIT.createUpdatedEntity(em);
            em.persist(zubehoerTyp);
            em.flush();
        } else {
            zubehoerTyp = TestUtil.findAll(em, ZubehoerTyp.class).get(0);
        }
        zubehoer.setZubehoerTyp(zubehoerTyp);
        return zubehoer;
    }

    @BeforeEach
    public void initTest() {
        zubehoer = createEntity(em);
    }

    @Test
    @Transactional
    public void createZubehoer() throws Exception {
        int databaseSizeBeforeCreate = zubehoerRepository.findAll().size();

        // Create the Zubehoer
        ZubehoerDTO zubehoerDTO = zubehoerMapper.toDto(zubehoer);
        restZubehoerMockMvc.perform(post("/api/zubehoers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerDTO)))
            .andExpect(status().isCreated());

        // Validate the Zubehoer in the database
        List<Zubehoer> zubehoerList = zubehoerRepository.findAll();
        assertThat(zubehoerList).hasSize(databaseSizeBeforeCreate + 1);
        Zubehoer testZubehoer = zubehoerList.get(zubehoerList.size() - 1);
        assertThat(testZubehoer.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);
        assertThat(testZubehoer.getGueltigBis()).isEqualTo(DEFAULT_GUELTIG_BIS);

        // Validate the Zubehoer in Elasticsearch
        verify(mockZubehoerSearchRepository, times(1)).save(testZubehoer);
    }

    @Test
    @Transactional
    public void createZubehoerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = zubehoerRepository.findAll().size();

        // Create the Zubehoer with an existing ID
        zubehoer.setId(1L);
        ZubehoerDTO zubehoerDTO = zubehoerMapper.toDto(zubehoer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restZubehoerMockMvc.perform(post("/api/zubehoers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Zubehoer in the database
        List<Zubehoer> zubehoerList = zubehoerRepository.findAll();
        assertThat(zubehoerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Zubehoer in Elasticsearch
        verify(mockZubehoerSearchRepository, times(0)).save(zubehoer);
    }


    @Test
    @Transactional
    public void checkBezeichnungIsRequired() throws Exception {
        int databaseSizeBeforeTest = zubehoerRepository.findAll().size();
        // set the field null
        zubehoer.setBezeichnung(null);

        // Create the Zubehoer, which fails.
        ZubehoerDTO zubehoerDTO = zubehoerMapper.toDto(zubehoer);

        restZubehoerMockMvc.perform(post("/api/zubehoers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerDTO)))
            .andExpect(status().isBadRequest());

        List<Zubehoer> zubehoerList = zubehoerRepository.findAll();
        assertThat(zubehoerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllZubehoers() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList
        restZubehoerMockMvc.perform(get("/api/zubehoers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zubehoer.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));
    }
    
    @Test
    @Transactional
    public void getZubehoer() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get the zubehoer
        restZubehoerMockMvc.perform(get("/api/zubehoers/{id}", zubehoer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(zubehoer.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()));
    }


    @Test
    @Transactional
    public void getZubehoersByIdFiltering() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        Long id = zubehoer.getId();

        defaultZubehoerShouldBeFound("id.equals=" + id);
        defaultZubehoerShouldNotBeFound("id.notEquals=" + id);

        defaultZubehoerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultZubehoerShouldNotBeFound("id.greaterThan=" + id);

        defaultZubehoerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultZubehoerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllZubehoersByBezeichnungIsEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where bezeichnung equals to DEFAULT_BEZEICHNUNG
        defaultZubehoerShouldBeFound("bezeichnung.equals=" + DEFAULT_BEZEICHNUNG);

        // Get all the zubehoerList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultZubehoerShouldNotBeFound("bezeichnung.equals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllZubehoersByBezeichnungIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where bezeichnung not equals to DEFAULT_BEZEICHNUNG
        defaultZubehoerShouldNotBeFound("bezeichnung.notEquals=" + DEFAULT_BEZEICHNUNG);

        // Get all the zubehoerList where bezeichnung not equals to UPDATED_BEZEICHNUNG
        defaultZubehoerShouldBeFound("bezeichnung.notEquals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllZubehoersByBezeichnungIsInShouldWork() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where bezeichnung in DEFAULT_BEZEICHNUNG or UPDATED_BEZEICHNUNG
        defaultZubehoerShouldBeFound("bezeichnung.in=" + DEFAULT_BEZEICHNUNG + "," + UPDATED_BEZEICHNUNG);

        // Get all the zubehoerList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultZubehoerShouldNotBeFound("bezeichnung.in=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllZubehoersByBezeichnungIsNullOrNotNull() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where bezeichnung is not null
        defaultZubehoerShouldBeFound("bezeichnung.specified=true");

        // Get all the zubehoerList where bezeichnung is null
        defaultZubehoerShouldNotBeFound("bezeichnung.specified=false");
    }
                @Test
    @Transactional
    public void getAllZubehoersByBezeichnungContainsSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where bezeichnung contains DEFAULT_BEZEICHNUNG
        defaultZubehoerShouldBeFound("bezeichnung.contains=" + DEFAULT_BEZEICHNUNG);

        // Get all the zubehoerList where bezeichnung contains UPDATED_BEZEICHNUNG
        defaultZubehoerShouldNotBeFound("bezeichnung.contains=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllZubehoersByBezeichnungNotContainsSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where bezeichnung does not contain DEFAULT_BEZEICHNUNG
        defaultZubehoerShouldNotBeFound("bezeichnung.doesNotContain=" + DEFAULT_BEZEICHNUNG);

        // Get all the zubehoerList where bezeichnung does not contain UPDATED_BEZEICHNUNG
        defaultZubehoerShouldBeFound("bezeichnung.doesNotContain=" + UPDATED_BEZEICHNUNG);
    }


    @Test
    @Transactional
    public void getAllZubehoersByGueltigBisIsEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where gueltigBis equals to DEFAULT_GUELTIG_BIS
        defaultZubehoerShouldBeFound("gueltigBis.equals=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultZubehoerShouldNotBeFound("gueltigBis.equals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoersByGueltigBisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where gueltigBis not equals to DEFAULT_GUELTIG_BIS
        defaultZubehoerShouldNotBeFound("gueltigBis.notEquals=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerList where gueltigBis not equals to UPDATED_GUELTIG_BIS
        defaultZubehoerShouldBeFound("gueltigBis.notEquals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoersByGueltigBisIsInShouldWork() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where gueltigBis in DEFAULT_GUELTIG_BIS or UPDATED_GUELTIG_BIS
        defaultZubehoerShouldBeFound("gueltigBis.in=" + DEFAULT_GUELTIG_BIS + "," + UPDATED_GUELTIG_BIS);

        // Get all the zubehoerList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultZubehoerShouldNotBeFound("gueltigBis.in=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoersByGueltigBisIsNullOrNotNull() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where gueltigBis is not null
        defaultZubehoerShouldBeFound("gueltigBis.specified=true");

        // Get all the zubehoerList where gueltigBis is null
        defaultZubehoerShouldNotBeFound("gueltigBis.specified=false");
    }

    @Test
    @Transactional
    public void getAllZubehoersByGueltigBisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where gueltigBis is greater than or equal to DEFAULT_GUELTIG_BIS
        defaultZubehoerShouldBeFound("gueltigBis.greaterThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerList where gueltigBis is greater than or equal to UPDATED_GUELTIG_BIS
        defaultZubehoerShouldNotBeFound("gueltigBis.greaterThanOrEqual=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoersByGueltigBisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where gueltigBis is less than or equal to DEFAULT_GUELTIG_BIS
        defaultZubehoerShouldBeFound("gueltigBis.lessThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerList where gueltigBis is less than or equal to SMALLER_GUELTIG_BIS
        defaultZubehoerShouldNotBeFound("gueltigBis.lessThanOrEqual=" + SMALLER_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoersByGueltigBisIsLessThanSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where gueltigBis is less than DEFAULT_GUELTIG_BIS
        defaultZubehoerShouldNotBeFound("gueltigBis.lessThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerList where gueltigBis is less than UPDATED_GUELTIG_BIS
        defaultZubehoerShouldBeFound("gueltigBis.lessThan=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoersByGueltigBisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        // Get all the zubehoerList where gueltigBis is greater than DEFAULT_GUELTIG_BIS
        defaultZubehoerShouldNotBeFound("gueltigBis.greaterThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerList where gueltigBis is greater than SMALLER_GUELTIG_BIS
        defaultZubehoerShouldBeFound("gueltigBis.greaterThan=" + SMALLER_GUELTIG_BIS);
    }


    @Test
    @Transactional
    public void getAllZubehoersBySystemtypIsEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);
        Systemtyp systemtyp = SystemtypResourceIT.createEntity(em);
        em.persist(systemtyp);
        em.flush();
        zubehoer.addSystemtyp(systemtyp);
        zubehoerRepository.saveAndFlush(zubehoer);
        Long systemtypId = systemtyp.getId();

        // Get all the zubehoerList where systemtyp equals to systemtypId
        defaultZubehoerShouldBeFound("systemtypId.equals=" + systemtypId);

        // Get all the zubehoerList where systemtyp equals to systemtypId + 1
        defaultZubehoerShouldNotBeFound("systemtypId.equals=" + (systemtypId + 1));
    }


    @Test
    @Transactional
    public void getAllZubehoersByHerstellerIsEqualToSomething() throws Exception {
        // Get already existing entity
        Hersteller hersteller = zubehoer.getHersteller();
        zubehoerRepository.saveAndFlush(zubehoer);
        Long herstellerId = hersteller.getId();

        // Get all the zubehoerList where hersteller equals to herstellerId
        defaultZubehoerShouldBeFound("herstellerId.equals=" + herstellerId);

        // Get all the zubehoerList where hersteller equals to herstellerId + 1
        defaultZubehoerShouldNotBeFound("herstellerId.equals=" + (herstellerId + 1));
    }


    @Test
    @Transactional
    public void getAllZubehoersByZubehoerTypIsEqualToSomething() throws Exception {
        // Get already existing entity
        ZubehoerTyp zubehoerTyp = zubehoer.getZubehoerTyp();
        zubehoerRepository.saveAndFlush(zubehoer);
        Long zubehoerTypId = zubehoerTyp.getId();

        // Get all the zubehoerList where zubehoerTyp equals to zubehoerTypId
        defaultZubehoerShouldBeFound("zubehoerTypId.equals=" + zubehoerTypId);

        // Get all the zubehoerList where zubehoerTyp equals to zubehoerTypId + 1
        defaultZubehoerShouldNotBeFound("zubehoerTypId.equals=" + (zubehoerTypId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultZubehoerShouldBeFound(String filter) throws Exception {
        restZubehoerMockMvc.perform(get("/api/zubehoers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zubehoer.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));

        // Check, that the count call also returns 1
        restZubehoerMockMvc.perform(get("/api/zubehoers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultZubehoerShouldNotBeFound(String filter) throws Exception {
        restZubehoerMockMvc.perform(get("/api/zubehoers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restZubehoerMockMvc.perform(get("/api/zubehoers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingZubehoer() throws Exception {
        // Get the zubehoer
        restZubehoerMockMvc.perform(get("/api/zubehoers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateZubehoer() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        int databaseSizeBeforeUpdate = zubehoerRepository.findAll().size();

        // Update the zubehoer
        Zubehoer updatedZubehoer = zubehoerRepository.findById(zubehoer.getId()).get();
        // Disconnect from session so that the updates on updatedZubehoer are not directly saved in db
        em.detach(updatedZubehoer);
        updatedZubehoer
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS);
        ZubehoerDTO zubehoerDTO = zubehoerMapper.toDto(updatedZubehoer);

        restZubehoerMockMvc.perform(put("/api/zubehoers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerDTO)))
            .andExpect(status().isOk());

        // Validate the Zubehoer in the database
        List<Zubehoer> zubehoerList = zubehoerRepository.findAll();
        assertThat(zubehoerList).hasSize(databaseSizeBeforeUpdate);
        Zubehoer testZubehoer = zubehoerList.get(zubehoerList.size() - 1);
        assertThat(testZubehoer.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);
        assertThat(testZubehoer.getGueltigBis()).isEqualTo(UPDATED_GUELTIG_BIS);

        // Validate the Zubehoer in Elasticsearch
        verify(mockZubehoerSearchRepository, times(1)).save(testZubehoer);
    }

    @Test
    @Transactional
    public void updateNonExistingZubehoer() throws Exception {
        int databaseSizeBeforeUpdate = zubehoerRepository.findAll().size();

        // Create the Zubehoer
        ZubehoerDTO zubehoerDTO = zubehoerMapper.toDto(zubehoer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZubehoerMockMvc.perform(put("/api/zubehoers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Zubehoer in the database
        List<Zubehoer> zubehoerList = zubehoerRepository.findAll();
        assertThat(zubehoerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Zubehoer in Elasticsearch
        verify(mockZubehoerSearchRepository, times(0)).save(zubehoer);
    }

    @Test
    @Transactional
    public void deleteZubehoer() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);

        int databaseSizeBeforeDelete = zubehoerRepository.findAll().size();

        // Delete the zubehoer
        restZubehoerMockMvc.perform(delete("/api/zubehoers/{id}", zubehoer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Zubehoer> zubehoerList = zubehoerRepository.findAll();
        assertThat(zubehoerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Zubehoer in Elasticsearch
        verify(mockZubehoerSearchRepository, times(1)).deleteById(zubehoer.getId());
    }

    @Test
    @Transactional
    public void searchZubehoer() throws Exception {
        // Initialize the database
        zubehoerRepository.saveAndFlush(zubehoer);
        when(mockZubehoerSearchRepository.search(queryStringQuery("id:" + zubehoer.getId())))
            .thenReturn(Collections.singletonList(zubehoer));
        // Search the zubehoer
        restZubehoerMockMvc.perform(get("/api/_search/zubehoers?query=id:" + zubehoer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zubehoer.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));
    }
}
