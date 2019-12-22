package de.kvb.eps.service.mapper;

import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.SystemtypDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Systemtyp} and its DTO {@link SystemtypDTO}.
 */
@Mapper(componentModel = "spring", uses = {GeraetMapper.class, ZubehoerMapper.class})
public interface SystemtypMapper extends EntityMapper<SystemtypDTO, Systemtyp> {

    @Mapping(source = "geraet.id", target = "geraetId")
    @Mapping(source = "geraet.bezeichnung", target = "geraetBezeichnung")
    @Mapping(source = "zubehoer.id", target = "zubehoerId")
    @Mapping(source = "zubehoer.bezeichnung", target = "zubehoerBezeichnung")
    SystemtypDTO toDto(Systemtyp systemtyp);

    @Mapping(target = "systeminstanzs", ignore = true)
    @Mapping(target = "removeSysteminstanz", ignore = true)
    @Mapping(source = "geraetId", target = "geraet")
    @Mapping(source = "zubehoerId", target = "zubehoer")
    Systemtyp toEntity(SystemtypDTO systemtypDTO);

    default Systemtyp fromId(Long id) {
        if (id == null) {
            return null;
        }
        Systemtyp systemtyp = new Systemtyp();
        systemtyp.setId(id);
        return systemtyp;
    }
}
