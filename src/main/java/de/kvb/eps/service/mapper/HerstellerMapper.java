package de.kvb.eps.service.mapper;


import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.HerstellerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hersteller} and its DTO {@link HerstellerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HerstellerMapper extends EntityMapper<HerstellerDTO, Hersteller> {


    @Mapping(target = "geraets", ignore = true)
    @Mapping(target = "removeGeraet", ignore = true)
    @Mapping(target = "zubehoers", ignore = true)
    @Mapping(target = "removeZubehoer", ignore = true)
    Hersteller toEntity(HerstellerDTO herstellerDTO);

    default Hersteller fromId(Long id) {
        if (id == null) {
            return null;
        }
        Hersteller hersteller = new Hersteller();
        hersteller.setId(id);
        return hersteller;
    }
}
