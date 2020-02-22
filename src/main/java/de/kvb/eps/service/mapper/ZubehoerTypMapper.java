package de.kvb.eps.service.mapper;


import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.ZubehoerTypDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ZubehoerTyp} and its DTO {@link ZubehoerTypDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ZubehoerTypMapper extends EntityMapper<ZubehoerTypDTO, ZubehoerTyp> {


    @Mapping(target = "zubehoers", ignore = true)
    @Mapping(target = "removeZubehoer", ignore = true)
    ZubehoerTyp toEntity(ZubehoerTypDTO zubehoerTypDTO);

    default ZubehoerTyp fromId(Long id) {
        if (id == null) {
            return null;
        }
        ZubehoerTyp zubehoerTyp = new ZubehoerTyp();
        zubehoerTyp.setId(id);
        return zubehoerTyp;
    }
}
