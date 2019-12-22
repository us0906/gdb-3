package de.kvb.eps.repository;
import de.kvb.eps.domain.Arzt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Arzt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArztRepository extends JpaRepository<Arzt, Long> {

}
