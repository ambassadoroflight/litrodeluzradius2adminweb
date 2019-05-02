package org.unlitrodeluzcolombia.radius.gui.rate;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.form.HtmlInputNumber;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlSpan;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.PrepaidRate;
import org.unlitrodeluzcolombia.radius.web.facade.PrepaidRateWebFacade;
import web.global.LitroDeLuzImages;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class PrepaidRateController
        extends AbstractComtorFacadeAdministratorControllerI18n<PrepaidRate, Long> {

    @Override
    public String getEntityName() {
        return "Tarifa";
    }

    @Override
    public WebLogicFacade<PrepaidRate, Long> getLogicFacade() {
        return new PrepaidRateWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, PrepaidRate rate)
            throws BusinessLogicException {
        if (rate != null) {
            final long rate_id = rate.getId();

            form.addInputHidden("id", rate_id);

            HtmlText id = new HtmlText(rate_id);
            form.addField("ID", id, null);
        }

        HtmlInputText description = new HtmlInputText("description", 32, 64);
        form.addField("Descripción", description, null, true);

        HtmlInputNumber duration_in_seconds = new HtmlInputNumber("duration_in_seconds",
                8, 10, HtmlInputNumber.Type.INTEGER);
        duration_in_seconds.addAttribute("onKeyUp", "showPinTime(this)");

        form.addField("Duración (En segundos)", duration_in_seconds, null, true);

        HtmlSpan duration = new HtmlSpan("duration", "");
        duration.addAttribute("style", "width: 100%; font-weight: bolder;");
        form.addField("", duration, null);
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_PREPAID_RATE";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_PREPAID_RATE";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_PREPAID_RATE";
    }

    @Override
    public String getFormName() {
        return "prepaid_rate_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("description", "Descripcion");
        headers.put("duration_in_seconds", "Duración");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(PrepaidRate rate) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(rate.getId());
        row.add(rate.getDescription());
        row.add(calculateTotalTime(rate.getDuration_in_seconds()));

        return row;
    }

    @Override
    public String getLogModule() {
        return "Tarifas";
    }

    @Override
    public String getAddFormLabel() {
        return "Nueva Tarifa";
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Crear Tarifa";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar Tarifa";
    }

    @Override
    public String getConfirmDeleteMessage(PrepaidRate rate) {
        return "¿Está seguro que desea eliminar la tarifa <b>[" + rate.getId()
                + "] " + rate.getDescription() + "</b>?";
    }

    @Override
    public String getAddedMessage(PrepaidRate rate) {
        return "La tarifa <b>[" + rate.getId() + "] " + rate.getDescription()
                + "</b> ha sido creada.";
    }

    @Override
    public String getDeletedMessage(PrepaidRate rate) {
        return "La tarifa <b>[" + rate.getId() + "] " + rate.getDescription()
                + "</b> ha sido eliminada.";
    }

    @Override
    public String getUpdatedMessage(PrepaidRate rate) {
        return "La tarifa <b>[" + rate.getId() + "] " + rate.getDescription()
                + "</b> ha sido actualizada.";
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.RATE_CONTROLLER;
    }

    private String calculateTotalTime(final long diffInSeconds) {
        String resp = getPlural(diffInSeconds, "segundo") + " (";

        if ((diffInSeconds >= 60) && (diffInSeconds < 3600)) {
            int minutes = (int) (diffInSeconds / 60);
            int seconds = (int) (diffInSeconds % 60);

            return resp += (getPlural(minutes, "minuto") + ", "
                    + getPlural(seconds, "segundo") + ")");
        } else if ((diffInSeconds >= 3600) && (diffInSeconds < 86400)) {
            int hours = (int) (diffInSeconds / 3600);
            int minutes = (int) ((diffInSeconds % 3600) / 60);

            return resp += (getPlural(hours, "hora")) + ", "
                    + (getPlural(minutes, "minuto") + ")");
        } else {
            int days = (int) (diffInSeconds / 86400);
            int hours = (int) ((diffInSeconds % 86400) / 60);

            return resp += (getPlural(days, "día") + ", "
                    + (getPlural(hours, "hora")) + ")");
        }

    }

    private String getPlural(long qty, String singular) {
        return qty + " " + singular + ((qty == 1) ? "" : "s");
    }

}
