package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.GeraetTyp;
import de.kvb.eps.repository.GeraetTypRepository;
import de.kvb.eps.repository.search.GeraetTypSearchRepository;
import de.kvb.eps.service.GeraetTypService;
import de.kvb.eps.service.dto.GeraetTypDTO;
import de.kvb.eps.service.mapper.GeraetTypMapper;
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
        final GeraetTypResource geraetTypResource = new GeraetTypResource(geraetTypService);
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(geraetTyp.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()))
            .andExpect(jsonPath("$.technologie").value(DEFAULT_TECHNOLOGIE.toString()));
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .accept(TestUtil.APPLICATION_JSON_UTF8))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraetTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE.toString())));
    }
}
