package org.unlitrodeluzcolombia.radius.gui.hotspot;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.AdvertisingCampaign;
import net.comtor.radius.element.Zone;
import net.comtor.radius.facade.HotspotDAOFacade;
import org.unlitrodeluzcolombia.radius.gui.finder.AdvertisingCampaignFinder;
import org.unlitrodeluzcolombia.radius.web.facade.AdvertisingCampaignWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.ZoneWebFacade;
import web.Images;

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

            HtmlText id = new HtmlText(zone_id);
            form.addField("ID", id, null);
        }

        HtmlInputText name = new HtmlInputText("name", 32, 64);
        form.addField("Nombre", name, null, true);
        
        HtmlFinder advertising_campaign = getCampaignFinder(zone);
        form.addField("Campaña Publicitaria", advertising_campaign, null);
        //TODO: AGREGAR HOTSPOTS DESDE ESTRE CRUD?
    }

    @Override
    public void initFormView(AdministrableForm form, Zone zone) {
        HtmlText field = new HtmlText(zone.getId());
        form.addField("ID", field, null);

        field = new HtmlText(zone.getName());
        form.addField("Descripción", field, null);

        super.initFormView(form, zone);
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
        return "VIEW_SPONSOR";
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
        return Images.HOTSPOTS_WHITE_32;
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
                + "] " + zone.getName() + "</b>? Los hotspots pertenecientes "
                + "quedarán sin una zona asignada.";
    }

    @Override
    public String getAddedMessage(Zone zone) {
        return "La zona <b>[" + zone.getId() + "] " + zone.getName()
                + "</b> ha sido creada.";
    }

    @Override
    public String getDeletedMessage(Zone zone) {
        return "El hotspot <b>[" + zone.getId() + "] " + zone.getName()
                + "</b> ha sido eliminado.";
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

    private HtmlFinder getCampaignFinder(final Zone zone) {
        AdvertisingCampaign campaign = null;
        String valueToShow = "";

        try {
            campaign = ((zone == null)
                    ? null
                    : new AdvertisingCampaignWebFacade().find(zone.getAdvertising_campaign()));
            valueToShow = ((campaign == null)
                    ? ""
                    : new AdvertisingCampaignFinder().getValueToShow(campaign));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return new HtmlFinder("advertising_campaign", AdvertisingCampaignFinder.class, valueToShow, 32);
    }

}
