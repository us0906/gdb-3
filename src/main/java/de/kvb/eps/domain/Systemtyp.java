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
 * A Systemtyp.
 */
@Entity
@Table(name = "systemtyp")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "systemtyp")
public class Systemtyp implements Serializable {

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

    @OneToMany(mappedBy = "systemtyp")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Systeminstanz> systeminstanzs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("systemtyps")
    private Geraet geraet;

    @ManyToOne
    @JsonIgnoreProperties("systemtyps")
    private Zubehoer zubehoer;

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

    public Systemtyp bezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public Systemtyp gueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
        return this;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public Set<Systeminstanz> getSysteminstanzs() {
        return systeminstanzs;
    }

    public Systemtyp systeminstanzs(Set<Systeminstanz> systeminstanzs) {
        this.systeminstanzs = systeminstanzs;
        return this;
    }

    public Systemtyp addSysteminstanz(Systeminstanz systeminstanz) {
        this.systeminstanzs.add(systeminstanz);
        systeminstanz.setSystemtyp(this);
        return this;
    }

    public Systemtyp removeSysteminstanz(Systeminstanz systeminstanz) {
        this.systeminstanzs.remove(systeminstanz);
        systeminstanz.setSystemtyp(null);
        return this;
    }

    public void setSysteminstanzs(Set<Systeminstanz> systeminstanzs) {
        this.systeminstanzs = systeminstanzs;
    }

    public Geraet getGeraet() {
        return geraet;
    }

    public Systemtyp geraet(Geraet geraet) {
        this.geraet = geraet;
        return this;
    }

    public void setGeraet(Geraet geraet) {
        this.geraet = geraet;
    }

    public Zubehoer getZubehoer() {
        return zubehoer;
    }

    public Systemtyp zubehoer(Zubehoer zubehoer) {
        this.zubehoer = zubehoer;
        return this;
    }

    public void setZubehoer(Zubehoer zubehoer) {
        this.zubehoer = zubehoer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Systemtyp)) {
            return false;
        }
        return id != null && id.equals(((Systemtyp) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Systemtyp{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            "}";
    }
}
