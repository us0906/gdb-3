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
 * Criteria class for the {@link de.kvb.eps.domain.Arzt} entity. This class is used
 * in {@link de.kvb.eps.web.rest.ArztResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /arzts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArztCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter lanr;

    private StringFilter titel;

    private StringFilter vorname;

    private StringFilter nachname;

    private StringFilter bezeichnung;

    private LongFilter systemnutzungId;

    public ArztCriteria(){
    }

    public ArztCriteria(ArztCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.lanr = other.lanr == null ? null : other.lanr.copy();
        this.titel = other.titel == null ? null : other.titel.copy();
        this.vorname = other.vorname == null ? null : other.vorname.copy();
        this.nachname = other.nachname == null ? null : other.nachname.copy();
        this.bezeichnung = other.bezeichnung == null ? null : other.bezeichnung.copy();
        this.systemnutzungId = other.systemnutzungId == null ? null : other.systemnutzungId.copy();
    }

    @Override
    public ArztCriteria copy() {
        return new ArztCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLanr() {
        return lanr;
    }

    public void setLanr(StringFilter lanr) {
        this.lanr = lanr;
    }

    public StringFilter getTitel() {
        return titel;
    }

    public void setTitel(StringFilter titel) {
        this.titel = titel;
    }

    public StringFilter getVorname() {
        return vorname;
    }

    public void setVorname(StringFilter vorname) {
        this.vorname = vorname;
    }

    public StringFilter getNachname() {
        return nachname;
    }

    public void setNachname(StringFilter nachname) {
        this.nachname = nachname;
    }

    public StringFilter getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(StringFilter bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LongFilter getSystemnutzungId() {
        return systemnutzungId;
    }

    public void setSystemnutzungId(LongFilter systemnutzungId) {
        this.systemnutzungId = systemnutzungId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArztCriteria that = (ArztCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(lanr, that.lanr) &&
            Objects.equals(titel, that.titel) &&
            Objects.equals(vorname, that.vorname) &&
            Objects.equals(nachname, that.nachname) &&
            Objects.equals(bezeichnung, that.bezeichnung) &&
            Objects.equals(systemnutzungId, that.systemnutzungId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        lanr,
        titel,
        vorname,
        nachname,
        bezeichnung,
        systemnutzungId
        );
    }

    @Override
    public String toString() {
        return "ArztCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (lanr != null ? "lanr=" + lanr + ", " : "") +
                (titel != null ? "titel=" + titel + ", " : "") +
                (vorname != null ? "vorname=" + vorname + ", " : "") +
                (nachname != null ? "nachname=" + nachname + ", " : "") +
                (bezeichnung != null ? "bezeichnung=" + bezeichnung + ", " : "") +
                (systemnutzungId != null ? "systemnutzungId=" + systemnutzungId + ", " : "") +
            "}";
    }

}
