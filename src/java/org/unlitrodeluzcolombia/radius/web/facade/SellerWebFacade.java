package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.radius.element.Seller;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.radius.facade.SellerDAOFacade;
import net.comtor.util.StringUtil;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class SellerWebFacade
        extends AbstractWebLogicFacade<Seller, String, SellerDAOFacade> {

    private static final Logger LOG = Logger.getLogger(SellerWebFacade.class.getName());

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
                                + "     seller.login    LIKE ? \n"
                                + "     OR seller.name  LIKE ? \n"
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
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Seller seller) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        String login = seller.getLogin();

        if (StringUtil.isValid(login)) {
            try {
                if (find(login) != null) {
                    exceptions.add(new ObjectValidatorException("login", "El usuario <b>"
                            + login + "</b> ya está en uso."));
                }
            } catch (BusinessLogicException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            exceptions.add(new ObjectValidatorException("login", "Debe ingresar "
                    + "el nombre de usuario."));
        }

        validate(seller, exceptions);

        return exceptions;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Seller seller) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        validate(seller, exceptions);

        return exceptions;
    }

    private void validate(Seller seller, LinkedList<ObjectValidatorException> exceptions) {
        if (!StringUtil.isValid(seller.getPassword())) {
            exceptions.add(new ObjectValidatorException("password", "Debe ingresar "
                    + "la contraseña."));
        } else if (!seller.getConfirm_password().equals(seller.getPassword())) {
            exceptions.add(new ObjectValidatorException("password", "Las contraseñas "
                    + "no coinciden."));
            exceptions.add(new ObjectValidatorException("confirm_password",
                    "Las contraseñas no coinciden."));
        }

        if (!StringUtil.isValid(seller.getName())) {
            exceptions.add(new ObjectValidatorException("name", "Debe ingresar el "
                    + "nombre del vendedor."));
        }

        if (seller.getKiosk() <= 0) {
            exceptions.add(new ObjectValidatorException("kiosk", "Debe indicar a "
                    + "cuál kiosco pertenece el vendedor."));
        }
    }

}
