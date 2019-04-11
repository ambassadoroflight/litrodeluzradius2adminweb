package org.unlitrodeluzcolombia.radius.web.facade;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import net.comtor.aaa.helper.UserHelper;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.framework.request.validator.RequestBasicValidator;
import net.comtor.util.StringUtil;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;
import net.comtor.radius.element.HappyHour;
import net.comtor.radius.facade.HappyHourDAOFacade;
import net.comtor.radius.utils.ComtorRadiusUtils;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class HappyHourWebFacade
        extends AbstractWebLogicFacade<HappyHour, Long, HappyHourDAOFacade> {

    private static final Logger LOG = Logger.getLogger(HappyHourWebFacade.class.getName());

    @Override
    public void insert(HappyHour happyHour) throws BusinessLogicException {
        happyHour.setLogin(createHappyHourPin());
        happyHour.setCreation_date(new Timestamp(System.currentTimeMillis()));
        happyHour.setCreated_by(UserHelper.getCurrentUser(getRequest()).getLogin());

        super.insert(happyHour);
    }

    @Override
    public void update(HappyHour happyHour) throws BusinessLogicException {
        String newPin = RequestBasicValidator.getStringFromRequest(getRequest(), "newPin");

        if (StringUtil.isValid(newPin) && newPin.equals("active")) {
            happyHour.setLogin(createHappyHourPin());
        }

        happyHour.setCreation_date(new Timestamp(System.currentTimeMillis()));
        happyHour.setCreated_by(UserHelper.getCurrentUser(getRequest()).getLogin());

        super.update(happyHour);
    }

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
                                + "     happy_hour.id       LIKE ? \n"
                                + "     OR happy_hour.login LIKE ? \n"
                                + " ) \n";

                        params.add(token);
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
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(HappyHour happyHour) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        validate(happyHour, exceptions);

        return exceptions;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(HappyHour happyHour) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        validate(happyHour, exceptions);

        return exceptions;
    }

    public LinkedList<HappyHour> findAllByProperty(String property, Object value)
            throws BusinessLogicException {
        try {
            return getDaoFacade().findAllByProperty(property, value);
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    private void validate(HappyHour happyHour, LinkedList<ObjectValidatorException> exceptions) {
        if (happyHour.getHotspot() <= 0) {
            exceptions.add(new ObjectValidatorException("hotspot", "Debe indicar "
                    + "a qué Hotspot se aplicará la Hora Libre."));
        }

        if (happyHour.getStart_time() >= happyHour.getEnd_time()) {
            exceptions.add(new ObjectValidatorException("start_time", "La hora "
                    + "de inicio no puede ser depués de la hora final."));
            exceptions.add(new ObjectValidatorException("end_time", "La hora de "
                    + "inicio no puede ser depués de la hora final."));
        }
    }

    private String createHappyHourPin() {
        return HappyHour.PIN_PREFIX + ComtorRadiusUtils.generatePin(2, 3);
    }

}
