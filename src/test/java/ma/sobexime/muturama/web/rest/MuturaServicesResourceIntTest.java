package ma.sobexime.muturama.web.rest;

import ma.sobexime.muturama.MuturamaApp;

import ma.sobexime.muturama.domain.MuturaServices;
import ma.sobexime.muturama.repository.MuturaServicesRepository;
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
 * Test class for the MuturaServicesResource REST controller.
 *
 * @see MuturaServicesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MuturamaApp.class)
public class MuturaServicesResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private MuturaServicesRepository muturaServicesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMuturaServicesMockMvc;

    private MuturaServices muturaServices;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MuturaServicesResource muturaServicesResource = new MuturaServicesResource(muturaServicesRepository);
        this.restMuturaServicesMockMvc = MockMvcBuilders.standaloneSetup(muturaServicesResource)
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
    public static MuturaServices createEntity(EntityManager em) {
        MuturaServices muturaServices = new MuturaServices()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION);
        return muturaServices;
    }

    @Before
    public void initTest() {
        muturaServices = createEntity(em);
    }

    @Test
    @Transactional
    public void createMuturaServices() throws Exception {
        int databaseSizeBeforeCreate = muturaServicesRepository.findAll().size();

        // Create the MuturaServices
        restMuturaServicesMockMvc.perform(post("/api/mutura-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(muturaServices)))
            .andExpect(status().isCreated());

        // Validate the MuturaServices in the database
        List<MuturaServices> muturaServicesList = muturaServicesRepository.findAll();
        assertThat(muturaServicesList).hasSize(databaseSizeBeforeCreate + 1);
        MuturaServices testMuturaServices = muturaServicesList.get(muturaServicesList.size() - 1);
        assertThat(testMuturaServices.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMuturaServices.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createMuturaServicesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = muturaServicesRepository.findAll().size();

        // Create the MuturaServices with an existing ID
        muturaServices.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMuturaServicesMockMvc.perform(post("/api/mutura-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(muturaServices)))
            .andExpect(status().isBadRequest());

        // Validate the MuturaServices in the database
        List<MuturaServices> muturaServicesList = muturaServicesRepository.findAll();
        assertThat(muturaServicesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMuturaServices() throws Exception {
        // Initialize the database
        muturaServicesRepository.saveAndFlush(muturaServices);

        // Get all the muturaServicesList
        restMuturaServicesMockMvc.perform(get("/api/mutura-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(muturaServices.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMuturaServices() throws Exception {
        // Initialize the database
        muturaServicesRepository.saveAndFlush(muturaServices);

        // Get the muturaServices
        restMuturaServicesMockMvc.perform(get("/api/mutura-services/{id}", muturaServices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(muturaServices.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMuturaServices() throws Exception {
        // Get the muturaServices
        restMuturaServicesMockMvc.perform(get("/api/mutura-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMuturaServices() throws Exception {
        // Initialize the database
        muturaServicesRepository.saveAndFlush(muturaServices);
        int databaseSizeBeforeUpdate = muturaServicesRepository.findAll().size();

        // Update the muturaServices
        MuturaServices updatedMuturaServices = muturaServicesRepository.findOne(muturaServices.getId());
        updatedMuturaServices
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);

        restMuturaServicesMockMvc.perform(put("/api/mutura-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMuturaServices)))
            .andExpect(status().isOk());

        // Validate the MuturaServices in the database
        List<MuturaServices> muturaServicesList = muturaServicesRepository.findAll();
        assertThat(muturaServicesList).hasSize(databaseSizeBeforeUpdate);
        MuturaServices testMuturaServices = muturaServicesList.get(muturaServicesList.size() - 1);
        assertThat(testMuturaServices.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMuturaServices.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingMuturaServices() throws Exception {
        int databaseSizeBeforeUpdate = muturaServicesRepository.findAll().size();

        // Create the MuturaServices

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMuturaServicesMockMvc.perform(put("/api/mutura-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(muturaServices)))
            .andExpect(status().isCreated());

        // Validate the MuturaServices in the database
        List<MuturaServices> muturaServicesList = muturaServicesRepository.findAll();
        assertThat(muturaServicesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMuturaServices() throws Exception {
        // Initialize the database
        muturaServicesRepository.saveAndFlush(muturaServices);
        int databaseSizeBeforeDelete = muturaServicesRepository.findAll().size();

        // Get the muturaServices
        restMuturaServicesMockMvc.perform(delete("/api/mutura-services/{id}", muturaServices.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MuturaServices> muturaServicesList = muturaServicesRepository.findAll();
        assertThat(muturaServicesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MuturaServices.class);
        MuturaServices muturaServices1 = new MuturaServices();
        muturaServices1.setId(1L);
        MuturaServices muturaServices2 = new MuturaServices();
        muturaServices2.setId(muturaServices1.getId());
        assertThat(muturaServices1).isEqualTo(muturaServices2);
        muturaServices2.setId(2L);
        assertThat(muturaServices1).isNotEqualTo(muturaServices2);
        muturaServices1.setId(null);
        assertThat(muturaServices1).isNotEqualTo(muturaServices2);
    }
}
