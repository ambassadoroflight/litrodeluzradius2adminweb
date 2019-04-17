package org.unlitrodeluzcolombia.radius.ws.io;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SurveyResponse implements Serializable {

    private final static long serialVersionUID = -120635716372144676L;

    private String survey;
    private String time;
    private Map<Long, String> answers = new HashMap<>();

    /**
     * No args constructor for use in serialization
     *     
*/
    public SurveyResponse() {
    }

    /**
     *
     * @param time
     * @param answers
     * @param survey
     */
    public SurveyResponse(String survey, String time, Map<Long, String> answers) {
        super();

        this.survey = survey;
        this.time = time;
        this.answers = answers;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public SurveyResponse withSurvey(String survey) {
        this.survey = survey;
        return this;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SurveyResponse withTime(String time) {
        this.time = time;
        return this;
    }

    public Map<Long, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, String> answers) {
        this.answers = answers;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.survey);
        hash = 67 * hash + Objects.hashCode(this.time);
        hash = 67 * hash + Objects.hashCode(this.answers);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SurveyResponse other = (SurveyResponse) obj;
        if (!Objects.equals(this.survey, other.survey)) {
            return false;
        }
        if (!Objects.equals(this.time, other.time)) {
            return false;
        }
        if (!Objects.equals(this.answers, other.answers)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SurveyResponse{" + "survey=" + survey + ", time=" + time + ", answers=" + answers + '}';
    }

}
