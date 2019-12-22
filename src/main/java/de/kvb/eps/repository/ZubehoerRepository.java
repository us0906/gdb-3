package de.kvb.eps.repository;
import de.kvb.eps.domain.Zubehoer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Zubehoer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZubehoerRepository extends JpaRepository<Zubehoer, Long> {

}
