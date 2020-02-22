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
 * A Zubehoer.
 */
@Entity
@Table(name = "zubehoer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "zubehoer")
public class Zubehoer implements Serializable {

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

    @OneToMany(mappedBy = "zubehoer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Systemtyp> systemtyps = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("zubehoers")
    private Hersteller hersteller;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("zubehoers")
    private ZubehoerTyp zubehoerTyp;

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

    public Zubehoer bezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public Zubehoer gueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
        return this;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public Set<Systemtyp> getSystemtyps() {
        return systemtyps;
    }

    public Zubehoer systemtyps(Set<Systemtyp> systemtyps) {
        this.systemtyps = systemtyps;
        return this;
    }

    public Zubehoer addSystemtyp(Systemtyp systemtyp) {
        this.systemtyps.add(systemtyp);
        systemtyp.setZubehoer(this);
        return this;
    }

    public Zubehoer removeSystemtyp(Systemtyp systemtyp) {
        this.systemtyps.remove(systemtyp);
        systemtyp.setZubehoer(null);
        return this;
    }

    public void setSystemtyps(Set<Systemtyp> systemtyps) {
        this.systemtyps = systemtyps;
    }

    public Hersteller getHersteller() {
        return hersteller;
    }

    public Zubehoer hersteller(Hersteller hersteller) {
        this.hersteller = hersteller;
        return this;
    }

    public void setHersteller(Hersteller hersteller) {
        this.hersteller = hersteller;
    }

    public ZubehoerTyp getZubehoerTyp() {
        return zubehoerTyp;
    }

    public Zubehoer zubehoerTyp(ZubehoerTyp zubehoerTyp) {
        this.zubehoerTyp = zubehoerTyp;
        return this;
    }

    public void setZubehoerTyp(ZubehoerTyp zubehoerTyp) {
        this.zubehoerTyp = zubehoerTyp;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Zubehoer)) {
            return false;
        }
        return id != null && id.equals(((Zubehoer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Zubehoer{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            "}";
    }
}
