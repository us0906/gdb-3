package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Betriebsstaette;
import de.kvb.eps.domain.Systeminstanz;
import de.kvb.eps.repository.BetriebsstaetteRepository;
import de.kvb.eps.repository.search.BetriebsstaetteSearchRepository;
import de.kvb.eps.service.BetriebsstaetteService;
import de.kvb.eps.service.dto.BetriebsstaetteDTO;
import de.kvb.eps.service.mapper.BetriebsstaetteMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.BetriebsstaetteCriteria;
import de.kvb.eps.service.BetriebsstaetteQueryService;

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
    private BetriebsstaetteQueryService betriebsstaetteQueryService;

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
        final BetriebsstaetteResource betriebsstaetteResource = new BetriebsstaetteResource(betriebsstaetteService, betriebsstaetteQueryService);
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
    public void getBetriebsstaettesByIdFiltering() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        Long id = betriebsstaette.getId();

        defaultBetriebsstaetteShouldBeFound("id.equals=" + id);
        defaultBetriebsstaetteShouldNotBeFound("id.notEquals=" + id);

        defaultBetriebsstaetteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBetriebsstaetteShouldNotBeFound("id.greaterThan=" + id);

        defaultBetriebsstaetteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBetriebsstaetteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBetriebsstaettesByBsnrIsEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where bsnr equals to DEFAULT_BSNR
        defaultBetriebsstaetteShouldBeFound("bsnr.equals=" + DEFAULT_BSNR);

        // Get all the betriebsstaetteList where bsnr equals to UPDATED_BSNR
        defaultBetriebsstaetteShouldNotBeFound("bsnr.equals=" + UPDATED_BSNR);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByBsnrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where bsnr not equals to DEFAULT_BSNR
        defaultBetriebsstaetteShouldNotBeFound("bsnr.notEquals=" + DEFAULT_BSNR);

        // Get all the betriebsstaetteList where bsnr not equals to UPDATED_BSNR
        defaultBetriebsstaetteShouldBeFound("bsnr.notEquals=" + UPDATED_BSNR);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByBsnrIsInShouldWork() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where bsnr in DEFAULT_BSNR or UPDATED_BSNR
        defaultBetriebsstaetteShouldBeFound("bsnr.in=" + DEFAULT_BSNR + "," + UPDATED_BSNR);

        // Get all the betriebsstaetteList where bsnr equals to UPDATED_BSNR
        defaultBetriebsstaetteShouldNotBeFound("bsnr.in=" + UPDATED_BSNR);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByBsnrIsNullOrNotNull() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where bsnr is not null
        defaultBetriebsstaetteShouldBeFound("bsnr.specified=true");

        // Get all the betriebsstaetteList where bsnr is null
        defaultBetriebsstaetteShouldNotBeFound("bsnr.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetriebsstaettesByBsnrContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where bsnr contains DEFAULT_BSNR
        defaultBetriebsstaetteShouldBeFound("bsnr.contains=" + DEFAULT_BSNR);

        // Get all the betriebsstaetteList where bsnr contains UPDATED_BSNR
        defaultBetriebsstaetteShouldNotBeFound("bsnr.contains=" + UPDATED_BSNR);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByBsnrNotContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where bsnr does not contain DEFAULT_BSNR
        defaultBetriebsstaetteShouldNotBeFound("bsnr.doesNotContain=" + DEFAULT_BSNR);

        // Get all the betriebsstaetteList where bsnr does not contain UPDATED_BSNR
        defaultBetriebsstaetteShouldBeFound("bsnr.doesNotContain=" + UPDATED_BSNR);
    }


    @Test
    @Transactional
    public void getAllBetriebsstaettesByStrasseIsEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where strasse equals to DEFAULT_STRASSE
        defaultBetriebsstaetteShouldBeFound("strasse.equals=" + DEFAULT_STRASSE);

        // Get all the betriebsstaetteList where strasse equals to UPDATED_STRASSE
        defaultBetriebsstaetteShouldNotBeFound("strasse.equals=" + UPDATED_STRASSE);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByStrasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where strasse not equals to DEFAULT_STRASSE
        defaultBetriebsstaetteShouldNotBeFound("strasse.notEquals=" + DEFAULT_STRASSE);

        // Get all the betriebsstaetteList where strasse not equals to UPDATED_STRASSE
        defaultBetriebsstaetteShouldBeFound("strasse.notEquals=" + UPDATED_STRASSE);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByStrasseIsInShouldWork() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where strasse in DEFAULT_STRASSE or UPDATED_STRASSE
        defaultBetriebsstaetteShouldBeFound("strasse.in=" + DEFAULT_STRASSE + "," + UPDATED_STRASSE);

        // Get all the betriebsstaetteList where strasse equals to UPDATED_STRASSE
        defaultBetriebsstaetteShouldNotBeFound("strasse.in=" + UPDATED_STRASSE);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByStrasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where strasse is not null
        defaultBetriebsstaetteShouldBeFound("strasse.specified=true");

        // Get all the betriebsstaetteList where strasse is null
        defaultBetriebsstaetteShouldNotBeFound("strasse.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetriebsstaettesByStrasseContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where strasse contains DEFAULT_STRASSE
        defaultBetriebsstaetteShouldBeFound("strasse.contains=" + DEFAULT_STRASSE);

        // Get all the betriebsstaetteList where strasse contains UPDATED_STRASSE
        defaultBetriebsstaetteShouldNotBeFound("strasse.contains=" + UPDATED_STRASSE);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByStrasseNotContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where strasse does not contain DEFAULT_STRASSE
        defaultBetriebsstaetteShouldNotBeFound("strasse.doesNotContain=" + DEFAULT_STRASSE);

        // Get all the betriebsstaetteList where strasse does not contain UPDATED_STRASSE
        defaultBetriebsstaetteShouldBeFound("strasse.doesNotContain=" + UPDATED_STRASSE);
    }


    @Test
    @Transactional
    public void getAllBetriebsstaettesByHausnummerIsEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where hausnummer equals to DEFAULT_HAUSNUMMER
        defaultBetriebsstaetteShouldBeFound("hausnummer.equals=" + DEFAULT_HAUSNUMMER);

        // Get all the betriebsstaetteList where hausnummer equals to UPDATED_HAUSNUMMER
        defaultBetriebsstaetteShouldNotBeFound("hausnummer.equals=" + UPDATED_HAUSNUMMER);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByHausnummerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where hausnummer not equals to DEFAULT_HAUSNUMMER
        defaultBetriebsstaetteShouldNotBeFound("hausnummer.notEquals=" + DEFAULT_HAUSNUMMER);

        // Get all the betriebsstaetteList where hausnummer not equals to UPDATED_HAUSNUMMER
        defaultBetriebsstaetteShouldBeFound("hausnummer.notEquals=" + UPDATED_HAUSNUMMER);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByHausnummerIsInShouldWork() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where hausnummer in DEFAULT_HAUSNUMMER or UPDATED_HAUSNUMMER
        defaultBetriebsstaetteShouldBeFound("hausnummer.in=" + DEFAULT_HAUSNUMMER + "," + UPDATED_HAUSNUMMER);

        // Get all the betriebsstaetteList where hausnummer equals to UPDATED_HAUSNUMMER
        defaultBetriebsstaetteShouldNotBeFound("hausnummer.in=" + UPDATED_HAUSNUMMER);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByHausnummerIsNullOrNotNull() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where hausnummer is not null
        defaultBetriebsstaetteShouldBeFound("hausnummer.specified=true");

        // Get all the betriebsstaetteList where hausnummer is null
        defaultBetriebsstaetteShouldNotBeFound("hausnummer.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetriebsstaettesByHausnummerContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where hausnummer contains DEFAULT_HAUSNUMMER
        defaultBetriebsstaetteShouldBeFound("hausnummer.contains=" + DEFAULT_HAUSNUMMER);

        // Get all the betriebsstaetteList where hausnummer contains UPDATED_HAUSNUMMER
        defaultBetriebsstaetteShouldNotBeFound("hausnummer.contains=" + UPDATED_HAUSNUMMER);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByHausnummerNotContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where hausnummer does not contain DEFAULT_HAUSNUMMER
        defaultBetriebsstaetteShouldNotBeFound("hausnummer.doesNotContain=" + DEFAULT_HAUSNUMMER);

        // Get all the betriebsstaetteList where hausnummer does not contain UPDATED_HAUSNUMMER
        defaultBetriebsstaetteShouldBeFound("hausnummer.doesNotContain=" + UPDATED_HAUSNUMMER);
    }


    @Test
    @Transactional
    public void getAllBetriebsstaettesByPlzIsEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where plz equals to DEFAULT_PLZ
        defaultBetriebsstaetteShouldBeFound("plz.equals=" + DEFAULT_PLZ);

        // Get all the betriebsstaetteList where plz equals to UPDATED_PLZ
        defaultBetriebsstaetteShouldNotBeFound("plz.equals=" + UPDATED_PLZ);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByPlzIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where plz not equals to DEFAULT_PLZ
        defaultBetriebsstaetteShouldNotBeFound("plz.notEquals=" + DEFAULT_PLZ);

        // Get all the betriebsstaetteList where plz not equals to UPDATED_PLZ
        defaultBetriebsstaetteShouldBeFound("plz.notEquals=" + UPDATED_PLZ);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByPlzIsInShouldWork() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where plz in DEFAULT_PLZ or UPDATED_PLZ
        defaultBetriebsstaetteShouldBeFound("plz.in=" + DEFAULT_PLZ + "," + UPDATED_PLZ);

        // Get all the betriebsstaetteList where plz equals to UPDATED_PLZ
        defaultBetriebsstaetteShouldNotBeFound("plz.in=" + UPDATED_PLZ);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByPlzIsNullOrNotNull() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where plz is not null
        defaultBetriebsstaetteShouldBeFound("plz.specified=true");

        // Get all the betriebsstaetteList where plz is null
        defaultBetriebsstaetteShouldNotBeFound("plz.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetriebsstaettesByPlzContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where plz contains DEFAULT_PLZ
        defaultBetriebsstaetteShouldBeFound("plz.contains=" + DEFAULT_PLZ);

        // Get all the betriebsstaetteList where plz contains UPDATED_PLZ
        defaultBetriebsstaetteShouldNotBeFound("plz.contains=" + UPDATED_PLZ);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByPlzNotContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where plz does not contain DEFAULT_PLZ
        defaultBetriebsstaetteShouldNotBeFound("plz.doesNotContain=" + DEFAULT_PLZ);

        // Get all the betriebsstaetteList where plz does not contain UPDATED_PLZ
        defaultBetriebsstaetteShouldBeFound("plz.doesNotContain=" + UPDATED_PLZ);
    }


    @Test
    @Transactional
    public void getAllBetriebsstaettesByOrtIsEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where ort equals to DEFAULT_ORT
        defaultBetriebsstaetteShouldBeFound("ort.equals=" + DEFAULT_ORT);

        // Get all the betriebsstaetteList where ort equals to UPDATED_ORT
        defaultBetriebsstaetteShouldNotBeFound("ort.equals=" + UPDATED_ORT);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByOrtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where ort not equals to DEFAULT_ORT
        defaultBetriebsstaetteShouldNotBeFound("ort.notEquals=" + DEFAULT_ORT);

        // Get all the betriebsstaetteList where ort not equals to UPDATED_ORT
        defaultBetriebsstaetteShouldBeFound("ort.notEquals=" + UPDATED_ORT);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByOrtIsInShouldWork() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where ort in DEFAULT_ORT or UPDATED_ORT
        defaultBetriebsstaetteShouldBeFound("ort.in=" + DEFAULT_ORT + "," + UPDATED_ORT);

        // Get all the betriebsstaetteList where ort equals to UPDATED_ORT
        defaultBetriebsstaetteShouldNotBeFound("ort.in=" + UPDATED_ORT);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByOrtIsNullOrNotNull() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where ort is not null
        defaultBetriebsstaetteShouldBeFound("ort.specified=true");

        // Get all the betriebsstaetteList where ort is null
        defaultBetriebsstaetteShouldNotBeFound("ort.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetriebsstaettesByOrtContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where ort contains DEFAULT_ORT
        defaultBetriebsstaetteShouldBeFound("ort.contains=" + DEFAULT_ORT);

        // Get all the betriebsstaetteList where ort contains UPDATED_ORT
        defaultBetriebsstaetteShouldNotBeFound("ort.contains=" + UPDATED_ORT);
    }

    @Test
    @Transactional
    public void getAllBetriebsstaettesByOrtNotContainsSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);

        // Get all the betriebsstaetteList where ort does not contain DEFAULT_ORT
        defaultBetriebsstaetteShouldNotBeFound("ort.doesNotContain=" + DEFAULT_ORT);

        // Get all the betriebsstaetteList where ort does not contain UPDATED_ORT
        defaultBetriebsstaetteShouldBeFound("ort.doesNotContain=" + UPDATED_ORT);
    }


    @Test
    @Transactional
    public void getAllBetriebsstaettesBySysteminstanzIsEqualToSomething() throws Exception {
        // Initialize the database
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);
        Systeminstanz systeminstanz = SysteminstanzResourceIT.createEntity(em);
        em.persist(systeminstanz);
        em.flush();
        betriebsstaette.addSysteminstanz(systeminstanz);
        betriebsstaetteRepository.saveAndFlush(betriebsstaette);
        Long systeminstanzId = systeminstanz.getId();

        // Get all the betriebsstaetteList where systeminstanz equals to systeminstanzId
        defaultBetriebsstaetteShouldBeFound("systeminstanzId.equals=" + systeminstanzId);

        // Get all the betriebsstaetteList where systeminstanz equals to systeminstanzId + 1
        defaultBetriebsstaetteShouldNotBeFound("systeminstanzId.equals=" + (systeminstanzId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBetriebsstaetteShouldBeFound(String filter) throws Exception {
        restBetriebsstaetteMockMvc.perform(get("/api/betriebsstaettes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betriebsstaette.getId().intValue())))
            .andExpect(jsonPath("$.[*].bsnr").value(hasItem(DEFAULT_BSNR)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].hausnummer").value(hasItem(DEFAULT_HAUSNUMMER)))
            .andExpect(jsonPath("$.[*].plz").value(hasItem(DEFAULT_PLZ)))
            .andExpect(jsonPath("$.[*].ort").value(hasItem(DEFAULT_ORT)));

        // Check, that the count call also returns 1
        restBetriebsstaetteMockMvc.perform(get("/api/betriebsstaettes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBetriebsstaetteShouldNotBeFound(String filter) throws Exception {
        restBetriebsstaetteMockMvc.perform(get("/api/betriebsstaettes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBetriebsstaetteMockMvc.perform(get("/api/betriebsstaettes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
