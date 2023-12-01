package com.codewranglers.workflowmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Operation {

    @Id
    public Long id;
    public Long stepNumber;
    public String stepName;
    public String stepDescription;
    public List<String> requirements = new ArrayList<String>();

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", stepNumber=" + stepNumber +
                ", stepName='" + stepName + '\'' +
                ", stepDescription='" + stepDescription + '\'' +
                ", requirements=" + requirements +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Long stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }
}

