package com.codewranglers.workflowmanager.models;

import jakarta.persistence.*;

@Entity
public class NCR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ncrId;

    @ManyToOne
    @JoinColumn(name = "ncrGenerator_id")
    private User ncrGenerator;

    private String ncrTitle;

    @ManyToOne
    @JoinColumn(name = "ncrPart_id")
    private Part ncrPart;

    @ManyToOne
    @JoinColumn(name = "ncrReviewer_id")
    private User ncrReviewer;

    private String ncrDispositionText;

    private boolean isComplete;

    private boolean isPassing;

    private boolean isScrap;

    private boolean needsRework;

    @Override
    public String toString() {
        return "NCR{" +
                "ncrId=" + ncrId +
                ", ncrGenerator=" + ncrGenerator +
                ", ncrTitle='" + ncrTitle + '\'' +
                ", ncrPart=" + ncrPart +
                ", ncrReviewer=" + ncrReviewer +
                ", ncrDispositionText='" + ncrDispositionText + '\'' +
                ", isComplete=" + isComplete +
                ", isPassing=" + isPassing +
                ", isScrap=" + isScrap +
                ", needsRework=" + needsRework +
                '}';
    }

    public Integer getNcrId() {
        return ncrId;
    }

    public User getNcrGenerator() {
        return ncrGenerator;
    }

    public void setNcrGenerator(User ncrGenerator) {
        this.ncrGenerator = ncrGenerator;
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
