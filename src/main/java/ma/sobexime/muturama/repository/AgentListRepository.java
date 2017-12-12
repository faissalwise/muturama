package ma.sobexime.muturama.repository;

import ma.sobexime.muturama.domain.AgentList;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AgentList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentListRepository extends JpaRepository<AgentList, Long> {

}
