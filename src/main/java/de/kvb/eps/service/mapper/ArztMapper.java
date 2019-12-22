package de.kvb.eps.service.mapper;

import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.ArztDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Arzt} and its DTO {@link ArztDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArztMapper extends EntityMapper<ArztDTO, Arzt> {


    @Mapping(target = "systemnutzungs", ignore = true)
    @Mapping(target = "removeSystemnutzung", ignore = true)
    Arzt toEntity(ArztDTO arztDTO);

    default Arzt fromId(Long id) {
        if (id == null) {
            return null;
        }
        Arzt arzt = new Arzt();
        arzt.setId(id);
        return arzt;
    }
}
