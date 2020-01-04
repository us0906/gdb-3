package de.kvb.eps.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Systeminstanz.
 */
@Entity
@Table(name = "sys_inst")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "systeminstanz")
public class Systeminstanz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "bezeichnung", length = 200, nullable = false)
    private String bezeichnung;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "geraet_nummer", length = 200, nullable = false)
    private String geraetNummer;

    @NotNull
    @Size(min = 4, max = 4)
    @Column(name = "geraet_baujahr", length = 4, nullable = false)
    private String geraetBaujahr;

    @Column(name = "gueltig_bis")
    private LocalDate gueltigBis;

    @Lob
    @Column(name = "gwe")
    private byte[] gwe;

    @Column(name = "gwe_content_type")
    private String gweContentType;

    @Lob
    @Column(name = "bemerkung")
    private String bemerkung;

    @OneToMany(mappedBy = "systeminstanz")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Systemnutzung> systemnutzungs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("systeminstanzs")
    private Systemtyp systemtyp;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("systeminstanzs")
    private Betriebsstaette betriebsstaette;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("systeminstanzs")
    private Betreiber betreiber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public Systeminstanz bezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getGeraetNummer() {
        return geraetNummer;
    }

    public Systeminstanz geraetNummer(String geraetNummer) {
        this.geraetNummer = geraetNummer;
        return this;
    }

    public void setGeraetNummer(String geraetNummer) {
        this.geraetNummer = geraetNummer;
    }

    public String getGeraetBaujahr() {
        return geraetBaujahr;
    }

    public Systeminstanz geraetBaujahr(String geraetBaujahr) {
        this.geraetBaujahr = geraetBaujahr;
        return this;
    }

    public void setGeraetBaujahr(String geraetBaujahr) {
        this.geraetBaujahr = geraetBaujahr;
    }

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public Systeminstanz gueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
        return this;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public byte[] getGwe() {
        return gwe;
    }

    public Systeminstanz gwe(byte[] gwe) {
        this.gwe = gwe;
        return this;
    }

    public void setGwe(byte[] gwe) {
        this.gwe = gwe;
    }

    public String getGweContentType() {
        return gweContentType;
    }

    public Systeminstanz gweContentType(String gweContentType) {
        this.gweContentType = gweContentType;
        return this;
    }

    public void setGweContentType(String gweContentType) {
        this.gweContentType = gweContentType;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public Systeminstanz bemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
        return this;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    public Set<Systemnutzung> getSystemnutzungs() {
        return systemnutzungs;
    }

    public Systeminstanz systemnutzungs(Set<Systemnutzung> systemnutzungs) {
        this.systemnutzungs = systemnutzungs;
        return this;
    }

    public Systeminstanz addSystemnutzung(Systemnutzung systemnutzung) {
        this.systemnutzungs.add(systemnutzung);
        systemnutzung.setSysteminstanz(this);
        return this;
    }

    public Systeminstanz removeSystemnutzung(Systemnutzung systemnutzung) {
        this.systemnutzungs.remove(systemnutzung);
        systemnutzung.setSysteminstanz(null);
        return this;
    }

    public void setSystemnutzungs(Set<Systemnutzung> systemnutzungs) {
        this.systemnutzungs = systemnutzungs;
    }

    public Systemtyp getSystemtyp() {
        return systemtyp;
    }

    public Systeminstanz systemtyp(Systemtyp systemtyp) {
        this.systemtyp = systemtyp;
        return this;
    }

    public void setSystemtyp(Systemtyp systemtyp) {
        this.systemtyp = systemtyp;
    }

    public Betriebsstaette getBetriebsstaette() {
        return betriebsstaette;
    }

    public Systeminstanz betriebsstaette(Betriebsstaette betriebsstaette) {
        this.betriebsstaette = betriebsstaette;
        return this;
    }

    public void setBetriebsstaette(Betriebsstaette betriebsstaette) {
        this.betriebsstaette = betriebsstaette;
    }

    public Betreiber getBetreiber() {
        return betreiber;
    }

    public Systeminstanz betreiber(Betreiber betreiber) {
        this.betreiber = betreiber;
        return this;
    }

    public void setBetreiber(Betreiber betreiber) {
        this.betreiber = betreiber;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Systeminstanz)) {
            return false;
        }
        return id != null && id.equals(((Systeminstanz) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Systeminstanz{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", geraetNummer='" + getGeraetNummer() + "'" +
            ", geraetBaujahr='" + getGeraetBaujahr() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            ", gwe='" + getGwe() + "'" +
            ", gweContentType='" + getGweContentType() + "'" +
            ", bemerkung='" + getBemerkung() + "'" +
            "}";
    }
}
