package de.kvb.eps.repository;
import de.kvb.eps.domain.Systemtyp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Systemtyp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemtypRepository extends JpaRepository<Systemtyp, Long> {

}
