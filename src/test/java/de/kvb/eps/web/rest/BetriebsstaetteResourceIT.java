package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Betriebsstaette;
import de.kvb.eps.repository.BetriebsstaetteRepository;
import de.kvb.eps.repository.search.BetriebsstaetteSearchRepository;
import de.kvb.eps.service.BetriebsstaetteService;
import de.kvb.eps.service.dto.BetriebsstaetteDTO;
import de.kvb.eps.service.mapper.BetriebsstaetteMapper;
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
 * Integration tests for the {@link BetriebsstaetteResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class BetriebsstaetteResourceIT {

    private static final String DEFAULT_BSNR = "AAAAAAAAAA";
    private static final String UPDATED_BSNR = "BBBBBBBBBB";

    private static final String DEFAULT_STRASSE = "AAAAAAAAAA";
    private static final String UPDATED_STRASSE = "BBBBBBBBBB";

    private static final String DEFAULT_HAUSNUMMER = "AAAAAAAAAA";
    private static final String UPDATED_HAUSNUMMER = "BBBBBBBBBB";

    private static final String DEFAULT_PLZ = "AAAAAAAAAA";
    private static final String UPDATED_PLZ = "BBBBBBBBBB";

    private static final String DEFAULT_ORT = "AAAAAAAAAA";
    private static final String UPDATED_ORT = "BBBBBBBBBB";

    @Autowired
    private BetriebsstaetteRepository betriebsstaetteRepository;

    @Autowired
    private BetriebsstaetteMapper betriebsstaetteMapper;

    @Autowired
    private BetriebsstaetteService betriebsstaetteService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.BetriebsstaetteSearchRepositoryMockConfiguration
     */
    @Autowired
    private BetriebsstaetteSearchRepository mockBetriebsstaetteSearchRepository;

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

    private MockMvc restBetriebsstaetteMockMvc;

    private Betriebsstaette betriebsstaette;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BetriebsstaetteResource betriebsstaetteResource = new BetriebsstaetteResource(betriebsstaetteService);
        this.restBetriebsstaetteMockMvc = MockMvcBuilders.standaloneSetup(betriebsstaetteResource)
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
    public static Betriebsstaette createEntity(EntityManager em) {
        Betriebsstaette betriebsstaette = new Betriebsstaette()
            .bsnr(DEFAULT_BSNR)
            .strasse(DEFAULT_STRASSE)
            .hausnummer(DEFAULT_HAUSNUMMER)
            .plz(DEFAULT_PLZ)
            .ort(DEFAULT_ORT);
        return betriebsstaette;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Betriebsstaette createUpdatedEntity(EntityManager em) {
        Betriebsstaette betriebsstaette = new Betriebsstaette()
            .bsnr(UPDATED_BSNR)
            .strasse(UPDATED_STRASSE)
            .hausnummer(UPDATED_HAUSNUMMER)
            .plz(UPDATED_PLZ)
            .ort(UPDATED_ORT);
        return betriebsstaette;
    }

    @BeforeEach
    public void initTest() {
        betriebsstaette = createEntity(em);
    }

    @Test
    @Transactional
    public void createBetriebsstaette() throws Exception {
        int databaseSizeBeforeCreate = betriebsstaetteRepository.findAll().size();

        // Create the Betriebsstaette
        BetriebsstaetteDTO betriebsstaetteDTO = betriebsstaetteMapper.toDto(betriebsstaette);
        restBetriebsstaetteMockMvc.perform(post("/api/betriebsstaettes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betriebsstaetteDTO)))
            .andExpect(status().isCreated());

        // Validate the Betriebsstaette in the database
        List<Betriebsstaette> betriebsstaetteList = betriebsstaetteRepository.findAll();
        assertThat(betriebsstaetteList).hasSize(databaseSizeBeforeCreate + 1);
        Betriebsstaette testBetriebsstaette = betriebsstaetteList.get(betriebsstaetteList.size() - 1);
        assertThat(testBetriebsstaette.getBsnr()).isEqualTo(DEFAULT_BSNR);
        assertThat(testBetriebsstaette.getStrasse()).isEqualTo(DEFAULT_STRASSE);
        assertThat(testBetriebsstaette.getHausnummer()).isEqualTo(DEFAULT_HAUSNUMMER);
        assertThat(testBetriebsstaette.getPlz()).isEqualTo(DEFAULT_PLZ);
        assertThat(testBetriebsstaette.getOrt()).isEqualTo(DEFAULT_ORT);

        // Validate the Betriebsstaette in Elasticsearch
        verify(mockBetriebsstaetteSearchRepository, times(1)).save(testBetriebsstaette);
    }

    @Test
    @Transactional
    public void createBetriebsstaetteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = betriebsstaetteRepository.findAll().size();

        // Create the Betriebsstaette with an existing ID
        betriebsstaette.setId(1L);
        BetriebsstaetteDTO betriebsstaetteDTO = betriebsstaetteMapper.toDto(betriebsstaette);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBetriebsstaetteMockMvc.perform(post("/api/betriebsstaettes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betriebsstaetteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Betriebsstaette in the database
        List<Betriebsstaette> betriebsstaetteList = betriebsstaetteRepository.findAll();
        assertThat(betriebsstaetteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Betriebsstaette in Elasticsearch
        verify(mockBetriebsstaetteSearchRepository, times(0)).save(betriebsstaette);
    }


    @Test
    @Transactional
    public void getAllBetriebsstaettes() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList
        restBetriebsstaetteMockMvc.perform(get("/api/betriebsstaettes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betriebsstaette.getId().intValue())))
            .andExpect(jsonPath("$.[*].bsnr").value(hasItem(DEFAULT_BSNR)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].hausnummer").value(hasItem(DEFAULT_HAUSNUMMER)))
            .andExpect(jsonPath("$.[*].plz").value(hasItem(DEFAULT_PLZ)))
            .andExpect(jsonPath("$.[*].ort").value(hasItem(DEFAULT_ORT)));
    }
    
    @Test
    @Transactional
    public void getBetriebsstaette() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get the betriebsstaette
        restBetriebsstaetteMockMvc.perform(get("/api/betriebsstaettes/{id}", betriebsstaette.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(betriebsstaette.getId().intValue()))
            .andExpect(jsonPath("$.bsnr").value(DEFAULT_BSNR))
            .andExpect(jsonPath("$.strasse").value(DEFAULT_STRASSE))
            .andExpect(jsonPath("$.hausnummer").value(DEFAULT_HAUSNUMMER))
            .andExpect(jsonPath("$.plz").value(DEFAULT_PLZ))
            .andExpect(jsonPath("$.ort").value(DEFAULT_ORT));
    }

    @Test
    @Transactional
    public void getNonExistingBetriebsstaette() throws Exception {
        // Get the betriebsstaette
        restBetriebsstaetteMockMvc.perform(get("/api/betriebsstaettes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBetriebsstaette() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        int databaseSizeBeforeUpdate = betriebsstaetteRepository.findAll().size();

        // Update the betriebsstaette
        Betriebsstaette updatedBetriebsstaette = betriebsstaetteRepository.findById(betriebsstaette.getId()).get();
        // Disconnect from session so that the updates on updatedBetriebsstaette are not directly saved in db
        em.detach(updatedBetriebsstaette);
        updatedBetriebsstaette
            .bsnr(UPDATED_BSNR)
            .strasse(UPDATED_STRASSE)
            .hausnummer(UPDATED_HAUSNUMMER)
            .plz(UPDATED_PLZ)
            .ort(UPDATED_ORT);
        BetriebsstaetteDTO betriebsstaetteDTO = betriebsstaetteMapper.toDto(updatedBetriebsstaette);

        restBetriebsstaetteMockMvc.perform(put("/api/betriebsstaettes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betriebsstaetteDTO)))
            .andExpect(status().isOk());

        // Validate the Betriebsstaette in the database
        List<Betriebsstaette> betriebsstaetteList = betriebsstaetteRepository.findAll();
        assertThat(betriebsstaetteList).hasSize(databaseSizeBeforeUpdate);
        Betriebsstaette testBetriebsstaette = betriebsstaetteList.get(betriebsstaetteList.size() - 1);
        assertThat(testBetriebsstaette.getBsnr()).isEqualTo(UPDATED_BSNR);
        assertThat(testBetriebsstaette.getStrasse()).isEqualTo(UPDATED_STRASSE);
        assertThat(testBetriebsstaette.getHausnummer()).isEqualTo(UPDATED_HAUSNUMMER);
        assertThat(testBetriebsstaette.getPlz()).isEqualTo(UPDATED_PLZ);
        assertThat(testBetriebsstaette.getOrt()).isEqualTo(UPDATED_ORT);

        // Validate the Betriebsstaette in Elasticsearch
        verify(mockBetriebsstaetteSearchRepository, times(1)).save(testBetriebsstaette);
    }

    @Test
    @Transactional
    public void updateNonExistingBetriebsstaette() throws Exception {
        int databaseSizeBeforeUpdate = betriebsstaetteRepository.findAll().size();

        // Create the Betriebsstaette
        BetriebsstaetteDTO betriebsstaetteDTO = betriebsstaetteMapper.toDto(betriebsstaette);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBetriebsstaetteMockMvc.perform(put("/api/betriebsstaettes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betriebsstaetteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Betriebsstaette in the database
        List<Betriebsstaette> betriebsstaetteList = betriebsstaetteRepository.findAll();
        assertThat(betriebsstaetteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Betriebsstaette in Elasticsearch
        verify(mockBetriebsstaetteSearchRepository, times(0)).save(betriebsstaette);
    }

    @Test
    @Transactional
    public void deleteBetriebsstaette() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        int databaseSizeBeforeDelete = betriebsstaetteRepository.findAll().size();

        // Delete the betriebsstaette
        restBetriebsstaetteMockMvc.perform(delete("/api/betriebsstaettes/{id}", betriebsstaette.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Betriebsstaette> betriebsstaetteList = betriebsstaetteRepository.findAll();
        assertThat(betriebsstaetteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Betriebsstaette in Elasticsearch
        verify(mockBetriebsstaetteSearchRepository, times(1)).deleteById(betriebsstaette.getId());
    }

    @Test
    @Transactional
    public void searchBetriebsstaette() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);
        when(mockBetriebsstaetteSearchRepository.search(queryStringQuery("id:" + betriebsstaette.getId())))
            .thenReturn(Collections.singletonList(betriebsstaette));
        // Search the betriebsstaette
        restBetriebsstaetteMockMvc.perform(get("/api/_search/betriebsstaettes?query=id:" + betriebsstaette.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betriebsstaette.getId().intValue())))
            .andExpect(jsonPath("$.[*].bsnr").value(hasItem(DEFAULT_BSNR)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].hausnummer").value(hasItem(DEFAULT_HAUSNUMMER)))
            .andExpect(jsonPath("$.[*].plz").value(hasItem(DEFAULT_PLZ)))
            .andExpect(jsonPath("$.[*].ort").value(hasItem(DEFAULT_ORT)));
    }
}
