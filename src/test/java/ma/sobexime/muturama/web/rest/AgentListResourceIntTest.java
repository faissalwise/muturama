package ma.sobexime.muturama.web.rest;

import ma.sobexime.muturama.MuturamaApp;

import ma.sobexime.muturama.domain.AgentList;
import ma.sobexime.muturama.repository.AgentListRepository;
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
import java.math.BigDecimal;
import java.util.List;

import static ma.sobexime.muturama.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AgentListResource REST controller.
 *
 * @see AgentListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MuturamaApp.class)
public class AgentListResourceIntTest {

    private static final String DEFAULT_CIN = "AAAAAAAAAA";
    private static final String UPDATED_CIN = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LAT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LAT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LON = new BigDecimal(1);
    private static final BigDecimal UPDATED_LON = new BigDecimal(2);

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private AgentListRepository agentListRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAgentListMockMvc;

    private AgentList agentList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgentListResource agentListResource = new AgentListResource(agentListRepository);
        this.restAgentListMockMvc = MockMvcBuilders.standaloneSetup(agentListResource)
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
    public static AgentList createEntity(EntityManager em) {
        AgentList agentList = new AgentList()
            .cin(DEFAULT_CIN)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .address(DEFAULT_ADDRESS)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .status(DEFAULT_STATUS);
        return agentList;
    }

    @Before
    public void initTest() {
        agentList = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgentList() throws Exception {
        int databaseSizeBeforeCreate = agentListRepository.findAll().size();

        // Create the AgentList
        restAgentListMockMvc.perform(post("/api/agent-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentList)))
            .andExpect(status().isCreated());

        // Validate the AgentList in the database
        List<AgentList> agentListList = agentListRepository.findAll();
        assertThat(agentListList).hasSize(databaseSizeBeforeCreate + 1);
        AgentList testAgentList = agentListList.get(agentListList.size() - 1);
        assertThat(testAgentList.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testAgentList.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAgentList.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testAgentList.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAgentList.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testAgentList.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testAgentList.isStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createAgentListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentListRepository.findAll().size();

        // Create the AgentList with an existing ID
        agentList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentListMockMvc.perform(post("/api/agent-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentList)))
            .andExpect(status().isBadRequest());

        // Validate the AgentList in the database
        List<AgentList> agentListList = agentListRepository.findAll();
        assertThat(agentListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAgentLists() throws Exception {
        // Initialize the database
        agentListRepository.saveAndFlush(agentList);

        // Get all the agentListList
        restAgentListMockMvc.perform(get("/api/agent-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentList.getId().intValue())))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.intValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getAgentList() throws Exception {
        // Initialize the database
        agentListRepository.saveAndFlush(agentList);

        // Get the agentList
        restAgentListMockMvc.perform(get("/api/agent-lists/{id}", agentList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agentList.getId().intValue()))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.intValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAgentList() throws Exception {
        // Get the agentList
        restAgentListMockMvc.perform(get("/api/agent-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentList() throws Exception {
        // Initialize the database
        agentListRepository.saveAndFlush(agentList);
        int databaseSizeBeforeUpdate = agentListRepository.findAll().size();

        // Update the agentList
        AgentList updatedAgentList = agentListRepository.findOne(agentList.getId());
        updatedAgentList
            .cin(UPDATED_CIN)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .address(UPDATED_ADDRESS)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .status(UPDATED_STATUS);

        restAgentListMockMvc.perform(put("/api/agent-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgentList)))
            .andExpect(status().isOk());

        // Validate the AgentList in the database
        List<AgentList> agentListList = agentListRepository.findAll();
        assertThat(agentListList).hasSize(databaseSizeBeforeUpdate);
        AgentList testAgentList = agentListList.get(agentListList.size() - 1);
        assertThat(testAgentList.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testAgentList.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAgentList.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAgentList.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAgentList.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testAgentList.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testAgentList.isStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingAgentList() throws Exception {
        int databaseSizeBeforeUpdate = agentListRepository.findAll().size();

        // Create the AgentList

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgentListMockMvc.perform(put("/api/agent-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentList)))
            .andExpect(status().isCreated());

        // Validate the AgentList in the database
        List<AgentList> agentListList = agentListRepository.findAll();
        assertThat(agentListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAgentList() throws Exception {
        // Initialize the database
        agentListRepository.saveAndFlush(agentList);
        int databaseSizeBeforeDelete = agentListRepository.findAll().size();

        // Get the agentList
        restAgentListMockMvc.perform(delete("/api/agent-lists/{id}", agentList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AgentList> agentListList = agentListRepository.findAll();
        assertThat(agentListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentList.class);
        AgentList agentList1 = new AgentList();
        agentList1.setId(1L);
        AgentList agentList2 = new AgentList();
        agentList2.setId(agentList1.getId());
        assertThat(agentList1).isEqualTo(agentList2);
        agentList2.setId(2L);
        assertThat(agentList1).isNotEqualTo(agentList2);
        agentList1.setId(null);
        assertThat(agentList1).isNotEqualTo(agentList2);
    }
}
