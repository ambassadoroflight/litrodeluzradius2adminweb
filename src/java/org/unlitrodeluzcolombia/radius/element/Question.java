package org.unlitrodeluzcolombia.radius.element;

import java.io.Serializable;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.dao.annotations.ComtorDaoFactory;
import net.comtor.dao.annotations.ComtorElement;
import net.comtor.dao.annotations.ComtorId;
import net.comtor.dao.annotations.ComtorSequence;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
@ComtorElement(tableName = "question")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class Question implements Serializable {

    private static final long serialVersionUID = 1350688140253038340L;

    @ComtorId
    @ComtorSequence(name = ComtorJDBCDao.MYSQL_SEQUENCE, typeInsert = ComtorSequence.POST_INSERT)
    private long id;
    private String type;
    private String question;
    private String options;
    private long survey;

    public Question() {
    }

    public Question(long survey, String question, String options) {
        this.survey = survey;
        this.question = question;
        this.options = options;
    }

    public Question(long survey, String question, String options, String type) {
        this.survey = survey;
        this.question = question;
        this.options = options;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSurvey() {
        return survey;
    }

    public void setSurvey(long survey) {
        this.survey = survey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String[] getOptionsArray() {
        return options.split("\r\n");
    }

    @Override
    public String toString() {
        return "Question{"
                + "id=" + id
                + ", survey=" + survey
                + ", type=" + type
                + ", question=" + question
                + '}';
    }

}
