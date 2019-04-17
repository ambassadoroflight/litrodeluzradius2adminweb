package org.unlitrodeluzcolombia.radius.gui.hotspot;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.Hotspot;
import net.comtor.radius.element.Zone;
import org.unlitrodeluzcolombia.radius.gui.finder.ZoneFinder;
import org.unlitrodeluzcolombia.radius.web.facade.HotspotWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.ZoneWebFacade;
import web.Images;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class HotspotController
        extends AbstractComtorFacadeAdministratorControllerI18n<Hotspot, Long> {

    private static final Logger LOG = Logger.getLogger(HotspotController.class.getName());

    @Override
    public String getEntityName() {
        return "Hotspot";
    }

    @Override
    public WebLogicFacade<Hotspot, Long> getLogicFacade() {
        return new HotspotWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, Hotspot hotspot)
            throws BusinessLogicException {
        if (hotspot != null) {
            final long hotspot_id = hotspot.getId();

            form.addInputHidden("id", hotspot_id);

            HtmlText id = new HtmlText(hotspot_id);
            form.addField("ID", id, null);
        }

        HtmlInputText called_station_id = new HtmlInputText("called_station_id", 32, 64);
        form.addField("Called Station ID", called_station_id, null, true);

        HtmlInputText ip_address = new HtmlInputText("ip_address", 32, 64);
        form.addField("Direcci�n IP", ip_address, null, true);

        HtmlInputText name = new HtmlInputText("name", 32, 64);
        form.addField("Nombre", name, null, true);

        HtmlFinder zone = getZoneFinder(hotspot);
        form.addField("Zona", zone, "Indique la zona a la que pertenece este hotspot", true);

        HtmlInputText username = new HtmlInputText("username", 32, 64);
        form.addField("Usuario", username, "Indique el usuario de acceso al "
                + "Hotspot para configuraci�n.");

        HtmlInputText password = new HtmlInputText("password", 32, 64);
        form.addField("Contrase�a", password, "Indique la contrase�a de acceso "
                + "al Hotspot para configuraci�n.");
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_HOTSPOT";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_HOTSPOT";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_HOTSPOT";
    }

    @Override
    public String getFormName() {
        return "hotspot_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("called_station_id", "Called Station ID");
        headers.put("ip_address", "Direcci�n IP");
        headers.put("name", "Nombre");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Hotspot hotspot) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(hotspot.getId());
        row.add(hotspot.getCalled_station_id());
        row.add(hotspot.getIp_address());
        row.add(hotspot.getName());

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return Images.HOTSPOTS_WHITE_32;
    }

    @Override
    public String getLogModule() {
        return "Hotspots";
    }

    @Override
    public String getAddFormLabel() {
        return "Nuevo Hotspot";
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Crear Hotspot";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar Hotspot";
    }

    @Override
    public String getConfirmDeleteMessage(Hotspot hotspot) {
        return "�Est� seguro que desea eliminar el hotspot <b>[" + hotspot.getId()
                + "] " + hotspot.getName() + "</b>?";
    }

    @Override
    public String getAddedMessage(Hotspot hotspot) {
        return "El hotspot <b>[" + hotspot.getId() + "] " + hotspot.getName()
                + "</b> ha sido creado.";
    }

    @Override
    public String getDeletedMessage(Hotspot hotspot) {
        return "El hotspot <b>[" + hotspot.getId() + "] " + hotspot.getName()
                + "</b> ha sido eliminado.";
    }

    @Override
    public String getUpdatedMessage(Hotspot hotspot) {
        return "El hotspot <b>[" + hotspot.getId() + "] " + hotspot.getName()
                + "</b> ha sido actualizado.";
    }

    private HtmlFinder getZoneFinder(final Hotspot hotspot) {
        Zone zone = null;
        String valueToShow = "";

        try {
            zone = ((hotspot == null)
                    ? null
                    : new ZoneWebFacade().find(hotspot.getZone()));
            valueToShow = ((zone == null)
                    ? ""
                    : new ZoneFinder().getValueToShow(zone));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return new HtmlFinder("zone", ZoneFinder.class, valueToShow, 32);
    }

}
