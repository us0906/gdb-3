package de.kvb.eps.repository;
import de.kvb.eps.domain.ZubehoerTyp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ZubehoerTyp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZubehoerTypRepository extends JpaRepository<ZubehoerTyp, Long>, JpaSpecificationExecutor<ZubehoerTyp> {

}
