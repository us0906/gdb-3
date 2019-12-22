package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Betreiber;
import de.kvb.eps.repository.BetreiberRepository;
import de.kvb.eps.repository.search.BetreiberSearchRepository;
import de.kvb.eps.service.BetreiberService;
import de.kvb.eps.service.dto.BetreiberDTO;
import de.kvb.eps.service.mapper.BetreiberMapper;
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
 * Integration tests for the {@link BetreiberResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class BetreiberResourceIT {

    private static final String DEFAULT_VORNAME = "AAAAAAAAAA";
    private static final String UPDATED_VORNAME = "BBBBBBBBBB";

    private static final String DEFAULT_NACHNAME = "AAAAAAAAAA";
    private static final String UPDATED_NACHNAME = "BBBBBBBBBB";

    private static final String DEFAULT_STRASSE = "AAAAAAAAAA";
    private static final String UPDATED_STRASSE = "BBBBBBBBBB";

    private static final String DEFAULT_HAUSNUMMER = "AAAAAAAAAA";
    private static final String UPDATED_HAUSNUMMER = "BBBBBBBBBB";

    private static final String DEFAULT_PLZ = "AAAAAAAAAA";
    private static final String UPDATED_PLZ = "BBBBBBBBBB";

    private static final String DEFAULT_ORT = "AAAAAAAAAA";
    private static final String UPDATED_ORT = "BBBBBBBBBB";

    @Autowired
    private BetreiberRepository betreiberRepository;

    @Autowired
    private BetreiberMapper betreiberMapper;

    @Autowired
    private BetreiberService betreiberService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.BetreiberSearchRepositoryMockConfiguration
     */
    @Autowired
    private BetreiberSearchRepository mockBetreiberSearchRepository;

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

    private MockMvc restBetreiberMockMvc;

    private Betreiber betreiber;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BetreiberResource betreiberResource = new BetreiberResource(betreiberService);
        this.restBetreiberMockMvc = MockMvcBuilders.standaloneSetup(betreiberResource)
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
    public static Betreiber createEntity(EntityManager em) {
        Betreiber betreiber = new Betreiber()
            .vorname(DEFAULT_VORNAME)
            .nachname(DEFAULT_NACHNAME)
            .strasse(DEFAULT_STRASSE)
            .hausnummer(DEFAULT_HAUSNUMMER)
            .plz(DEFAULT_PLZ)
            .ort(DEFAULT_ORT);
        return betreiber;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Betreiber createUpdatedEntity(EntityManager em) {
        Betreiber betreiber = new Betreiber()
            .vorname(UPDATED_VORNAME)
            .nachname(UPDATED_NACHNAME)
            .strasse(UPDATED_STRASSE)
            .hausnummer(UPDATED_HAUSNUMMER)
            .plz(UPDATED_PLZ)
            .ort(UPDATED_ORT);
        return betreiber;
    }

    @BeforeEach
    public void initTest() {
        betreiber = createEntity(em);
    }

    @Test
    @Transactional
    public void createBetreiber() throws Exception {
        int databaseSizeBeforeCreate = betreiberRepository.findAll().size();

        // Create the Betreiber
        BetreiberDTO betreiberDTO = betreiberMapper.toDto(betreiber);
        restBetreiberMockMvc.perform(post("/api/betreibers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betreiberDTO)))
            .andExpect(status().isCreated());

        // Validate the Betreiber in the database
        List<Betreiber> betreiberList = betreiberRepository.findAll();
        assertThat(betreiberList).hasSize(databaseSizeBeforeCreate + 1);
        Betreiber testBetreiber = betreiberList.get(betreiberList.size() - 1);
        assertThat(testBetreiber.getVorname()).isEqualTo(DEFAULT_VORNAME);
        assertThat(testBetreiber.getNachname()).isEqualTo(DEFAULT_NACHNAME);
        assertThat(testBetreiber.getStrasse()).isEqualTo(DEFAULT_STRASSE);
        assertThat(testBetreiber.getHausnummer()).isEqualTo(DEFAULT_HAUSNUMMER);
        assertThat(testBetreiber.getPlz()).isEqualTo(DEFAULT_PLZ);
        assertThat(testBetreiber.getOrt()).isEqualTo(DEFAULT_ORT);

        // Validate the Betreiber in Elasticsearch
        verify(mockBetreiberSearchRepository, times(1)).save(testBetreiber);
    }

    @Test
    @Transactional
    public void createBetreiberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = betreiberRepository.findAll().size();

        // Create the Betreiber with an existing ID
        betreiber.setId(1L);
        BetreiberDTO betreiberDTO = betreiberMapper.toDto(betreiber);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBetreiberMockMvc.perform(post("/api/betreibers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betreiberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Betreiber in the database
        List<Betreiber> betreiberList = betreiberRepository.findAll();
        assertThat(betreiberList).hasSize(databaseSizeBeforeCreate);

        // Validate the Betreiber in Elasticsearch
        verify(mockBetreiberSearchRepository, times(0)).save(betreiber);
    }


    @Test
    @Transactional
    public void checkVornameIsRequired() throws Exception {
        int databaseSizeBeforeTest = betreiberRepository.findAll().size();
        // set the field null
        betreiber.setVorname(null);

        // Create the Betreiber, which fails.
        BetreiberDTO betreiberDTO = betreiberMapper.toDto(betreiber);

        restBetreiberMockMvc.perform(post("/api/betreibers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betreiberDTO)))
            .andExpect(status().isBadRequest());

        List<Betreiber> betreiberList = betreiberRepository.findAll();
        assertThat(betreiberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNachnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = betreiberRepository.findAll().size();
        // set the field null
        betreiber.setNachname(null);

        // Create the Betreiber, which fails.
        BetreiberDTO betreiberDTO = betreiberMapper.toDto(betreiber);

        restBetreiberMockMvc.perform(post("/api/betreibers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betreiberDTO)))
            .andExpect(status().isBadRequest());

        List<Betreiber> betreiberList = betreiberRepository.findAll();
        assertThat(betreiberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBetreibers() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList
        restBetreiberMockMvc.perform(get("/api/betreibers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betreiber.getId().intValue())))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].hausnummer").value(hasItem(DEFAULT_HAUSNUMMER)))
            .andExpect(jsonPath("$.[*].plz").value(hasItem(DEFAULT_PLZ)))
            .andExpect(jsonPath("$.[*].ort").value(hasItem(DEFAULT_ORT)));
    }
    
    @Test
    @Transactional
    public void getBetreiber() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get the betreiber
        restBetreiberMockMvc.perform(get("/api/betreibers/{id}", betreiber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(betreiber.getId().intValue()))
            .andExpect(jsonPath("$.vorname").value(DEFAULT_VORNAME))
            .andExpect(jsonPath("$.nachname").value(DEFAULT_NACHNAME))
            .andExpect(jsonPath("$.strasse").value(DEFAULT_STRASSE))
            .andExpect(jsonPath("$.hausnummer").value(DEFAULT_HAUSNUMMER))
            .andExpect(jsonPath("$.plz").value(DEFAULT_PLZ))
            .andExpect(jsonPath("$.ort").value(DEFAULT_ORT));
    }

    @Test
    @Transactional
    public void getNonExistingBetreiber() throws Exception {
        // Get the betreiber
        restBetreiberMockMvc.perform(get("/api/betreibers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBetreiber() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        int databaseSizeBeforeUpdate = betreiberRepository.findAll().size();

        // Update the betreiber
        Betreiber updatedBetreiber = betreiberRepository.findById(betreiber.getId()).get();
        // Disconnect from session so that the updates on updatedBetreiber are not directly saved in db
        em.detach(updatedBetreiber);
        updatedBetreiber
            .vorname(UPDATED_VORNAME)
            .nachname(UPDATED_NACHNAME)
            .strasse(UPDATED_STRASSE)
            .hausnummer(UPDATED_HAUSNUMMER)
            .plz(UPDATED_PLZ)
            .ort(UPDATED_ORT);
        BetreiberDTO betreiberDTO = betreiberMapper.toDto(updatedBetreiber);

        restBetreiberMockMvc.perform(put("/api/betreibers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betreiberDTO)))
            .andExpect(status().isOk());

        // Validate the Betreiber in the database
        List<Betreiber> betreiberList = betreiberRepository.findAll();
        assertThat(betreiberList).hasSize(databaseSizeBeforeUpdate);
        Betreiber testBetreiber = betreiberList.get(betreiberList.size() - 1);
        assertThat(testBetreiber.getVorname()).isEqualTo(UPDATED_VORNAME);
        assertThat(testBetreiber.getNachname()).isEqualTo(UPDATED_NACHNAME);
        assertThat(testBetreiber.getStrasse()).isEqualTo(UPDATED_STRASSE);
        assertThat(testBetreiber.getHausnummer()).isEqualTo(UPDATED_HAUSNUMMER);
        assertThat(testBetreiber.getPlz()).isEqualTo(UPDATED_PLZ);
        assertThat(testBetreiber.getOrt()).isEqualTo(UPDATED_ORT);

        // Validate the Betreiber in Elasticsearch
        verify(mockBetreiberSearchRepository, times(1)).save(testBetreiber);
    }

    @Test
    @Transactional
    public void updateNonExistingBetreiber() throws Exception {
        int databaseSizeBeforeUpdate = betreiberRepository.findAll().size();

        // Create the Betreiber
        BetreiberDTO betreiberDTO = betreiberMapper.toDto(betreiber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBetreiberMockMvc.perform(put("/api/betreibers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betreiberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Betreiber in the database
        List<Betreiber> betreiberList = betreiberRepository.findAll();
        assertThat(betreiberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Betreiber in Elasticsearch
        verify(mockBetreiberSearchRepository, times(0)).save(betreiber);
    }

    @Test
    @Transactional
    public void deleteBetreiber() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        int databaseSizeBeforeDelete = betreiberRepository.findAll().size();

        // Delete the betreiber
        restBetreiberMockMvc.perform(delete("/api/betreibers/{id}", betreiber.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Betreiber> betreiberList = betreiberRepository.findAll();
        assertThat(betreiberList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Betreiber in Elasticsearch
        verify(mockBetreiberSearchRepository, times(1)).deleteById(betreiber.getId());
    }

    @Test
    @Transactional
    public void searchBetreiber() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);
        when(mockBetreiberSearchRepository.search(queryStringQuery("id:" + betreiber.getId())))
            .thenReturn(Collections.singletonList(betreiber));
        // Search the betreiber
        restBetreiberMockMvc.perform(get("/api/_search/betreibers?query=id:" + betreiber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betreiber.getId().intValue())))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].hausnummer").value(hasItem(DEFAULT_HAUSNUMMER)))
            .andExpect(jsonPath("$.[*].plz").value(hasItem(DEFAULT_PLZ)))
            .andExpect(jsonPath("$.[*].ort").value(hasItem(DEFAULT_ORT)));
    }
}
