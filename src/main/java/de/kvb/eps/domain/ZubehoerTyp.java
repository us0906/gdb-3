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

import de.kvb.eps.domain.enumeration.Technologie;

/**
 * A ZubehoerTyp.
 */
@Entity
@Table(name = "zubehoer_typ")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "zubehoertyp")
public class ZubehoerTyp implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "technologie", nullable = false)
    private Technologie technologie;

    @OneToMany(mappedBy = "zubehoerTyp")
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

    public ZubehoerTyp bezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        return this;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public ZubehoerTyp gueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
        return this;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public Technologie getTechnologie() {
        return technologie;
    }

    public ZubehoerTyp technologie(Technologie technologie) {
        this.technologie = technologie;
        return this;
    }

    public void setTechnologie(Technologie technologie) {
        this.technologie = technologie;
    }

    public Set<Zubehoer> getZubehoers() {
        return zubehoers;
    }

    public ZubehoerTyp zubehoers(Set<Zubehoer> zubehoers) {
        this.zubehoers = zubehoers;
        return this;
    }

    public ZubehoerTyp addZubehoer(Zubehoer zubehoer) {
        this.zubehoers.add(zubehoer);
        zubehoer.setZubehoerTyp(this);
        return this;
    }

    public ZubehoerTyp removeZubehoer(Zubehoer zubehoer) {
        this.zubehoers.remove(zubehoer);
        zubehoer.setZubehoerTyp(null);
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
        if (!(o instanceof ZubehoerTyp)) {
            return false;
        }
        return id != null && id.equals(((ZubehoerTyp) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ZubehoerTyp{" +
            "id=" + getId() +
            ", bezeichnung='" + getBezeichnung() + "'" +
            ", gueltigBis='" + getGueltigBis() + "'" +
            ", technologie='" + getTechnologie() + "'" +
            "}";
    }
}
