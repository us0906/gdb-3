package de.kvb.eps.service.mapper;

import de.kvb.eps.domain.*;
import de.kvb.eps.service.dto.SysteminstanzDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Systeminstanz} and its DTO {@link SysteminstanzDTO}.
 */
@Mapper(componentModel = "spring", uses = {SystemtypMapper.class, BetriebsstaetteMapper.class, BetreiberMapper.class})
public interface SysteminstanzMapper extends EntityMapper<SysteminstanzDTO, Systeminstanz> {

    @Mapping(source = "systemtyp.id", target = "systemtypId")
    @Mapping(source = "systemtyp.bezeichnung", target = "systemtypBezeichnung")
    @Mapping(source = "betriebsstaette.id", target = "betriebsstaetteId")
    @Mapping(source = "betriebsstaette.bezeichnung", target = "betriebsstaetteBezeichnung")
    @Mapping(source = "betreiber.id", target = "betreiberId")
    @Mapping(source = "betreiber.bezeichnung", target = "betreiberBezeichnung")
    SysteminstanzDTO toDto(Systeminstanz systeminstanz);

    @Mapping(target = "systemnutzungs", ignore = true)
    @Mapping(target = "removeSystemnutzung", ignore = true)
    @Mapping(source = "systemtypId", target = "systemtyp")
    @Mapping(source = "betriebsstaetteId", target = "betriebsstaette")
    @Mapping(source = "betreiberId", target = "betreiber")
    Systeminstanz toEntity(SysteminstanzDTO systeminstanzDTO);

    default Systeminstanz fromId(Long id) {
        if (id == null) {
            return null;
        }
        Systeminstanz systeminstanz = new Systeminstanz();
        systeminstanz.setId(id);
        return systeminstanz;
    }
}
