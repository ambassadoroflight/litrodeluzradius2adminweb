package org.unlitrodeluzcolombia.radius.gui.advertising;

import net.comtor.framework.html.administrable.AbstractComtorAdministrable;
import net.comtor.framework.html.administrable.ComtorAdministratorController;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class SurveyAdmin extends AbstractComtorAdministrable {

    @Override
    protected ComtorAdministratorController getComtorAdministratorController() {
        return new SurveyController();
    }

}
