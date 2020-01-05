package de.kvb.eps.service.mapper;

import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.SystemnutzungDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Systemnutzung} and its DTO {@link SystemnutzungDTO}.
 */
@Mapper(componentModel = "spring", uses = {SysteminstanzMapper.class, ArztMapper.class})
public interface SystemnutzungMapper extends EntityMapper<SystemnutzungDTO, Systemnutzung> {

    @Mapping(source = "systeminstanz.id", target = "systeminstanzId")
    @Mapping(source = "systeminstanz.bezeichnung", target = "systeminstanzBezeichnung")
    @Mapping(source = "arzt.id", target = "arztId")
    @Mapping(source = "arzt.bezeichnung", target = "arztBezeichnung")
    SystemnutzungDTO toDto(Systemnutzung systemnutzung);

    @Mapping(source = "systeminstanzId", target = "systeminstanz")
    @Mapping(source = "arztId", target = "arzt")
    Systemnutzung toEntity(SystemnutzungDTO systemnutzungDTO);

    default Systemnutzung fromId(Long id) {
        if (id == null) {
            return null;
        }
        Systemnutzung systemnutzung = new Systemnutzung();
        systemnutzung.setId(id);
        return systemnutzung;
    }
}
