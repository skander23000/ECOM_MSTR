package fr.ecom.mstr.tire.service;

import fr.ecom.mstr.tire.domain.Tire;
import fr.ecom.mstr.tire.repository.TireRepository;
import fr.ecom.mstr.tire.service.dto.TireDTO;
import fr.ecom.mstr.tire.service.mapper.TireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.ecom.mstr.tire.domain.Tire}.
 */
@Service
@Transactional
public class TireService {

    private static final Logger LOG = LoggerFactory.getLogger(TireService.class);

    private final TireRepository tireRepository;

    private final TireMapper tireMapper;

    public TireService(TireRepository tireRepository, TireMapper tireMapper) {
        this.tireRepository = tireRepository;
        this.tireMapper = tireMapper;
    }

    /**
     * Save a tire.
     *
     * @param tireDTO the entity to save.
     * @return the persisted entity.
     */
    public TireDTO save(TireDTO tireDTO) {
        LOG.debug("Request to save Tire : {}", tireDTO);
        Tire tire = tireMapper.toEntity(tireDTO);
        tire = tireRepository.save(tire);
        return tireMapper.toDto(tire);
    }

    /**
     * Update a tire.
     *
     * @param tireDTO the entity to save.
     * @return the persisted entity.
     */
    public TireDTO update(TireDTO tireDTO) {
        LOG.debug("Request to update Tire : {}", tireDTO);
        Tire tire = tireMapper.toEntity(tireDTO);
        tire = tireRepository.save(tire);
        return tireMapper.toDto(tire);
    }

    /**
     * Partially update a tire.
     *
     * @param tireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TireDTO> partialUpdate(TireDTO tireDTO) {
        LOG.debug("Request to partially update Tire : {}", tireDTO);

        return tireRepository
            .findById(tireDTO.getId())
            .map(existingTire -> {
                tireMapper.partialUpdate(existingTire, tireDTO);

                return existingTire;
            })
            .map(tireRepository::save)
            .map(tireMapper::toDto);
    }

    /**
     * Get all the tires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TireDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Tires");
        return tireRepository.findAll(pageable).map(tireMapper::toDto);
    }

    /**
     * Get all the tires with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TireDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tireRepository.findAllWithEagerRelationships(pageable).map(tireMapper::toDto);
    }

    /**
     * Get one tire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TireDTO> findOne(Long id) {
        LOG.debug("Request to get Tire : {}", id);
        return tireRepository.findOneWithEagerRelationships(id).map(tireMapper::toDto);
    }

    /**
     * Delete the tire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Tire : {}", id);
        tireRepository.deleteById(id);
    }
}
