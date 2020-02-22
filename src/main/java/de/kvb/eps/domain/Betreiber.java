package de.kvb.eps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Betreiber.
 */
@Entity
@Table(name = "betreiber")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "betreiber")
public class Betreiber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "vorname", nullable = false)
    private String vorname;

    @NotNull
    @Column(name = "nachname", nullable = false)
    private String nachname;

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

    @OneToMany(mappedBy = "betreiber")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Systeminstanz> systeminstanzs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public Betreiber vorname(String vorname) {
        this.vorname = vorname;
        return this;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public Betreiber nachname(String nachname) {
        this.nachname = nachname;
        return this;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getStrasse() {
        return strasse;
    }

    public Betreiber strasse(String strasse) {
        this.strasse = strasse;
        return this;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public Betreiber hausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
        return this;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public Betreiber plz(String plz) {
        this.plz = plz;
        return this;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public Betreiber ort(String ort) {
        this.ort = ort;
        return this;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getBezeichnung() {

        return vorname + " " + nachname + " " + strasse + " " + hausnummer + " " + plz + " " + ort;
    }

    public Betreiber bezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Set<Systeminstanz> getSysteminstanzs() {
        return systeminstanzs;
    }

    public Betreiber systeminstanzs(Set<Systeminstanz> systeminstanzs) {
        this.systeminstanzs = systeminstanzs;
        return this;
    }

    public Betreiber addSysteminstanz(Systeminstanz systeminstanz) {
        this.systeminstanzs.add(systeminstanz);
        systeminstanz.setBetreiber(this);
        return this;
    }

    public Betreiber removeSysteminstanz(Systeminstanz systeminstanz) {
        this.systeminstanzs.remove(systeminstanz);
        systeminstanz.setBetreiber(null);
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
        if (!(o instanceof Betreiber)) {
            return false;
        }
        return id != null && id.equals(((Betreiber) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Betreiber{" +
            "id=" + getId() +
            ", vorname='" + getVorname() + "'" +
            ", nachname='" + getNachname() + "'" +
            ", strasse='" + getStrasse() + "'" +
            ", hausnummer='" + getHausnummer() + "'" +
            ", plz='" + getPlz() + "'" +
            ", ort='" + getOrt() + "'" +
            ", bezeichnung='" + getBezeichnung() + "'" +
            "}";
    }
}
