package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.radius.element.Sponsor;
import net.comtor.radius.facade.CampaignDAOFacade;
import net.comtor.radius.facade.SponsorDAOFacade;
import net.comtor.util.StringUtil;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 03, 2019
 */
public class SponsorWebFacade extends AbstractWebLogicFacade<Sponsor, Long, SponsorDAOFacade> {

    private static final Logger LOG = Logger.getLogger(SponsorWebFacade.class.getName());

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Sponsor sponsor) {
        return validate(sponsor);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Sponsor sponsor) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        if (!StringUtil.isValid(sponsor.getName())) {
            exceptions.add(new ObjectValidatorException("name", "Debe ingresar un nombre."));
        }

        return exceptions;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreDelete(Sponsor sponsor) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        // No puedo borrar un patrocinador si tiene campañas.
        try {
            String query = "\n"
                    + " SELECT \n"
                    + "     campaign.* \n"
                    + " FROM \n"
                    + "     campaign \n"
                    + " WHERE \n"
                    + "     campaign.sponsor = ? \n";

            long adsQty = new CampaignDAOFacade().getCountElements(query,
                    sponsor.getId());

            if (adsQty > 0) {
                exceptions.add(new ObjectValidatorException("sponsor",
                        "Este patrocinador no puede ser eliminado, tiene "
                        + adsQty + " campaña(s) publicitaria(s) asociada(s)."));
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return exceptions;
    }

    private LinkedList<ObjectValidatorException> validate(Sponsor sponsor) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        if (!StringUtil.isValid(sponsor.getName())) {
            exceptions.add(new ObjectValidatorException("name", "Debe ingresar un nombre."));
        }

        return exceptions;
    }

}
