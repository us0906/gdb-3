package de.kvb.eps.service.mapper;


import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.BetriebsstaetteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Betriebsstaette} and its DTO {@link BetriebsstaetteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BetriebsstaetteMapper extends EntityMapper<BetriebsstaetteDTO, Betriebsstaette> {


    @Mapping(target = "systeminstanzs", ignore = true)
    @Mapping(target = "removeSysteminstanz", ignore = true)
    Betriebsstaette toEntity(BetriebsstaetteDTO betriebsstaetteDTO);

    default Betriebsstaette fromId(Long id) {
        if (id == null) {
            return null;
        }
        Betriebsstaette betriebsstaette = new Betriebsstaette();
        betriebsstaette.setId(id);
        return betriebsstaette;
    }
}
