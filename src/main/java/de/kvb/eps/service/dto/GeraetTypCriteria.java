package de.kvb.eps.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import de.kvb.eps.domain.enumeration.Technologie;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link de.kvb.eps.domain.GeraetTyp} entity. This class is used
 * in {@link de.kvb.eps.web.rest.GeraetTypResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /geraet-typs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GeraetTypCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Technologie
     */
    public static class TechnologieFilter extends Filter<Technologie> {

        public TechnologieFilter() {
        }

        public TechnologieFilter(TechnologieFilter filter) {
            super(filter);
        }

        @Override
        public TechnologieFilter copy() {
            return new TechnologieFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bezeichnung;

    private LocalDateFilter gueltigBis;

    private TechnologieFilter technologie;

    private LongFilter geraetId;

    public GeraetTypCriteria() {
    }

    public GeraetTypCriteria(GeraetTypCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bezeichnung = other.bezeichnung == null ? null : other.bezeichnung.copy();
        this.gueltigBis = other.gueltigBis == null ? null : other.gueltigBis.copy();
        this.technologie = other.technologie == null ? null : other.technologie.copy();
        this.geraetId = other.geraetId == null ? null : other.geraetId.copy();
    }

    @Override
    public GeraetTypCriteria copy() {
        return new GeraetTypCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(StringFilter bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDateFilter getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(LocalDateFilter gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public TechnologieFilter getTechnologie() {
        return technologie;
    }

    public void setTechnologie(TechnologieFilter technologie) {
        this.technologie = technologie;
    }

    public LongFilter getGeraetId() {
        return geraetId;
    }

    public void setGeraetId(LongFilter geraetId) {
        this.geraetId = geraetId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GeraetTypCriteria that = (GeraetTypCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(bezeichnung, that.bezeichnung) &&
            Objects.equals(gueltigBis, that.gueltigBis) &&
            Objects.equals(technologie, that.technologie) &&
            Objects.equals(geraetId, that.geraetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        bezeichnung,
        gueltigBis,
        technologie,
        geraetId
        );
    }

    @Override
    public String toString() {
        return "GeraetTypCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bezeichnung != null ? "bezeichnung=" + bezeichnung + ", " : "") +
                (gueltigBis != null ? "gueltigBis=" + gueltigBis + ", " : "") +
                (technologie != null ? "technologie=" + technologie + ", " : "") +
                (geraetId != null ? "geraetId=" + geraetId + ", " : "") +
            "}";
    }

}
