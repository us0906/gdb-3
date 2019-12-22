package de.kvb.eps.service.mapper;

import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.GeraetTypDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link GeraetTyp} and its DTO {@link GeraetTypDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GeraetTypMapper extends EntityMapper<GeraetTypDTO, GeraetTyp> {


    @Mapping(target = "geraets", ignore = true)
    @Mapping(target = "removeGeraet", ignore = true)
    GeraetTyp toEntity(GeraetTypDTO geraetTypDTO);

    default GeraetTyp fromId(Long id) {
        if (id == null) {
            return null;
        }
        GeraetTyp geraetTyp = new GeraetTyp();
        geraetTyp.setId(id);
        return geraetTyp;
    }
}
