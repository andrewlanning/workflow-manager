package com.codewranglers.workflowmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Process {

    @Id
    public Long id;
    public Long serialNumber;
    public String partNumber;
    public String partName;
    public String partDescription;

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", serialNumber=" + serialNumber +
                ", partNumber='" + partNumber + '\'' +
                ", partName='" + partName + '\'' +
                ", partDescription='" + partDescription + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public void setPartDescription(String partDescription) {
        this.partDescription = partDescription;
    }
}
