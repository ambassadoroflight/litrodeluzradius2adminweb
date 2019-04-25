package org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto;

import java.io.Serializable;
import java.util.ArrayList;
import org.unlitrodeluzcolombia.radius.element.Survey;

/**
 *
 * @author juandiego@comtor.net
 * @since
 * @version Apr 25, 2019
 */
public class HotspotSurvey implements Serializable {

    private static final long serialVersionUID = -8238368981139565942L;

    private Long id;
    private String description;
    private ArrayList<HotspotQuestion> questions;

    public HotspotSurvey() {
        questions = new ArrayList<>();
    }

    public HotspotSurvey(Survey survey) {
        this.id = survey.getId();
        this.description = survey.getDescription();
        this.questions = new ArrayList<>();
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

    public ArrayList<HotspotQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<HotspotQuestion> questions) {
        this.questions = questions;
    }

    public void addQuestion(HotspotQuestion question) {
        questions.add(question);
    }

    @Override
    public String toString() {
        return "HotspotSurvey{"
                + "id=" + id
                + ", description=" + description
                + ", questions=" + questions
                + '}';
    }

}
