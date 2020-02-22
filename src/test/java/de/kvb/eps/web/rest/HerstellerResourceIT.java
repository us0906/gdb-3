package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Hersteller;
import de.kvb.eps.domain.Geraet;
import de.kvb.eps.domain.Zubehoer;
import de.kvb.eps.repository.HerstellerRepository;
import de.kvb.eps.repository.search.HerstellerSearchRepository;
import de.kvb.eps.service.HerstellerService;
import de.kvb.eps.service.dto.HerstellerDTO;
import de.kvb.eps.service.mapper.HerstellerMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.HerstellerCriteria;
import de.kvb.eps.service.HerstellerQueryService;

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
 * Integration tests for the {@link HerstellerResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class HerstellerResourceIT {

    private static final String DEFAULT_BEZEICHNUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEZEICHNUNG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GUELTIG_BIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUELTIG_BIS = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_GUELTIG_BIS = LocalDate.ofEpochDay(-1L);

    @Autowired
    private HerstellerRepository herstellerRepository;

    @Autowired
    private HerstellerMapper herstellerMapper;

    @Autowired
    private HerstellerService herstellerService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.HerstellerSearchRepositoryMockConfiguration
     */
    @Autowired
    private HerstellerSearchRepository mockHerstellerSearchRepository;

    @Autowired
    private HerstellerQueryService herstellerQueryService;

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

    private MockMvc restHerstellerMockMvc;

    private Hersteller hersteller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HerstellerResource herstellerResource = new HerstellerResource(herstellerService, herstellerQueryService);
        this.restHerstellerMockMvc = MockMvcBuilders.standaloneSetup(herstellerResource)
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
    public static Hersteller createEntity(EntityManager em) {
        Hersteller hersteller = new Hersteller()
            .bezeichnung(DEFAULT_BEZEICHNUNG)
            .gueltigBis(DEFAULT_GUELTIG_BIS);
        return hersteller;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hersteller createUpdatedEntity(EntityManager em) {
        Hersteller hersteller = new Hersteller()
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS);
        return hersteller;
    }

    @BeforeEach
    public void initTest() {
        hersteller = createEntity(em);
    }

    @Test
    @Transactional
    public void createHersteller() throws Exception {
        int databaseSizeBeforeCreate = herstellerRepository.findAll().size();

        // Create the Hersteller
        HerstellerDTO herstellerDTO = herstellerMapper.toDto(hersteller);
        restHerstellerMockMvc.perform(post("/api/herstellers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herstellerDTO)))
            .andExpect(status().isCreated());

        // Validate the Hersteller in the database
        List<Hersteller> herstellerList = herstellerRepository.findAll();
        assertThat(herstellerList).hasSize(databaseSizeBeforeCreate + 1);
        Hersteller testHersteller = herstellerList.get(herstellerList.size() - 1);
        assertThat(testHersteller.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);
        assertThat(testHersteller.getGueltigBis()).isEqualTo(DEFAULT_GUELTIG_BIS);

        // Validate the Hersteller in Elasticsearch
        verify(mockHerstellerSearchRepository, times(1)).save(testHersteller);
    }

    @Test
    @Transactional
    public void createHerstellerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = herstellerRepository.findAll().size();

        // Create the Hersteller with an existing ID
        hersteller.setId(1L);
        HerstellerDTO herstellerDTO = herstellerMapper.toDto(hersteller);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHerstellerMockMvc.perform(post("/api/herstellers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herstellerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hersteller in the database
        List<Hersteller> herstellerList = herstellerRepository.findAll();
        assertThat(herstellerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Hersteller in Elasticsearch
        verify(mockHerstellerSearchRepository, times(0)).save(hersteller);
    }


    @Test
    @Transactional
    public void checkBezeichnungIsRequired() throws Exception {
        int databaseSizeBeforeTest = herstellerRepository.findAll().size();
        // set the field null
        hersteller.setBezeichnung(null);

        // Create the Hersteller, which fails.
        HerstellerDTO herstellerDTO = herstellerMapper.toDto(hersteller);

        restHerstellerMockMvc.perform(post("/api/herstellers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herstellerDTO)))
            .andExpect(status().isBadRequest());

        List<Hersteller> herstellerList = herstellerRepository.findAll();
        assertThat(herstellerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHerstellers() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList
        restHerstellerMockMvc.perform(get("/api/herstellers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hersteller.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));
    }
    
    @Test
    @Transactional
    public void getHersteller() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get the hersteller
        restHerstellerMockMvc.perform(get("/api/herstellers/{id}", hersteller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hersteller.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()));
    }


    @Test
    @Transactional
    public void getHerstellersByIdFiltering() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        Long id = hersteller.getId();

        defaultHerstellerShouldBeFound("id.equals=" + id);
        defaultHerstellerShouldNotBeFound("id.notEquals=" + id);

        defaultHerstellerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHerstellerShouldNotBeFound("id.greaterThan=" + id);

        defaultHerstellerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHerstellerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHerstellersByBezeichnungIsEqualToSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where bezeichnung equals to DEFAULT_BEZEICHNUNG
        defaultHerstellerShouldBeFound("bezeichnung.equals=" + DEFAULT_BEZEICHNUNG);

        // Get all the herstellerList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultHerstellerShouldNotBeFound("bezeichnung.equals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllHerstellersByBezeichnungIsNotEqualToSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where bezeichnung not equals to DEFAULT_BEZEICHNUNG
        defaultHerstellerShouldNotBeFound("bezeichnung.notEquals=" + DEFAULT_BEZEICHNUNG);

        // Get all the herstellerList where bezeichnung not equals to UPDATED_BEZEICHNUNG
        defaultHerstellerShouldBeFound("bezeichnung.notEquals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllHerstellersByBezeichnungIsInShouldWork() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where bezeichnung in DEFAULT_BEZEICHNUNG or UPDATED_BEZEICHNUNG
        defaultHerstellerShouldBeFound("bezeichnung.in=" + DEFAULT_BEZEICHNUNG + "," + UPDATED_BEZEICHNUNG);

        // Get all the herstellerList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultHerstellerShouldNotBeFound("bezeichnung.in=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllHerstellersByBezeichnungIsNullOrNotNull() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where bezeichnung is not null
        defaultHerstellerShouldBeFound("bezeichnung.specified=true");

        // Get all the herstellerList where bezeichnung is null
        defaultHerstellerShouldNotBeFound("bezeichnung.specified=false");
    }
                @Test
    @Transactional
    public void getAllHerstellersByBezeichnungContainsSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where bezeichnung contains DEFAULT_BEZEICHNUNG
        defaultHerstellerShouldBeFound("bezeichnung.contains=" + DEFAULT_BEZEICHNUNG);

        // Get all the herstellerList where bezeichnung contains UPDATED_BEZEICHNUNG
        defaultHerstellerShouldNotBeFound("bezeichnung.contains=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllHerstellersByBezeichnungNotContainsSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where bezeichnung does not contain DEFAULT_BEZEICHNUNG
        defaultHerstellerShouldNotBeFound("bezeichnung.doesNotContain=" + DEFAULT_BEZEICHNUNG);

        // Get all the herstellerList where bezeichnung does not contain UPDATED_BEZEICHNUNG
        defaultHerstellerShouldBeFound("bezeichnung.doesNotContain=" + UPDATED_BEZEICHNUNG);
    }


    @Test
    @Transactional
    public void getAllHerstellersByGueltigBisIsEqualToSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where gueltigBis equals to DEFAULT_GUELTIG_BIS
        defaultHerstellerShouldBeFound("gueltigBis.equals=" + DEFAULT_GUELTIG_BIS);

        // Get all the herstellerList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultHerstellerShouldNotBeFound("gueltigBis.equals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllHerstellersByGueltigBisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where gueltigBis not equals to DEFAULT_GUELTIG_BIS
        defaultHerstellerShouldNotBeFound("gueltigBis.notEquals=" + DEFAULT_GUELTIG_BIS);

        // Get all the herstellerList where gueltigBis not equals to UPDATED_GUELTIG_BIS
        defaultHerstellerShouldBeFound("gueltigBis.notEquals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllHerstellersByGueltigBisIsInShouldWork() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where gueltigBis in DEFAULT_GUELTIG_BIS or UPDATED_GUELTIG_BIS
        defaultHerstellerShouldBeFound("gueltigBis.in=" + DEFAULT_GUELTIG_BIS + "," + UPDATED_GUELTIG_BIS);

        // Get all the herstellerList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultHerstellerShouldNotBeFound("gueltigBis.in=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllHerstellersByGueltigBisIsNullOrNotNull() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where gueltigBis is not null
        defaultHerstellerShouldBeFound("gueltigBis.specified=true");

        // Get all the herstellerList where gueltigBis is null
        defaultHerstellerShouldNotBeFound("gueltigBis.specified=false");
    }

    @Test
    @Transactional
    public void getAllHerstellersByGueltigBisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where gueltigBis is greater than or equal to DEFAULT_GUELTIG_BIS
        defaultHerstellerShouldBeFound("gueltigBis.greaterThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the herstellerList where gueltigBis is greater than or equal to UPDATED_GUELTIG_BIS
        defaultHerstellerShouldNotBeFound("gueltigBis.greaterThanOrEqual=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllHerstellersByGueltigBisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where gueltigBis is less than or equal to DEFAULT_GUELTIG_BIS
        defaultHerstellerShouldBeFound("gueltigBis.lessThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the herstellerList where gueltigBis is less than or equal to SMALLER_GUELTIG_BIS
        defaultHerstellerShouldNotBeFound("gueltigBis.lessThanOrEqual=" + SMALLER_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllHerstellersByGueltigBisIsLessThanSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where gueltigBis is less than DEFAULT_GUELTIG_BIS
        defaultHerstellerShouldNotBeFound("gueltigBis.lessThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the herstellerList where gueltigBis is less than UPDATED_GUELTIG_BIS
        defaultHerstellerShouldBeFound("gueltigBis.lessThan=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllHerstellersByGueltigBisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        // Get all the herstellerList where gueltigBis is greater than DEFAULT_GUELTIG_BIS
        defaultHerstellerShouldNotBeFound("gueltigBis.greaterThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the herstellerList where gueltigBis is greater than SMALLER_GUELTIG_BIS
        defaultHerstellerShouldBeFound("gueltigBis.greaterThan=" + SMALLER_GUELTIG_BIS);
    }


    @Test
    @Transactional
    public void getAllHerstellersByGeraetIsEqualToSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);
        Geraet geraet = GeraetResourceIT.createEntity(em);
        em.persist(geraet);
        em.flush();
        hersteller.addGeraet(geraet);
        herstellerRepository.saveAndFlush(hersteller);
        Long geraetId = geraet.getId();

        // Get all the herstellerList where geraet equals to geraetId
        defaultHerstellerShouldBeFound("geraetId.equals=" + geraetId);

        // Get all the herstellerList where geraet equals to geraetId + 1
        defaultHerstellerShouldNotBeFound("geraetId.equals=" + (geraetId + 1));
    }


    @Test
    @Transactional
    public void getAllHerstellersByZubehoerIsEqualToSomething() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);
        Zubehoer zubehoer = ZubehoerResourceIT.createEntity(em);
        em.persist(zubehoer);
        em.flush();
        hersteller.addZubehoer(zubehoer);
        herstellerRepository.saveAndFlush(hersteller);
        Long zubehoerId = zubehoer.getId();

        // Get all the herstellerList where zubehoer equals to zubehoerId
        defaultHerstellerShouldBeFound("zubehoerId.equals=" + zubehoerId);

        // Get all the herstellerList where zubehoer equals to zubehoerId + 1
        defaultHerstellerShouldNotBeFound("zubehoerId.equals=" + (zubehoerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHerstellerShouldBeFound(String filter) throws Exception {
        restHerstellerMockMvc.perform(get("/api/herstellers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hersteller.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));

        // Check, that the count call also returns 1
        restHerstellerMockMvc.perform(get("/api/herstellers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHerstellerShouldNotBeFound(String filter) throws Exception {
        restHerstellerMockMvc.perform(get("/api/herstellers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHerstellerMockMvc.perform(get("/api/herstellers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHersteller() throws Exception {
        // Get the hersteller
        restHerstellerMockMvc.perform(get("/api/herstellers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHersteller() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        int databaseSizeBeforeUpdate = herstellerRepository.findAll().size();

        // Update the hersteller
        Hersteller updatedHersteller = herstellerRepository.findById(hersteller.getId()).get();
        // Disconnect from session so that the updates on updatedHersteller are not directly saved in db
        em.detach(updatedHersteller);
        updatedHersteller
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS);
        HerstellerDTO herstellerDTO = herstellerMapper.toDto(updatedHersteller);

        restHerstellerMockMvc.perform(put("/api/herstellers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herstellerDTO)))
            .andExpect(status().isOk());

        // Validate the Hersteller in the database
        List<Hersteller> herstellerList = herstellerRepository.findAll();
        assertThat(herstellerList).hasSize(databaseSizeBeforeUpdate);
        Hersteller testHersteller = herstellerList.get(herstellerList.size() - 1);
        assertThat(testHersteller.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);
        assertThat(testHersteller.getGueltigBis()).isEqualTo(UPDATED_GUELTIG_BIS);

        // Validate the Hersteller in Elasticsearch
        verify(mockHerstellerSearchRepository, times(1)).save(testHersteller);
    }

    @Test
    @Transactional
    public void updateNonExistingHersteller() throws Exception {
        int databaseSizeBeforeUpdate = herstellerRepository.findAll().size();

        // Create the Hersteller
        HerstellerDTO herstellerDTO = herstellerMapper.toDto(hersteller);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHerstellerMockMvc.perform(put("/api/herstellers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herstellerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hersteller in the database
        List<Hersteller> herstellerList = herstellerRepository.findAll();
        assertThat(herstellerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hersteller in Elasticsearch
        verify(mockHerstellerSearchRepository, times(0)).save(hersteller);
    }

    @Test
    @Transactional
    public void deleteHersteller() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);

        int databaseSizeBeforeDelete = herstellerRepository.findAll().size();

        // Delete the hersteller
        restHerstellerMockMvc.perform(delete("/api/herstellers/{id}", hersteller.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hersteller> herstellerList = herstellerRepository.findAll();
        assertThat(herstellerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Hersteller in Elasticsearch
        verify(mockHerstellerSearchRepository, times(1)).deleteById(hersteller.getId());
    }

    @Test
    @Transactional
    public void searchHersteller() throws Exception {
        // Initialize the database
        herstellerRepository.saveAndFlush(hersteller);
        when(mockHerstellerSearchRepository.search(queryStringQuery("id:" + hersteller.getId())))
            .thenReturn(Collections.singletonList(hersteller));
        // Search the hersteller
        restHerstellerMockMvc.perform(get("/api/_search/herstellers?query=id:" + hersteller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hersteller.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));
    }
}
