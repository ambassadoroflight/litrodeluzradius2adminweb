package org.unlitrodeluzcolombia.radius.ws;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 28, 2019
 */
@javax.ws.rs.ApplicationPath("webservices")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();

        addRestResourceClasses(resources);

        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.unlitrodeluzcolombia.radius.ws.LitroDeLuzRadiusResource.class);
    }

}
