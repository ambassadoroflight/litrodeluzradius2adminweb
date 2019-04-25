package org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto;

import java.io.Serializable;
import org.unlitrodeluzcolombia.radius.element.Question;

/**
 *
 * @author juandiego@comtor.net
 * @since
 * @version Apr 25, 2019
 */
public class HotspotQuestion implements Serializable {

    private static final long serialVersionUID = 7616362908292559525L;

    private Long id;
    private String question;
    private String type;
    private String options;

    public HotspotQuestion() {
    }

    public HotspotQuestion(Question question) {
        this.id = question.getId();
        this.question = question.getQuestion();
        this.type = question.getType();
        this.options = question.getOptions();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "HotspotQuestion{"
                + "id=" + id
                + ", question=" + question
                + ", type=" + type
                + ", options=" + options
                + '}';
    }

}
