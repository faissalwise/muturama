package ma.sobexime.muturama.repository;

import ma.sobexime.muturama.domain.Service;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Service entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    @Query("select distinct service from Service service left join fetch service.user_services")
    List<Service> findAllWithEagerRelationships();

    @Query("select service from Service service left join fetch service.user_services where service.id =:id")
    Service findOneWithEagerRelationships(@Param("id") Long id);

}
