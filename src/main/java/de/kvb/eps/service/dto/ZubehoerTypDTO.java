package de.kvb.eps.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import de.kvb.eps.domain.enumeration.Technologie;

/**
 * A DTO for the {@link de.kvb.eps.domain.ZubehoerTyp} entity.
 */
public class ZubehoerTypDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String bezeichnung;

    private LocalDate gueltigBis;

    @NotNull
    private Technologie technologie;


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

    public Technologie getTechnologie() {
        return technologie;
    }

    public void setTechnologie(Technologie technologie) {
        this.technologie = technologie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ZubehoerTypDTO zubehoerTypDTO = (ZubehoerTypDTO) o;
        if (zubehoerTypDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), zubehoerTypDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ZubehoerTypDTO{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            ", technologie='" + getTechnologie() + "'" +
            "}";
    }
}
