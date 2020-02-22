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
 * A Arzt.
 */
@Entity
@Table(name = "arzt")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "arzt")
public class Arzt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 7, max = 7)
    @Column(name = "lanr", length = 7, nullable = false)
    private String lanr;

    @Column(name = "titel")
    private String titel;

    @NotNull
    @Column(name = "vorname", nullable = false)
    private String vorname;

    @NotNull
    @Column(name = "nachname", nullable = false)
    private String nachname;

    @Column(name = "bezeichnung")
    private String bezeichnung;

    @OneToMany(mappedBy = "arzt")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Systemnutzung> systemnutzungs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanr() {
        return lanr;
    }

    public Arzt lanr(String lanr) {
        this.lanr = lanr;
        return this;
    }

    public void setLanr(String lanr) {
        this.lanr = lanr;
    }

    public String getTitel() {
        return titel;
    }

    public Arzt titel(String titel) {
        this.titel = titel;
        return this;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getVorname() {
        return vorname;
    }

    public Arzt vorname(String vorname) {
        this.vorname = vorname;
        return this;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public Arzt nachname(String nachname) {
        this.nachname = nachname;
        return this;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getBezeichnung() {

        return lanr + " " + titel + " " + vorname + " " + nachname;
    }

    public Arzt bezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Set<Systemnutzung> getSystemnutzungs() {
        return systemnutzungs;
    }

    public Arzt systemnutzungs(Set<Systemnutzung> systemnutzungs) {
        this.systemnutzungs = systemnutzungs;
        return this;
    }

    public Arzt addSystemnutzung(Systemnutzung systemnutzung) {
        this.systemnutzungs.add(systemnutzung);
        systemnutzung.setArzt(this);
        return this;
    }

    public Arzt removeSystemnutzung(Systemnutzung systemnutzung) {
        this.systemnutzungs.remove(systemnutzung);
        systemnutzung.setArzt(null);
        return this;
    }

    public void setSystemnutzungs(Set<Systemnutzung> systemnutzungs) {
        this.systemnutzungs = systemnutzungs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Arzt)) {
            return false;
        }
        return id != null && id.equals(((Arzt) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Arzt{" +
            "id=" + getId() +
            ", lanr='" + getLanr() + "'" +
            ", titel='" + getTitel() + "'" +
            ", vorname='" + getVorname() + "'" +
            ", nachname='" + getNachname() + "'" +
            ", bezeichnung='" + getBezeichnung() + "'" +
            "}";
    }
}
