package org.unlitrodeluzcolombia.radius.gui.hotspot;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.ajax.HtmlJavaScript;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlIFrame;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlButton;
import net.comtor.html.form.HtmlInputText;
import net.comtor.html.form.HtmlSelect;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.Hotspot;
import net.comtor.radius.element.Zone;
import net.comtor.radius.facade.ZoneDAOFacade;
import org.unlitrodeluzcolombia.radius.gui.finder.ZoneFinder;
import org.unlitrodeluzcolombia.radius.web.facade.HotspotWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.ZoneWebFacade;
import web.global.LitroDeLuzImages;

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
        form.addField("Dirección IP", ip_address, null, true);

        HtmlInputText name = new HtmlInputText("name", 32, 64);
        form.addField("Nombre", name, null, true);

        HtmlInputText username = new HtmlInputText("username", 32, 64);
        form.addField("Usuario", username, "Indique el usuario de acceso al "
                + "Hotspot para configuración.");

        HtmlInputText password = new HtmlInputText("password", 32, 64);
        form.addField("Contraseña", password, "Indique la contraseña de acceso "
                + "al Hotspot para configuración.");
        
        form.addSubTitle("Ubicación");

        HtmlInputText what3words = new HtmlInputText("what3words");
        form.addField("What3Words", what3words, "Ingrese las 3 palabras correspondientes a esta ubicación.", true);

        HtmlButton button = new HtmlButton(HtmlButton.SCRIPT_BUTTON, "w3w_button",
                "Confirmar W3W", "getW3WInfo();");
        form.addRowInOneCell(button);

        form.addInputHidden("latitude", (hotspot == null) ? "" : hotspot.getLatitude());
        form.addInputHidden("longitude", (hotspot == null) ? "" : hotspot.getLongitude());
        form.addInputHidden("country", (hotspot == null) ? "" : hotspot.getZone_country());

        HtmlInputText coordinates = new HtmlInputText("coordinates", true);
        coordinates.setValue((hotspot == null) ? "0" : hotspot.getCoordinates());
        form.addField("Coordenadas", coordinates, null);

        HtmlInputText country_name = new HtmlInputText("country_name", true);
        country_name.setValue((hotspot == null) ? "" : hotspot.getZone_country());
        form.addField("País", country_name, null);

        HtmlSelect zone = getZoneSelect(hotspot);
        form.addField("Zona", zone, null, true);

        form.addRowInOneCell(getW3WScript());
    }

    @Override
    public void initFormView(AdministrableForm form, Hotspot hotspot) {
        HtmlText field;

        field = new HtmlText(hotspot.getCalled_station_id());
        form.addField("Called Station ID", field, null);

        field = new HtmlText(hotspot.getIp_address());
        form.addField("Dirección IP", field, null);

        field = new HtmlText(hotspot.getName());
        form.addField("Nombre", field, null);

        field = new HtmlText(hotspot.getUsername());
        form.addField("Usuario", field, null);

        field = new HtmlText(hotspot.getZone_name());
        form.addField("Zona", field, null);

        field = new HtmlText(hotspot.getCoordinates());
        form.addField("Coordenadas", field, null);

        field = new HtmlText(hotspot.getWhat3words());
        form.addField("What3Words", field, null);

        HtmlIFrame map = new HtmlIFrame("w3w_map_frame", "", "https://map.what3words.com/" + hotspot.getWhat3words());
        form.addRowInOneCell(map);
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
    public String getViewPrivilege() {
        return "VIEW_HOTSPOT";
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
        headers.put("ip_address", "Dirección IP");
        headers.put("name", "Nombre");
        headers.put("zone", "Zona");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Hotspot hotspot) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(hotspot.getId());
        row.add(hotspot.getCalled_station_id());
        row.add(hotspot.getIp_address());
        row.add(hotspot.getName());
        row.add(hotspot.getZone_name());

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.HOTSPOT_CONTROLLER;
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
        return "¿Está seguro que desea eliminar el hotspot <b>[" + hotspot.getId()
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

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este módulo.";
    }

    private HtmlSelect getZoneSelect(Hotspot hotspot) {
        HtmlSelect select = new HtmlSelect("zone");
        
        if (hotspot == null) {
            select.addOption("", "Seleccione una zona");
        } else {
            try {
                LinkedList<Zone> zones = new ZoneDAOFacade()
                        .findAllByProperty("country", hotspot.getZone_country());

                if (!zones.isEmpty()) {
                    for (Zone zone1 : zones) {
                        select.addOption(zone1.getId() + "", zone1.getName());
                    }
                }
            } catch (ComtorDaoException ex) {
               LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        
        return select;
    }

    private HtmlJavaScript getW3WScript() {
        String js = "\n"
                + "function getW3WInfo() { \n"
                + "    var $what3words = $(\"input#what3words\"); \n"
                + "    var $lat = $(\"input#latitude\"); \n"
                + "    var $lng = $(\"input#longitude\"); \n"
                + "    var w3w = $what3words.val(); \n"
                + "\n"
                + "    var settings = {\n"
                + "        \"async\": true,\n"
                + "        \"crossDomain\": true,\n"
                + "        \"url\": \"https://api.what3words.com/v3/convert-to-coordinates?key=NEVMR2XP&words=\" + w3w + \"&language=es&format=json\", \n"
                + "        \"method\": \"GET\",\n"
                + "        \"headers\": {}\n"
                + "    }\n"
                + "\n"
                + "    $.ajax(settings)\n"
                + "     .done(function (response) { \n"
                + "         var x = response[\"coordinates\"][\"lat\"]; \n"
                + "         var y = response[\"coordinates\"][\"lng\"]; \n"
                + "         var iso = response[\"country\"];\n\n"
                + ""
                + "         $lat.val(x); \n"
                + "         $lng.val(y); \n"
                + "         getCountryName(iso); \n"
                + "         $(\"#coordinates\").val(x + \", \" + y);\n"
                + "      getZonesSelect(iso);\n"
                + "     }) \n"
                + "     .fail(function(jqXHR, textStatus){ \n"
                + "         alert(\"Las palabras ingresadas '\" + w3w + \"' no son válidas. Intente nuevamente.\"); \n"
                + "\n"
                + "         $lat.val(\"\"); \n"
                + "         $lng.val(\"\"); \n"
                + "         $(\"#coordinates\").val(\"\"); \n"
                + "        $(\"#country_name\").val(\"\"); \n"
                + "        $(\"#zone\").empty();\n"
                + "     });\n"
                + "} \n"
                + "\n"
                + "function getCountryName(iso) { \n"
                + "  console.log(\"buscando \" + iso);\n"
                + "      var settings = {\n"
                + "        \"url\": \"/litrodeluz/radius/webservices/hotspot_services/map/get_country?iso=\" + iso, \n"
                + "        \"method\": \"GET\",\n"
                + "    }\n"
                + "\n"
                + "       $.ajax(settings) \n"
                + "     .done(function (response) { \n"
                + "        $(\"#country_name\").val(response); \n"
                + "    }) \n"
                + "     .fail(function(jqXHR, textStatus){ \n"
                + "        $(\"#country_name\").val(\"\"); \n"
                + "     });\n"
                + "}\n"
                + "\n"
                + "function getZonesSelect(country) {\n"
                + "      var settings = {\n"
                + "        \"url\": \"/litrodeluz/radius/webservices/hotspot_services/map/get_zones?country=\" + country, \n"
                + "        \"method\": \"GET\",\n"
                + "    }\n"
                + "\n"
                + "       $.ajax(settings) \n"
                + "     .done(function (response) { \n"
                + "         var $zone = $(\"#zone\");\n"
                + "         $zone.empty();\n"
                + "         $.each(response, function(i, item) {\n"
                + "             $zone.append('<option value=\"'+item.id+'\">'+item.name+'</option>');\n"
                + "         });\n"
                + "     }) \n"
                + "     .fail(function(jqXHR, textStatus){ \n"
                + "         $zone.empty().append('<option selected=\"selected\" value=\"\">Seleccione una zona</option>');\n"
                + "     });\n"
                + "} ";

        return new HtmlJavaScript(js);
    }   

}
