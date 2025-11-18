package br.com.uem.sebo.web.rest;

import br.com.uem.sebo.domain.Disco;
import br.com.uem.sebo.repository.DiscoRepository;
import br.com.uem.sebo.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.uem.sebo.domain.Disco}.
 */
@RestController
@RequestMapping("/api/discos")
@Transactional
public class DiscoResource {

    private static final Logger LOG = LoggerFactory.getLogger(DiscoResource.class);

    private static final String ENTITY_NAME = "disco";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiscoRepository discoRepository;

    public DiscoResource(DiscoRepository discoRepository) {
        this.discoRepository = discoRepository;
    }

    /**
     * {@code POST  /discos} : Create a new disco.
     *
     * @param disco the disco to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disco, or with status {@code 400 (Bad Request)} if the disco has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Disco> createDisco(@Valid @RequestBody Disco disco) throws URISyntaxException {
        LOG.debug("REST request to save Disco : {}", disco);
        if (disco.getId() != null) {
            throw new BadRequestAlertException("A new disco cannot already have an ID", ENTITY_NAME, "idexists");
        }
        disco = discoRepository.save(disco);
        return ResponseEntity.created(new URI("/api/discos/" + disco.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, disco.getId().toString()))
            .body(disco);
    }

    /**
     * {@code PUT  /discos/:id} : Updates an existing disco.
     *
     * @param id the id of the disco to save.
     * @param disco the disco to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disco,
     * or with status {@code 400 (Bad Request)} if the disco is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disco couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Disco> updateDisco(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Disco disco)
        throws URISyntaxException {
        LOG.debug("REST request to update Disco : {}, {}", id, disco);
        if (disco.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disco.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!discoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        disco = discoRepository.save(disco);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disco.getId().toString()))
            .body(disco);
    }

    /**
     * {@code PATCH  /discos/:id} : Partial updates given fields of an existing disco, field will ignore if it is null
     *
     * @param id the id of the disco to save.
     * @param disco the disco to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disco,
     * or with status {@code 400 (Bad Request)} if the disco is not valid,
     * or with status {@code 404 (Not Found)} if the disco is not found,
     * or with status {@code 500 (Internal Server Error)} if the disco couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Disco> partialUpdateDisco(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Disco disco
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Disco partially : {}, {}", id, disco);
        if (disco.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disco.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!discoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Disco> result = discoRepository
            .findById(disco.getId())
            .map(existingDisco -> {
                if (disco.getTipoMidia() != null) {
                    existingDisco.setTipoMidia(disco.getTipoMidia());
                }
                if (disco.getDuracaoMinutos() != null) {
                    existingDisco.setDuracaoMinutos(disco.getDuracaoMinutos());
                }

                return existingDisco;
            })
            .map(discoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disco.getId().toString())
        );
    }

    /**
     * {@code GET  /discos} : get all the discos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of discos in body.
     */
    @GetMapping("")
    public List<Disco> getAllDiscos() {
        LOG.debug("REST request to get all Discos");
        return discoRepository.findAll();
    }

    /**
     * {@code GET  /discos/:id} : get the "id" disco.
     *
     * @param id the id of the disco to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disco, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Disco> getDisco(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Disco : {}", id);
        Optional<Disco> disco = discoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(disco);
    }

    /**
     * {@code DELETE  /discos/:id} : delete the "id" disco.
     *
     * @param id the id of the disco to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisco(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Disco : {}", id);
        discoRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
