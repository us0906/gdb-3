package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Betreiber;
import de.kvb.eps.domain.Systeminstanz;
import de.kvb.eps.repository.BetreiberRepository;
import de.kvb.eps.repository.search.BetreiberSearchRepository;
import de.kvb.eps.service.BetreiberService;
import de.kvb.eps.service.dto.BetreiberDTO;
import de.kvb.eps.service.mapper.BetreiberMapper;
import de.kvb.eps.web.rest.errors.ExceptionTranslator;
import de.kvb.eps.service.dto.BetreiberCriteria;
import de.kvb.eps.service.BetreiberQueryService;

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

    private static final String DEFAULT_BEZEICHNUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEZEICHNUNG = "BBBBBBBBBB";

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
    private BetreiberQueryService betreiberQueryService;

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
        final BetreiberResource betreiberResource = new BetreiberResource(betreiberService, betreiberQueryService);
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
            .ort(DEFAULT_ORT)
            .bezeichnung(DEFAULT_BEZEICHNUNG);
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
            .ort(UPDATED_ORT)
            .bezeichnung(UPDATED_BEZEICHNUNG);
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
            .contentType(TestUtil.APPLICATION_JSON)
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
        assertThat(testBetreiber.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);

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
            .contentType(TestUtil.APPLICATION_JSON)
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
            .contentType(TestUtil.APPLICATION_JSON)
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
            .contentType(TestUtil.APPLICATION_JSON)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betreiber.getId().intValue())))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].hausnummer").value(hasItem(DEFAULT_HAUSNUMMER)))
            .andExpect(jsonPath("$.[*].plz").value(hasItem(DEFAULT_PLZ)))
            .andExpect(jsonPath("$.[*].ort").value(hasItem(DEFAULT_ORT)))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)));
    }
    
    @Test
    @Transactional
    public void getBetreiber() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get the betreiber
        restBetreiberMockMvc.perform(get("/api/betreibers/{id}", betreiber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(betreiber.getId().intValue()))
            .andExpect(jsonPath("$.vorname").value(DEFAULT_VORNAME))
            .andExpect(jsonPath("$.nachname").value(DEFAULT_NACHNAME))
            .andExpect(jsonPath("$.strasse").value(DEFAULT_STRASSE))
            .andExpect(jsonPath("$.hausnummer").value(DEFAULT_HAUSNUMMER))
            .andExpect(jsonPath("$.plz").value(DEFAULT_PLZ))
            .andExpect(jsonPath("$.ort").value(DEFAULT_ORT))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG));
    }


    @Test
    @Transactional
    public void getBetreibersByIdFiltering() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        Long id = betreiber.getId();

        defaultBetreiberShouldBeFound("id.equals=" + id);
        defaultBetreiberShouldNotBeFound("id.notEquals=" + id);

        defaultBetreiberShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBetreiberShouldNotBeFound("id.greaterThan=" + id);

        defaultBetreiberShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBetreiberShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBetreibersByVornameIsEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where vorname equals to DEFAULT_VORNAME
        defaultBetreiberShouldBeFound("vorname.equals=" + DEFAULT_VORNAME);

        // Get all the betreiberList where vorname equals to UPDATED_VORNAME
        defaultBetreiberShouldNotBeFound("vorname.equals=" + UPDATED_VORNAME);
    }

    @Test
    @Transactional
    public void getAllBetreibersByVornameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where vorname not equals to DEFAULT_VORNAME
        defaultBetreiberShouldNotBeFound("vorname.notEquals=" + DEFAULT_VORNAME);

        // Get all the betreiberList where vorname not equals to UPDATED_VORNAME
        defaultBetreiberShouldBeFound("vorname.notEquals=" + UPDATED_VORNAME);
    }

    @Test
    @Transactional
    public void getAllBetreibersByVornameIsInShouldWork() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where vorname in DEFAULT_VORNAME or UPDATED_VORNAME
        defaultBetreiberShouldBeFound("vorname.in=" + DEFAULT_VORNAME + "," + UPDATED_VORNAME);

        // Get all the betreiberList where vorname equals to UPDATED_VORNAME
        defaultBetreiberShouldNotBeFound("vorname.in=" + UPDATED_VORNAME);
    }

    @Test
    @Transactional
    public void getAllBetreibersByVornameIsNullOrNotNull() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where vorname is not null
        defaultBetreiberShouldBeFound("vorname.specified=true");

        // Get all the betreiberList where vorname is null
        defaultBetreiberShouldNotBeFound("vorname.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetreibersByVornameContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where vorname contains DEFAULT_VORNAME
        defaultBetreiberShouldBeFound("vorname.contains=" + DEFAULT_VORNAME);

        // Get all the betreiberList where vorname contains UPDATED_VORNAME
        defaultBetreiberShouldNotBeFound("vorname.contains=" + UPDATED_VORNAME);
    }

    @Test
    @Transactional
    public void getAllBetreibersByVornameNotContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where vorname does not contain DEFAULT_VORNAME
        defaultBetreiberShouldNotBeFound("vorname.doesNotContain=" + DEFAULT_VORNAME);

        // Get all the betreiberList where vorname does not contain UPDATED_VORNAME
        defaultBetreiberShouldBeFound("vorname.doesNotContain=" + UPDATED_VORNAME);
    }


    @Test
    @Transactional
    public void getAllBetreibersByNachnameIsEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where nachname equals to DEFAULT_NACHNAME
        defaultBetreiberShouldBeFound("nachname.equals=" + DEFAULT_NACHNAME);

        // Get all the betreiberList where nachname equals to UPDATED_NACHNAME
        defaultBetreiberShouldNotBeFound("nachname.equals=" + UPDATED_NACHNAME);
    }

    @Test
    @Transactional
    public void getAllBetreibersByNachnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where nachname not equals to DEFAULT_NACHNAME
        defaultBetreiberShouldNotBeFound("nachname.notEquals=" + DEFAULT_NACHNAME);

        // Get all the betreiberList where nachname not equals to UPDATED_NACHNAME
        defaultBetreiberShouldBeFound("nachname.notEquals=" + UPDATED_NACHNAME);
    }

    @Test
    @Transactional
    public void getAllBetreibersByNachnameIsInShouldWork() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where nachname in DEFAULT_NACHNAME or UPDATED_NACHNAME
        defaultBetreiberShouldBeFound("nachname.in=" + DEFAULT_NACHNAME + "," + UPDATED_NACHNAME);

        // Get all the betreiberList where nachname equals to UPDATED_NACHNAME
        defaultBetreiberShouldNotBeFound("nachname.in=" + UPDATED_NACHNAME);
    }

    @Test
    @Transactional
    public void getAllBetreibersByNachnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where nachname is not null
        defaultBetreiberShouldBeFound("nachname.specified=true");

        // Get all the betreiberList where nachname is null
        defaultBetreiberShouldNotBeFound("nachname.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetreibersByNachnameContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where nachname contains DEFAULT_NACHNAME
        defaultBetreiberShouldBeFound("nachname.contains=" + DEFAULT_NACHNAME);

        // Get all the betreiberList where nachname contains UPDATED_NACHNAME
        defaultBetreiberShouldNotBeFound("nachname.contains=" + UPDATED_NACHNAME);
    }

    @Test
    @Transactional
    public void getAllBetreibersByNachnameNotContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where nachname does not contain DEFAULT_NACHNAME
        defaultBetreiberShouldNotBeFound("nachname.doesNotContain=" + DEFAULT_NACHNAME);

        // Get all the betreiberList where nachname does not contain UPDATED_NACHNAME
        defaultBetreiberShouldBeFound("nachname.doesNotContain=" + UPDATED_NACHNAME);
    }


    @Test
    @Transactional
    public void getAllBetreibersByStrasseIsEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where strasse equals to DEFAULT_STRASSE
        defaultBetreiberShouldBeFound("strasse.equals=" + DEFAULT_STRASSE);

        // Get all the betreiberList where strasse equals to UPDATED_STRASSE
        defaultBetreiberShouldNotBeFound("strasse.equals=" + UPDATED_STRASSE);
    }

    @Test
    @Transactional
    public void getAllBetreibersByStrasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where strasse not equals to DEFAULT_STRASSE
        defaultBetreiberShouldNotBeFound("strasse.notEquals=" + DEFAULT_STRASSE);

        // Get all the betreiberList where strasse not equals to UPDATED_STRASSE
        defaultBetreiberShouldBeFound("strasse.notEquals=" + UPDATED_STRASSE);
    }

    @Test
    @Transactional
    public void getAllBetreibersByStrasseIsInShouldWork() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where strasse in DEFAULT_STRASSE or UPDATED_STRASSE
        defaultBetreiberShouldBeFound("strasse.in=" + DEFAULT_STRASSE + "," + UPDATED_STRASSE);

        // Get all the betreiberList where strasse equals to UPDATED_STRASSE
        defaultBetreiberShouldNotBeFound("strasse.in=" + UPDATED_STRASSE);
    }

    @Test
    @Transactional
    public void getAllBetreibersByStrasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where strasse is not null
        defaultBetreiberShouldBeFound("strasse.specified=true");

        // Get all the betreiberList where strasse is null
        defaultBetreiberShouldNotBeFound("strasse.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetreibersByStrasseContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where strasse contains DEFAULT_STRASSE
        defaultBetreiberShouldBeFound("strasse.contains=" + DEFAULT_STRASSE);

        // Get all the betreiberList where strasse contains UPDATED_STRASSE
        defaultBetreiberShouldNotBeFound("strasse.contains=" + UPDATED_STRASSE);
    }

    @Test
    @Transactional
    public void getAllBetreibersByStrasseNotContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where strasse does not contain DEFAULT_STRASSE
        defaultBetreiberShouldNotBeFound("strasse.doesNotContain=" + DEFAULT_STRASSE);

        // Get all the betreiberList where strasse does not contain UPDATED_STRASSE
        defaultBetreiberShouldBeFound("strasse.doesNotContain=" + UPDATED_STRASSE);
    }


    @Test
    @Transactional
    public void getAllBetreibersByHausnummerIsEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where hausnummer equals to DEFAULT_HAUSNUMMER
        defaultBetreiberShouldBeFound("hausnummer.equals=" + DEFAULT_HAUSNUMMER);

        // Get all the betreiberList where hausnummer equals to UPDATED_HAUSNUMMER
        defaultBetreiberShouldNotBeFound("hausnummer.equals=" + UPDATED_HAUSNUMMER);
    }

    @Test
    @Transactional
    public void getAllBetreibersByHausnummerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where hausnummer not equals to DEFAULT_HAUSNUMMER
        defaultBetreiberShouldNotBeFound("hausnummer.notEquals=" + DEFAULT_HAUSNUMMER);

        // Get all the betreiberList where hausnummer not equals to UPDATED_HAUSNUMMER
        defaultBetreiberShouldBeFound("hausnummer.notEquals=" + UPDATED_HAUSNUMMER);
    }

    @Test
    @Transactional
    public void getAllBetreibersByHausnummerIsInShouldWork() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where hausnummer in DEFAULT_HAUSNUMMER or UPDATED_HAUSNUMMER
        defaultBetreiberShouldBeFound("hausnummer.in=" + DEFAULT_HAUSNUMMER + "," + UPDATED_HAUSNUMMER);

        // Get all the betreiberList where hausnummer equals to UPDATED_HAUSNUMMER
        defaultBetreiberShouldNotBeFound("hausnummer.in=" + UPDATED_HAUSNUMMER);
    }

    @Test
    @Transactional
    public void getAllBetreibersByHausnummerIsNullOrNotNull() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where hausnummer is not null
        defaultBetreiberShouldBeFound("hausnummer.specified=true");

        // Get all the betreiberList where hausnummer is null
        defaultBetreiberShouldNotBeFound("hausnummer.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetreibersByHausnummerContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where hausnummer contains DEFAULT_HAUSNUMMER
        defaultBetreiberShouldBeFound("hausnummer.contains=" + DEFAULT_HAUSNUMMER);

        // Get all the betreiberList where hausnummer contains UPDATED_HAUSNUMMER
        defaultBetreiberShouldNotBeFound("hausnummer.contains=" + UPDATED_HAUSNUMMER);
    }

    @Test
    @Transactional
    public void getAllBetreibersByHausnummerNotContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where hausnummer does not contain DEFAULT_HAUSNUMMER
        defaultBetreiberShouldNotBeFound("hausnummer.doesNotContain=" + DEFAULT_HAUSNUMMER);

        // Get all the betreiberList where hausnummer does not contain UPDATED_HAUSNUMMER
        defaultBetreiberShouldBeFound("hausnummer.doesNotContain=" + UPDATED_HAUSNUMMER);
    }


    @Test
    @Transactional
    public void getAllBetreibersByPlzIsEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where plz equals to DEFAULT_PLZ
        defaultBetreiberShouldBeFound("plz.equals=" + DEFAULT_PLZ);

        // Get all the betreiberList where plz equals to UPDATED_PLZ
        defaultBetreiberShouldNotBeFound("plz.equals=" + UPDATED_PLZ);
    }

    @Test
    @Transactional
    public void getAllBetreibersByPlzIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where plz not equals to DEFAULT_PLZ
        defaultBetreiberShouldNotBeFound("plz.notEquals=" + DEFAULT_PLZ);

        // Get all the betreiberList where plz not equals to UPDATED_PLZ
        defaultBetreiberShouldBeFound("plz.notEquals=" + UPDATED_PLZ);
    }

    @Test
    @Transactional
    public void getAllBetreibersByPlzIsInShouldWork() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where plz in DEFAULT_PLZ or UPDATED_PLZ
        defaultBetreiberShouldBeFound("plz.in=" + DEFAULT_PLZ + "," + UPDATED_PLZ);

        // Get all the betreiberList where plz equals to UPDATED_PLZ
        defaultBetreiberShouldNotBeFound("plz.in=" + UPDATED_PLZ);
    }

    @Test
    @Transactional
    public void getAllBetreibersByPlzIsNullOrNotNull() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where plz is not null
        defaultBetreiberShouldBeFound("plz.specified=true");

        // Get all the betreiberList where plz is null
        defaultBetreiberShouldNotBeFound("plz.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetreibersByPlzContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where plz contains DEFAULT_PLZ
        defaultBetreiberShouldBeFound("plz.contains=" + DEFAULT_PLZ);

        // Get all the betreiberList where plz contains UPDATED_PLZ
        defaultBetreiberShouldNotBeFound("plz.contains=" + UPDATED_PLZ);
    }

    @Test
    @Transactional
    public void getAllBetreibersByPlzNotContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where plz does not contain DEFAULT_PLZ
        defaultBetreiberShouldNotBeFound("plz.doesNotContain=" + DEFAULT_PLZ);

        // Get all the betreiberList where plz does not contain UPDATED_PLZ
        defaultBetreiberShouldBeFound("plz.doesNotContain=" + UPDATED_PLZ);
    }


    @Test
    @Transactional
    public void getAllBetreibersByOrtIsEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where ort equals to DEFAULT_ORT
        defaultBetreiberShouldBeFound("ort.equals=" + DEFAULT_ORT);

        // Get all the betreiberList where ort equals to UPDATED_ORT
        defaultBetreiberShouldNotBeFound("ort.equals=" + UPDATED_ORT);
    }

    @Test
    @Transactional
    public void getAllBetreibersByOrtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where ort not equals to DEFAULT_ORT
        defaultBetreiberShouldNotBeFound("ort.notEquals=" + DEFAULT_ORT);

        // Get all the betreiberList where ort not equals to UPDATED_ORT
        defaultBetreiberShouldBeFound("ort.notEquals=" + UPDATED_ORT);
    }

    @Test
    @Transactional
    public void getAllBetreibersByOrtIsInShouldWork() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where ort in DEFAULT_ORT or UPDATED_ORT
        defaultBetreiberShouldBeFound("ort.in=" + DEFAULT_ORT + "," + UPDATED_ORT);

        // Get all the betreiberList where ort equals to UPDATED_ORT
        defaultBetreiberShouldNotBeFound("ort.in=" + UPDATED_ORT);
    }

    @Test
    @Transactional
    public void getAllBetreibersByOrtIsNullOrNotNull() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where ort is not null
        defaultBetreiberShouldBeFound("ort.specified=true");

        // Get all the betreiberList where ort is null
        defaultBetreiberShouldNotBeFound("ort.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetreibersByOrtContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where ort contains DEFAULT_ORT
        defaultBetreiberShouldBeFound("ort.contains=" + DEFAULT_ORT);

        // Get all the betreiberList where ort contains UPDATED_ORT
        defaultBetreiberShouldNotBeFound("ort.contains=" + UPDATED_ORT);
    }

    @Test
    @Transactional
    public void getAllBetreibersByOrtNotContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where ort does not contain DEFAULT_ORT
        defaultBetreiberShouldNotBeFound("ort.doesNotContain=" + DEFAULT_ORT);

        // Get all the betreiberList where ort does not contain UPDATED_ORT
        defaultBetreiberShouldBeFound("ort.doesNotContain=" + UPDATED_ORT);
    }


    @Test
    @Transactional
    public void getAllBetreibersByBezeichnungIsEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where bezeichnung equals to DEFAULT_BEZEICHNUNG
        defaultBetreiberShouldBeFound("bezeichnung.equals=" + DEFAULT_BEZEICHNUNG);

        // Get all the betreiberList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultBetreiberShouldNotBeFound("bezeichnung.equals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllBetreibersByBezeichnungIsNotEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where bezeichnung not equals to DEFAULT_BEZEICHNUNG
        defaultBetreiberShouldNotBeFound("bezeichnung.notEquals=" + DEFAULT_BEZEICHNUNG);

        // Get all the betreiberList where bezeichnung not equals to UPDATED_BEZEICHNUNG
        defaultBetreiberShouldBeFound("bezeichnung.notEquals=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllBetreibersByBezeichnungIsInShouldWork() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where bezeichnung in DEFAULT_BEZEICHNUNG or UPDATED_BEZEICHNUNG
        defaultBetreiberShouldBeFound("bezeichnung.in=" + DEFAULT_BEZEICHNUNG + "," + UPDATED_BEZEICHNUNG);

        // Get all the betreiberList where bezeichnung equals to UPDATED_BEZEICHNUNG
        defaultBetreiberShouldNotBeFound("bezeichnung.in=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllBetreibersByBezeichnungIsNullOrNotNull() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where bezeichnung is not null
        defaultBetreiberShouldBeFound("bezeichnung.specified=true");

        // Get all the betreiberList where bezeichnung is null
        defaultBetreiberShouldNotBeFound("bezeichnung.specified=false");
    }
                @Test
    @Transactional
    public void getAllBetreibersByBezeichnungContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where bezeichnung contains DEFAULT_BEZEICHNUNG
        defaultBetreiberShouldBeFound("bezeichnung.contains=" + DEFAULT_BEZEICHNUNG);

        // Get all the betreiberList where bezeichnung contains UPDATED_BEZEICHNUNG
        defaultBetreiberShouldNotBeFound("bezeichnung.contains=" + UPDATED_BEZEICHNUNG);
    }

    @Test
    @Transactional
    public void getAllBetreibersByBezeichnungNotContainsSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);

        // Get all the betreiberList where bezeichnung does not contain DEFAULT_BEZEICHNUNG
        defaultBetreiberShouldNotBeFound("bezeichnung.doesNotContain=" + DEFAULT_BEZEICHNUNG);

        // Get all the betreiberList where bezeichnung does not contain UPDATED_BEZEICHNUNG
        defaultBetreiberShouldBeFound("bezeichnung.doesNotContain=" + UPDATED_BEZEICHNUNG);
    }


    @Test
    @Transactional
    public void getAllBetreibersBySysteminstanzIsEqualToSomething() throws Exception {
        // Initialize the database
        betreiberRepository.saveAndFlush(betreiber);
        Systeminstanz systeminstanz = SysteminstanzResourceIT.createEntity(em);
        em.persist(systeminstanz);
        em.flush();
        betreiber.addSysteminstanz(systeminstanz);
        betreiberRepository.saveAndFlush(betreiber);
        Long systeminstanzId = systeminstanz.getId();

        // Get all the betreiberList where systeminstanz equals to systeminstanzId
        defaultBetreiberShouldBeFound("systeminstanzId.equals=" + systeminstanzId);

        // Get all the betreiberList where systeminstanz equals to systeminstanzId + 1
        defaultBetreiberShouldNotBeFound("systeminstanzId.equals=" + (systeminstanzId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBetreiberShouldBeFound(String filter) throws Exception {
        restBetreiberMockMvc.perform(get("/api/betreibers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betreiber.getId().intValue())))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].hausnummer").value(hasItem(DEFAULT_HAUSNUMMER)))
            .andExpect(jsonPath("$.[*].plz").value(hasItem(DEFAULT_PLZ)))
            .andExpect(jsonPath("$.[*].ort").value(hasItem(DEFAULT_ORT)))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)));

        // Check, that the count call also returns 1
        restBetreiberMockMvc.perform(get("/api/betreibers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBetreiberShouldNotBeFound(String filter) throws Exception {
        restBetreiberMockMvc.perform(get("/api/betreibers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBetreiberMockMvc.perform(get("/api/betreibers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .ort(UPDATED_ORT)
            .bezeichnung(UPDATED_BEZEICHNUNG);
        BetreiberDTO betreiberDTO = betreiberMapper.toDto(updatedBetreiber);

        restBetreiberMockMvc.perform(put("/api/betreibers")
            .contentType(TestUtil.APPLICATION_JSON)
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
        assertThat(testBetreiber.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);

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
            .contentType(TestUtil.APPLICATION_JSON)
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
            .accept(TestUtil.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betreiber.getId().intValue())))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].hausnummer").value(hasItem(DEFAULT_HAUSNUMMER)))
            .andExpect(jsonPath("$.[*].plz").value(hasItem(DEFAULT_PLZ)))
            .andExpect(jsonPath("$.[*].ort").value(hasItem(DEFAULT_ORT)))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)));
    }
}
