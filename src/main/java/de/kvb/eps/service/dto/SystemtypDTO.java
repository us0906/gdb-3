package de.kvb.eps.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.kvb.eps.domain.Systemtyp} entity.
 */
public class SystemtypDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String bezeichnung;

    private LocalDate gueltigBis;


    private Long geraetId;

    private String geraetBezeichnung;

    private Long zubehoerId;

    private String zubehoerBezeichnung;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public Long getGeraetId() {
        return geraetId;
    }

    public void setGeraetId(Long geraetId) {
        this.geraetId = geraetId;
    }

    public String getGeraetBezeichnung() {
        return geraetBezeichnung;
    }

    public void setGeraetBezeichnung(String geraetBezeichnung) {
        this.geraetBezeichnung = geraetBezeichnung;
    }

    public Long getZubehoerId() {
        return zubehoerId;
    }

    public void setZubehoerId(Long zubehoerId) {
        this.zubehoerId = zubehoerId;
    }

    public String getZubehoerBezeichnung() {
        return zubehoerBezeichnung;
    }

    public void setZubehoerBezeichnung(String zubehoerBezeichnung) {
        this.zubehoerBezeichnung = zubehoerBezeichnung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemtypDTO systemtypDTO = (SystemtypDTO) o;
        if (systemtypDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemtypDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemtypDTO{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            ", geraetId=" + getGeraetId() +
            ", geraetBezeichnung='" + getGeraetBezeichnung() + "'" +
            ", zubehoerId=" + getZubehoerId() +
            ", zubehoerBezeichnung='" + getZubehoerBezeichnung() + "'" +
            "}";
    }
}
