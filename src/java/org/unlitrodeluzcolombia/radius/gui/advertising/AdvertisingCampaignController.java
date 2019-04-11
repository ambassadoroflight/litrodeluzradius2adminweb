package org.unlitrodeluzcolombia.radius.gui.advertising;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlCalendarJQuery;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.global.ComtorGlobal;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.framework.util.FormUtil;
import net.comtor.html.HtmlImg;
import net.comtor.html.HtmlLink;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlCheckbox;
import net.comtor.html.form.HtmlInputFile;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.AdvertisingCampaign;
import net.comtor.radius.element.Sponsor;
import org.unlitrodeluzcolombia.radius.element.Survey;
import org.unlitrodeluzcolombia.radius.gui.finder.SponsorFinder;
import org.unlitrodeluzcolombia.radius.gui.finder.SurveyFinder;
import org.unlitrodeluzcolombia.radius.web.facade.AdvertisingCampaignWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.SponsorWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.SurveyWebFacade;
import web.Images;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 04, 2019
 */
public class AdvertisingCampaignController
        extends AbstractComtorFacadeAdministratorControllerI18n<AdvertisingCampaign, Long> {

    private static final Logger LOG = Logger.getLogger(AdvertisingCampaignController.class.getName());

    @Override
    public String getEntityName() {
        return "Campaña Publicitaria";
    }

    @Override
    public WebLogicFacade<AdvertisingCampaign, Long> getLogicFacade() {
        return new AdvertisingCampaignWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, AdvertisingCampaign campaign)
            throws BusinessLogicException {
        form.setEnctype(AdministrableForm.ENCTYPE_MULTIPART_FORM_DATA);

        if (campaign == null) {
            HtmlFinder sponsor = getSponsorFinder(campaign);
            form.addField("Patrocinador", sponsor, null, true);
        } else {
            long adId = campaign.getId();
            long sponsorId = campaign.getSponsor();

            form.addInputHidden("id", adId);
            form.addInputHidden("sponsor", sponsorId);
            form.addInputHidden("banner_1", campaign.getBanner_1());
            form.addInputHidden("banner_2", campaign.getBanner_2());

            HtmlText field = new HtmlText(adId);
            form.addField("ID", field, null);

            field = new HtmlText("[" + sponsorId + "] " + campaign.getSponsor_name());
            form.addField("Patrocinador", field, null);
        }

        HtmlInputText description = new HtmlInputText("description", 32, 64);
        form.addField("Descripción", description, "Puede ingresar una breve "
                + "descripción para identificar a la campaña publicitaria (p.e. "
                + "el nombre o eslógan).");

        if (campaign != null) {
            HtmlImg img = new HtmlImg("ImagesServlet?id=" + campaign.getId() + "&banner=1");
            form.addField("Banner #1 Actual", img, null);
        }

        HtmlInputFile banner_1 = new HtmlInputFile("banner_1_file");
        form.addField("Banner #1", banner_1, "Debe subir una imagen de 2301x1500 pixeles", true);

        if (campaign != null) {
            HtmlImg img = new HtmlImg("ImagesServlet?id=" + campaign.getId() + "&banner=2");
            form.addField("Banner #2 Actual", img, null);
        }

        HtmlInputFile banner_2 = new HtmlInputFile("banner_2_file");
        form.addField("Banner #2", banner_2, "Debe subir una imagen de 991x1500 pixeles", true);

        HtmlFinder survey = getSurveyFinder(campaign);
        form.addField("Encuesta", survey, null, true);

        HtmlCalendarJQuery start_date = FormUtil.getFilterDatepicker("start_date",
                FormUtil.DatepickerRange.CURRENT_MONTH);
        form.addField("Desde", start_date, "Indique desde qué fecha se estará "
                + "disponible esta campaña.", true);

        HtmlCalendarJQuery end_date = new HtmlCalendarJQuery("end_date");
        form.addField("Hasta", end_date, "Indique hasta qué fecha se "
                + "estará disponible esta campaña.");

        HtmlCheckbox active = new HtmlCheckbox("active", "active");
        active.checked((campaign == null) ? true : campaign.isActive());
        form.addField("Activo", active, "Indique si esta campaña se encuentra "
                + "activa para visualización en los hotspot.");
    }

    @Override
    public void initFormView(AdministrableForm form, AdvertisingCampaign campaign) {
        HtmlText field = new HtmlText(campaign.getId());
        form.addField("ID", field, null);

        field = new HtmlText("[" + campaign.getSponsor() + "] " + campaign.getSponsor_name());
        form.addField("Patrocinador", field, null);

        field = new HtmlText(campaign.getDescription());
        form.addField("Descripción", field, null);

        HtmlImg img = new HtmlImg("ImagesServlet?id=" + campaign.getId() + "&banner=1");
        form.addField("Banner #1", img, null);

        img = new HtmlImg("ImagesServlet?id=" + campaign.getId() + "&banner=2");
        form.addField("Banner #2", img, null);

        long survey = campaign.getSurvey();

        if (survey == 0) {
            field = new HtmlText("Ninguna");
            form.addField("Encuesta", img, null);
        } else {
            HtmlLink link = new HtmlLink("[" + survey + "] Ver encuesta",
                    ComtorGlobal.getLink(SurveyAdmin.class) + "&action=viewform&key="
                    + survey);
            form.addField("Encuesta", link, null);
        }

        field = new HtmlText(campaign.getStart_date());
        form.addField("Desde", field, null);

        field = new HtmlText(campaign.getEnd_date());
        form.addField("Hasta", field, null);

        field = new HtmlText(campaign.getStatus());
        form.addField("Estado", field, null);
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_SPONSOR";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_SPONSOR";
    }

    @Override
    public String getViewPrivilege() {
        return "VIEW_SPONSOR";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_SPONSOR";
    }

    @Override
    public String getFormName() {
        return "advertising_campaign_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("sponsor", "Patrocinador");
        headers.put("description", "Descripción");
        headers.put("start_date", "Desde");
        headers.put("end_date", "Hasta");
        headers.put("active", "Estado");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(AdvertisingCampaign sponsor) {
        java.sql.Date endDate = sponsor.getEnd_date();

        LinkedList<Object> row = new LinkedList<>();
        row.add(sponsor.getId());
        row.add(sponsor.getSponsor_name());
        row.add(sponsor.getDescription());
        row.add(sponsor.getStart_date());
        row.add((endDate == null) ? "" : endDate);
        row.add(sponsor.getStatus());

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return Images.HOTSPOTS_WHITE_32;
    }

    @Override
    public String getLogModule() {
        return "Campañas Publicitarias";
    }

    @Override
    public String getAddFormLabel() {
        return "Nueva Campaña";
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Crear Campaña";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar Campaña";
    }

    @Override
    public String getConfirmDeleteMessage(AdvertisingCampaign campaign) {
        return "¿Está seguro que desea eliminar la campaña publicitaria <b>["
                + campaign.getId() + "] " + campaign.getDescription() + "</b>?";
    }

    @Override
    public String getAddedMessage(AdvertisingCampaign campaign) {
        return "La campaña publicitaria <b>[" + campaign.getId() + "] "
                + campaign.getDescription() + "</b> ha sido creada.";
    }

    @Override
    public String getDeletedMessage(AdvertisingCampaign campaign) {
        return "La campaña publicitaria <b>[" + campaign.getId() + "] "
                + campaign.getDescription() + "</b> ha sido eliminada.";
    }

    @Override
    public String getUpdatedMessage(AdvertisingCampaign campaign) {
        return "La campaña publicitaria <b>[" + campaign.getId() + "] "
                + campaign.getDescription() + "</b> ha sido actualizada.";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este módulo.";
    }

    private HtmlFinder getSponsorFinder(final AdvertisingCampaign campaign) {
        Sponsor sponsor = null;
        String valueToShow = "";

        try {
            sponsor = ((campaign == null)
                    ? null
                    : new SponsorWebFacade().find(campaign.getSponsor()));
            valueToShow = ((sponsor == null)
                    ? ""
                    : new SponsorFinder().getValueToShow(sponsor));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return new HtmlFinder("sponsor", SponsorFinder.class, valueToShow, 32);
    }

    private HtmlFinder getSurveyFinder(final AdvertisingCampaign campaign) {
        Survey survey = null;
        String valueToShow = "";

        try {
            survey = ((campaign == null)
                    ? null
                    : new SurveyWebFacade().find(campaign.getSurvey()));
            valueToShow = ((survey == null)
                    ? ""
                    : new SurveyFinder().getValueToShow(survey));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return new HtmlFinder("survey", SurveyFinder.class, valueToShow, 32);
    }

}
