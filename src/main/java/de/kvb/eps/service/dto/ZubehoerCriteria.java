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
 * Criteria class for the {@link de.kvb.eps.domain.Zubehoer} entity. This class is used
 * in {@link de.kvb.eps.web.rest.ZubehoerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /zubehoers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ZubehoerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bezeichnung;

    private LocalDateFilter gueltigBis;

    private LongFilter systemtypId;

    private LongFilter herstellerId;

    private LongFilter zubehoerTypId;

    public ZubehoerCriteria() {
    }

    public ZubehoerCriteria(ZubehoerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bezeichnung = other.bezeichnung == null ? null : other.bezeichnung.copy();
        this.gueltigBis = other.gueltigBis == null ? null : other.gueltigBis.copy();
        this.systemtypId = other.systemtypId == null ? null : other.systemtypId.copy();
        this.herstellerId = other.herstellerId == null ? null : other.herstellerId.copy();
        this.zubehoerTypId = other.zubehoerTypId == null ? null : other.zubehoerTypId.copy();
    }

    @Override
    public ZubehoerCriteria copy() {
        return new ZubehoerCriteria(this);
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

    public LongFilter getHerstellerId() {
        return herstellerId;
    }

    public void setHerstellerId(LongFilter herstellerId) {
        this.herstellerId = herstellerId;
    }

    public LongFilter getZubehoerTypId() {
        return zubehoerTypId;
    }

    public void setZubehoerTypId(LongFilter zubehoerTypId) {
        this.zubehoerTypId = zubehoerTypId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ZubehoerCriteria that = (ZubehoerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(bezeichnung, that.bezeichnung) &&
            Objects.equals(gueltigBis, that.gueltigBis) &&
            Objects.equals(systemtypId, that.systemtypId) &&
            Objects.equals(herstellerId, that.herstellerId) &&
            Objects.equals(zubehoerTypId, that.zubehoerTypId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        bezeichnung,
        gueltigBis,
        systemtypId,
        herstellerId,
        zubehoerTypId
        );
    }

    @Override
    public String toString() {
        return "ZubehoerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bezeichnung != null ? "bezeichnung=" + bezeichnung + ", " : "") +
                (gueltigBis != null ? "gueltigBis=" + gueltigBis + ", " : "") +
                (systemtypId != null ? "systemtypId=" + systemtypId + ", " : "") +
                (herstellerId != null ? "herstellerId=" + herstellerId + ", " : "") +
                (zubehoerTypId != null ? "zubehoerTypId=" + zubehoerTypId + ", " : "") +
            "}";
    }

}
