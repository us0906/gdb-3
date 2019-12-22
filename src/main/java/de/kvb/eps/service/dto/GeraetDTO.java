package de.kvb.eps.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.kvb.eps.domain.Geraet} entity.
 */
public class GeraetDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String bezeichnung;

    private LocalDate gueltigBis;


    private Long geraetTypId;

    private String geraetTypBezeichnung;

    private Long herstellerId;

    private String herstellerBezeichnung;

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

    public Long getGeraetTypId() {
        return geraetTypId;
    }

    public void setGeraetTypId(Long geraetTypId) {
        this.geraetTypId = geraetTypId;
    }

    public String getGeraetTypBezeichnung() {
        return geraetTypBezeichnung;
    }

    public void setGeraetTypBezeichnung(String geraetTypBezeichnung) {
        this.geraetTypBezeichnung = geraetTypBezeichnung;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GeraetDTO geraetDTO = (GeraetDTO) o;
        if (geraetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), geraetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeraetDTO{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            ", geraetTyp=" + getGeraetTypId() +
            ", geraetTyp='" + getGeraetTypBezeichnung() + "'" +
            ", hersteller=" + getHerstellerId() +
            ", hersteller='" + getHerstellerBezeichnung() + "'" +
            "}";
    }
}
