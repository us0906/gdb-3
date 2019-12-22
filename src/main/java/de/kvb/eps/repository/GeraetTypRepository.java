package de.kvb.eps.repository;
import de.kvb.eps.domain.GeraetTyp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GeraetTyp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeraetTypRepository extends JpaRepository<GeraetTyp, Long> {

}
