package fr.ecom.mstr.tire.service;

import fr.ecom.mstr.tire.domain.*; // for static metamodels
import fr.ecom.mstr.tire.domain.Tire;
import fr.ecom.mstr.tire.repository.TireRepository;
import fr.ecom.mstr.tire.service.criteria.TireCriteria;
import fr.ecom.mstr.tire.service.dto.TireDTO;
import fr.ecom.mstr.tire.service.mapper.TireMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Tire} entities in the database.
 * The main input is a {@link TireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TireQueryService extends QueryService<Tire> {

    private static final Logger LOG = LoggerFactory.getLogger(TireQueryService.class);

    private final TireRepository tireRepository;

    private final TireMapper tireMapper;

    public TireQueryService(TireRepository tireRepository, TireMapper tireMapper) {
        this.tireRepository = tireRepository;
        this.tireMapper = tireMapper;
    }

    /**
     * Return a {@link Page} of {@link TireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TireDTO> findByCriteria(TireCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tire> specification = createSpecification(criteria);
        return tireRepository.findAll(specification, page).map(tireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TireCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Tire> specification = createSpecification(criteria);
        return tireRepository.count(specification);
    }

    /**
     * Function to convert {@link TireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tire> createSpecification(TireCriteria criteria) {
        Specification<Tire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tire_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), Tire_.reference));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Tire_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Tire_.price));
            }
            if (criteria.getTireWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTireWidth(), Tire_.tireWidth));
            }
            if (criteria.getTireHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTireHeight(), Tire_.tireHeight));
            }
            if (criteria.getTireDiameter() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTireDiameter(), Tire_.tireDiameter));
            }
            if (criteria.getTireType() != null) {
                specification = specification.and(buildSpecification(criteria.getTireType(), Tire_.tireType));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), Tire_.imageUrl));
            }
            if (criteria.getSpeedIndex() != null) {
                specification = specification.and(buildSpecification(criteria.getSpeedIndex(), Tire_.speedIndex));
            }
            if (criteria.getWeightIndex() != null) {
                specification = specification.and(buildSpecification(criteria.getWeightIndex(), Tire_.weightIndex));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Tire_.quantity));
            }
            if (criteria.getDisable() != null) {
                specification = specification.and(buildSpecification(criteria.getDisable(), Tire_.disable));
            }
            if (criteria.getDisableReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisableReason(), Tire_.disableReason));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Tire_.description));
            }
            if (criteria.getItemListLocksId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getItemListLocksId(), root ->
                        root.join(Tire_.itemListLocks, JoinType.LEFT).get(ItemListLock_.id)
                    )
                );
            }
            if (criteria.getTireBrandId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTireBrandId(), root -> root.join(Tire_.tireBrand, JoinType.LEFT).get(TireBrand_.id))
                );
            }
        }
        return specification;
    }
}
