package org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto;

import java.io.Serializable;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 23, 2019
 */
public class SurveyQuestion implements Serializable {

    private static final long serialVersionUID = 212323054456594637L;

    private Integer questionId;
    private String answer;

    public SurveyQuestion() {
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "[" + questionId + "] => " + answer;
    }

}
