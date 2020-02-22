package de.kvb.eps.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link de.kvb.eps.domain.Geraet} entity. This class is used
 * in {@link de.kvb.eps.web.rest.GeraetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /geraets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GeraetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bezeichnung;

    private LocalDateFilter gueltigBis;

    private LongFilter systemtypId;

    private LongFilter geraetTypId;

    private LongFilter herstellerId;

    public GeraetCriteria() {
    }

    public GeraetCriteria(GeraetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bezeichnung = other.bezeichnung == null ? null : other.bezeichnung.copy();
        this.gueltigBis = other.gueltigBis == null ? null : other.gueltigBis.copy();
        this.systemtypId = other.systemtypId == null ? null : other.systemtypId.copy();
        this.geraetTypId = other.geraetTypId == null ? null : other.geraetTypId.copy();
        this.herstellerId = other.herstellerId == null ? null : other.herstellerId.copy();
    }

    @Override
    public GeraetCriteria copy() {
        return new GeraetCriteria(this);
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

    public LongFilter getSystemtypId() {
        return systemtypId;
    }

    public void setSystemtypId(LongFilter systemtypId) {
        this.systemtypId = systemtypId;
    }

    public LongFilter getGeraetTypId() {
        return geraetTypId;
    }

    public void setGeraetTypId(LongFilter geraetTypId) {
        this.geraetTypId = geraetTypId;
    }

    public LongFilter getHerstellerId() {
        return herstellerId;
    }

    public void setHerstellerId(LongFilter herstellerId) {
        this.herstellerId = herstellerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GeraetCriteria that = (GeraetCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(bezeichnung, that.bezeichnung) &&
            Objects.equals(gueltigBis, that.gueltigBis) &&
            Objects.equals(systemtypId, that.systemtypId) &&
            Objects.equals(geraetTypId, that.geraetTypId) &&
            Objects.equals(herstellerId, that.herstellerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        bezeichnung,
        gueltigBis,
        systemtypId,
        geraetTypId,
        herstellerId
        );
    }

    @Override
    public String toString() {
        return "GeraetCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bezeichnung != null ? "bezeichnung=" + bezeichnung + ", " : "") +
                (gueltigBis != null ? "gueltigBis=" + gueltigBis + ", " : "") +
                (systemtypId != null ? "systemtypId=" + systemtypId + ", " : "") +
                (geraetTypId != null ? "geraetTypId=" + geraetTypId + ", " : "") +
                (herstellerId != null ? "herstellerId=" + herstellerId + ", " : "") +
            "}";
    }

}
