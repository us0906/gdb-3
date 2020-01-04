package de.kvb.eps.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.kvb.eps.domain.Systemnutzung} entity.
 */
public class SystemnutzungDTO implements Serializable {

    private Long id;


    private Long systeminstanzId;

    private String systeminstanzBezeichnung;

    private Long arztId;

    private String arztBezeichnung;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSysteminstanzId() {
        return systeminstanzId;
    }

    public void setSysteminstanzId(Long systeminstanzId) {
        this.systeminstanzId = systeminstanzId;
    }

    public String getSysteminstanzBezeichnung() {
        return systeminstanzBezeichnung;
    }

    public void setSysteminstanzBezeichnung(String systeminstanzBezeichnung) {
        this.systeminstanzBezeichnung = systeminstanzBezeichnung;
    }

    public Long getArztId() {
        return arztId;
    }

    public void setArztId(Long arztId) {
        this.arztId = arztId;
    }

    public String getArztBezeichnung() {
        return arztBezeichnung;
    }

    public void setArztBezeichnung(String arztBezeichnung) {
        this.arztBezeichnung = arztBezeichnung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemnutzungDTO systemnutzungDTO = (SystemnutzungDTO) o;
        if (systemnutzungDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemnutzungDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemnutzungDTO{" +
            "id=" + getId() +
            ", systeminstanz=" + getSysteminstanzId() +
            ", systeminstanz='" + getSysteminstanzBezeichnung() + "'" +
            ", arzt=" + getArztId() +
            ", arzt='" + getArztBezeichnung() + "'" +
            "}";
    }
}
