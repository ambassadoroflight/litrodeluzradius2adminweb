package org.unlitrodeluzcolombia.radius.gui.advertising;

import net.comtor.framework.html.administrable.AbstractComtorAdministrable;
import net.comtor.framework.html.administrable.ComtorAdministratorController;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 04, 2019
 */
public class SponsorAdmin extends AbstractComtorAdministrable {

    @Override
    protected ComtorAdministratorController getComtorAdministratorController() {
        return new SponsorController();
    }

}
