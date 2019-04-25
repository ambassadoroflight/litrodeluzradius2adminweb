package org.unlitrodeluzcolombia.radius.facade;

import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import org.unlitrodeluzcolombia.radius.element.Answer;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class AnswerDAOFacade extends ComtorDaoElementLogicFacade<Answer, Long> {

    @Override
    public void create(Answer answer) throws ComtorDaoException {
        long now = System.currentTimeMillis();
        
        answer.setAnswer_date(new java.sql.Date(now));
        
        super.create(answer);
    }

}
