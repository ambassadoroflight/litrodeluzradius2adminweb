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
@ComtorElement(tableName = "answer")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class Answer implements Serializable {

    private static final long serialVersionUID = -6876722431142964639L;

    @ComtorId
    @ComtorSequence(name = ComtorJDBCDao.MYSQL_SEQUENCE, typeInsert = ComtorSequence.POST_INSERT)
    private long id;
    private String response;
    private java.sql.Date answer_date;
    private long question;
    private long hotspot;

    public Answer() {
    }

    public Answer(long hotspot, java.sql.Date answer_date) {
        this.hotspot = hotspot;
        this.answer_date = answer_date;
    }

    public Answer(long question, String response) {
        this.question = question;
        this.response = response;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestion() {
        return question;
    }

    public void setQuestion(long question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public java.sql.Date getAnswer_date() {
        return answer_date;
    }

    public void setAnswer_date(java.sql.Date answer_date) {
        this.answer_date = answer_date;
    }

    public long getHotspot() {
        return hotspot;
    }

    public void setHotspot(long hotspot) {
        this.hotspot = hotspot;
    }

    @Override
    public String toString() {
        return "Answer{"
                + "id=" + id
                + ", question=" + question
                + ", response=" + response
                + ", hotspot=" + hotspot
                + '}';
    }

}
