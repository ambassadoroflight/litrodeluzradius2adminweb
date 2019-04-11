package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.radius.element.Zone;
import net.comtor.radius.facade.ZoneDAOFacade;
import net.comtor.util.StringUtil;

/**
 *
 * @author juandiego@comtor.net
 * @since Apr 03, 2019
 */
public class ZoneWebFacade extends AbstractWebLogicFacade<Zone, Long, ZoneDAOFacade> {

    private static final Logger LOG = Logger.getLogger(ZoneWebFacade.class.getName());

    @Override
    public boolean delete(Long key) throws BusinessLogicException {
        try {
            new HotspotWebFacade().deleteHotspotsFromZone(key);
        } catch (BusinessLogicException ex) {
            throw ex;
        }

        return super.delete(key);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Zone zone) {
        return validate(zone);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Zone zone) {
        return validate(zone);
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

        return exceptions;
    }

}
