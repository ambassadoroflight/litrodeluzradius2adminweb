package web.connection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import web.global.GlobalLib;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2019
 */
public class ApplicationDAOListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String projectPath = sce.getServletContext().getRealPath("");
        String nameConnection = GlobalWeb.NAME_CONNECTION;
        
        GlobalLib.setProjectParams(projectPath, nameConnection);
        
        System.out.println("\n> Cargando Parametros de Conexion "
                + GlobalWeb.PROJECT_NAME + " [" + GlobalWeb.VERSION + "]: "
                + GlobalWeb.NAME_CONNECTION + "\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        GlobalLib.dispose();

        System.out.println("\n> Destruyendo Parametros de Conexion "
                + GlobalWeb.PROJECT_NAME + " [" + GlobalWeb.VERSION + "]: "
                + GlobalWeb.NAME_CONNECTION + "\n");
    }
}
