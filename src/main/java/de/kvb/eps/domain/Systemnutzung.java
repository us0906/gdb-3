package de.kvb.eps.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Systemnutzung.
 */
@Entity
@Table(name = "sys_nutz")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "systemnutzung")
public class Systemnutzung implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("systemnutzungs")
    private Systeminstanz systeminstanz;

    @ManyToOne
    @JsonIgnoreProperties("systemnutzungs")
    private Arzt arzt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Systeminstanz getSysteminstanz() {
        return systeminstanz;
    }

    public Systemnutzung systeminstanz(Systeminstanz systeminstanz) {
        this.systeminstanz = systeminstanz;
        return this;
    }

    public void setSysteminstanz(Systeminstanz systeminstanz) {
        this.systeminstanz = systeminstanz;
    }

    public Arzt getArzt() {
        return arzt;
    }

    public Systemnutzung arzt(Arzt arzt) {
        this.arzt = arzt;
        return this;
    }

    public void setArzt(Arzt arzt) {
        this.arzt = arzt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Systemnutzung)) {
            return false;
        }
        return id != null && id.equals(((Systemnutzung) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Systemnutzung{" +
            "id=" + getId() +
            "}";
    }
}
