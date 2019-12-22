package de.kvb.eps.service.mapper;

import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.BetreiberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Betreiber} and its DTO {@link BetreiberDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BetreiberMapper extends EntityMapper<BetreiberDTO, Betreiber> {


    @Mapping(target = "systeminstanzs", ignore = true)
    @Mapping(target = "removeSysteminstanz", ignore = true)
    Betreiber toEntity(BetreiberDTO betreiberDTO);

    default Betreiber fromId(Long id) {
        if (id == null) {
            return null;
        }
        Betreiber betreiber = new Betreiber();
        betreiber.setId(id);
        return betreiber;
    }
}
