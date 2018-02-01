package ma.sobexime.muturama.web.rest;

import ma.sobexime.muturama.MuturamaApp;

import ma.sobexime.muturama.domain.Affinite;
import ma.sobexime.muturama.repository.AffiniteRepository;
import ma.sobexime.muturama.repository.search.AffiniteSearchRepository;
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
 * Test class for the AffiniteResource REST controller.
 *
 * @see AffiniteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MuturamaApp.class)
public class AffiniteResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private AffiniteRepository affiniteRepository;

    @Autowired
    private AffiniteSearchRepository affiniteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAffiniteMockMvc;

    private Affinite affinite;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AffiniteResource affiniteResource = new AffiniteResource(affiniteRepository, affiniteSearchRepository);
        this.restAffiniteMockMvc = MockMvcBuilders.standaloneSetup(affiniteResource)
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
    public static Affinite createEntity(EntityManager em) {
        Affinite affinite = new Affinite()
            .nom(DEFAULT_NOM);
        return affinite;
    }

    @Before
    public void initTest() {
        affiniteSearchRepository.deleteAll();
        affinite = createEntity(em);
    }

    @Test
    @Transactional
    public void createAffinite() throws Exception {
        int databaseSizeBeforeCreate = affiniteRepository.findAll().size();

        // Create the Affinite
        restAffiniteMockMvc.perform(post("/api/affinites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(affinite)))
            .andExpect(status().isCreated());

        // Validate the Affinite in the database
        List<Affinite> affiniteList = affiniteRepository.findAll();
        assertThat(affiniteList).hasSize(databaseSizeBeforeCreate + 1);
        Affinite testAffinite = affiniteList.get(affiniteList.size() - 1);
        assertThat(testAffinite.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Affinite in Elasticsearch
        Affinite affiniteEs = affiniteSearchRepository.findOne(testAffinite.getId());
        assertThat(affiniteEs).isEqualToComparingFieldByField(testAffinite);
    }

    @Test
    @Transactional
    public void createAffiniteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = affiniteRepository.findAll().size();

        // Create the Affinite with an existing ID
        affinite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffiniteMockMvc.perform(post("/api/affinites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(affinite)))
            .andExpect(status().isBadRequest());

        // Validate the Affinite in the database
        List<Affinite> affiniteList = affiniteRepository.findAll();
        assertThat(affiniteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAffinites() throws Exception {
        // Initialize the database
        affiniteRepository.saveAndFlush(affinite);

        // Get all the affiniteList
        restAffiniteMockMvc.perform(get("/api/affinites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affinite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getAffinite() throws Exception {
        // Initialize the database
        affiniteRepository.saveAndFlush(affinite);

        // Get the affinite
        restAffiniteMockMvc.perform(get("/api/affinites/{id}", affinite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(affinite.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAffinite() throws Exception {
        // Get the affinite
        restAffiniteMockMvc.perform(get("/api/affinites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAffinite() throws Exception {
        // Initialize the database
        affiniteRepository.saveAndFlush(affinite);
        affiniteSearchRepository.save(affinite);
        int databaseSizeBeforeUpdate = affiniteRepository.findAll().size();

        // Update the affinite
        Affinite updatedAffinite = affiniteRepository.findOne(affinite.getId());
        updatedAffinite
            .nom(UPDATED_NOM);

        restAffiniteMockMvc.perform(put("/api/affinites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAffinite)))
            .andExpect(status().isOk());

        // Validate the Affinite in the database
        List<Affinite> affiniteList = affiniteRepository.findAll();
        assertThat(affiniteList).hasSize(databaseSizeBeforeUpdate);
        Affinite testAffinite = affiniteList.get(affiniteList.size() - 1);
        assertThat(testAffinite.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Affinite in Elasticsearch
        Affinite affiniteEs = affiniteSearchRepository.findOne(testAffinite.getId());
        assertThat(affiniteEs).isEqualToComparingFieldByField(testAffinite);
    }

    @Test
    @Transactional
    public void updateNonExistingAffinite() throws Exception {
        int databaseSizeBeforeUpdate = affiniteRepository.findAll().size();

        // Create the Affinite

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAffiniteMockMvc.perform(put("/api/affinites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(affinite)))
            .andExpect(status().isCreated());

        // Validate the Affinite in the database
        List<Affinite> affiniteList = affiniteRepository.findAll();
        assertThat(affiniteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAffinite() throws Exception {
        // Initialize the database
        affiniteRepository.saveAndFlush(affinite);
        affiniteSearchRepository.save(affinite);
        int databaseSizeBeforeDelete = affiniteRepository.findAll().size();

        // Get the affinite
        restAffiniteMockMvc.perform(delete("/api/affinites/{id}", affinite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean affiniteExistsInEs = affiniteSearchRepository.exists(affinite.getId());
        assertThat(affiniteExistsInEs).isFalse();

        // Validate the database is empty
        List<Affinite> affiniteList = affiniteRepository.findAll();
        assertThat(affiniteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAffinite() throws Exception {
        // Initialize the database
        affiniteRepository.saveAndFlush(affinite);
        affiniteSearchRepository.save(affinite);

        // Search the affinite
        restAffiniteMockMvc.perform(get("/api/_search/affinites?query=id:" + affinite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affinite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Affinite.class);
        Affinite affinite1 = new Affinite();
        affinite1.setId(1L);
        Affinite affinite2 = new Affinite();
        affinite2.setId(affinite1.getId());
        assertThat(affinite1).isEqualTo(affinite2);
        affinite2.setId(2L);
        assertThat(affinite1).isNotEqualTo(affinite2);
        affinite1.setId(null);
        assertThat(affinite1).isNotEqualTo(affinite2);
    }
}
