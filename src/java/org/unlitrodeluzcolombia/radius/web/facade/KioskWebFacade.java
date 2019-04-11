package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.StringTokenizer;
import net.comtor.radius.facade.KioskDAOFacade;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.radius.element.Kiosk;
import net.comtor.util.StringUtil;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class KioskWebFacade extends AbstractWebLogicFacade<Kiosk, Long, KioskDAOFacade> {

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
                                + "     kiosk.nit           LIKE ? \n"
                                + "     OR kiosk.name       LIKE ? \n"
                                + "     OR kiosk.city       LIKE ? \n"
                                + "     OR kiosk.address    LIKE ? \n"
                                + " ) \n";

                        params.add(token);
                        params.add(token);
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
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Kiosk kiosk) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        validate(exceptions, kiosk);

        return exceptions;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Kiosk kiosk) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        validate(exceptions, kiosk);

        return exceptions;
    }

    private void validate(LinkedList<ObjectValidatorException> exceptions, Kiosk kiosk) {
        if (kiosk.getHotspot() <= 0) {
            exceptions.add(new ObjectValidatorException("hotspot", "Debe indicar el "
                    + "Hotspot"));
        }

        if (!StringUtil.isValid(kiosk.getNit())) {
            exceptions.add(new ObjectValidatorException("nit", "Debe ingresar el "
                    + "ID. del Beneficiario."));
        }

        if (!StringUtil.isValid(kiosk.getName())) {
            exceptions.add(new ObjectValidatorException("name", "Debe ingresar el "
                    + "nombre del kiosco."));
        }

        String email = kiosk.getEmail();

        if (StringUtil.isValid(email) && !StringUtil.isEmail(email)) {
            exceptions.add(new ObjectValidatorException("email", "El e-mail no "
                    + "es válido."));
        }
    }
}
