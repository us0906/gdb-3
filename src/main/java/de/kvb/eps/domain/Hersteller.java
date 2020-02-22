package de.kvb.eps.domain;

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
 * A Hersteller.
 */
@Entity
@Table(name = "hersteller")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "hersteller")
public class Hersteller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "bezeichnung", length = 200, nullable = false)
    private String bezeichnung;

    @Column(name = "gueltig_bis")
    private LocalDate gueltigBis;

    @OneToMany(mappedBy = "hersteller")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Geraet> geraets = new HashSet<>();

    @OneToMany(mappedBy = "hersteller")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Zubehoer> zubehoers = new HashSet<>();

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

    public Hersteller bezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public Hersteller gueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
        return this;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public Set<Geraet> getGeraets() {
        return geraets;
    }

    public Hersteller geraets(Set<Geraet> geraets) {
        this.geraets = geraets;
        return this;
    }

    public Hersteller addGeraet(Geraet geraet) {
        this.geraets.add(geraet);
        geraet.setHersteller(this);
        return this;
    }

    public Hersteller removeGeraet(Geraet geraet) {
        this.geraets.remove(geraet);
        geraet.setHersteller(null);
        return this;
    }

    public void setGeraets(Set<Geraet> geraets) {
        this.geraets = geraets;
    }

    public Set<Zubehoer> getZubehoers() {
        return zubehoers;
    }

    public Hersteller zubehoers(Set<Zubehoer> zubehoers) {
        this.zubehoers = zubehoers;
        return this;
    }

    public Hersteller addZubehoer(Zubehoer zubehoer) {
        this.zubehoers.add(zubehoer);
        zubehoer.setHersteller(this);
        return this;
    }

    public Hersteller removeZubehoer(Zubehoer zubehoer) {
        this.zubehoers.remove(zubehoer);
        zubehoer.setHersteller(null);
        return this;
    }

    public void setZubehoers(Set<Zubehoer> zubehoers) {
        this.zubehoers = zubehoers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hersteller)) {
            return false;
        }
        return id != null && id.equals(((Hersteller) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Hersteller{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            "}";
    }
}
