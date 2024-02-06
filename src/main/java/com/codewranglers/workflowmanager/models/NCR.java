package com.codewranglers.workflowmanager.models;

import jakarta.persistence.*;

@Entity
public class NCR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ncrId;

    @Column(name = "ncr_title")
    private String ncrTitle;

    @Column(name = "ncr_description")
    private String ncrDescription;

    @ManyToOne
    @JoinColumn(name = "ncr_user_id")
    private User ncrUserId;

    @ManyToOne
    @JoinColumn(name = "ncr_part_id")
    private Part ncrPart;

    @ManyToOne
    @JoinColumn(name = "ncr_reviewer_id")
    private User ncrReviewer;

    @Column(name = "ncr_disposition_text")
    private String ncrDispositionText;

    @Column(name = "is_dispositioned")
    private boolean isDispositioned;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Column(name = "is_passing")
    private boolean isPassing;

    @Column(name = "is_scrap")
    private boolean isScrap;

    @Column(name = "needs_rework")
    private boolean needsRework;

    public void setNcrDescription(String ncrDescription) {
        this.ncrDescription = ncrDescription;
    }

    public NCR() {
    }

    public String getNcrDescription() {
        return ncrDescription;
    }


    public boolean isDispositioned() {
        return isDispositioned;
    }

    public void setDispositioned(boolean dispositioned) {
        isDispositioned = dispositioned;
    }

    public Integer getNcrId() {
        return ncrId;
    }

    public User getNcrUserId() {
        return ncrUserId;
    }

    public void setNcrUserId(User ncrUserId) {
        this.ncrUserId = ncrUserId;
    }

    public String getNcrTitle() {
        return ncrTitle;
    }

    public void setNcrTitle(String ncrTitle) {
        this.ncrTitle = ncrTitle;
    }

    public Part getNcrPart() {
        return ncrPart;
    }

    public void setNcrPart(Part ncrPart) {
        this.ncrPart = ncrPart;
    }

    public User getNcrReviewer() {
        return ncrReviewer;
    }

    public void setNcrReviewer(User ncrReviewer) {
        this.ncrReviewer = ncrReviewer;
    }

    public String getNcrDispositionText() {
        return ncrDispositionText;
    }

    public void setNcrDispositionText(String ncrDispositionText) {
        this.ncrDispositionText = ncrDispositionText;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean isPassing() {
        return isPassing;
    }

    public void setPassing(boolean passing) {
        isPassing = passing;
    }

    public boolean isScrap() {
        return isScrap;
    }

    public void setScrap(boolean scrap) {
        isScrap = scrap;
    }

    public boolean isNeedsRework() {
        return needsRework;
    }

    public void setNeedsRework(boolean needsRework) {
        this.needsRework = needsRework;
    }
}
