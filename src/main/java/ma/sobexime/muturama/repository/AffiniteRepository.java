package ma.sobexime.muturama.repository;

import ma.sobexime.muturama.domain.Affinite;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Affinite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffiniteRepository extends JpaRepository<Affinite, Long> {

}
