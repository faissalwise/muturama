package ma.sobexime.muturama.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A AgentList.
 */
@Entity
@Table(name = "agent_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "agentlist")
public class AgentList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cin")
    private String cin;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "address")
    private String address;

    @Column(name = "lat", precision=10, scale=2)
    private BigDecimal lat;

    @Column(name = "lon", precision=10, scale=2)
    private BigDecimal lon;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public AgentList cin(String cin) {
        this.cin = cin;
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public AgentList nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public AgentList prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAddress() {
        return address;
    }

    public AgentList address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public AgentList lat(BigDecimal lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public AgentList lon(BigDecimal lon) {
        this.lon = lon;
        return this;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public Boolean isStatus() {
        return status;
    }

    public AgentList status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public City getCity() {
        return city;
    }

    public AgentList city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentList agentList = (AgentList) o;
        if (agentList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agentList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AgentList{" +
            "id=" + getId() +
            ", cin='" + getCin() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", address='" + getAddress() + "'" +
            ", lat='" + getLat() + "'" +
            ", lon='" + getLon() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
