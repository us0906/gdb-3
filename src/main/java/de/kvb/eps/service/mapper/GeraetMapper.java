package de.kvb.eps.service.mapper;


import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.GeraetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Geraet} and its DTO {@link GeraetDTO}.
 */
@Mapper(componentModel = "spring", uses = {GeraetTypMapper.class, HerstellerMapper.class})
public interface GeraetMapper extends EntityMapper<GeraetDTO, Geraet> {

    @Mapping(source = "geraetTyp.id", target = "geraetTypId")
    @Mapping(source = "geraetTyp.bezeichnung", target = "geraetTypBezeichnung")
    @Mapping(source = "hersteller.id", target = "herstellerId")
    @Mapping(source = "hersteller.bezeichnung", target = "herstellerBezeichnung")
    GeraetDTO toDto(Geraet geraet);

    @Mapping(target = "systemtyps", ignore = true)
    @Mapping(target = "removeSystemtyp", ignore = true)
    @Mapping(source = "geraetTypId", target = "geraetTyp")
    @Mapping(source = "herstellerId", target = "hersteller")
    Geraet toEntity(GeraetDTO geraetDTO);

    default Geraet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Geraet geraet = new Geraet();
        geraet.setId(id);
        return geraet;
    }
}
