package de.kvb.eps.repository;
import de.kvb.eps.domain.Hersteller;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Hersteller entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HerstellerRepository extends JpaRepository<Hersteller, Long> {

}
