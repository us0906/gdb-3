package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.ZubehoerTyp;
import de.kvb.eps.domain.Zubehoer;
import de.kvb.eps.repository.ZubehoerTypRepository;
import de.kvb.eps.repository.search.ZubehoerTypSearchRepository;
import de.kvb.eps.service.ZubehoerTypService;
import de.kvb.eps.service.dto.ZubehoerTypDTO;
import de.kvb.eps.service.mapper.ZubehoerTypMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.ZubehoerTypCriteria;
import de.kvb.eps.service.ZubehoerTypQueryService;

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

import de.kvb.eps.domain.enumeration.Technologie;
/**
 * Integration tests for the {@link ZubehoerTypResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class ZubehoerTypResourceIT {

    private static final String DEFAULT_BEZEICHNUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEZEICHNUNG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GUELTIG_BIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUELTIG_BIS = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_GUELTIG_BIS = LocalDate.ofEpochDay(-1L);

    private static final Technologie DEFAULT_TECHNOLOGIE = Technologie.SONO;
    private static final Technologie UPDATED_TECHNOLOGIE = Technologie.BILD;

    @Autowired
    private ZubehoerTypRepository zubehoerTypRepository;

    @Autowired
    private ZubehoerTypMapper zubehoerTypMapper;

    @Autowired
    private ZubehoerTypService zubehoerTypService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.ZubehoerTypSearchRepositoryMockConfiguration
     */
    @Autowired
    private ZubehoerTypSearchRepository mockZubehoerTypSearchRepository;

    @Autowired
    private ZubehoerTypQueryService zubehoerTypQueryService;

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

    private MockMvc restZubehoerTypMockMvc;

    private ZubehoerTyp zubehoerTyp;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ZubehoerTypResource zubehoerTypResource = new ZubehoerTypResource(zubehoerTypService, zubehoerTypQueryService);
        this.restZubehoerTypMockMvc = MockMvcBuilders.standaloneSetup(zubehoerTypResource)
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
    public static ZubehoerTyp createEntity(EntityManager em) {
        ZubehoerTyp zubehoerTyp = new ZubehoerTyp()
            .bezeichnung(DEFAULT_BEZEICHNUNG)
            .gueltigBis(DEFAULT_GUELTIG_BIS)
            .technologie(DEFAULT_TECHNOLOGIE);
        return zubehoerTyp;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZubehoerTyp createUpdatedEntity(EntityManager em) {
        ZubehoerTyp zubehoerTyp = new ZubehoerTyp()
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS)
            .technologie(UPDATED_TECHNOLOGIE);
        return zubehoerTyp;
    }

    @BeforeEach
    public void initTest() {
        zubehoerTyp = createEntity(em);
    }

    @Test
    @Transactional
    public void createZubehoerTyp() throws Exception {
        int databaseSizeBeforeCreate = zubehoerTypRepository.findAll().size();

        // Create the ZubehoerTyp
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);
        restZubehoerTypMockMvc.perform(post("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isCreated());

        // Validate the ZubehoerTyp in the database
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeCreate + 1);
        ZubehoerTyp testZubehoerTyp = zubehoerTypList.get(zubehoerTypList.size() - 1);
        assertThat(testZubehoerTyp.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);
        assertThat(testZubehoerTyp.getGueltigBis()).isEqualTo(DEFAULT_GUELTIG_BIS);
        assertThat(testZubehoerTyp.getTechnologie()).isEqualTo(DEFAULT_TECHNOLOGIE);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(1)).save(testZubehoerTyp);
    }

    @Test
    @Transactional
    public void createZubehoerTypWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = zubehoerTypRepository.findAll().size();

        // Create the ZubehoerTyp with an existing ID
        zubehoerTyp.setId(1L);
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restZubehoerTypMockMvc.perform(post("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ZubehoerTyp in the database
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeCreate);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(0)).save(zubehoerTyp);
    }


    @Test
    @Transactional
    public void checkBezeichnungIsRequired() throws Exception {
        int databaseSizeBeforeTest = zubehoerTypRepository.findAll().size();
        // set the field null
        zubehoerTyp.setBezeichnung(null);

        // Create the ZubehoerTyp, which fails.
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);

        restZubehoerTypMockMvc.perform(post("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isBadRequest());

        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTechnologieIsRequired() throws Exception {
        int databaseSizeBeforeTest = zubehoerTypRepository.findAll().size();
        // set the field null
        zubehoerTyp.setTechnologie(null);

        // Create the ZubehoerTyp, which fails.
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);

        restZubehoerTypMockMvc.perform(post("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isBadRequest());

        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllZubehoerTyps() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zubehoerTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE.toString())));
    }
    
    @Test
    @Transactional
    public void getZubehoerTyp() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get the zubehoerTyp
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs/{id}", zubehoerTyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zubehoerTyp.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()))
            .andExpect(jsonPath("$.technologie").value(DEFAULT_TECHNOLOGIE.toString()));
    }


    @Test
    @Transactional
    public void getZubehoerTypsByIdFiltering() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        Long id = zubehoerTyp.getId();

        defaultZubehoerTypShouldBeFound("id.equals=" + id);
        defaultZubehoerTypShouldNotBeFound("id.notEquals=" + id);

        defaultZubehoerTypShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultZubehoerTypShouldNotBeFound("id.greaterThan=" + id);

        defaultZubehoerTypShouldBeFound("id.lessThanOrEqual=" + id);
        defaultZubehoerTypShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllZubehoerTypsByBezeichnungIsEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where bezeichnung equals to DEFAULT_BEZEICHNUNG
        defaultZubehoerTypShouldBeFound("bezeichnung.equals=" + DEFAULT_BEZEICHNUNG);

        // Get all the zubehoerTypList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultZubehoerTypShouldNotBeFound("bezeichnung.equals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByBezeichnungIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where bezeichnung not equals to DEFAULT_BEZEICHNUNG
        defaultZubehoerTypShouldNotBeFound("bezeichnung.notEquals=" + DEFAULT_BEZEICHNUNG);

        // Get all the zubehoerTypList where bezeichnung not equals to UPDATED_BEZEICHNUNG
        defaultZubehoerTypShouldBeFound("bezeichnung.notEquals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByBezeichnungIsInShouldWork() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where bezeichnung in DEFAULT_BEZEICHNUNG or UPDATED_BEZEICHNUNG
        defaultZubehoerTypShouldBeFound("bezeichnung.in=" + DEFAULT_BEZEICHNUNG + "," + UPDATED_BEZEICHNUNG);

        // Get all the zubehoerTypList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultZubehoerTypShouldNotBeFound("bezeichnung.in=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByBezeichnungIsNullOrNotNull() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where bezeichnung is not null
        defaultZubehoerTypShouldBeFound("bezeichnung.specified=true");

        // Get all the zubehoerTypList where bezeichnung is null
        defaultZubehoerTypShouldNotBeFound("bezeichnung.specified=false");
    }
                @Test
    @Transactional
    public void getAllZubehoerTypsByBezeichnungContainsSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where bezeichnung contains DEFAULT_BEZEICHNUNG
        defaultZubehoerTypShouldBeFound("bezeichnung.contains=" + DEFAULT_BEZEICHNUNG);

        // Get all the zubehoerTypList where bezeichnung contains UPDATED_BEZEICHNUNG
        defaultZubehoerTypShouldNotBeFound("bezeichnung.contains=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByBezeichnungNotContainsSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where bezeichnung does not contain DEFAULT_BEZEICHNUNG
        defaultZubehoerTypShouldNotBeFound("bezeichnung.doesNotContain=" + DEFAULT_BEZEICHNUNG);

        // Get all the zubehoerTypList where bezeichnung does not contain UPDATED_BEZEICHNUNG
        defaultZubehoerTypShouldBeFound("bezeichnung.doesNotContain=" + UPDATED_BEZEICHNUNG);
    }


    @Test
    @Transactional
    public void getAllZubehoerTypsByGueltigBisIsEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where gueltigBis equals to DEFAULT_GUELTIG_BIS
        defaultZubehoerTypShouldBeFound("gueltigBis.equals=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerTypList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultZubehoerTypShouldNotBeFound("gueltigBis.equals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByGueltigBisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where gueltigBis not equals to DEFAULT_GUELTIG_BIS
        defaultZubehoerTypShouldNotBeFound("gueltigBis.notEquals=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerTypList where gueltigBis not equals to UPDATED_GUELTIG_BIS
        defaultZubehoerTypShouldBeFound("gueltigBis.notEquals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByGueltigBisIsInShouldWork() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where gueltigBis in DEFAULT_GUELTIG_BIS or UPDATED_GUELTIG_BIS
        defaultZubehoerTypShouldBeFound("gueltigBis.in=" + DEFAULT_GUELTIG_BIS + "," + UPDATED_GUELTIG_BIS);

        // Get all the zubehoerTypList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultZubehoerTypShouldNotBeFound("gueltigBis.in=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByGueltigBisIsNullOrNotNull() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where gueltigBis is not null
        defaultZubehoerTypShouldBeFound("gueltigBis.specified=true");

        // Get all the zubehoerTypList where gueltigBis is null
        defaultZubehoerTypShouldNotBeFound("gueltigBis.specified=false");
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByGueltigBisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where gueltigBis is greater than or equal to DEFAULT_GUELTIG_BIS
        defaultZubehoerTypShouldBeFound("gueltigBis.greaterThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerTypList where gueltigBis is greater than or equal to UPDATED_GUELTIG_BIS
        defaultZubehoerTypShouldNotBeFound("gueltigBis.greaterThanOrEqual=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByGueltigBisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where gueltigBis is less than or equal to DEFAULT_GUELTIG_BIS
        defaultZubehoerTypShouldBeFound("gueltigBis.lessThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerTypList where gueltigBis is less than or equal to SMALLER_GUELTIG_BIS
        defaultZubehoerTypShouldNotBeFound("gueltigBis.lessThanOrEqual=" + SMALLER_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByGueltigBisIsLessThanSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where gueltigBis is less than DEFAULT_GUELTIG_BIS
        defaultZubehoerTypShouldNotBeFound("gueltigBis.lessThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerTypList where gueltigBis is less than UPDATED_GUELTIG_BIS
        defaultZubehoerTypShouldBeFound("gueltigBis.lessThan=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByGueltigBisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where gueltigBis is greater than DEFAULT_GUELTIG_BIS
        defaultZubehoerTypShouldNotBeFound("gueltigBis.greaterThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the zubehoerTypList where gueltigBis is greater than SMALLER_GUELTIG_BIS
        defaultZubehoerTypShouldBeFound("gueltigBis.greaterThan=" + SMALLER_GUELTIG_BIS);
    }


    @Test
    @Transactional
    public void getAllZubehoerTypsByTechnologieIsEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where technologie equals to DEFAULT_TECHNOLOGIE
        defaultZubehoerTypShouldBeFound("technologie.equals=" + DEFAULT_TECHNOLOGIE);

        // Get all the zubehoerTypList where technologie equals to UPDATED_TECHNOLOGIE
        defaultZubehoerTypShouldNotBeFound("technologie.equals=" + UPDATED_TECHNOLOGIE);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByTechnologieIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where technologie not equals to DEFAULT_TECHNOLOGIE
        defaultZubehoerTypShouldNotBeFound("technologie.notEquals=" + DEFAULT_TECHNOLOGIE);

        // Get all the zubehoerTypList where technologie not equals to UPDATED_TECHNOLOGIE
        defaultZubehoerTypShouldBeFound("technologie.notEquals=" + UPDATED_TECHNOLOGIE);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByTechnologieIsInShouldWork() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where technologie in DEFAULT_TECHNOLOGIE or UPDATED_TECHNOLOGIE
        defaultZubehoerTypShouldBeFound("technologie.in=" + DEFAULT_TECHNOLOGIE + "," + UPDATED_TECHNOLOGIE);

        // Get all the zubehoerTypList where technologie equals to UPDATED_TECHNOLOGIE
        defaultZubehoerTypShouldNotBeFound("technologie.in=" + UPDATED_TECHNOLOGIE);
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByTechnologieIsNullOrNotNull() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList where technologie is not null
        defaultZubehoerTypShouldBeFound("technologie.specified=true");

        // Get all the zubehoerTypList where technologie is null
        defaultZubehoerTypShouldNotBeFound("technologie.specified=false");
    }

    @Test
    @Transactional
    public void getAllZubehoerTypsByZubehoerIsEqualToSomething() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);
        Zubehoer zubehoer = ZubehoerResourceIT.createEntity(em);
        em.persist(zubehoer);
        em.flush();
        zubehoerTyp.addZubehoer(zubehoer);
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);
        Long zubehoerId = zubehoer.getId();

        // Get all the zubehoerTypList where zubehoer equals to zubehoerId
        defaultZubehoerTypShouldBeFound("zubehoerId.equals=" + zubehoerId);

        // Get all the zubehoerTypList where zubehoer equals to zubehoerId + 1
        defaultZubehoerTypShouldNotBeFound("zubehoerId.equals=" + (zubehoerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultZubehoerTypShouldBeFound(String filter) throws Exception {
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zubehoerTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE.toString())));

        // Check, that the count call also returns 1
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultZubehoerTypShouldNotBeFound(String filter) throws Exception {
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingZubehoerTyp() throws Exception {
        // Get the zubehoerTyp
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateZubehoerTyp() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        int databaseSizeBeforeUpdate = zubehoerTypRepository.findAll().size();

        // Update the zubehoerTyp
        ZubehoerTyp updatedZubehoerTyp = zubehoerTypRepository.findById(zubehoerTyp.getId()).get();
        // Disconnect from session so that the updates on updatedZubehoerTyp are not directly saved in db
        em.detach(updatedZubehoerTyp);
        updatedZubehoerTyp
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS)
            .technologie(UPDATED_TECHNOLOGIE);
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(updatedZubehoerTyp);

        restZubehoerTypMockMvc.perform(put("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isOk());

        // Validate the ZubehoerTyp in the database
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeUpdate);
        ZubehoerTyp testZubehoerTyp = zubehoerTypList.get(zubehoerTypList.size() - 1);
        assertThat(testZubehoerTyp.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);
        assertThat(testZubehoerTyp.getGueltigBis()).isEqualTo(UPDATED_GUELTIG_BIS);
        assertThat(testZubehoerTyp.getTechnologie()).isEqualTo(UPDATED_TECHNOLOGIE);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(1)).save(testZubehoerTyp);
    }

    @Test
    @Transactional
    public void updateNonExistingZubehoerTyp() throws Exception {
        int databaseSizeBeforeUpdate = zubehoerTypRepository.findAll().size();

        // Create the ZubehoerTyp
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZubehoerTypMockMvc.perform(put("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ZubehoerTyp in the database
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(0)).save(zubehoerTyp);
    }

    @Test
    @Transactional
    public void deleteZubehoerTyp() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        int databaseSizeBeforeDelete = zubehoerTypRepository.findAll().size();

        // Delete the zubehoerTyp
        restZubehoerTypMockMvc.perform(delete("/api/zubehoer-typs/{id}", zubehoerTyp.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(1)).deleteById(zubehoerTyp.getId());
    }

    @Test
    @Transactional
    public void searchZubehoerTyp() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);
        when(mockZubehoerTypSearchRepository.search(queryStringQuery("id:" + zubehoerTyp.getId())))
            .thenReturn(Collections.singletonList(zubehoerTyp));
        // Search the zubehoerTyp
        restZubehoerTypMockMvc.perform(get("/api/_search/zubehoer-typs?query=id:" + zubehoerTyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zubehoerTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE.toString())));
    }
}
