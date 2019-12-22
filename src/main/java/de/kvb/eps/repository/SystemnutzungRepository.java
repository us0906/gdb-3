package de.kvb.eps.repository;
import de.kvb.eps.domain.Systemnutzung;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Systemnutzung entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemnutzungRepository extends JpaRepository<Systemnutzung, Long> {

}
