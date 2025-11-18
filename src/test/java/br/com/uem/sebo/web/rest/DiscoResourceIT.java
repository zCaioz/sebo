package br.com.uem.sebo.web.rest;

import static br.com.uem.sebo.domain.DiscoAsserts.*;
import static br.com.uem.sebo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.uem.sebo.IntegrationTest;
import br.com.uem.sebo.domain.Disco;
import br.com.uem.sebo.domain.enumeration.TipoMidia;
import br.com.uem.sebo.repository.DiscoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DiscoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiscoResourceIT {

    private static final TipoMidia DEFAULT_TIPO_MIDIA = TipoMidia.VINIL;
    private static final TipoMidia UPDATED_TIPO_MIDIA = TipoMidia.CD;

    private static final Integer DEFAULT_DURACAO_MINUTOS = 1;
    private static final Integer UPDATED_DURACAO_MINUTOS = 2;

    private static final String ENTITY_API_URL = "/api/discos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DiscoRepository discoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiscoMockMvc;

    private Disco disco;

    private Disco insertedDisco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disco createEntity() {
        return new Disco().tipoMidia(DEFAULT_TIPO_MIDIA).duracaoMinutos(DEFAULT_DURACAO_MINUTOS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disco createUpdatedEntity() {
        return new Disco().tipoMidia(UPDATED_TIPO_MIDIA).duracaoMinutos(UPDATED_DURACAO_MINUTOS);
    }

    @BeforeEach
    void initTest() {
        disco = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDisco != null) {
            discoRepository.delete(insertedDisco);
            insertedDisco = null;
        }
    }

    @Test
    @Transactional
    void createDisco() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Disco
        var returnedDisco = om.readValue(
            restDiscoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disco)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Disco.class
        );

        // Validate the Disco in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDiscoUpdatableFieldsEquals(returnedDisco, getPersistedDisco(returnedDisco));

        insertedDisco = returnedDisco;
    }

    @Test
    @Transactional
    void createDiscoWithExistingId() throws Exception {
        // Create the Disco with an existing ID
        disco.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disco)))
            .andExpect(status().isBadRequest());

        // Validate the Disco in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoMidiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disco.setTipoMidia(null);

        // Create the Disco, which fails.

        restDiscoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disco)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiscos() throws Exception {
        // Initialize the database
        insertedDisco = discoRepository.saveAndFlush(disco);

        // Get all the discoList
        restDiscoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disco.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoMidia").value(hasItem(DEFAULT_TIPO_MIDIA.toString())))
            .andExpect(jsonPath("$.[*].duracaoMinutos").value(hasItem(DEFAULT_DURACAO_MINUTOS)));
    }

    @Test
    @Transactional
    void getDisco() throws Exception {
        // Initialize the database
        insertedDisco = discoRepository.saveAndFlush(disco);

        // Get the disco
        restDiscoMockMvc
            .perform(get(ENTITY_API_URL_ID, disco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disco.getId().intValue()))
            .andExpect(jsonPath("$.tipoMidia").value(DEFAULT_TIPO_MIDIA.toString()))
            .andExpect(jsonPath("$.duracaoMinutos").value(DEFAULT_DURACAO_MINUTOS));
    }

    @Test
    @Transactional
    void getNonExistingDisco() throws Exception {
        // Get the disco
        restDiscoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisco() throws Exception {
        // Initialize the database
        insertedDisco = discoRepository.saveAndFlush(disco);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disco
        Disco updatedDisco = discoRepository.findById(disco.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDisco are not directly saved in db
        em.detach(updatedDisco);
        updatedDisco.tipoMidia(UPDATED_TIPO_MIDIA).duracaoMinutos(UPDATED_DURACAO_MINUTOS);

        restDiscoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDisco.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDisco))
            )
            .andExpect(status().isOk());

        // Validate the Disco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDiscoToMatchAllProperties(updatedDisco);
    }

    @Test
    @Transactional
    void putNonExistingDisco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disco.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disco.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disco))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disco.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disco))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disco.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disco)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiscoWithPatch() throws Exception {
        // Initialize the database
        insertedDisco = discoRepository.saveAndFlush(disco);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disco using partial update
        Disco partialUpdatedDisco = new Disco();
        partialUpdatedDisco.setId(disco.getId());

        partialUpdatedDisco.tipoMidia(UPDATED_TIPO_MIDIA);

        restDiscoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisco.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisco))
            )
            .andExpect(status().isOk());

        // Validate the Disco in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiscoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDisco, disco), getPersistedDisco(disco));
    }

    @Test
    @Transactional
    void fullUpdateDiscoWithPatch() throws Exception {
        // Initialize the database
        insertedDisco = discoRepository.saveAndFlush(disco);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disco using partial update
        Disco partialUpdatedDisco = new Disco();
        partialUpdatedDisco.setId(disco.getId());

        partialUpdatedDisco.tipoMidia(UPDATED_TIPO_MIDIA).duracaoMinutos(UPDATED_DURACAO_MINUTOS);

        restDiscoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisco.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisco))
            )
            .andExpect(status().isOk());

        // Validate the Disco in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiscoUpdatableFieldsEquals(partialUpdatedDisco, getPersistedDisco(partialUpdatedDisco));
    }

    @Test
    @Transactional
    void patchNonExistingDisco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disco.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disco.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disco))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disco.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disco))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disco.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscoMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(disco)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisco() throws Exception {
        // Initialize the database
        insertedDisco = discoRepository.saveAndFlush(disco);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the disco
        restDiscoMockMvc
            .perform(delete(ENTITY_API_URL_ID, disco.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return discoRepository.count();
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

    protected Disco getPersistedDisco(Disco disco) {
        return discoRepository.findById(disco.getId()).orElseThrow();
    }

    protected void assertPersistedDiscoToMatchAllProperties(Disco expectedDisco) {
        assertDiscoAllPropertiesEquals(expectedDisco, getPersistedDisco(expectedDisco));
    }

    protected void assertPersistedDiscoToMatchUpdatableProperties(Disco expectedDisco) {
        assertDiscoAllUpdatablePropertiesEquals(expectedDisco, getPersistedDisco(expectedDisco));
    }
}
