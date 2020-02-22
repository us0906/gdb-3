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

/**
 * Criteria class for the {@link de.kvb.eps.domain.Systemnutzung} entity. This class is used
 * in {@link de.kvb.eps.web.rest.SystemnutzungResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /systemnutzungs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SystemnutzungCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter systeminstanzId;

    private LongFilter arztId;

    public SystemnutzungCriteria() {
    }

    public SystemnutzungCriteria(SystemnutzungCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.systeminstanzId = other.systeminstanzId == null ? null : other.systeminstanzId.copy();
        this.arztId = other.arztId == null ? null : other.arztId.copy();
    }

    @Override
    public SystemnutzungCriteria copy() {
        return new SystemnutzungCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSysteminstanzId() {
        return systeminstanzId;
    }

    public void setSysteminstanzId(LongFilter systeminstanzId) {
        this.systeminstanzId = systeminstanzId;
    }

    public LongFilter getArztId() {
        return arztId;
    }

    public void setArztId(LongFilter arztId) {
        this.arztId = arztId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SystemnutzungCriteria that = (SystemnutzungCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(systeminstanzId, that.systeminstanzId) &&
            Objects.equals(arztId, that.arztId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        systeminstanzId,
        arztId
        );
    }

    @Override
    public String toString() {
        return "SystemnutzungCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (systeminstanzId != null ? "systeminstanzId=" + systeminstanzId + ", " : "") +
                (arztId != null ? "arztId=" + arztId + ", " : "") +
            "}";
    }

}
