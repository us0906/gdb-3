package de.kvb.eps.repository;
import de.kvb.eps.domain.Betreiber;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Betreiber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BetreiberRepository extends JpaRepository<Betreiber, Long>, JpaSpecificationExecutor<Betreiber> {

}
