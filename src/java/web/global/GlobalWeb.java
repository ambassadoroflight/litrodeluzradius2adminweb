package web.global;

import java.io.File;
import java.util.logging.Logger;
import net.comtor.util.connection.ConnectionType;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2019
 */
public class GlobalWeb {

    private static final Logger LOG = Logger.getLogger(GlobalWeb.class.getName());

    public static final String PROJECT_NAME = "Litro de Luz - Administrador Web Radius 2";
    public static String VERSION = "Versión 0.0.1 [14-Feb-2019]";
    public static final String NAME_CONNECTION = "litrodeluz_radius2";
    public static final ConnectionType CONNECTION_TYPE = ConnectionType.DEVELOPMENT;

    public static final String BASE_PATH = File.separator + "opt";

    public static final String ADS_DIRECTORY = BASE_PATH + File.separator
            + "litro_de_luz" + File.separator + "hotspot" + File.separator 
            + "publicidad";

    static {
        switch (CONNECTION_TYPE) {
            case DEVELOPMENT:
                VERSION += " (Desarrollo)";
                break;
            case TEST:
                VERSION += " (Pruebas)";
                break;
        }
    }

}
