package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.util.StringUtil;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;
import net.comtor.radius.element.Hotspot;
import net.comtor.radius.facade.HotspotDAOFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class HotspotWebFacade extends AbstractWebLogicFacade<Hotspot, Long, HotspotDAOFacade> {

    private static final Logger LOG = Logger.getLogger(HotspotWebFacade.class.getName());

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
                                + "     hotspot.name                        LIKE ? \n"
                                + "     OR hotspot.called_station_id        LIKE ? \n"
                                + "     OR hotspot.ip_address               LIKE ? \n"
                                + " ) \n";

                        params.add(token);
                        params.add(token);
                        params.add(token);
                    }
                } else if (id.equals(ComtorFilterHelper.PARAMETER_NAME)
                        && (filter.getValue() != null)) {
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
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Hotspot hotspot) {
        return validate(hotspot);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Hotspot hotspot) {
        return validate(hotspot);
    }

    public long getCountByZone(long zone) throws BusinessLogicException {
        try {
            return getDaoFacade().getCountByZone(zone);
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    public LinkedList<Hotspot> findAllByProperty(String property, Object value)
            throws BusinessLogicException {
        try {
            return getDaoFacade().findAllByProperty(property, value);
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    public void deleteHotspotsFromZone(final long zone) throws BusinessLogicException {
        try {
            getDaoFacade().deleteHotspotsFromZone(zone);
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            throw new BusinessLogicException(ex);
        }
    }

    public List<Hotspot> findBySponsor(final Long sponsor) throws BusinessLogicException {
        try {
            return getDaoFacade().findBySponsor(sponsor);
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            throw new BusinessLogicException(ex);
        }
    }

    private LinkedList<ObjectValidatorException> validate(final Hotspot hotspot) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        String called_station_id = hotspot.getCalled_station_id();

        if (StringUtil.isValid(called_station_id)) {
            try {
                Hotspot other = getDaoFacade().findByProperty("called_station_id",
                        called_station_id);

                if ((other != null) && (other.getId() != hotspot.getId())) {
                    exceptions.add(new ObjectValidatorException("called_station_id",
                            "El Called Station ID. <b>" + called_station_id
                            + "</b> ya está en uso."));
                }
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            exceptions.add(new ObjectValidatorException("called_station_id",
                    "Debe ingresar el Called Station ID. del Hotspot."));
        }

        if (!StringUtil.isValid(hotspot.getName())) {
            exceptions.add(new ObjectValidatorException("name",
                    "Debe ingresa el nombre del Hotspot."));
        }

        String ip_address = hotspot.getIp_address();

        if (StringUtil.isValid(ip_address)) {
            if (!isIpAddress(ip_address)) {
                exceptions.add(new ObjectValidatorException("ip_address",
                        "La dirección IP ingresada no es válida."));
            }
        } else {
            exceptions.add(new ObjectValidatorException("ip_address",
                    "Debe ingresar la dirección IP del Hotspot."));
        }

        if (hotspot.getZone() <= 0) {
            exceptions.add(new ObjectValidatorException("zone",
                    "Debe indicar a qué zona pertenece el Hotspot."));
        }

        return exceptions;
    }

    private boolean isIpAddress(String ip_address) {
        return ip_address.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25"
                + "[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    }
}
