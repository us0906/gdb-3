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
 * A Geraet.
 */
@Entity
@Table(name = "geraet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "geraet")
public class Geraet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "bezeichnung", length = 200, nullable = false)
    private String bezeichnung;

    @Column(name = "gueltig_bis")
    private LocalDate gueltigBis;

    @OneToMany(mappedBy = "geraet")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Systemtyp> systemtyps = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("geraets")
    private GeraetTyp geraetTyp;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("geraets")
    private Hersteller hersteller;

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

    public Geraet bezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public Geraet gueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
        return this;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public Set<Systemtyp> getSystemtyps() {
        return systemtyps;
    }

    public Geraet systemtyps(Set<Systemtyp> systemtyps) {
        this.systemtyps = systemtyps;
        return this;
    }

    public Geraet addSystemtyp(Systemtyp systemtyp) {
        this.systemtyps.add(systemtyp);
        systemtyp.setGeraet(this);
        return this;
    }

    public Geraet removeSystemtyp(Systemtyp systemtyp) {
        this.systemtyps.remove(systemtyp);
        systemtyp.setGeraet(null);
        return this;
    }

    public void setSystemtyps(Set<Systemtyp> systemtyps) {
        this.systemtyps = systemtyps;
    }

    public GeraetTyp getGeraetTyp() {
        return geraetTyp;
    }

    public Geraet geraetTyp(GeraetTyp geraetTyp) {
        this.geraetTyp = geraetTyp;
        return this;
    }

    public void setGeraetTyp(GeraetTyp geraetTyp) {
        this.geraetTyp = geraetTyp;
    }

    public Hersteller getHersteller() {
        return hersteller;
    }

    public Geraet hersteller(Hersteller hersteller) {
        this.hersteller = hersteller;
        return this;
    }

    public void setHersteller(Hersteller hersteller) {
        this.hersteller = hersteller;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Geraet)) {
            return false;
        }
        return id != null && id.equals(((Geraet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Geraet{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            "}";
    }
}
