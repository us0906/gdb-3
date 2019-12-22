package de.kvb.eps.repository;
import de.kvb.eps.domain.Systeminstanz;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Systeminstanz entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysteminstanzRepository extends JpaRepository<Systeminstanz, Long> {

}
