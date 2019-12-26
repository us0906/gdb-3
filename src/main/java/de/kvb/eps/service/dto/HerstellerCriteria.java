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
 * Criteria class for the {@link de.kvb.eps.domain.Hersteller} entity. This class is used
 * in {@link de.kvb.eps.web.rest.HerstellerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /herstellers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HerstellerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bezeichnung;

    private LocalDateFilter gueltigBis;

    private LongFilter geraetId;

    private LongFilter zubehoerId;

    public HerstellerCriteria(){
    }

    public HerstellerCriteria(HerstellerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.bezeichnung = other.bezeichnung == null ? null : other.bezeichnung.copy();
        this.gueltigBis = other.gueltigBis == null ? null : other.gueltigBis.copy();
        this.geraetId = other.geraetId == null ? null : other.geraetId.copy();
        this.zubehoerId = other.zubehoerId == null ? null : other.zubehoerId.copy();
    }

    @Override
    public HerstellerCriteria copy() {
        return new HerstellerCriteria(this);
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

    public LongFilter getGeraetId() {
        return geraetId;
    }

    public void setGeraetId(LongFilter geraetId) {
        this.geraetId = geraetId;
    }

    public LongFilter getZubehoerId() {
        return zubehoerId;
    }

    public void setZubehoerId(LongFilter zubehoerId) {
        this.zubehoerId = zubehoerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HerstellerCriteria that = (HerstellerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(bezeichnung, that.bezeichnung) &&
            Objects.equals(gueltigBis, that.gueltigBis) &&
            Objects.equals(geraetId, that.geraetId) &&
            Objects.equals(zubehoerId, that.zubehoerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        bezeichnung,
        gueltigBis,
        geraetId,
        zubehoerId
        );
    }

    @Override
    public String toString() {
        return "HerstellerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bezeichnung != null ? "bezeichnung=" + bezeichnung + ", " : "") +
                (gueltigBis != null ? "gueltigBis=" + gueltigBis + ", " : "") +
                (geraetId != null ? "geraetId=" + geraetId + ", " : "") +
                (zubehoerId != null ? "zubehoerId=" + zubehoerId + ", " : "") +
            "}";
    }

}
