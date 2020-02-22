package de.kvb.eps.repository;

import de.kvb.eps.domain.Betriebsstaette;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Betriebsstaette entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BetriebsstaetteRepository extends JpaRepository<Betriebsstaette, Long>, JpaSpecificationExecutor<Betriebsstaette> {

}
