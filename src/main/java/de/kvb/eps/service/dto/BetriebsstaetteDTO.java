package de.kvb.eps.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.kvb.eps.domain.Betriebsstaette} entity.
 */
public class BetriebsstaetteDTO implements Serializable {

    private Long id;

    private String bsnr;

    private String strasse;

    private String hausnummer;

    private String plz;

    private String ort;

    private String bezeichnung;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBsnr() {
        return bsnr;
    }

    public void setBsnr(String bsnr) {
        this.bsnr = bsnr;
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

        BetriebsstaetteDTO betriebsstaetteDTO = (BetriebsstaetteDTO) o;
        if (betriebsstaetteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), betriebsstaetteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BetriebsstaetteDTO{" +
            "id=" + getId() +
            ", bsnr='" + getBsnr() + "'" +
            ", strasse='" + getStrasse() + "'" +
            ", hausnummer='" + getHausnummer() + "'" +
            ", plz='" + getPlz() + "'" +
            ", ort='" + getOrt() + "'" +
            ", bezeichnung='" + getBezeichnung() + "'" +
            "}";
    }
}
