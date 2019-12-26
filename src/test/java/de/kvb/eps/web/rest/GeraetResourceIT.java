package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Geraet;
import de.kvb.eps.domain.Systemtyp;
import de.kvb.eps.domain.GeraetTyp;
import de.kvb.eps.domain.Hersteller;
import de.kvb.eps.repository.GeraetRepository;
import de.kvb.eps.repository.search.GeraetSearchRepository;
import de.kvb.eps.service.GeraetService;
import de.kvb.eps.service.dto.GeraetDTO;
import de.kvb.eps.service.mapper.GeraetMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.GeraetCriteria;
import de.kvb.eps.service.GeraetQueryService;

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
 * Integration tests for the {@link GeraetResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class GeraetResourceIT {

    private static final String DEFAULT_BEZEICHNUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEZEICHNUNG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GUELTIG_BIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUELTIG_BIS = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_GUELTIG_BIS = LocalDate.ofEpochDay(-1L);

    @Autowired
    private GeraetRepository geraetRepository;

    @Autowired
    private GeraetMapper geraetMapper;

    @Autowired
    private GeraetService geraetService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.GeraetSearchRepositoryMockConfiguration
     */
    @Autowired
    private GeraetSearchRepository mockGeraetSearchRepository;

    @Autowired
    private GeraetQueryService geraetQueryService;

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

    private MockMvc restGeraetMockMvc;

    private Geraet geraet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GeraetResource geraetResource = new GeraetResource(geraetService, geraetQueryService);
        this.restGeraetMockMvc = MockMvcBuilders.standaloneSetup(geraetResource)
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
    public static Geraet createEntity(EntityManager em) {
        Geraet geraet = new Geraet()
            .bezeichnung(DEFAULT_BEZEICHNUNG)
            .gueltigBis(DEFAULT_GUELTIG_BIS);
        // Add required entity
        GeraetTyp geraetTyp;
        if (TestUtil.findAll(em, GeraetTyp.class).isEmpty()) {
            geraetTyp = GeraetTypResourceIT.createEntity(em);
            em.persist(geraetTyp);
            em.flush();
        } else {
            geraetTyp = TestUtil.findAll(em, GeraetTyp.class).get(0);
        }
        geraet.setGeraetTyp(geraetTyp);
        // Add required entity
        Hersteller hersteller;
        if (TestUtil.findAll(em, Hersteller.class).isEmpty()) {
            hersteller = HerstellerResourceIT.createEntity(em);
            em.persist(hersteller);
            em.flush();
        } else {
            hersteller = TestUtil.findAll(em, Hersteller.class).get(0);
        }
        geraet.setHersteller(hersteller);
        return geraet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Geraet createUpdatedEntity(EntityManager em) {
        Geraet geraet = new Geraet()
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS);
        // Add required entity
        GeraetTyp geraetTyp;
        if (TestUtil.findAll(em, GeraetTyp.class).isEmpty()) {
            geraetTyp = GeraetTypResourceIT.createUpdatedEntity(em);
            em.persist(geraetTyp);
            em.flush();
        } else {
            geraetTyp = TestUtil.findAll(em, GeraetTyp.class).get(0);
        }
        geraet.setGeraetTyp(geraetTyp);
        // Add required entity
        Hersteller hersteller;
        if (TestUtil.findAll(em, Hersteller.class).isEmpty()) {
            hersteller = HerstellerResourceIT.createUpdatedEntity(em);
            em.persist(hersteller);
            em.flush();
        } else {
            hersteller = TestUtil.findAll(em, Hersteller.class).get(0);
        }
        geraet.setHersteller(hersteller);
        return geraet;
    }

    @BeforeEach
    public void initTest() {
        geraet = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeraet() throws Exception {
        int databaseSizeBeforeCreate = geraetRepository.findAll().size();

        // Create the Geraet
        GeraetDTO geraetDTO = geraetMapper.toDto(geraet);
        restGeraetMockMvc.perform(post("/api/geraets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraetDTO)))
            .andExpect(status().isCreated());

        // Validate the Geraet in the database
        List<Geraet> geraetList = geraetRepository.findAll();
        assertThat(geraetList).hasSize(databaseSizeBeforeCreate + 1);
        Geraet testGeraet = geraetList.get(geraetList.size() - 1);
        assertThat(testGeraet.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);
        assertThat(testGeraet.getGueltigBis()).isEqualTo(DEFAULT_GUELTIG_BIS);

        // Validate the Geraet in Elasticsearch
        verify(mockGeraetSearchRepository, times(1)).save(testGeraet);
    }

    @Test
    @Transactional
    public void createGeraetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = geraetRepository.findAll().size();

        // Create the Geraet with an existing ID
        geraet.setId(1L);
        GeraetDTO geraetDTO = geraetMapper.toDto(geraet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeraetMockMvc.perform(post("/api/geraets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Geraet in the database
        List<Geraet> geraetList = geraetRepository.findAll();
        assertThat(geraetList).hasSize(databaseSizeBeforeCreate);

        // Validate the Geraet in Elasticsearch
        verify(mockGeraetSearchRepository, times(0)).save(geraet);
    }


    @Test
    @Transactional
    public void checkBezeichnungIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraetRepository.findAll().size();
        // set the field null
        geraet.setBezeichnung(null);

        // Create the Geraet, which fails.
        GeraetDTO geraetDTO = geraetMapper.toDto(geraet);

        restGeraetMockMvc.perform(post("/api/geraets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraetDTO)))
            .andExpect(status().isBadRequest());

        List<Geraet> geraetList = geraetRepository.findAll();
        assertThat(geraetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeraets() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList
        restGeraetMockMvc.perform(get("/api/geraets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraet.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));
    }
    
    @Test
    @Transactional
    public void getGeraet() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get the geraet
        restGeraetMockMvc.perform(get("/api/geraets/{id}", geraet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(geraet.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()));
    }


    @Test
    @Transactional
    public void getGeraetsByIdFiltering() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        Long id = geraet.getId();

        defaultGeraetShouldBeFound("id.equals=" + id);
        defaultGeraetShouldNotBeFound("id.notEquals=" + id);

        defaultGeraetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGeraetShouldNotBeFound("id.greaterThan=" + id);

        defaultGeraetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGeraetShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllGeraetsByBezeichnungIsEqualToSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where bezeichnung equals to DEFAULT_BEZEICHNUNG
        defaultGeraetShouldBeFound("bezeichnung.equals=" + DEFAULT_BEZEICHNUNG);

        // Get all the geraetList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultGeraetShouldNotBeFound("bezeichnung.equals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllGeraetsByBezeichnungIsNotEqualToSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where bezeichnung not equals to DEFAULT_BEZEICHNUNG
        defaultGeraetShouldNotBeFound("bezeichnung.notEquals=" + DEFAULT_BEZEICHNUNG);

        // Get all the geraetList where bezeichnung not equals to UPDATED_BEZEICHNUNG
        defaultGeraetShouldBeFound("bezeichnung.notEquals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllGeraetsByBezeichnungIsInShouldWork() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where bezeichnung in DEFAULT_BEZEICHNUNG or UPDATED_BEZEICHNUNG
        defaultGeraetShouldBeFound("bezeichnung.in=" + DEFAULT_BEZEICHNUNG + "," + UPDATED_BEZEICHNUNG);

        // Get all the geraetList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultGeraetShouldNotBeFound("bezeichnung.in=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllGeraetsByBezeichnungIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where bezeichnung is not null
        defaultGeraetShouldBeFound("bezeichnung.specified=true");

        // Get all the geraetList where bezeichnung is null
        defaultGeraetShouldNotBeFound("bezeichnung.specified=false");
    }
                @Test
    @Transactional
    public void getAllGeraetsByBezeichnungContainsSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where bezeichnung contains DEFAULT_BEZEICHNUNG
        defaultGeraetShouldBeFound("bezeichnung.contains=" + DEFAULT_BEZEICHNUNG);

        // Get all the geraetList where bezeichnung contains UPDATED_BEZEICHNUNG
        defaultGeraetShouldNotBeFound("bezeichnung.contains=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllGeraetsByBezeichnungNotContainsSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where bezeichnung does not contain DEFAULT_BEZEICHNUNG
        defaultGeraetShouldNotBeFound("bezeichnung.doesNotContain=" + DEFAULT_BEZEICHNUNG);

        // Get all the geraetList where bezeichnung does not contain UPDATED_BEZEICHNUNG
        defaultGeraetShouldBeFound("bezeichnung.doesNotContain=" + UPDATED_BEZEICHNUNG);
    }


    @Test
    @Transactional
    public void getAllGeraetsByGueltigBisIsEqualToSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where gueltigBis equals to DEFAULT_GUELTIG_BIS
        defaultGeraetShouldBeFound("gueltigBis.equals=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultGeraetShouldNotBeFound("gueltigBis.equals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetsByGueltigBisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where gueltigBis not equals to DEFAULT_GUELTIG_BIS
        defaultGeraetShouldNotBeFound("gueltigBis.notEquals=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetList where gueltigBis not equals to UPDATED_GUELTIG_BIS
        defaultGeraetShouldBeFound("gueltigBis.notEquals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetsByGueltigBisIsInShouldWork() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where gueltigBis in DEFAULT_GUELTIG_BIS or UPDATED_GUELTIG_BIS
        defaultGeraetShouldBeFound("gueltigBis.in=" + DEFAULT_GUELTIG_BIS + "," + UPDATED_GUELTIG_BIS);

        // Get all the geraetList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultGeraetShouldNotBeFound("gueltigBis.in=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetsByGueltigBisIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where gueltigBis is not null
        defaultGeraetShouldBeFound("gueltigBis.specified=true");

        // Get all the geraetList where gueltigBis is null
        defaultGeraetShouldNotBeFound("gueltigBis.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraetsByGueltigBisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where gueltigBis is greater than or equal to DEFAULT_GUELTIG_BIS
        defaultGeraetShouldBeFound("gueltigBis.greaterThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetList where gueltigBis is greater than or equal to UPDATED_GUELTIG_BIS
        defaultGeraetShouldNotBeFound("gueltigBis.greaterThanOrEqual=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetsByGueltigBisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where gueltigBis is less than or equal to DEFAULT_GUELTIG_BIS
        defaultGeraetShouldBeFound("gueltigBis.lessThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetList where gueltigBis is less than or equal to SMALLER_GUELTIG_BIS
        defaultGeraetShouldNotBeFound("gueltigBis.lessThanOrEqual=" + SMALLER_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetsByGueltigBisIsLessThanSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where gueltigBis is less than DEFAULT_GUELTIG_BIS
        defaultGeraetShouldNotBeFound("gueltigBis.lessThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetList where gueltigBis is less than UPDATED_GUELTIG_BIS
        defaultGeraetShouldBeFound("gueltigBis.lessThan=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllGeraetsByGueltigBisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        // Get all the geraetList where gueltigBis is greater than DEFAULT_GUELTIG_BIS
        defaultGeraetShouldNotBeFound("gueltigBis.greaterThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the geraetList where gueltigBis is greater than SMALLER_GUELTIG_BIS
        defaultGeraetShouldBeFound("gueltigBis.greaterThan=" + SMALLER_GUELTIG_BIS);
    }


    @Test
    @Transactional
    public void getAllGeraetsBySystemtypIsEqualToSomething() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);
        Systemtyp systemtyp = SystemtypResourceIT.createEntity(em);
        em.persist(systemtyp);
        em.flush();
        geraet.addSystemtyp(systemtyp);
        geraetRepository.saveAndFlush(geraet);
        Long systemtypId = systemtyp.getId();

        // Get all the geraetList where systemtyp equals to systemtypId
        defaultGeraetShouldBeFound("systemtypId.equals=" + systemtypId);

        // Get all the geraetList where systemtyp equals to systemtypId + 1
        defaultGeraetShouldNotBeFound("systemtypId.equals=" + (systemtypId + 1));
    }


    @Test
    @Transactional
    public void getAllGeraetsByGeraetTypIsEqualToSomething() throws Exception {
        // Get already existing entity
        GeraetTyp geraetTyp = geraet.getGeraetTyp();
        geraetRepository.saveAndFlush(geraet);
        Long geraetTypId = geraetTyp.getId();

        // Get all the geraetList where geraetTyp equals to geraetTypId
        defaultGeraetShouldBeFound("geraetTypId.equals=" + geraetTypId);

        // Get all the geraetList where geraetTyp equals to geraetTypId + 1
        defaultGeraetShouldNotBeFound("geraetTypId.equals=" + (geraetTypId + 1));
    }


    @Test
    @Transactional
    public void getAllGeraetsByHerstellerIsEqualToSomething() throws Exception {
        // Get already existing entity
        Hersteller hersteller = geraet.getHersteller();
        geraetRepository.saveAndFlush(geraet);
        Long herstellerId = hersteller.getId();

        // Get all the geraetList where hersteller equals to herstellerId
        defaultGeraetShouldBeFound("herstellerId.equals=" + herstellerId);

        // Get all the geraetList where hersteller equals to herstellerId + 1
        defaultGeraetShouldNotBeFound("herstellerId.equals=" + (herstellerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeraetShouldBeFound(String filter) throws Exception {
        restGeraetMockMvc.perform(get("/api/geraets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraet.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));

        // Check, that the count call also returns 1
        restGeraetMockMvc.perform(get("/api/geraets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeraetShouldNotBeFound(String filter) throws Exception {
        restGeraetMockMvc.perform(get("/api/geraets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeraetMockMvc.perform(get("/api/geraets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGeraet() throws Exception {
        // Get the geraet
        restGeraetMockMvc.perform(get("/api/geraets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeraet() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        int databaseSizeBeforeUpdate = geraetRepository.findAll().size();

        // Update the geraet
        Geraet updatedGeraet = geraetRepository.findById(geraet.getId()).get();
        // Disconnect from session so that the updates on updatedGeraet are not directly saved in db
        em.detach(updatedGeraet);
        updatedGeraet
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS);
        GeraetDTO geraetDTO = geraetMapper.toDto(updatedGeraet);

        restGeraetMockMvc.perform(put("/api/geraets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraetDTO)))
            .andExpect(status().isOk());

        // Validate the Geraet in the database
        List<Geraet> geraetList = geraetRepository.findAll();
        assertThat(geraetList).hasSize(databaseSizeBeforeUpdate);
        Geraet testGeraet = geraetList.get(geraetList.size() - 1);
        assertThat(testGeraet.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);
        assertThat(testGeraet.getGueltigBis()).isEqualTo(UPDATED_GUELTIG_BIS);

        // Validate the Geraet in Elasticsearch
        verify(mockGeraetSearchRepository, times(1)).save(testGeraet);
    }

    @Test
    @Transactional
    public void updateNonExistingGeraet() throws Exception {
        int databaseSizeBeforeUpdate = geraetRepository.findAll().size();

        // Create the Geraet
        GeraetDTO geraetDTO = geraetMapper.toDto(geraet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeraetMockMvc.perform(put("/api/geraets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Geraet in the database
        List<Geraet> geraetList = geraetRepository.findAll();
        assertThat(geraetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Geraet in Elasticsearch
        verify(mockGeraetSearchRepository, times(0)).save(geraet);
    }

    @Test
    @Transactional
    public void deleteGeraet() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);

        int databaseSizeBeforeDelete = geraetRepository.findAll().size();

        // Delete the geraet
        restGeraetMockMvc.perform(delete("/api/geraets/{id}", geraet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Geraet> geraetList = geraetRepository.findAll();
        assertThat(geraetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Geraet in Elasticsearch
        verify(mockGeraetSearchRepository, times(1)).deleteById(geraet.getId());
    }

    @Test
    @Transactional
    public void searchGeraet() throws Exception {
        // Initialize the database
        geraetRepository.saveAndFlush(geraet);
        when(mockGeraetSearchRepository.search(queryStringQuery("id:" + geraet.getId())))
            .thenReturn(Collections.singletonList(geraet));
        // Search the geraet
        restGeraetMockMvc.perform(get("/api/_search/geraets?query=id:" + geraet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraet.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));
    }
}
