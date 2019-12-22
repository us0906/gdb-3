package de.kvb.eps.web.rest;

import de.kvb.eps.Gdb3App;
import de.kvb.eps.config.TestSecurityConfiguration;
import de.kvb.eps.domain.ZubehoerTyp;
import de.kvb.eps.repository.ZubehoerTypRepository;
import de.kvb.eps.repository.search.ZubehoerTypSearchRepository;
import de.kvb.eps.service.ZubehoerTypService;
import de.kvb.eps.service.dto.ZubehoerTypDTO;
import de.kvb.eps.service.mapper.ZubehoerTypMapper;
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
 * Integration tests for the {@link ZubehoerTypResource} REST controller.
 */
@SpringBootTest(classes = {Gdb3App.class, TestSecurityConfiguration.class})
public class ZubehoerTypResourceIT {

    private static final String DEFAULT_BEZEICHNUNG = "AAAAAAAAAA";
    private static final String UPDATED_BEZEICHNUNG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GUELTIG_BIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUELTIG_BIS = LocalDate.now(ZoneId.systemDefault());

    private static final Technologie DEFAULT_TECHNOLOGIE = Technologie.SONO;
    private static final Technologie UPDATED_TECHNOLOGIE = Technologie.BILD;

    @Autowired
    private ZubehoerTypRepository zubehoerTypRepository;

    @Autowired
    private ZubehoerTypMapper zubehoerTypMapper;

    @Autowired
    private ZubehoerTypService zubehoerTypService;

    /**
     * This repository is mocked in the de.kvb.eps.repository.search test package.
     *
     * @see de.kvb.eps.repository.search.ZubehoerTypSearchRepositoryMockConfiguration
     */
    @Autowired
    private ZubehoerTypSearchRepository mockZubehoerTypSearchRepository;

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

    private MockMvc restZubehoerTypMockMvc;

    private ZubehoerTyp zubehoerTyp;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ZubehoerTypResource zubehoerTypResource = new ZubehoerTypResource(zubehoerTypService);
        this.restZubehoerTypMockMvc = MockMvcBuilders.standaloneSetup(zubehoerTypResource)
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
    public static ZubehoerTyp createEntity(EntityManager em) {
        ZubehoerTyp zubehoerTyp = new ZubehoerTyp()
            .bezeichnung(DEFAULT_BEZEICHNUNG)
            .gueltigBis(DEFAULT_GUELTIG_BIS)
            .technologie(DEFAULT_TECHNOLOGIE);
        return zubehoerTyp;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZubehoerTyp createUpdatedEntity(EntityManager em) {
        ZubehoerTyp zubehoerTyp = new ZubehoerTyp()
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS)
            .technologie(UPDATED_TECHNOLOGIE);
        return zubehoerTyp;
    }

    @BeforeEach
    public void initTest() {
        zubehoerTyp = createEntity(em);
    }

    @Test
    @Transactional
    public void createZubehoerTyp() throws Exception {
        int databaseSizeBeforeCreate = zubehoerTypRepository.findAll().size();

        // Create the ZubehoerTyp
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);
        restZubehoerTypMockMvc.perform(post("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isCreated());

        // Validate the ZubehoerTyp in the database
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeCreate + 1);
        ZubehoerTyp testZubehoerTyp = zubehoerTypList.get(zubehoerTypList.size() - 1);
        assertThat(testZubehoerTyp.getBezeichnung()).isEqualTo(DEFAULT_BEZEICHNUNG);
        assertThat(testZubehoerTyp.getGueltigBis()).isEqualTo(DEFAULT_GUELTIG_BIS);
        assertThat(testZubehoerTyp.getTechnologie()).isEqualTo(DEFAULT_TECHNOLOGIE);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(1)).save(testZubehoerTyp);
    }

    @Test
    @Transactional
    public void createZubehoerTypWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = zubehoerTypRepository.findAll().size();

        // Create the ZubehoerTyp with an existing ID
        zubehoerTyp.setId(1L);
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restZubehoerTypMockMvc.perform(post("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ZubehoerTyp in the database
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeCreate);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(0)).save(zubehoerTyp);
    }


    @Test
    @Transactional
    public void checkBezeichnungIsRequired() throws Exception {
        int databaseSizeBeforeTest = zubehoerTypRepository.findAll().size();
        // set the field null
        zubehoerTyp.setBezeichnung(null);

        // Create the ZubehoerTyp, which fails.
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);

        restZubehoerTypMockMvc.perform(post("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isBadRequest());

        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTechnologieIsRequired() throws Exception {
        int databaseSizeBeforeTest = zubehoerTypRepository.findAll().size();
        // set the field null
        zubehoerTyp.setTechnologie(null);

        // Create the ZubehoerTyp, which fails.
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);

        restZubehoerTypMockMvc.perform(post("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isBadRequest());

        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllZubehoerTyps() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get all the zubehoerTypList
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zubehoerTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE.toString())));
    }
    
    @Test
    @Transactional
    public void getZubehoerTyp() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        // Get the zubehoerTyp
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs/{id}", zubehoerTyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(zubehoerTyp.getId().intValue()))
            .andExpect(jsonPath("$.bezeichnung").value(DEFAULT_BEZEICHNUNG))
            .andExpect(jsonPath("$.gueltigBis").value(DEFAULT_GUELTIG_BIS.toString()))
            .andExpect(jsonPath("$.technologie").value(DEFAULT_TECHNOLOGIE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingZubehoerTyp() throws Exception {
        // Get the zubehoerTyp
        restZubehoerTypMockMvc.perform(get("/api/zubehoer-typs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateZubehoerTyp() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        int databaseSizeBeforeUpdate = zubehoerTypRepository.findAll().size();

        // Update the zubehoerTyp
        ZubehoerTyp updatedZubehoerTyp = zubehoerTypRepository.findById(zubehoerTyp.getId()).get();
        // Disconnect from session so that the updates on updatedZubehoerTyp are not directly saved in db
        em.detach(updatedZubehoerTyp);
        updatedZubehoerTyp
            .bezeichnung(UPDATED_BEZEICHNUNG)
            .gueltigBis(UPDATED_GUELTIG_BIS)
            .technologie(UPDATED_TECHNOLOGIE);
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(updatedZubehoerTyp);

        restZubehoerTypMockMvc.perform(put("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isOk());

        // Validate the ZubehoerTyp in the database
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeUpdate);
        ZubehoerTyp testZubehoerTyp = zubehoerTypList.get(zubehoerTypList.size() - 1);
        assertThat(testZubehoerTyp.getBezeichnung()).isEqualTo(UPDATED_BEZEICHNUNG);
        assertThat(testZubehoerTyp.getGueltigBis()).isEqualTo(UPDATED_GUELTIG_BIS);
        assertThat(testZubehoerTyp.getTechnologie()).isEqualTo(UPDATED_TECHNOLOGIE);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(1)).save(testZubehoerTyp);
    }

    @Test
    @Transactional
    public void updateNonExistingZubehoerTyp() throws Exception {
        int databaseSizeBeforeUpdate = zubehoerTypRepository.findAll().size();

        // Create the ZubehoerTyp
        ZubehoerTypDTO zubehoerTypDTO = zubehoerTypMapper.toDto(zubehoerTyp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZubehoerTypMockMvc.perform(put("/api/zubehoer-typs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zubehoerTypDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ZubehoerTyp in the database
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(0)).save(zubehoerTyp);
    }

    @Test
    @Transactional
    public void deleteZubehoerTyp() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);

        int databaseSizeBeforeDelete = zubehoerTypRepository.findAll().size();

        // Delete the zubehoerTyp
        restZubehoerTypMockMvc.perform(delete("/api/zubehoer-typs/{id}", zubehoerTyp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ZubehoerTyp> zubehoerTypList = zubehoerTypRepository.findAll();
        assertThat(zubehoerTypList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ZubehoerTyp in Elasticsearch
        verify(mockZubehoerTypSearchRepository, times(1)).deleteById(zubehoerTyp.getId());
    }

    @Test
    @Transactional
    public void searchZubehoerTyp() throws Exception {
        // Initialize the database
        zubehoerTypRepository.saveAndFlush(zubehoerTyp);
        when(mockZubehoerTypSearchRepository.search(queryStringQuery("id:" + zubehoerTyp.getId())))
            .thenReturn(Collections.singletonList(zubehoerTyp));
        // Search the zubehoerTyp
        restZubehoerTypMockMvc.perform(get("/api/_search/zubehoer-typs?query=id:" + zubehoerTyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zubehoerTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].bezeichnung").value(hasItem(DEFAULT_BEZEICHNUNG)))
            .andExpect(jsonPath("$.[*].gueltigBis").value(hasItem(DEFAULT_GUELTIG_BIS.toString())))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE.toString())));
    }
}
