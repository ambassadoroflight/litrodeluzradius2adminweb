package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.radius.element.Zone;
import net.comtor.radius.facade.ZoneDAOFacade;
import net.comtor.util.StringUtil;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;

/**
 *
 * @author juandiego@comtor.net
 * @since Apr 03, 2019
 */
public class ZoneWebFacade extends AbstractWebLogicFacade<Zone, Long, ZoneDAOFacade> {

    private static final Logger LOG = Logger.getLogger(ZoneWebFacade.class.getName());

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
                                + "     zone.name                LIKE ? \n"
                                + "     OR country.name             LIKE ? \n"
                                + " ) \n";

                        params.add(token);
                        params.add(token);
                    }
                } else if (id.equals(ComtorFilterHelper.PARAMETER_NAME) && (filter.getValue() != null)) {
                    // Si hay finder con parmetro
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
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Zone zone) {
        return validate(zone);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Zone zone) {
        return validate(zone);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreDelete(Zone zone) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        // No puedo borrar una zona si tiene hotspots. Primero hay que cambiarles la zona.
        try {
            long hotspots = new HotspotWebFacade().getCountByZone(zone.getId());

            if (hotspots > 0) {
                exceptions.add(new ObjectValidatorException("id", "Hay hotspot "
                        + "asociados a esta zona. Antes de eliminarla, reasigne "
                        + "la zona de los hotspots."));
            }
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return exceptions;
    }

    private LinkedList<ObjectValidatorException> validate(Zone zone) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();
        String name = zone.getName();

        if (StringUtil.isValid(name)) {
            Zone other = null;

            try {
                other = getDaoFacade().findByProperty("name", name);
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }

            if ((other != null) && (other.getId() != zone.getId())) {
                exceptions.add(new ObjectValidatorException("name",
                        "El nombre <b><i>" + name + "</i></b> ya está en uso."));
            }
        } else {
            exceptions.add(new ObjectValidatorException("name", "Debe ingresar un nombre."));
        }

        if (!StringUtil.isValid(zone.getCountry())) {
            exceptions.add(new ObjectValidatorException("country", 
                    "Debe indicar el país al que pertenece la zona."));
        }

        return exceptions;
    }

    public LinkedList<Zone> findAll() throws BusinessLogicException {
        try {
            return getDaoFacade().findAll();
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
    }

}
