package de.kvb.eps.repository;

import de.kvb.eps.domain.Geraet;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Geraet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeraetRepository extends JpaRepository<Geraet, Long>, JpaSpecificationExecutor<Geraet> {

}
