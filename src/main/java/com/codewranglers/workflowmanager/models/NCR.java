package com.codewranglers.workflowmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class NCR {

    @Id
    public Long ncr_id;
    public String ncr_name;
    public String ncr_type;
    public String ncr_requirement;
    public String ncr_text;

    @Override
    public String toString() {
        return "NCR{" +
                "ncr_id=" + ncr_id +
                ", ncr_name='" + ncr_name + '\'' +
                ", ncr_type='" + ncr_type + '\'' +
                ", ncr_requirement='" + ncr_requirement + '\'' +
                ", ncr_text='" + ncr_text + '\'' +
                '}';
    }

    public Long getNcr_id() {
        return ncr_id;
    }

    public void setNcr_id(Long ncr_id) {
        this.ncr_id = ncr_id;
    }

    public String getNcr_name() {
        return ncr_name;
    }

    public void setNcr_name(String ncr_name) {
        this.ncr_name = ncr_name;
    }

    public String getNcr_type() {
        return ncr_type;
    }

    public void setNcr_type(String ncr_type) {
        this.ncr_type = ncr_type;
    }

    public String getNcr_requirement() {
        return ncr_requirement;
    }

    public void setNcr_requirement(String ncr_requirement) {
        this.ncr_requirement = ncr_requirement;
    }

    public String getNcr_text() {
        return ncr_text;
    }

    public void setNcr_text(String ncr_text) {
        this.ncr_text = ncr_text;
    }
}
