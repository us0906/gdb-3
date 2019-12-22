package de.kvb.eps.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link de.kvb.eps.domain.Systeminstanz} entity.
 */
public class SysteminstanzDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String bezeichnung;

    @NotNull
    @Size(min = 1, max = 200)
    private String geraetNummer;

    @NotNull
    @Size(min = 4, max = 4)
    private String geraetBaujahr;

    private LocalDate gueltigBis;

    @Lob
    private byte[] gwe;

    private String gweContentType;
    @Lob
    private String bemerkung;


    private Long systemtypId;

    private String systemtypBezeichnung;

    private Long betriebsstaetteId;

    private Long betreiberId;

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

    public String getGeraetNummer() {
        return geraetNummer;
    }

    public void setGeraetNummer(String geraetNummer) {
        this.geraetNummer = geraetNummer;
    }

    public String getGeraetBaujahr() {
        return geraetBaujahr;
    }

    public void setGeraetBaujahr(String geraetBaujahr) {
        this.geraetBaujahr = geraetBaujahr;
    }

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public byte[] getGwe() {
        return gwe;
    }

    public void setGwe(byte[] gwe) {
        this.gwe = gwe;
    }

    public String getGweContentType() {
        return gweContentType;
    }

    public void setGweContentType(String gweContentType) {
        this.gweContentType = gweContentType;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    public Long getSystemtypId() {
        return systemtypId;
    }

    public void setSystemtypId(Long systemtypId) {
        this.systemtypId = systemtypId;
    }

    public String getSystemtypBezeichnung() {
        return systemtypBezeichnung;
    }

    public void setSystemtypBezeichnung(String systemtypBezeichnung) {
        this.systemtypBezeichnung = systemtypBezeichnung;
    }

    public Long getBetriebsstaetteId() {
        return betriebsstaetteId;
    }

    public void setBetriebsstaetteId(Long betriebsstaetteId) {
        this.betriebsstaetteId = betriebsstaetteId;
    }

    public Long getBetreiberId() {
        return betreiberId;
    }

    public void setBetreiberId(Long betreiberId) {
        this.betreiberId = betreiberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SysteminstanzDTO systeminstanzDTO = (SysteminstanzDTO) o;
        if (systeminstanzDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systeminstanzDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SysteminstanzDTO{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", geraetNummer='" + getGeraetNummer() + "'" +
            ", geraetBaujahr='" + getGeraetBaujahr() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            ", gwe='" + getGwe() + "'" +
            ", bemerkung='" + getBemerkung() + "'" +
            ", systemtyp=" + getSystemtypId() +
            ", systemtyp='" + getSystemtypBezeichnung() + "'" +
            ", betriebsstaette=" + getBetriebsstaetteId() +
            ", betreiber=" + getBetreiberId() +
            "}";
    }
}
