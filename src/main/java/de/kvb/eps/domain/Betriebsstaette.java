package de.kvb.eps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Betriebsstaette.
 */
@Entity
@Table(name = "b_staette")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "betriebsstaette")
public class Betriebsstaette implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bsnr")
    private String bsnr;

    @Column(name = "strasse")
    private String strasse;

    @Column(name = "hausnummer")
    private String hausnummer;

    @Column(name = "plz")
    private String plz;

    @Column(name = "ort")
    private String ort;

    @Column(name = "bezeichnung")
    private String bezeichnung;

    @OneToMany(mappedBy = "betriebsstaette")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Systeminstanz> systeminstanzs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBsnr() {
        return bsnr;
    }

    public Betriebsstaette bsnr(String bsnr) {
        this.bsnr = bsnr;
        return this;
    }

    public void setBsnr(String bsnr) {
        this.bsnr = bsnr;
    }

    public String getStrasse() {
        return strasse;
    }

    public Betriebsstaette strasse(String strasse) {
        this.strasse = strasse;
        return this;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public Betriebsstaette hausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
        return this;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public Betriebsstaette plz(String plz) {
        this.plz = plz;
        return this;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public Betriebsstaette ort(String ort) {
        this.ort = ort;
        return this;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getBezeichnung() {

        return bsnr + " " + plz + " " + ort + " " + strasse + " " + hausnummer;
    }

    public Betriebsstaette bezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Set<Systeminstanz> getSysteminstanzs() {
        return systeminstanzs;
    }

    public Betriebsstaette systeminstanzs(Set<Systeminstanz> systeminstanzs) {
        this.systeminstanzs = systeminstanzs;
        return this;
    }

    public Betriebsstaette addSysteminstanz(Systeminstanz systeminstanz) {
        this.systeminstanzs.add(systeminstanz);
        systeminstanz.setBetriebsstaette(this);
        return this;
    }

    public Betriebsstaette removeSysteminstanz(Systeminstanz systeminstanz) {
        this.systeminstanzs.remove(systeminstanz);
        systeminstanz.setBetriebsstaette(null);
        return this;
    }

    public void setSysteminstanzs(Set<Systeminstanz> systeminstanzs) {
        this.systeminstanzs = systeminstanzs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Betriebsstaette)) {
            return false;
        }
        return id != null && id.equals(((Betriebsstaette) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Betriebsstaette{" +
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
