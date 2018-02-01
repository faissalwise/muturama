package ma.sobexime.muturama.repository;

import ma.sobexime.muturama.domain.MuturaServices;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MuturaServices entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MuturaServicesRepository extends JpaRepository<MuturaServices, Long> {

}
