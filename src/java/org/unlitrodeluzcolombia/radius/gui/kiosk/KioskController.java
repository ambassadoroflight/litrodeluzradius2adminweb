package org.unlitrodeluzcolombia.radius.gui.kiosk;

import net.comtor.radius.element.Kiosk;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.Hotspot;
import org.unlitrodeluzcolombia.radius.gui.finder.HotspotFinder;
import org.unlitrodeluzcolombia.radius.web.facade.HotspotWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.KioskWebFacade;
import web.global.LitroDeLuzImages;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class KioskController
        extends AbstractComtorFacadeAdministratorControllerI18n<Kiosk, Long> {

    private static final Logger LOG = Logger.getLogger(KioskController.class.getName());

    @Override
    public String getEntityName() {
        return "Kiosco";
    }

    @Override
    public WebLogicFacade<Kiosk, Long> getLogicFacade() {
        return new KioskWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, Kiosk kiosk)
            throws BusinessLogicException {
        if (kiosk != null) {
            form.addInputHidden("id", kiosk.getId());
        }

        HtmlInputText nit = new HtmlInputText("nit", 32, 64);
        form.addField("ID. Beneficiario", nit, null, true);

        HtmlFinder hotspot = getHotspotFinder(kiosk);
        form.addField("Hotspot", hotspot, null, true);

        HtmlInputText name = new HtmlInputText("name", 32, 64);
        form.addField("Nombre", name, null, true);

        HtmlInputText email = new HtmlInputText("email", 32, 128);
        form.addField("E-mail", email, null);

        HtmlInputText phone = new HtmlInputText("phone", 16, 32);
        form.addField("Teléfono", phone, null);

        HtmlInputText address = new HtmlInputText("address", 32, 128);
        form.addField("Dirección", address, null);

        HtmlInputText city = new HtmlInputText("city", 32, 128);
        form.addField("Ciudad/Municipio", city, null);
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_KIOSK";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_KIOSK";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_KIOSK";
    }

    @Override
    public String getFormName() {
        return "kiosk_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("hotspot", "Hotspot");
        headers.put("nit", "ID. Beneficiario");
        headers.put("name", "Nombre");
        headers.put("city", "Ciudad");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Kiosk kiosk) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(kiosk.getId());
        row.add("[" + kiosk.getHotspot() + "] " + kiosk.getHotspot_name());
        row.add(kiosk.getNit());
        row.add(kiosk.getName());
        row.add(kiosk.getCity());

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.SELLER_CONTROLLER;
    }

    @Override
    public String getLogModule() {
        return "Kioscos";
    }

    @Override
    public String getAddFormLabel() {
        return "Nuevo Kiosco";
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Crear Kiosco";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar Kiosco";
    }

    @Override
    public String getConfirmDeleteMessage(Kiosk kiosk) {
        return "¿Está seguro que desea eliminar el kiosco <b>[" + kiosk.getId()
                + "] " + kiosk.getName() + "</b>?";
    }

    @Override
    public String getAddedMessage(Kiosk kiosk) {
        return "El kiosco <b>[" + kiosk.getId() + "] " + kiosk.getName()
                + "</b> ha sido creado.";
    }

    @Override
    public String getDeletedMessage(Kiosk kiosk) {
        return "El kiosco <b>[" + kiosk.getId() + "] " + kiosk.getName()
                + "</b> ha sido eliminado.";
    }

    @Override
    public String getUpdatedMessage(Kiosk kiosk) {
        return "El kiosco <b>[" + kiosk.getId() + "] " + kiosk.getName()
                + "</b> ha sido actualizado.";
    }

    private HtmlFinder getHotspotFinder(final Kiosk kiosk) {
        Hotspot hotspot = null;
        String valueToShow = "";

        try {
            hotspot = ((kiosk == null)
                    ? null
                    : new HotspotWebFacade().find(kiosk.getHotspot()));
            valueToShow = ((hotspot == null)
                    ? ""
                    : new HotspotFinder().getValueToShow(hotspot));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return new HtmlFinder("hotspot", HotspotFinder.class, valueToShow, 32);
    }

}
