package de.kvb.eps.service.mapper;


import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.ZubehoerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Zubehoer} and its DTO {@link ZubehoerDTO}.
 */
@Mapper(componentModel = "spring", uses = {HerstellerMapper.class, ZubehoerTypMapper.class})
public interface ZubehoerMapper extends EntityMapper<ZubehoerDTO, Zubehoer> {

    @Mapping(source = "hersteller.id", target = "herstellerId")
    @Mapping(source = "hersteller.bezeichnung", target = "herstellerBezeichnung")
    @Mapping(source = "zubehoerTyp.id", target = "zubehoerTypId")
    @Mapping(source = "zubehoerTyp.bezeichnung", target = "zubehoerTypBezeichnung")
    ZubehoerDTO toDto(Zubehoer zubehoer);

    @Mapping(target = "systemtyps", ignore = true)
    @Mapping(target = "removeSystemtyp", ignore = true)
    @Mapping(source = "herstellerId", target = "hersteller")
    @Mapping(source = "zubehoerTypId", target = "zubehoerTyp")
    Zubehoer toEntity(ZubehoerDTO zubehoerDTO);

    default Zubehoer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Zubehoer zubehoer = new Zubehoer();
        zubehoer.setId(id);
        return zubehoer;
    }
}
