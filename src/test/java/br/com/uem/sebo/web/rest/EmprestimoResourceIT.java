package br.com.uem.sebo.web.rest;

import static br.com.uem.sebo.domain.EmprestimoAsserts.*;
import static br.com.uem.sebo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.uem.sebo.IntegrationTest;
import br.com.uem.sebo.domain.Emprestimo;
import br.com.uem.sebo.domain.enumeration.StatusEmprestimo;
import br.com.uem.sebo.repository.EmprestimoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmprestimoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmprestimoResourceIT {

    private static final LocalDate DEFAULT_DATA_EMPRESTIMO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_EMPRESTIMO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_PREVISTA_DEVOLUCAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PREVISTA_DEVOLUCAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_DEVOLUCAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DEVOLUCAO = LocalDate.now(ZoneId.systemDefault());

    private static final StatusEmprestimo DEFAULT_STATUS = StatusEmprestimo.EM_ANDAMENTO;
    private static final StatusEmprestimo UPDATED_STATUS = StatusEmprestimo.DEVOLVIDO;

    private static final String ENTITY_API_URL = "/api/emprestimos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private EmprestimoRepository emprestimoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmprestimoMockMvc;

    private Emprestimo emprestimo;

    private Emprestimo insertedEmprestimo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emprestimo createEntity() {
        return new Emprestimo()
            .dataEmprestimo(DEFAULT_DATA_EMPRESTIMO)
            .dataPrevistaDevolucao(DEFAULT_DATA_PREVISTA_DEVOLUCAO)
            .dataDevolucao(DEFAULT_DATA_DEVOLUCAO)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emprestimo createUpdatedEntity() {
        return new Emprestimo()
            .dataEmprestimo(UPDATED_DATA_EMPRESTIMO)
            .dataPrevistaDevolucao(UPDATED_DATA_PREVISTA_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        emprestimo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEmprestimo != null) {
            emprestimoRepository.delete(insertedEmprestimo);
            insertedEmprestimo = null;
        }
    }

    @Test
    @Transactional
    void createEmprestimo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Emprestimo
        var returnedEmprestimo = om.readValue(
            restEmprestimoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emprestimo))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Emprestimo.class
        );

        // Validate the Emprestimo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmprestimoUpdatableFieldsEquals(returnedEmprestimo, getPersistedEmprestimo(returnedEmprestimo));

        insertedEmprestimo = returnedEmprestimo;
    }

    @Test
    @Transactional
    void createEmprestimoWithExistingId() throws Exception {
        // Create the Emprestimo with an existing ID
        emprestimo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmprestimoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emprestimo)))
            .andExpect(status().isBadRequest());

        // Validate the Emprestimo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataEmprestimoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emprestimo.setDataEmprestimo(null);

        // Create the Emprestimo, which fails.

        restEmprestimoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emprestimo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataPrevistaDevolucaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emprestimo.setDataPrevistaDevolucao(null);

        // Create the Emprestimo, which fails.

        restEmprestimoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emprestimo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emprestimo.setStatus(null);

        // Create the Emprestimo, which fails.

        restEmprestimoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emprestimo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmprestimos() throws Exception {
        // Initialize the database
        insertedEmprestimo = emprestimoRepository.saveAndFlush(emprestimo);

        // Get all the emprestimoList
        restEmprestimoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emprestimo.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataEmprestimo").value(hasItem(DEFAULT_DATA_EMPRESTIMO.toString())))
            .andExpect(jsonPath("$.[*].dataPrevistaDevolucao").value(hasItem(DEFAULT_DATA_PREVISTA_DEVOLUCAO.toString())))
            .andExpect(jsonPath("$.[*].dataDevolucao").value(hasItem(DEFAULT_DATA_DEVOLUCAO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmprestimosWithEagerRelationshipsIsEnabled() throws Exception {
        when(emprestimoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmprestimoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(emprestimoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmprestimosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(emprestimoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmprestimoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(emprestimoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmprestimo() throws Exception {
        // Initialize the database
        insertedEmprestimo = emprestimoRepository.saveAndFlush(emprestimo);

        // Get the emprestimo
        restEmprestimoMockMvc
            .perform(get(ENTITY_API_URL_ID, emprestimo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emprestimo.getId().intValue()))
            .andExpect(jsonPath("$.dataEmprestimo").value(DEFAULT_DATA_EMPRESTIMO.toString()))
            .andExpect(jsonPath("$.dataPrevistaDevolucao").value(DEFAULT_DATA_PREVISTA_DEVOLUCAO.toString()))
            .andExpect(jsonPath("$.dataDevolucao").value(DEFAULT_DATA_DEVOLUCAO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmprestimo() throws Exception {
        // Get the emprestimo
        restEmprestimoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmprestimo() throws Exception {
        // Initialize the database
        insertedEmprestimo = emprestimoRepository.saveAndFlush(emprestimo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emprestimo
        Emprestimo updatedEmprestimo = emprestimoRepository.findById(emprestimo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmprestimo are not directly saved in db
        em.detach(updatedEmprestimo);
        updatedEmprestimo
            .dataEmprestimo(UPDATED_DATA_EMPRESTIMO)
            .dataPrevistaDevolucao(UPDATED_DATA_PREVISTA_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .status(UPDATED_STATUS);

        restEmprestimoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmprestimo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmprestimo))
            )
            .andExpect(status().isOk());

        // Validate the Emprestimo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmprestimoToMatchAllProperties(updatedEmprestimo);
    }

    @Test
    @Transactional
    void putNonExistingEmprestimo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emprestimo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmprestimoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emprestimo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emprestimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emprestimo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmprestimo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emprestimo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmprestimoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emprestimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emprestimo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmprestimo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emprestimo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmprestimoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emprestimo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emprestimo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmprestimoWithPatch() throws Exception {
        // Initialize the database
        insertedEmprestimo = emprestimoRepository.saveAndFlush(emprestimo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emprestimo using partial update
        Emprestimo partialUpdatedEmprestimo = new Emprestimo();
        partialUpdatedEmprestimo.setId(emprestimo.getId());

        partialUpdatedEmprestimo
            .dataEmprestimo(UPDATED_DATA_EMPRESTIMO)
            .dataPrevistaDevolucao(UPDATED_DATA_PREVISTA_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO);

        restEmprestimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmprestimo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmprestimo))
            )
            .andExpect(status().isOk());

        // Validate the Emprestimo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmprestimoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmprestimo, emprestimo),
            getPersistedEmprestimo(emprestimo)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmprestimoWithPatch() throws Exception {
        // Initialize the database
        insertedEmprestimo = emprestimoRepository.saveAndFlush(emprestimo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emprestimo using partial update
        Emprestimo partialUpdatedEmprestimo = new Emprestimo();
        partialUpdatedEmprestimo.setId(emprestimo.getId());

        partialUpdatedEmprestimo
            .dataEmprestimo(UPDATED_DATA_EMPRESTIMO)
            .dataPrevistaDevolucao(UPDATED_DATA_PREVISTA_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .status(UPDATED_STATUS);

        restEmprestimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmprestimo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmprestimo))
            )
            .andExpect(status().isOk());

        // Validate the Emprestimo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmprestimoUpdatableFieldsEquals(partialUpdatedEmprestimo, getPersistedEmprestimo(partialUpdatedEmprestimo));
    }

    @Test
    @Transactional
    void patchNonExistingEmprestimo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emprestimo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmprestimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emprestimo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emprestimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emprestimo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmprestimo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emprestimo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmprestimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emprestimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emprestimo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmprestimo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emprestimo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmprestimoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(emprestimo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emprestimo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmprestimo() throws Exception {
        // Initialize the database
        insertedEmprestimo = emprestimoRepository.saveAndFlush(emprestimo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the emprestimo
        restEmprestimoMockMvc
            .perform(delete(ENTITY_API_URL_ID, emprestimo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return emprestimoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Emprestimo getPersistedEmprestimo(Emprestimo emprestimo) {
        return emprestimoRepository.findById(emprestimo.getId()).orElseThrow();
    }

    protected void assertPersistedEmprestimoToMatchAllProperties(Emprestimo expectedEmprestimo) {
        assertEmprestimoAllPropertiesEquals(expectedEmprestimo, getPersistedEmprestimo(expectedEmprestimo));
    }

    protected void assertPersistedEmprestimoToMatchUpdatableProperties(Emprestimo expectedEmprestimo) {
        assertEmprestimoAllUpdatablePropertiesEquals(expectedEmprestimo, getPersistedEmprestimo(expectedEmprestimo));
    }
}
