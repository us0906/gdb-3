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
 * Criteria class for the {@link de.kvb.eps.domain.Systeminstanz} entity. This class is used
 * in {@link de.kvb.eps.web.rest.SysteminstanzResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /systeminstanzs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SysteminstanzCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bezeichnung;

    private StringFilter geraetNummer;

    private StringFilter geraetBaujahr;

    private LocalDateFilter gueltigBis;

    private LongFilter systemnutzungId;

    private LongFilter systemtypId;

    private LongFilter betriebsstaetteId;

    private LongFilter betreiberId;

    public SysteminstanzCriteria(){
    }

    public SysteminstanzCriteria(SysteminstanzCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.bezeichnung = other.bezeichnung == null ? null : other.bezeichnung.copy();
        this.geraetNummer = other.geraetNummer == null ? null : other.geraetNummer.copy();
        this.geraetBaujahr = other.geraetBaujahr == null ? null : other.geraetBaujahr.copy();
        this.gueltigBis = other.gueltigBis == null ? null : other.gueltigBis.copy();
        this.systemnutzungId = other.systemnutzungId == null ? null : other.systemnutzungId.copy();
        this.systemtypId = other.systemtypId == null ? null : other.systemtypId.copy();
        this.betriebsstaetteId = other.betriebsstaetteId == null ? null : other.betriebsstaetteId.copy();
        this.betreiberId = other.betreiberId == null ? null : other.betreiberId.copy();
    }

    @Override
    public SysteminstanzCriteria copy() {
        return new SysteminstanzCriteria(this);
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

    public StringFilter getGeraetNummer() {
        return geraetNummer;
    }

    public void setGeraetNummer(StringFilter geraetNummer) {
        this.geraetNummer = geraetNummer;
    }

    public StringFilter getGeraetBaujahr() {
        return geraetBaujahr;
    }

    public void setGeraetBaujahr(StringFilter geraetBaujahr) {
        this.geraetBaujahr = geraetBaujahr;
    }

    public LocalDateFilter getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(LocalDateFilter gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public LongFilter getSystemnutzungId() {
        return systemnutzungId;
    }

    public void setSystemnutzungId(LongFilter systemnutzungId) {
        this.systemnutzungId = systemnutzungId;
    }

    public LongFilter getSystemtypId() {
        return systemtypId;
    }

    public void setSystemtypId(LongFilter systemtypId) {
        this.systemtypId = systemtypId;
    }

    public LongFilter getBetriebsstaetteId() {
        return betriebsstaetteId;
    }

    public void setBetriebsstaetteId(LongFilter betriebsstaetteId) {
        this.betriebsstaetteId = betriebsstaetteId;
    }

    public LongFilter getBetreiberId() {
        return betreiberId;
    }

    public void setBetreiberId(LongFilter betreiberId) {
        this.betreiberId = betreiberId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SysteminstanzCriteria that = (SysteminstanzCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(bezeichnung, that.bezeichnung) &&
            Objects.equals(geraetNummer, that.geraetNummer) &&
            Objects.equals(geraetBaujahr, that.geraetBaujahr) &&
            Objects.equals(gueltigBis, that.gueltigBis) &&
            Objects.equals(systemnutzungId, that.systemnutzungId) &&
            Objects.equals(systemtypId, that.systemtypId) &&
            Objects.equals(betriebsstaetteId, that.betriebsstaetteId) &&
            Objects.equals(betreiberId, that.betreiberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        bezeichnung,
        geraetNummer,
        geraetBaujahr,
        gueltigBis,
        systemnutzungId,
        systemtypId,
        betriebsstaetteId,
        betreiberId
        );
    }

    @Override
    public String toString() {
        return "SysteminstanzCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bezeichnung != null ? "bezeichnung=" + bezeichnung + ", " : "") +
                (geraetNummer != null ? "geraetNummer=" + geraetNummer + ", " : "") +
                (geraetBaujahr != null ? "geraetBaujahr=" + geraetBaujahr + ", " : "") +
                (gueltigBis != null ? "gueltigBis=" + gueltigBis + ", " : "") +
                (systemnutzungId != null ? "systemnutzungId=" + systemnutzungId + ", " : "") +
                (systemtypId != null ? "systemtypId=" + systemtypId + ", " : "") +
                (betriebsstaetteId != null ? "betriebsstaetteId=" + betriebsstaetteId + ", " : "") +
                (betreiberId != null ? "betreiberId=" + betreiberId + ", " : "") +
            "}";
    }

}
