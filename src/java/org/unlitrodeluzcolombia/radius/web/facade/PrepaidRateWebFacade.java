package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.radius.element.PrepaidRate;
import net.comtor.radius.facade.PrepaidRateDAOFacade;
import net.comtor.util.StringUtil;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class PrepaidRateWebFacade extends AbstractWebLogicFacade<PrepaidRate, Long, PrepaidRateDAOFacade> {

    private static final Logger LOG = Logger.getLogger(PrepaidRateWebFacade.class.getName());

    @Override
    public String getWhere(ComtorObjectCriterions criterions, LinkedList<Object> params) {
        String where = ""
                + " WHERE \n"
                + "     1 = 1 \n";

        for (ComtorObjectListFilter filter : criterions.getFilters()) {
            final String id = filter.getId();
            String value = filter.getValue().toString().trim();

            if (StringUtil.isValid(value)) {
                if (id.equals(ComtorFilterHelper.FILTER_NAME)) {
                    StringTokenizer stt = new StringTokenizer(filter.getValue().toString(), " ");

                    while (stt.hasMoreTokens()) {
                        String token = "%" + stt.nextToken().replaceAll("'", " ") + "%";

                        where += ""
                                + " AND ( \n"
                                + "     prepaid_rate.description LIKE ? \n"
                                + " ) \n";

                        params.add(token);
                    }
                } else if (id.equals(ComtorFilterHelper.PARAMETER_NAME) && (filter.getValue() != null)) {
                    // Si hay finder con parámetro
                } else if (filter.getType() == ComtorObjectListFilter.TYPE_EQUALS) {
                    // Si hay filtro avanzado
                } else {
                    where += " AND " + id + " = ? \n";
                    params.add(value);
                }
            }
        }

        return where;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(PrepaidRate rate) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        String description = rate.getDescription();

        if (StringUtil.isValid(description)) {
            try {
                PrepaidRate other = getDaoFacade().findByProperty("description", description);

                if (other != null) {
                    exceptions.add(new ObjectValidatorException("description",
                            "La descripción <b>" + description + "</b> ya está en uso."));
                }
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            exceptions.add(new ObjectValidatorException("description", "Debe "
                    + "indicar una descripción."));
        }

        if (rate.getDuration_in_seconds() <= 0) {
            exceptions.add(new ObjectValidatorException("duration_in_seconds", "Debe "
                    + "ingresar una cantidad mayor a cero."));
        }

        return exceptions;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(PrepaidRate rate) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        String description = rate.getDescription();

        if (StringUtil.isValid(description)) {
            try {
                PrepaidRate other = getDaoFacade().findByProperty("description", description);

                if ((other != null) && (other.getId() != rate.getId())) {
                    exceptions.add(new ObjectValidatorException("description",
                            "La descripción <b>" + description + "</b> ya está en uso."));
                }
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            exceptions.add(new ObjectValidatorException("description", "Debe "
                    + "indicar una descripción."));
        }

    if (rate.getDuration_in_seconds() <= 0) {
            exceptions.add(new ObjectValidatorException("duration_in_seconds", "Debe "
                    + "ingresar una cantidad mayor a cero."));
        }

        return exceptions;
    }

}
