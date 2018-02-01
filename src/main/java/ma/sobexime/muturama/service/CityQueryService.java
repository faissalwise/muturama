package ma.sobexime.muturama.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import ma.sobexime.muturama.domain.City;
import ma.sobexime.muturama.domain.*; // for static metamodels
import ma.sobexime.muturama.repository.CityRepository;
import ma.sobexime.muturama.repository.search.CitySearchRepository;
import ma.sobexime.muturama.service.dto.CityCriteria;


/**
 * Service for executing complex queries for City entities in the database.
 * The main input is a {@link CityCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link City} or a {@link Page} of {%link City} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class CityQueryService extends QueryService<City> {

    private final Logger log = LoggerFactory.getLogger(CityQueryService.class);


    private final CityRepository cityRepository;

    private final CitySearchRepository citySearchRepository;

    public CityQueryService(CityRepository cityRepository, CitySearchRepository citySearchRepository) {
        this.cityRepository = cityRepository;
        this.citySearchRepository = citySearchRepository;
    }

    /**
     * Return a {@link List} of {%link City} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<City> findByCriteria(CityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<City> specification = createSpecification(criteria);
        return cityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link City} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<City> findByCriteria(CityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<City> specification = createSpecification(criteria);
        return cityRepository.findAll(specification, page);
    }

    /**
     * Function to convert CityCriteria to a {@link Specifications}
     */
    private Specifications<City> createSpecification(CityCriteria criteria) {
        Specifications<City> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), City_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), City_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), City_.status));
            }
        }
        return specification;
    }

}
