package org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 23, 2019
 */
public class SurveyResponse implements Serializable {

    private static final long serialVersionUID = 4130168833740249478L;

    private Long hotspot;
    private Long survey;
    private java.util.Date date;
    private ArrayList<SurveyQuestion> answers;

    public SurveyResponse() {
    }

    public Long getHotspot() {
        return hotspot;
    }

    public void setHotspot(Long hotspot) {
        this.hotspot = hotspot;
    }

    public SurveyResponse(Long hotspot, Long survey, java.util.Date date,
            ArrayList<SurveyQuestion> answers) {
        this.hotspot = hotspot;
        this.survey = survey;
        this.date = date;
        this.answers = answers;
    }

    public Long getSurvey() {
        return survey;
    }

    public void setSurvey(Long survey) {
        this.survey = survey;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public ArrayList<SurveyQuestion> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<SurveyQuestion> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        String toString = "SurveyAnswer{"
                + "hotspot=" + hotspot
                + ", survey=" + survey
                + ", date=" + date
                + ", answers={";

        for (SurveyQuestion response : answers) {
            toString += response.toString() + ", ";

        }

        toString += '}';

        return toString;
    }

}
