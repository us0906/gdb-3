package de.kvb.eps.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.kvb.eps.domain.Arzt} entity.
 */
public class ArztDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 7, max = 7)
    private String lanr;

    private String titel;

    @NotNull
    private String vorname;

    @NotNull
    private String nachname;

    private String bezeichnung;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanr() {
        return lanr;
    }

    public void setLanr(String lanr) {
        this.lanr = lanr;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArztDTO arztDTO = (ArztDTO) o;
        if (arztDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), arztDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArztDTO{" +
            "id=" + getId() +
            ", lanr='" + getLanr() + "'" +
            ", titel='" + getTitel() + "'" +
            ", vorname='" + getVorname() + "'" +
            ", nachname='" + getNachname() + "'" +
            ", bezeichnung='" + getBezeichnung() + "'" +
            "}";
    }
}
