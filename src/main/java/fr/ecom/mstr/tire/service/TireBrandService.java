package fr.ecom.mstr.tire.service;

import fr.ecom.mstr.tire.domain.TireBrand;
import fr.ecom.mstr.tire.repository.TireBrandRepository;
import fr.ecom.mstr.tire.service.dto.TireBrandDTO;
import fr.ecom.mstr.tire.service.mapper.TireBrandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.ecom.mstr.tire.domain.TireBrand}.
 */
@Service
@Transactional
public class TireBrandService {

    private static final Logger LOG = LoggerFactory.getLogger(TireBrandService.class);

    private final TireBrandRepository tireBrandRepository;

    private final TireBrandMapper tireBrandMapper;

    public TireBrandService(TireBrandRepository tireBrandRepository, TireBrandMapper tireBrandMapper) {
        this.tireBrandRepository = tireBrandRepository;
        this.tireBrandMapper = tireBrandMapper;
    }

    /**
     * Save a tireBrand.
     *
     * @param tireBrandDTO the entity to save.
     * @return the persisted entity.
     */
    public TireBrandDTO save(TireBrandDTO tireBrandDTO) {
        LOG.debug("Request to save TireBrand : {}", tireBrandDTO);
        TireBrand tireBrand = tireBrandMapper.toEntity(tireBrandDTO);
        tireBrand = tireBrandRepository.save(tireBrand);
        return tireBrandMapper.toDto(tireBrand);
    }

    /**
     * Update a tireBrand.
     *
     * @param tireBrandDTO the entity to save.
     * @return the persisted entity.
     */
    public TireBrandDTO update(TireBrandDTO tireBrandDTO) {
        LOG.debug("Request to update TireBrand : {}", tireBrandDTO);
        TireBrand tireBrand = tireBrandMapper.toEntity(tireBrandDTO);
        tireBrand = tireBrandRepository.save(tireBrand);
        return tireBrandMapper.toDto(tireBrand);
    }

    /**
     * Partially update a tireBrand.
     *
     * @param tireBrandDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TireBrandDTO> partialUpdate(TireBrandDTO tireBrandDTO) {
        LOG.debug("Request to partially update TireBrand : {}", tireBrandDTO);

        return tireBrandRepository
            .findById(tireBrandDTO.getId())
            .map(existingTireBrand -> {
                tireBrandMapper.partialUpdate(existingTireBrand, tireBrandDTO);

                return existingTireBrand;
            })
            .map(tireBrandRepository::save)
            .map(tireBrandMapper::toDto);
    }

    /**
     * Get all the tireBrands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TireBrandDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TireBrands");
        return tireBrandRepository.findAll(pageable).map(tireBrandMapper::toDto);
    }

    /**
     * Get one tireBrand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TireBrandDTO> findOne(Long id) {
        LOG.debug("Request to get TireBrand : {}", id);
        return tireBrandRepository.findById(id).map(tireBrandMapper::toDto);
    }

    /**
     * Delete the tireBrand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TireBrand : {}", id);
        tireBrandRepository.deleteById(id);
    }
}
