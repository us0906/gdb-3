package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.Geraet;
import de.kvb.eps.domain.GeraetTyp;
import de.kvb.eps.domain.Hersteller;
import de.kvb.eps.repository.GeraetRepository;
import de.kvb.eps.repository.search.GeraetSearchRepository;
import de.kvb.eps.service.GeraetService;
import de.kvb.eps.service.dto.GeraetDTO;
import de.kvb.eps.service.mapper.GeraetMapper;
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
        final GeraetResource geraetResource = new GeraetResource(geraetService);
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
