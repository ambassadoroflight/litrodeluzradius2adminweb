package org.unlitrodeluzcolombia.radius.gui.happyhour;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlImg;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlCheckbox;
import net.comtor.html.form.HtmlSelect;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.HappyHour;
import net.comtor.radius.element.Hotspot;
import org.unlitrodeluzcolombia.radius.gui.finder.HotspotFinder;
import org.unlitrodeluzcolombia.radius.web.facade.HappyHourWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.HotspotWebFacade;
import web.global.ComtorRadiusImages;
import web.global.LitroDeLuzImages;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class HappyHourController
        extends AbstractComtorFacadeAdministratorControllerI18n<HappyHour, Long> {

    private static final Logger LOG = Logger.getLogger(HappyHourController.class.getName());

    @Override
    public String getEntityName() {
        return "Pines Happy Hour";
    }

    @Override
    public WebLogicFacade<HappyHour, Long> getLogicFacade() {
        return new HappyHourWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, HappyHour happyHour)
            throws BusinessLogicException {
        if (happyHour != null) {
            form.addInputHidden("id", happyHour.getId());
            form.addInputHidden("login", happyHour.getLogin());

            HtmlText id = new HtmlText(happyHour.getId());
            form.addField("ID", id, null);

            HtmlCheckbox newPin = new HtmlCheckbox("newPin", "active");
            form.addField("Generar Nuevo Pin", newPin, "Si marca la casilla, se "
                    + "generará otro Pin para esta Hora Libre.");
        }

        HtmlFinder hostpot = getHotspotFinder(happyHour);
        form.addField("Hotspot", hostpot, null, true);

        HtmlSelect start_time = getHourSelect("start_time");
        form.addField("Hora de Inicio", start_time, null);

        HtmlSelect end_time = getHourSelect("end_time");
        form.addField("Hora de Finalización", end_time, null);

        getWeekdaysCheckboxes(form);
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_HAPPY_HOUR";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_HAPPY_HOUR";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_HAPPY_HOUR";
    }

    @Override
    public String getFormName() {
        return "happy_hour_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("hotspot", "Hotspot");
        headers.put("pin", "Pin");
        headers.put("start_time", "Hora de Inicio");
        headers.put("end_time", "Hora de Finalización");
        headers.put("monday", "Lun");
        headers.put("tuesday", "Mar");
        headers.put("wednesday", "Mié");
        headers.put("thursday", "Jue");
        headers.put("friday", "Vie");
        headers.put("saturday", "Sáb");
        headers.put("sunday", "Dom");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(HappyHour happyHour) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(happyHour.getId());
        row.add("[" + happyHour.getHotspot() + "]  " + happyHour.getHotspot_name());
        row.add(happyHour.getLogin());
        row.add((happyHour.getStart_time() / 60) + ":00");
        row.add((happyHour.getEnd_time() / 60) + ":00");
        row.add(getIcon(happyHour.isMonday()));
        row.add(getIcon(happyHour.isTuesday()));
        row.add(getIcon(happyHour.isWednesday()));
        row.add(getIcon(happyHour.isThursday()));
        row.add(getIcon(happyHour.isFriday()));
        row.add(getIcon(happyHour.isSaturday()));
        row.add(getIcon(happyHour.isSunday()));

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.HAPPYHOUR_CONTROLLER;
    }

    @Override
    public String getLogModule() {
        return "Pines Happy Hour";
    }

    @Override
    public String getAddFormLabel() {
        return "Nuevo Pin";
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Crear Pin Happy Hour";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar Pin Happy Hour";
    }

    @Override
    public String getConfirmDeleteMessage(HappyHour happyHour) {
        return "¿Está seguro que desea eliminar el pin happy hour <b>["
                + happyHour.getId() + "] Pin: " + happyHour.getLogin() + "</b>?";
    }

    @Override
    public String getAddedMessage(HappyHour happyHour) {
        return "El pin happy hour <b>[" + happyHour.getId() + "] Pin: "
                + happyHour.getLogin() + "</b> ha sido creado.";
    }

    @Override
    public String getDeletedMessage(HappyHour happyHour) {
        return "El pin happy hour <b>[" + happyHour.getId() + "] Pin: "
                + happyHour.getLogin() + "</b> ha sido eliminada.";
    }

    @Override
    public String getUpdatedMessage(HappyHour happyHour) {
        return "El pin happy hour <b>[" + happyHour.getId() + "] Pin: "
                + happyHour.getLogin() + "</b> ha sido actualizada.";
    }

    private HtmlFinder getHotspotFinder(final HappyHour happyHour) {
        Hotspot hotspot = null;
        String valueToShow = "";

        try {
            hotspot = ((happyHour == null)
                    ? null
                    : new HotspotWebFacade().find(happyHour.getHotspot()));
            valueToShow = ((hotspot == null)
                    ? ""
                    : new HotspotFinder().getValueToShow(hotspot));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return new HtmlFinder("hotspot", HotspotFinder.class, valueToShow, 32);
    }

    private HtmlSelect getHourSelect(String name) {
        HtmlSelect select = new HtmlSelect(name);

        for (int i = 0; i <= 23; i++) {
            select.addOption("" + (i * 60), i + ":00");
        }

        return select;
    }

    private void getWeekdaysCheckboxes(AdministrableForm form) {
        String[] keys = {
            "monday",
            "tuesday",
            "wednesday",
            "thursday",
            "friday",
            "saturday",
            "sunday"
        };
        String[] values = {
            "Lunes",
            "Martes",
            "Miércoles",
            "Jueves",
            "Viernes",
            "Sábado",
            "Domingo"
        };

        for (int i = 0; i < values.length; i++) {
            form.addField(values[i], new HtmlCheckbox(keys[i], "active"),
                    "Indique si el pin happy hour estará disponible este día.");
        }
    }

    private String getIcon(boolean ok) {
        if (ok) {
            return new HtmlImg(ComtorRadiusImages.CHECKMARK).getHtml();
        }

        return new HtmlImg(ComtorRadiusImages.CROSS).getHtml();
    }
}
