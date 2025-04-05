package com.eliasshallouf.msc.seminar2.domain.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Table
@Entity
public class Territory implements Serializable {
    @Id String territoryId;
    @Column(name = "territorydescription") String territoryDescription;
    @Column Long regionId;

    public String getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(String territoryId) {
        this.territoryId = territoryId;
    }

    public String getTerritoryDescription() {
        return territoryDescription;
    }

    public void setTerritoryDescription(String territoryDescription) {
        this.territoryDescription = territoryDescription;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getRegionId() {
        return regionId;
    }
}