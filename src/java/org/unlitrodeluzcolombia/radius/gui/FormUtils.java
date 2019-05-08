package org.unlitrodeluzcolombia.radius.gui;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.html.form.HtmlSelect;
import net.comtor.radius.element.Country;
import net.comtor.radius.element.Sponsor;
import net.comtor.radius.facade.CountryDAOFacade;
import net.comtor.radius.facade.SponsorDAOFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since
 * @version May 08, 2019
 */
public final class FormUtils {

    private static final Logger LOG = Logger.getLogger(FormUtils.class.getName());

    private FormUtils() {

    }

    public static HtmlSelect getSponsorSelect() {
        HtmlSelect select = new HtmlSelect("sponsor");
        select.addOption("0", "Todos");

        try {
            List<Sponsor> sponsors = new SponsorDAOFacade().findAll();

            if (!sponsors.isEmpty()) {
                for (Sponsor sponsor : sponsors) {
                    select.addOption(sponsor.getId() + "", sponsor.getName());
                }
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return null;

        }

        return select;
    }

    public static HtmlSelect getCountrySelect() {
        HtmlSelect select = new HtmlSelect("country");
        select.addOption("", "Todos");

        try {
            List<Country> countries = new CountryDAOFacade().findAll();

            if (!countries.isEmpty()) {
                for (Country country : countries) {
                    select.addOption(country.getIso(), country.getName());
                }
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return select;
    }
}
