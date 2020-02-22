package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Systemtyp;
import de.kvb.eps.domain.Systeminstanz;
import de.kvb.eps.domain.Geraet;
import de.kvb.eps.domain.Zubehoer;
import de.kvb.eps.repository.SystemtypRepository;
import de.kvb.eps.repository.search.SystemtypSearchRepository;
import de.kvb.eps.service.SystemtypService;
import de.kvb.eps.service.dto.SystemtypDTO;
import de.kvb.eps.service.mapper.SystemtypMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.SystemtypCriteria;
import de.kvb.eps.service.SystemtypQueryService;

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
    private static final LocalDate SMALLER_GUELTIG_BIS = LocalDate.ofEpochDay(-1L);

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
    private SystemtypQueryService systemtypQueryService;

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
        final SystemtypResource systemtypResource = new SystemtypResource(systemtypService, systemtypQueryService);
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
            .contentType(TestUtil.APPLICATION_JSON)
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
            .contentType(TestUtil.APPLICATION_JSON)
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
            .contentType(TestUtil.APPLICATION_JSON)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemtyp.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()));
    }


    @Test
    @Transactional
    public void getSystemtypsByIdFiltering() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        Long id = systemtyp.getId();

        defaultSystemtypShouldBeFound("id.equals=" + id);
        defaultSystemtypShouldNotBeFound("id.notEquals=" + id);

        defaultSystemtypShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSystemtypShouldNotBeFound("id.greaterThan=" + id);

        defaultSystemtypShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSystemtypShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSystemtypsByBezeichnungIsEqualToSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where bezeichnung equals to DEFAULT_BEZEICHNUNG
        defaultSystemtypShouldBeFound("bezeichnung.equals=" + DEFAULT_BEZEICHNUNG);

        // Get all the systemtypList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultSystemtypShouldNotBeFound("bezeichnung.equals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByBezeichnungIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where bezeichnung not equals to DEFAULT_BEZEICHNUNG
        defaultSystemtypShouldNotBeFound("bezeichnung.notEquals=" + DEFAULT_BEZEICHNUNG);

        // Get all the systemtypList where bezeichnung not equals to UPDATED_BEZEICHNUNG
        defaultSystemtypShouldBeFound("bezeichnung.notEquals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByBezeichnungIsInShouldWork() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where bezeichnung in DEFAULT_BEZEICHNUNG or UPDATED_BEZEICHNUNG
        defaultSystemtypShouldBeFound("bezeichnung.in=" + DEFAULT_BEZEICHNUNG + "," + UPDATED_BEZEICHNUNG);

        // Get all the systemtypList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultSystemtypShouldNotBeFound("bezeichnung.in=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByBezeichnungIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where bezeichnung is not null
        defaultSystemtypShouldBeFound("bezeichnung.specified=true");

        // Get all the systemtypList where bezeichnung is null
        defaultSystemtypShouldNotBeFound("bezeichnung.specified=false");
    }
                @Test
    @Transactional
    public void getAllSystemtypsByBezeichnungContainsSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where bezeichnung contains DEFAULT_BEZEICHNUNG
        defaultSystemtypShouldBeFound("bezeichnung.contains=" + DEFAULT_BEZEICHNUNG);

        // Get all the systemtypList where bezeichnung contains UPDATED_BEZEICHNUNG
        defaultSystemtypShouldNotBeFound("bezeichnung.contains=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByBezeichnungNotContainsSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where bezeichnung does not contain DEFAULT_BEZEICHNUNG
        defaultSystemtypShouldNotBeFound("bezeichnung.doesNotContain=" + DEFAULT_BEZEICHNUNG);

        // Get all the systemtypList where bezeichnung does not contain UPDATED_BEZEICHNUNG
        defaultSystemtypShouldBeFound("bezeichnung.doesNotContain=" + UPDATED_BEZEICHNUNG);
    }


    @Test
    @Transactional
    public void getAllSystemtypsByGueltigBisIsEqualToSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where gueltigBis equals to DEFAULT_GUELTIG_BIS
        defaultSystemtypShouldBeFound("gueltigBis.equals=" + DEFAULT_GUELTIG_BIS);

        // Get all the systemtypList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultSystemtypShouldNotBeFound("gueltigBis.equals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByGueltigBisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where gueltigBis not equals to DEFAULT_GUELTIG_BIS
        defaultSystemtypShouldNotBeFound("gueltigBis.notEquals=" + DEFAULT_GUELTIG_BIS);

        // Get all the systemtypList where gueltigBis not equals to UPDATED_GUELTIG_BIS
        defaultSystemtypShouldBeFound("gueltigBis.notEquals=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByGueltigBisIsInShouldWork() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where gueltigBis in DEFAULT_GUELTIG_BIS or UPDATED_GUELTIG_BIS
        defaultSystemtypShouldBeFound("gueltigBis.in=" + DEFAULT_GUELTIG_BIS + "," + UPDATED_GUELTIG_BIS);

        // Get all the systemtypList where gueltigBis equals to UPDATED_GUELTIG_BIS
        defaultSystemtypShouldNotBeFound("gueltigBis.in=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByGueltigBisIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where gueltigBis is not null
        defaultSystemtypShouldBeFound("gueltigBis.specified=true");

        // Get all the systemtypList where gueltigBis is null
        defaultSystemtypShouldNotBeFound("gueltigBis.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemtypsByGueltigBisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where gueltigBis is greater than or equal to DEFAULT_GUELTIG_BIS
        defaultSystemtypShouldBeFound("gueltigBis.greaterThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the systemtypList where gueltigBis is greater than or equal to UPDATED_GUELTIG_BIS
        defaultSystemtypShouldNotBeFound("gueltigBis.greaterThanOrEqual=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByGueltigBisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where gueltigBis is less than or equal to DEFAULT_GUELTIG_BIS
        defaultSystemtypShouldBeFound("gueltigBis.lessThanOrEqual=" + DEFAULT_GUELTIG_BIS);

        // Get all the systemtypList where gueltigBis is less than or equal to SMALLER_GUELTIG_BIS
        defaultSystemtypShouldNotBeFound("gueltigBis.lessThanOrEqual=" + SMALLER_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByGueltigBisIsLessThanSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where gueltigBis is less than DEFAULT_GUELTIG_BIS
        defaultSystemtypShouldNotBeFound("gueltigBis.lessThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the systemtypList where gueltigBis is less than UPDATED_GUELTIG_BIS
        defaultSystemtypShouldBeFound("gueltigBis.lessThan=" + UPDATED_GUELTIG_BIS);
    }

    @Test
    @Transactional
    public void getAllSystemtypsByGueltigBisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);

        // Get all the systemtypList where gueltigBis is greater than DEFAULT_GUELTIG_BIS
        defaultSystemtypShouldNotBeFound("gueltigBis.greaterThan=" + DEFAULT_GUELTIG_BIS);

        // Get all the systemtypList where gueltigBis is greater than SMALLER_GUELTIG_BIS
        defaultSystemtypShouldBeFound("gueltigBis.greaterThan=" + SMALLER_GUELTIG_BIS);
    }


    @Test
    @Transactional
    public void getAllSystemtypsBySysteminstanzIsEqualToSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);
        Systeminstanz systeminstanz = SysteminstanzResourceIT.createEntity(em);
        em.persist(systeminstanz);
        em.flush();
        systemtyp.addSysteminstanz(systeminstanz);
        systemtypRepository.saveAndFlush(systemtyp);
        Long systeminstanzId = systeminstanz.getId();

        // Get all the systemtypList where systeminstanz equals to systeminstanzId
        defaultSystemtypShouldBeFound("systeminstanzId.equals=" + systeminstanzId);

        // Get all the systemtypList where systeminstanz equals to systeminstanzId + 1
        defaultSystemtypShouldNotBeFound("systeminstanzId.equals=" + (systeminstanzId + 1));
    }


    @Test
    @Transactional
    public void getAllSystemtypsByGeraetIsEqualToSomething() throws Exception {
        // Get already existing entity
        Geraet geraet = systemtyp.getGeraet();
        systemtypRepository.saveAndFlush(systemtyp);
        Long geraetId = geraet.getId();

        // Get all the systemtypList where geraet equals to geraetId
        defaultSystemtypShouldBeFound("geraetId.equals=" + geraetId);

        // Get all the systemtypList where geraet equals to geraetId + 1
        defaultSystemtypShouldNotBeFound("geraetId.equals=" + (geraetId + 1));
    }


    @Test
    @Transactional
    public void getAllSystemtypsByZubehoerIsEqualToSomething() throws Exception {
        // Initialize the database
        systemtypRepository.saveAndFlush(systemtyp);
        Zubehoer zubehoer = ZubehoerResourceIT.createEntity(em);
        em.persist(zubehoer);
        em.flush();
        systemtyp.setZubehoer(zubehoer);
        systemtypRepository.saveAndFlush(systemtyp);
        Long zubehoerId = zubehoer.getId();

        // Get all the systemtypList where zubehoer equals to zubehoerId
        defaultSystemtypShouldBeFound("zubehoerId.equals=" + zubehoerId);

        // Get all the systemtypList where zubehoer equals to zubehoerId + 1
        defaultSystemtypShouldNotBeFound("zubehoerId.equals=" + (zubehoerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemtypShouldBeFound(String filter) throws Exception {
        restSystemtypMockMvc.perform(get("/api/systemtyps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemtyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));

        // Check, that the count call also returns 1
        restSystemtypMockMvc.perform(get("/api/systemtyps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemtypShouldNotBeFound(String filter) throws Exception {
        restSystemtypMockMvc.perform(get("/api/systemtyps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemtypMockMvc.perform(get("/api/systemtyps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .contentType(TestUtil.APPLICATION_JSON)
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
            .contentType(TestUtil.APPLICATION_JSON)
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
            .accept(TestUtil.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemtyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())));
    }
}
