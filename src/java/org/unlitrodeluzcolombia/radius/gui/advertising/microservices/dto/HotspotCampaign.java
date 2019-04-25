package org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto;

import java.io.Serializable;
import net.comtor.radius.element.Campaign;

/**
 *
 * @author juandiego@comtor.net
 * @since
 * @version Apr 25, 2019
 */
public class HotspotCampaign implements Serializable {

    private static final long serialVersionUID = 7671592566925192611L;

    private Long id;
    private String description;
    private java.util.Date start_date;
    private java.util.Date end_date;
    private String sponsor;
    private String banner_1;
    private String banner_2;
    private HotspotSurvey survey;

    public HotspotCampaign() {
    }

    public HotspotCampaign(Campaign campaign) {
        this.id = campaign.getId();
        this.description = campaign.getDescription();
        this.start_date = new java.util.Date(campaign.getStart_date().getTime());
        this.end_date = new java.util.Date(campaign.getEnd_date().getTime());
        this.sponsor = campaign.getSponsor_name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.Date getStart_date() {
        return start_date;
    }

    public void setStart_date(java.util.Date start_date) {
        this.start_date = start_date;
    }

    public java.util.Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(java.util.Date end_date) {
        this.end_date = end_date;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getBanner_1() {
        return banner_1;
    }

    public void setBanner_1(String banner_1) {
        this.banner_1 = banner_1;
    }

    public String getBanner_2() {
        return banner_2;
    }

    public void setBanner_2(String banner_2) {
        this.banner_2 = banner_2;
    }

    public HotspotSurvey getSurvey() {
        return survey;
    }

    public void setSurvey(HotspotSurvey survey) {
        this.survey = survey;
    }

    @Override
    public String toString() {
        return "HotspotCampaign{"
                + "id=" + id
                + ", description=" + description
                + ", start_date=" + start_date
                + ", end_date=" + end_date
                + ", sponsor=" + sponsor
                + ", survey=" + survey
                + '}';
    }

}
