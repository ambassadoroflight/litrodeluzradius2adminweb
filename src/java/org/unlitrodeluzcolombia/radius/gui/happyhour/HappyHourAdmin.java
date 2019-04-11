package org.unlitrodeluzcolombia.radius.gui.happyhour;

import net.comtor.framework.html.administrable.AbstractComtorAdministrable;
import net.comtor.framework.html.administrable.ComtorAdministratorController;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class HappyHourAdmin extends AbstractComtorAdministrable {

    @Override
    protected ComtorAdministratorController getComtorAdministratorController() {
        return new HappyHourController();
    }

}
