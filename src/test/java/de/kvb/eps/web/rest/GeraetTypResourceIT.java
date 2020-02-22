package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.GeraetTyp;
import de.kvb.eps.domain.Geraet;
import de.kvb.eps.repository.GeraetTypRepository;
import de.kvb.eps.repository.search.GeraetTypSearchRepository;
import de.kvb.eps.service.GeraetTypService;
import de.kvb.eps.service.dto.GeraetTypDTO;
import de.kvb.eps.service.mapper.GeraetTypMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.GeraetTypCriteria;
import de.kvb.eps.service.GeraetTypQueryService;

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
 * Integration tests for the {@link GeraetTypResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class GeraetTypResourceIT {

    private static final String DEFAULT_BEZEICHNUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEZEICHNUNG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GUELTIG_BIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUELTIG_BIS = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_GUELTIG_BIS = LocalDate.ofEpochDay(-1L);

    private static final Technologie DEFAULT_TECHNOLOGIE = Technologie.SONO;
    private static final Technologie UPDATED_TECHNOLOGIE = Technologie.BILD;

    @Autowired
    private GeraetTypRepository geraetTypRepository;

    @Autowired
    private GeraetTypMapper geraetTypMapper;

    @Autowired
    private GeraetTypService geraetTypService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.GeraetTypSearchRepositoryMockConfiguration
     */
    @Autowired
    private GeraetTypSearchRepository mockGeraetTypSearchRepository;

    @Autowired
    private GeraetTypQueryService geraetTypQueryService;

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

    private MockMvc restGeraetTypMockMvc;

    private GeraetTyp geraetTyp;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GeraetTypResource geraetTypResource = new GeraetTypResource(geraetTypService, geraetTypQueryService);
        this.restGeraetTypMockMvc = MockMvcBuilders.standaloneSetup(geraetTypResource)
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
    public static GeraetTyp createEntity(EntityManager em) {
        GeraetTyp geraetTyp = new GeraetTyp()
            .bezeichnung(DEFAULT_BEZEICHNUNG)
            .gueltigBis(DEFAULT_GUELTIG_BIS)
            .technologie(DEFAULT_TECHNOLOGIE);
        return geraetTyp;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeraetTyp createUpdatedEntity(EntityManager em) {
        GeraetTyp geraetTyp = new GeraetTyp()
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS)
            .technologie(UPDATED_TECHNOLOGIE);
        return geraetTyp;
    }

    @BeforeEach
    public void initTest() {
        geraetTyp = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeraetTyp() throws Exception {
        int databaseSizeBeforeCreate = geraetTypRepository.findAll().size();

        // Create the GeraetTyp
        GeraetTypDTO geraetTypDTO = geraetTypMapper.toDto(geraetTyp);
        restGeraetTypMockMvc.perform(post("/api/geraet-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geraetTypDTO)))
            .andExpect(status().isCreated());

        // Validate the GeraetTyp in the database
        List<GeraetTyp> geraetTypList = geraetTypRepository.findAll();
        assertThat(geraetTypList).hasSize(databaseSizeBeforeCreate + 1);
        GeraetTyp testGeraetTyp = geraetTypList.get(geraetTypList.size() - 1);
        assertThat(testGeraetTyp.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);
        assertThat(testGeraetTyp.getGueltigBis()).isEqualTo(DEFAULT_GUELTIG_BIS);
        assertThat(testGeraetTyp.getTechnologie()).isEqualTo(DEFAULT_TECHNOLOGIE);

        // Validate the GeraetTyp in Elasticsearch
        verify(mockGeraetTypSearchRepository, times(1)).save(testGeraetTyp);
    }

    @Test
    @Transactional
    public void createGeraetTypWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = geraetTypRepository.findAll().size();

        // Create the GeraetTyp with an existing ID
        geraetTyp.setId(1L);
        GeraetTypDTO geraetTypDTO = geraetTypMapper.toDto(geraetTyp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeraetTypMockMvc.perform(post("/api/geraet-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geraetTypDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeraetTyp in the database
        List<GeraetTyp> geraetTypList = geraetTypRepository.findAll();
        assertThat(geraetTypList).hasSize(databaseSizeBeforeCreate);

        // Validate the GeraetTyp in Elasticsearch
        verify(mockGeraetTypSearchRepository, times(0)).save(geraetTyp);
    }


    @Test
    @Transactional
    public void checkBezeichnungIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraetTypRepository.findAll().size();
        // set the field null
        geraetTyp.setBezeichnung(null);

        // Create the GeraetTyp, which fails.
        GeraetTypDTO geraetTypDTO = geraetTypMapper.toDto(geraetTyp);

        restGeraetTypMockMvc.perform(post("/api/geraet-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geraetTypDTO)))
            .andExpect(status().isBadRequest());

        List<GeraetTyp> geraetTypList = geraetTypRepository.findAll();
        assertThat(geraetTypList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTechnologieIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraetTypRepository.findAll().size();
        // set the field null
        geraetTyp.setTechnologie(null);

        // Create the GeraetTyp, which fails.
        GeraetTypDTO geraetTypDTO = geraetTypMapper.toDto(geraetTyp);

        restGeraetTypMockMvc.perform(post("/api/geraet-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geraetTypDTO)))
            .andExpect(status().isBadRequest());

        List<GeraetTyp> geraetTypList = geraetTypRepository.findAll();
        assertThat(geraetTypList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeraetTyps() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList
        restGeraetTypMockMvc.perform(get("/api/geraet-typs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraetTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE.toString())));
    }
    
    @Test
    @Transactional
    public void getGeraetTyp() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get the geraetTyp
        restGeraetTypMockMvc.perform(get("/api/geraet-typs/{id}", geraetTyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(geraetTyp.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()))
            .andExpect(jsonPath("$.technologie").value(DEFAULT_TECHNOLOGIE.toString()));
    }


    @Test
    @Transactional
    public void getGeraetTypsByIdFiltering() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        Long id = geraetTyp.getId();

        defaultGeraetTypShouldBeFound("id.equals=" + id);
        defaultGeraetTypShouldNotBeFound("id.notEquals=" + id);

        defaultGeraetTypShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGeraetTypShouldNotBeFound("id.greaterThan=" + id);

        defaultGeraetTypShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGeraetTypShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllGeraetTypsByBezeichnungIsEqualToSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where bezeichnung equals to DEFAULT_BEZEICHNUNG
        defaultGeraetTypShouldBeFound("bezeichnung.equals=" + DEFAULT_BEZEICHNUNG);

        // Get all the geraetTypList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultGeraetTypShouldNotBeFound("bezeichnung.equals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByBezeichnungIsNotEqualToSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where bezeichnung not equals to DEFAULT_BEZEICHNUNG
        defaultGeraetTypShouldNotBeFound("bezeichnung.notEquals=" + DEFAULT_BEZEICHNUNG);

        // Get all the geraetTypList where bezeichnung not equals to UPDATED_BEZEICHNUNG
        defaultGeraetTypShouldBeFound("bezeichnung.notEquals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByBezeichnungIsInShouldWork() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where bezeichnung in DEFAULT_BEZEICHNUNG or UPDATED_BEZEICHNUNG
        defaultGeraetTypShouldBeFound("bezeichnung.in=" + DEFAULT_BEZEICHNUNG + "," + UPDATED_BEZEICHNUNG);

        // Get all the geraetTypList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultGeraetTypShouldNotBeFound("bezeichnung.in=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByBezeichnungIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where bezeichnung is not null
        defaultGeraetTypShouldBeFound("bezeichnung.specified=true");

        // Get all the geraetTypList where bezeichnung is null
        defaultGeraetTypShouldNotBeFound("bezeichnung.specified=false");
    }
                @Test
    @Transactional
    public void getAllGeraetTypsByBezeichnungContainsSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where bezeichnung contains DEFAULT_BEZEICHNUNG
        defaultGeraetTypShouldBeFound("bezeichnung.contains=" + DEFAULT_BEZEICHNUNG);

        // Get all the geraetTypList where bezeichnung contains UPDATED_BEZEICHNUNG
        defaultGeraetTypShouldNotBeFound("bezeichnung.contains=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByBezeichnungNotContainsSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where bezeichnung does not contain DEFAULT_BEZEICHNUNG
        defaultGeraetTypShouldNotBeFound("bezeichnung.doesNotContain=" + DEFAULT_BEZEICHNUNG);

        // Get all the geraetTypList where bezeichnung does not contain UPDATED_BEZEICHNUNG
        defaultGeraetTypShouldBeFound("bezeichnung.doesNotContain=" + UPDATED_BEZEICHNUNG);
    }


    @Test
    @Transactional
    public void getAllGeraetTypsByGueltigBisIsEqualToSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where gueltigBis equals to DEFAULT_GUELTIG_BIS
        defaultGeraetTypShouldBeFound("gueltigBis.equals=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetTypList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultGeraetTypShouldNotBeFound("gueltigBis.equals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByGueltigBisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where gueltigBis not equals to DEFAULT_GUELTIG_BIS
        defaultGeraetTypShouldNotBeFound("gueltigBis.notEquals=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetTypList where gueltigBis not equals to UPDATED_GUELTIG_BIS
        defaultGeraetTypShouldBeFound("gueltigBis.notEquals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByGueltigBisIsInShouldWork() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where gueltigBis in DEFAULT_GUELTIG_BIS or UPDATED_GUELTIG_BIS
        defaultGeraetTypShouldBeFound("gueltigBis.in=" + DEFAULT_GUELTIG_BIS + "," + UPDATED_GUELTIG_BIS);

        // Get all the geraetTypList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultGeraetTypShouldNotBeFound("gueltigBis.in=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByGueltigBisIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where gueltigBis is not null
        defaultGeraetTypShouldBeFound("gueltigBis.specified=true");

        // Get all the geraetTypList where gueltigBis is null
        defaultGeraetTypShouldNotBeFound("gueltigBis.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByGueltigBisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where gueltigBis is greater than or equal to DEFAULT_GUELTIG_BIS
        defaultGeraetTypShouldBeFound("gueltigBis.greaterThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetTypList where gueltigBis is greater than or equal to UPDATED_GUELTIG_BIS
        defaultGeraetTypShouldNotBeFound("gueltigBis.greaterThanOrEqual=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByGueltigBisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where gueltigBis is less than or equal to DEFAULT_GUELTIG_BIS
        defaultGeraetTypShouldBeFound("gueltigBis.lessThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetTypList where gueltigBis is less than or equal to SMALLER_GUELTIG_BIS
        defaultGeraetTypShouldNotBeFound("gueltigBis.lessThanOrEqual=" + SMALLER_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByGueltigBisIsLessThanSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where gueltigBis is less than DEFAULT_GUELTIG_BIS
        defaultGeraetTypShouldNotBeFound("gueltigBis.lessThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetTypList where gueltigBis is less than UPDATED_GUELTIG_BIS
        defaultGeraetTypShouldBeFound("gueltigBis.lessThan=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByGueltigBisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where gueltigBis is greater than DEFAULT_GUELTIG_BIS
        defaultGeraetTypShouldNotBeFound("gueltigBis.greaterThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetTypList where gueltigBis is greater than SMALLER_GUELTIG_BIS
        defaultGeraetTypShouldBeFound("gueltigBis.greaterThan=" + SMALLER_GUELTIG_BIS);
    }


    @Test
    @Transactional
    public void getAllGeraetTypsByTechnologieIsEqualToSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where technologie equals to DEFAULT_TECHNOLOGIE
        defaultGeraetTypShouldBeFound("technologie.equals=" + DEFAULT_TECHNOLOGIE);

        // Get all the geraetTypList where technologie equals to UPDATED_TECHNOLOGIE
        defaultGeraetTypShouldNotBeFound("technologie.equals=" + UPDATED_TECHNOLOGIE);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByTechnologieIsNotEqualToSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where technologie not equals to DEFAULT_TECHNOLOGIE
        defaultGeraetTypShouldNotBeFound("technologie.notEquals=" + DEFAULT_TECHNOLOGIE);

        // Get all the geraetTypList where technologie not equals to UPDATED_TECHNOLOGIE
        defaultGeraetTypShouldBeFound("technologie.notEquals=" + UPDATED_TECHNOLOGIE);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByTechnologieIsInShouldWork() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where technologie in DEFAULT_TECHNOLOGIE or UPDATED_TECHNOLOGIE
        defaultGeraetTypShouldBeFound("technologie.in=" + DEFAULT_TECHNOLOGIE + "," + UPDATED_TECHNOLOGIE);

        // Get all the geraetTypList where technologie equals to UPDATED_TECHNOLOGIE
        defaultGeraetTypShouldNotBeFound("technologie.in=" + UPDATED_TECHNOLOGIE);
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByTechnologieIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        // Get all the geraetTypList where technologie is not null
        defaultGeraetTypShouldBeFound("technologie.specified=true");

        // Get all the geraetTypList where technologie is null
        defaultGeraetTypShouldNotBeFound("technologie.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraetTypsByGeraetIsEqualToSomething() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);
        Geraet geraet = GeraetResourceIT.createEntity(em);
        em.persist(geraet);
        em.flush();
        geraetTyp.addGeraet(geraet);
        geraetTypRepository.saveAndFlush(geraetTyp);
        Long geraetId = geraet.getId();

        // Get all the geraetTypList where geraet equals to geraetId
        defaultGeraetTypShouldBeFound("geraetId.equals=" + geraetId);

        // Get all the geraetTypList where geraet equals to geraetId + 1
        defaultGeraetTypShouldNotBeFound("geraetId.equals=" + (geraetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeraetTypShouldBeFound(String filter) throws Exception {
        restGeraetTypMockMvc.perform(get("/api/geraet-typs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraetTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE.toString())));

        // Check, that the count call also returns 1
        restGeraetTypMockMvc.perform(get("/api/geraet-typs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeraetTypShouldNotBeFound(String filter) throws Exception {
        restGeraetTypMockMvc.perform(get("/api/geraet-typs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeraetTypMockMvc.perform(get("/api/geraet-typs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGeraetTyp() throws Exception {
        // Get the geraetTyp
        restGeraetTypMockMvc.perform(get("/api/geraet-typs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeraetTyp() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        int databaseSizeBeforeUpdate = geraetTypRepository.findAll().size();

        // Update the geraetTyp
        GeraetTyp updatedGeraetTyp = geraetTypRepository.findById(geraetTyp.getId()).get();
        // Disconnect from session so that the updates on updatedGeraetTyp are not directly saved in db
        em.detach(updatedGeraetTyp);
        updatedGeraetTyp
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS)
            .technologie(UPDATED_TECHNOLOGIE);
        GeraetTypDTO geraetTypDTO = geraetTypMapper.toDto(updatedGeraetTyp);

        restGeraetTypMockMvc.perform(put("/api/geraet-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geraetTypDTO)))
            .andExpect(status().isOk());

        // Validate the GeraetTyp in the database
        List<GeraetTyp> geraetTypList = geraetTypRepository.findAll();
        assertThat(geraetTypList).hasSize(databaseSizeBeforeUpdate);
        GeraetTyp testGeraetTyp = geraetTypList.get(geraetTypList.size() - 1);
        assertThat(testGeraetTyp.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);
        assertThat(testGeraetTyp.getGueltigBis()).isEqualTo(UPDATED_GUELTIG_BIS);
        assertThat(testGeraetTyp.getTechnologie()).isEqualTo(UPDATED_TECHNOLOGIE);

        // Validate the GeraetTyp in Elasticsearch
        verify(mockGeraetTypSearchRepository, times(1)).save(testGeraetTyp);
    }

    @Test
    @Transactional
    public void updateNonExistingGeraetTyp() throws Exception {
        int databaseSizeBeforeUpdate = geraetTypRepository.findAll().size();

        // Create the GeraetTyp
        GeraetTypDTO geraetTypDTO = geraetTypMapper.toDto(geraetTyp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeraetTypMockMvc.perform(put("/api/geraet-typs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geraetTypDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeraetTyp in the database
        List<GeraetTyp> geraetTypList = geraetTypRepository.findAll();
        assertThat(geraetTypList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GeraetTyp in Elasticsearch
        verify(mockGeraetTypSearchRepository, times(0)).save(geraetTyp);
    }

    @Test
    @Transactional
    public void deleteGeraetTyp() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);

        int databaseSizeBeforeDelete = geraetTypRepository.findAll().size();

        // Delete the geraetTyp
        restGeraetTypMockMvc.perform(delete("/api/geraet-typs/{id}", geraetTyp.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GeraetTyp> geraetTypList = geraetTypRepository.findAll();
        assertThat(geraetTypList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GeraetTyp in Elasticsearch
        verify(mockGeraetTypSearchRepository, times(1)).deleteById(geraetTyp.getId());
    }

    @Test
    @Transactional
    public void searchGeraetTyp() throws Exception {
        // Initialize the database
        geraetTypRepository.saveAndFlush(geraetTyp);
        when(mockGeraetTypSearchRepository.search(queryStringQuery("id:" + geraetTyp.getId())))
            .thenReturn(Collections.singletonList(geraetTyp));
        // Search the geraetTyp
        restGeraetTypMockMvc.perform(get("/api/_search/geraet-typs?query=id:" + geraetTyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraetTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE.toString())));
    }
}
