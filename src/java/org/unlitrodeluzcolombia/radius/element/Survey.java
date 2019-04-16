package org.unlitrodeluzcolombia.radius.element;

import java.io.Serializable;
import java.util.LinkedList;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.dao.annotations.Cascade;
import net.comtor.dao.annotations.ComtorDaoFactory;
import net.comtor.dao.annotations.ComtorElement;
import net.comtor.dao.annotations.ComtorId;
import net.comtor.dao.annotations.ComtorOneToMany;
import net.comtor.dao.annotations.ComtorSequence;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
@ComtorElement(tableName = "survey")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class Survey implements Serializable {

    private static final long serialVersionUID = 4490621612207967377L;

    @ComtorId
    @ComtorSequence(name = ComtorJDBCDao.MYSQL_SEQUENCE, typeInsert = ComtorSequence.POST_INSERT)
    private long id;
    private String description;

    public Survey() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Survey{"
                + "id=" + id
                + ", description=" + description
                + '}';
    }

}
