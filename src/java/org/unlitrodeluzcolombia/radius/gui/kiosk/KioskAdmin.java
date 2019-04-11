package org.unlitrodeluzcolombia.radius.gui.kiosk;

import net.comtor.framework.html.administrable.AbstractComtorAdministrable;
import net.comtor.framework.html.administrable.ComtorAdministratorController;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class KioskAdmin  extends AbstractComtorAdministrable {

    @Override
    protected ComtorAdministratorController getComtorAdministratorController() {
        return new KioskController();
    }
    
}
