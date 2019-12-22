package de.kvb.eps.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.kvb.eps.domain.Betreiber} entity.
 */
public class BetreiberDTO implements Serializable {

    private Long id;

    @NotNull
    private String vorname;

    @NotNull
    private String nachname;

    private String strasse;

    private String hausnummer;

    private String plz;

    private String ort;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BetreiberDTO betreiberDTO = (BetreiberDTO) o;
        if (betreiberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), betreiberDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BetreiberDTO{" +
            "id=" + getId() +
            ", vorname='" + getVorname() + "'" +
            ", nachname='" + getNachname() + "'" +
            ", strasse='" + getStrasse() + "'" +
            ", hausnummer='" + getHausnummer() + "'" +
            ", plz='" + getPlz() + "'" +
            ", ort='" + getOrt() + "'" +
            "}";
    }
}
