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
    private long question;
    private String response;
    private java.sql.Timestamp answer_date;
    private java.sql.Date answer_ddate;
    private long hotspot;

    public Answer() {
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

    public java.sql.Timestamp getAnswer_date() {
        return answer_date;
    }

    public void setAnswer_date(java.sql.Timestamp answer_date) {
        this.answer_date = answer_date;
    }

    public java.sql.Date getAnswer_ddate() {
        return answer_ddate;
    }

    public void setAnswer_ddate(java.sql.Date answer_ddate) {
        this.answer_ddate = answer_ddate;
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
                + ", answer_date=" + answer_date
                + ", hotspot=" + hotspot
                + '}';
    }

}
