package ma.sobexime.muturama.web.rest;

import ma.sobexime.muturama.MuturamaApp;

import ma.sobexime.muturama.domain.Infohain;
import ma.sobexime.muturama.repository.InfohainRepository;
import ma.sobexime.muturama.repository.search.InfohainSearchRepository;
import ma.sobexime.muturama.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static ma.sobexime.muturama.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InfohainResource REST controller.
 *
 * @see InfohainResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MuturamaApp.class)
public class InfohainResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private InfohainRepository infohainRepository;

    @Autowired
    private InfohainSearchRepository infohainSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInfohainMockMvc;

    private Infohain infohain;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InfohainResource infohainResource = new InfohainResource(infohainRepository, infohainSearchRepository);
        this.restInfohainMockMvc = MockMvcBuilders.standaloneSetup(infohainResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Infohain createEntity(EntityManager em) {
        Infohain infohain = new Infohain()
            .nom(DEFAULT_NOM);
        return infohain;
    }

    @Before
    public void initTest() {
        infohainSearchRepository.deleteAll();
        infohain = createEntity(em);
    }

    @Test
    @Transactional
    public void createInfohain() throws Exception {
        int databaseSizeBeforeCreate = infohainRepository.findAll().size();

        // Create the Infohain
        restInfohainMockMvc.perform(post("/api/infohains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(infohain)))
            .andExpect(status().isCreated());

        // Validate the Infohain in the database
        List<Infohain> infohainList = infohainRepository.findAll();
        assertThat(infohainList).hasSize(databaseSizeBeforeCreate + 1);
        Infohain testInfohain = infohainList.get(infohainList.size() - 1);
        assertThat(testInfohain.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Infohain in Elasticsearch
        Infohain infohainEs = infohainSearchRepository.findOne(testInfohain.getId());
        assertThat(infohainEs).isEqualToComparingFieldByField(testInfohain);
    }

    @Test
    @Transactional
    public void createInfohainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = infohainRepository.findAll().size();

        // Create the Infohain with an existing ID
        infohain.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfohainMockMvc.perform(post("/api/infohains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(infohain)))
            .andExpect(status().isBadRequest());

        // Validate the Infohain in the database
        List<Infohain> infohainList = infohainRepository.findAll();
        assertThat(infohainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = infohainRepository.findAll().size();
        // set the field null
        infohain.setNom(null);

        // Create the Infohain, which fails.

        restInfohainMockMvc.perform(post("/api/infohains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(infohain)))
            .andExpect(status().isBadRequest());

        List<Infohain> infohainList = infohainRepository.findAll();
        assertThat(infohainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInfohains() throws Exception {
        // Initialize the database
        infohainRepository.saveAndFlush(infohain);

        // Get all the infohainList
        restInfohainMockMvc.perform(get("/api/infohains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infohain.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getInfohain() throws Exception {
        // Initialize the database
        infohainRepository.saveAndFlush(infohain);

        // Get the infohain
        restInfohainMockMvc.perform(get("/api/infohains/{id}", infohain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(infohain.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInfohain() throws Exception {
        // Get the infohain
        restInfohainMockMvc.perform(get("/api/infohains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInfohain() throws Exception {
        // Initialize the database
        infohainRepository.saveAndFlush(infohain);
        infohainSearchRepository.save(infohain);
        int databaseSizeBeforeUpdate = infohainRepository.findAll().size();

        // Update the infohain
        Infohain updatedInfohain = infohainRepository.findOne(infohain.getId());
        updatedInfohain
            .nom(UPDATED_NOM);

        restInfohainMockMvc.perform(put("/api/infohains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInfohain)))
            .andExpect(status().isOk());

        // Validate the Infohain in the database
        List<Infohain> infohainList = infohainRepository.findAll();
        assertThat(infohainList).hasSize(databaseSizeBeforeUpdate);
        Infohain testInfohain = infohainList.get(infohainList.size() - 1);
        assertThat(testInfohain.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Infohain in Elasticsearch
        Infohain infohainEs = infohainSearchRepository.findOne(testInfohain.getId());
        assertThat(infohainEs).isEqualToComparingFieldByField(testInfohain);
    }

    @Test
    @Transactional
    public void updateNonExistingInfohain() throws Exception {
        int databaseSizeBeforeUpdate = infohainRepository.findAll().size();

        // Create the Infohain

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInfohainMockMvc.perform(put("/api/infohains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(infohain)))
            .andExpect(status().isCreated());

        // Validate the Infohain in the database
        List<Infohain> infohainList = infohainRepository.findAll();
        assertThat(infohainList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInfohain() throws Exception {
        // Initialize the database
        infohainRepository.saveAndFlush(infohain);
        infohainSearchRepository.save(infohain);
        int databaseSizeBeforeDelete = infohainRepository.findAll().size();

        // Get the infohain
        restInfohainMockMvc.perform(delete("/api/infohains/{id}", infohain.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean infohainExistsInEs = infohainSearchRepository.exists(infohain.getId());
        assertThat(infohainExistsInEs).isFalse();

        // Validate the database is empty
        List<Infohain> infohainList = infohainRepository.findAll();
        assertThat(infohainList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInfohain() throws Exception {
        // Initialize the database
        infohainRepository.saveAndFlush(infohain);
        infohainSearchRepository.save(infohain);

        // Search the infohain
        restInfohainMockMvc.perform(get("/api/_search/infohains?query=id:" + infohain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infohain.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Infohain.class);
        Infohain infohain1 = new Infohain();
        infohain1.setId(1L);
        Infohain infohain2 = new Infohain();
        infohain2.setId(infohain1.getId());
        assertThat(infohain1).isEqualTo(infohain2);
        infohain2.setId(2L);
        assertThat(infohain1).isNotEqualTo(infohain2);
        infohain1.setId(null);
        assertThat(infohain1).isNotEqualTo(infohain2);
    }
}
