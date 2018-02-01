package ma.sobexime.muturama.repository;

import ma.sobexime.muturama.domain.Infohain;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Infohain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfohainRepository extends JpaRepository<Infohain, Long> {

}
