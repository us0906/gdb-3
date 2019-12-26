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
 * Criteria class for the {@link de.kvb.eps.domain.Betreiber} entity. This class is used
 * in {@link de.kvb.eps.web.rest.BetreiberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /betreibers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BetreiberCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter vorname;

    private StringFilter nachname;

    private StringFilter strasse;

    private StringFilter hausnummer;

    private StringFilter plz;

    private StringFilter ort;

    private LongFilter systeminstanzId;

    public BetreiberCriteria(){
    }

    public BetreiberCriteria(BetreiberCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.vorname = other.vorname == null ? null : other.vorname.copy();
        this.nachname = other.nachname == null ? null : other.nachname.copy();
        this.strasse = other.strasse == null ? null : other.strasse.copy();
        this.hausnummer = other.hausnummer == null ? null : other.hausnummer.copy();
        this.plz = other.plz == null ? null : other.plz.copy();
        this.ort = other.ort == null ? null : other.ort.copy();
        this.systeminstanzId = other.systeminstanzId == null ? null : other.systeminstanzId.copy();
    }

    @Override
    public BetreiberCriteria copy() {
        return new BetreiberCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public StringFilter getStrasse() {
        return strasse;
    }

    public void setStrasse(StringFilter strasse) {
        this.strasse = strasse;
    }

    public StringFilter getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(StringFilter hausnummer) {
        this.hausnummer = hausnummer;
    }

    public StringFilter getPlz() {
        return plz;
    }

    public void setPlz(StringFilter plz) {
        this.plz = plz;
    }

    public StringFilter getOrt() {
        return ort;
    }

    public void setOrt(StringFilter ort) {
        this.ort = ort;
    }

    public LongFilter getSysteminstanzId() {
        return systeminstanzId;
    }

    public void setSysteminstanzId(LongFilter systeminstanzId) {
        this.systeminstanzId = systeminstanzId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BetreiberCriteria that = (BetreiberCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(vorname, that.vorname) &&
            Objects.equals(nachname, that.nachname) &&
            Objects.equals(strasse, that.strasse) &&
            Objects.equals(hausnummer, that.hausnummer) &&
            Objects.equals(plz, that.plz) &&
            Objects.equals(ort, that.ort) &&
            Objects.equals(systeminstanzId, that.systeminstanzId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        vorname,
        nachname,
        strasse,
        hausnummer,
        plz,
        ort,
        systeminstanzId
        );
    }

    @Override
    public String toString() {
        return "BetreiberCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (vorname != null ? "vorname=" + vorname + ", " : "") +
                (nachname != null ? "nachname=" + nachname + ", " : "") +
                (strasse != null ? "strasse=" + strasse + ", " : "") +
                (hausnummer != null ? "hausnummer=" + hausnummer + ", " : "") +
                (plz != null ? "plz=" + plz + ", " : "") +
                (ort != null ? "ort=" + ort + ", " : "") +
                (systeminstanzId != null ? "systeminstanzId=" + systeminstanzId + ", " : "") +
            "}";
    }

}
