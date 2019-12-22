package de.kvb.eps.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.kvb.eps.domain.Zubehoer} entity.
 */
public class ZubehoerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String bezeichnung;

    private LocalDate gueltigBis;


    private Long herstellerId;

    private String herstellerBezeichnung;

    private Long zubehoerTypId;

    private String zubehoerTypBezeichnung;

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

    public Long getHerstellerId() {
        return herstellerId;
    }

    public void setHerstellerId(Long herstellerId) {
        this.herstellerId = herstellerId;
    }

    public String getHerstellerBezeichnung() {
        return herstellerBezeichnung;
    }

    public void setHerstellerBezeichnung(String herstellerBezeichnung) {
        this.herstellerBezeichnung = herstellerBezeichnung;
    }

    public Long getZubehoerTypId() {
        return zubehoerTypId;
    }

    public void setZubehoerTypId(Long zubehoerTypId) {
        this.zubehoerTypId = zubehoerTypId;
    }

    public String getZubehoerTypBezeichnung() {
        return zubehoerTypBezeichnung;
    }

    public void setZubehoerTypBezeichnung(String zubehoerTypBezeichnung) {
        this.zubehoerTypBezeichnung = zubehoerTypBezeichnung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ZubehoerDTO zubehoerDTO = (ZubehoerDTO) o;
        if (zubehoerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), zubehoerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ZubehoerDTO{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            ", hersteller=" + getHerstellerId() +
            ", hersteller='" + getHerstellerBezeichnung() + "'" +
            ", zubehoerTyp=" + getZubehoerTypId() +
            ", zubehoerTyp='" + getZubehoerTypBezeichnung() + "'" +
            "}";
    }
}
