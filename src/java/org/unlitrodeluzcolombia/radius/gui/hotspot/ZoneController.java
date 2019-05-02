package org.unlitrodeluzcolombia.radius.gui.hotspot;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlText;
import net.comtor.html.HtmlUl;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.Hotspot;
import net.comtor.radius.element.Zone;
import net.comtor.radius.facade.HotspotDAOFacade;
import org.unlitrodeluzcolombia.radius.web.facade.HotspotWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.ZoneWebFacade;
import web.global.LitroDeLuzImages;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 03, 2019
 */
public class ZoneController
        extends AbstractComtorFacadeAdministratorControllerI18n<Zone, Long> {

    private static final Logger LOG = Logger.getLogger(ZoneController.class.getName());

    private final HotspotDAOFacade hotspotFacade = new HotspotDAOFacade();

    @Override
    public String getEntityName() {
        return "Zonas de Hotspots";
    }

    @Override
    public WebLogicFacade<Zone, Long> getLogicFacade() {
        return new ZoneWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, Zone zone)
            throws BusinessLogicException {
        if (zone != null) {
            final long zone_id = zone.getId();

            form.addInputHidden("id", zone_id);
        }

        HtmlInputText name = new HtmlInputText("name", 32, 64);
        form.addField("Nombre", name, null, true);
    }

    @Override
    public void initFormView(AdministrableForm form, Zone zone) {
        HtmlText field = new HtmlText(zone.getName());
        form.addField("Nombre", field, null);

        List<Hotspot> hotspots = new LinkedList<>();

        try {
            hotspots = new HotspotWebFacade().findAllByProperty("zone", zone.getId());
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        if (!hotspots.isEmpty()) {
            HtmlUl list = new HtmlUl();

            for (Hotspot hotspot : hotspots) {
                list.addListElement("[" + hotspot.getCalled_station_id() + "] "
                        + hotspot.getName());
            }

            form.addField("Hotspots Asociados", list, null);
        }

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
    public String getViewPrivilege() {
        return "VIEW_ZONE";
    }

    @Override
    public String getFormName() {
        return "zone_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("name", "Nombre");
        headers.put("", "# Hotspots Asociados");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Zone zone) {
        LinkedList<Object> row = new LinkedList<>();
        long id = zone.getId();

        row.add(id);
        row.add(zone.getName());

        try {
            row.add(hotspotFacade.getCountByZone(id));
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            row.add("Error");
        }

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.HOTSPOT_CONTROLLER;
    }

    @Override
    public String getLogModule() {
        return "Zonas";
    }

    @Override
    public String getAddFormLabel() {
        return "Nueva Zona";
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Crear Zona";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar Zona";
    }

    @Override
    public String getConfirmDeleteMessage(Zone zone) {
        return "¿Está seguro que desea eliminar la zona <b>[" + zone.getId()
                + "] " + zone.getName() + "</b>?";
    }

    @Override
    public String getAddedMessage(Zone zone) {
        return "La zona <b>[" + zone.getId() + "] " + zone.getName()
                + "</b> ha sido creada.";
    }

    @Override
    public String getDeletedMessage(Zone zone) {
        return "La zona <b>[" + zone.getId() + "] " + zone.getName()
                + "</b> ha sido eliminada.";
    }

    @Override
    public String getUpdatedMessage(Zone zone) {
        return "La zona <b>[" + zone.getId() + "] " + zone.getName()
                + "</b> ha sido actualizada.";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este módulo.";
    }

}
