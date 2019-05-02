package org.unlitrodeluzcolombia.radius.gui.advertising;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.BigTable;
import net.comtor.advanced.html.HtmlCalendarJQuery;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.html.administrable.ComtorAdministrableHtmlException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.framework.util.FormUtil;
import net.comtor.html.HtmlImg;
import net.comtor.html.HtmlLi;
import net.comtor.html.HtmlText;
import net.comtor.html.HtmlUl;
import net.comtor.html.form.HtmlCheckbox;
import net.comtor.html.form.HtmlInputFile;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.Campaign;
import net.comtor.radius.element.Sponsor;
import net.comtor.radius.element.Zone;
import net.comtor.radius.facade.CampaignXZoneDAOFacade;
import org.unlitrodeluzcolombia.radius.gui.finder.SponsorFinder;
import org.unlitrodeluzcolombia.radius.web.facade.CampaignWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.SponsorWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.ZoneWebFacade;
import web.global.LitroDeLuzImages;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 16, 2019
 */
public class CampaignController
        extends AbstractComtorFacadeAdministratorControllerI18n<Campaign, Long> {

    private static final Logger LOG = Logger.getLogger(CampaignController.class.getName());

    @Override
    public String getEntityName() {
        return "Campaña Publicitaria";
    }

    @Override
    public WebLogicFacade<Campaign, Long> getLogicFacade() {
        return new CampaignWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, Campaign campaign)
            throws BusinessLogicException {
        form.setEnctype(AdministrableForm.ENCTYPE_MULTIPART_FORM_DATA);

        if (campaign == null) {
            HtmlFinder sponsor = getSponsorFinder(campaign);
            form.addField("Patrocinador", sponsor, null, true);
        } else {
            form.addInputHidden("id", campaign.getId());
            form.addInputHidden("banner_1", campaign.getBanner_1());
            form.addInputHidden("banner_2", campaign.getBanner_2());
            form.addInputHidden("sponsor", campaign.getSponsor());

            HtmlText sponsor = new HtmlText(campaign.getSponsor_name());
            form.addField("Patrocinador", sponsor, null);
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

        HtmlCalendarJQuery start_date = FormUtil.getFilterDatepicker("start_date",
                FormUtil.DatepickerRange.CURRENT_MONTH);
        form.addField("Desde", start_date, "Indique desde qué fecha se estará "
                + "disponible esta campaña.", true);

        HtmlCalendarJQuery end_date = new HtmlCalendarJQuery("end_date");
        form.addField("Hasta", end_date, "Indique hasta qué fecha se "
                + "estará disponible esta campaña.");

        try {
            getZonesCheckList(form, campaign);
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initFormView(AdministrableForm form, Campaign campaign) {
        HtmlText field;

        field = new HtmlText("[" + campaign.getSponsor() + "] " + campaign.getSponsor_name());
        form.addField("Patrocinador", field, null);

        field = new HtmlText(campaign.getDescription());
        form.addField("Descripción", field, null);

        HtmlImg img = new HtmlImg("ImagesServlet?id=" + campaign.getId() + "&banner=1");
        form.addField("Banner #1", img, null);

        img = new HtmlImg("ImagesServlet?id=" + campaign.getId() + "&banner=2");
        form.addField("Banner #2", img, null);

        field = new HtmlText(campaign.getStart_date());
        form.addField("Desde", field, null);

        field = new HtmlText(campaign.getEnd_date());
        form.addField("Hasta", field, null);

        try {
            List<Long> zonesIds = new CampaignXZoneDAOFacade().findAllByCampaign(campaign.getId());

            if (!zonesIds.isEmpty()) {
                ZoneWebFacade zoneFacade = new ZoneWebFacade();
                HtmlUl list = new HtmlUl();
                Zone zone;
                HtmlLi item;

                for (Long id : zonesIds) {
                    zone = zoneFacade.find(id);

                    item = new HtmlLi();
                    item.addElement(zone.getName());

                    list.addElement(item);
                }

                form.addField("Zonas", list, "Estas son las zonas donde está presente esta campaña.");
            }

        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (BusinessLogicException ex) {
            Logger.getLogger(CampaignController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        return "campaign_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("sponsor", "Patrocinador");
        headers.put("description", "Descripción");
        headers.put("start_date", "Desde");
        headers.put("end_date", "Hasta");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Campaign sponsor) {
        java.sql.Date endDate = sponsor.getEnd_date();

        LinkedList<Object> row = new LinkedList<>();
        row.add(sponsor.getId());
        row.add(sponsor.getSponsor_name());
        row.add(sponsor.getDescription());
        row.add(sponsor.getStart_date());
        row.add((endDate == null) ? "" : endDate);

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.ADVERTISING_CONTROLLER;
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
    public String getConfirmDeleteMessage(Campaign campaign) {
        return "¿Está seguro que desea eliminar la campaña publicitaria <b>["
                + campaign.getId() + "] " + campaign.getDescription() + "</b>?";
    }

    @Override
    public String getAddedMessage(Campaign campaign) {
        return "La campaña publicitaria <b>[" + campaign.getId() + "] "
                + campaign.getDescription() + "</b> ha sido creada.";
    }

    @Override
    public String getDeletedMessage(Campaign campaign) {
        return "La campaña publicitaria <b>[" + campaign.getId() + "] "
                + campaign.getDescription() + "</b> ha sido eliminada.";
    }

    @Override
    public String getUpdatedMessage(Campaign campaign) {
        return "La campaña publicitaria <b>[" + campaign.getId() + "] "
                + campaign.getDescription() + "</b> ha sido actualizada.";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este módulo.";
    }

    @Override
    public Campaign getObjectFromRequest(HttpServletMixedRequest request)
            throws ComtorAdministrableHtmlException {
        Campaign campaign = super.getObjectFromRequest(request);
        campaign.setZones(getZones(request));

        return campaign;
    }

    private LinkedList<String> getZones(HttpServletMixedRequest request) {
        String[] zones = request.getMparser().getParameterValues("zones");
        LinkedList<String> list = new LinkedList<>();

        if (zones != null) {
            list.addAll(Arrays.asList(zones));
        }

        return list;
    }

    private HtmlFinder getSponsorFinder(final Campaign campaign) {
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

    private void getZonesCheckList(AdministrableForm form, Campaign campaign)
            throws ComtorDaoException {
        BigTable table = new BigTable("big_table", "zone_table");
        table.setHeaders("Seleccione a qué zonas cubrirá esta campaña");
        table.setHeaderColspan(3);

        try {
            LinkedList<Zone> allZones = new ZoneWebFacade().findAll();

            if (!allZones.isEmpty()) {
                List<Long> zonesByCampaign = new LinkedList();

                if (campaign != null) {
                    zonesByCampaign
                            = new CampaignXZoneDAOFacade().findAllByCampaign(campaign.getId());
                }

                int col = 0;

                LinkedList<Object> row = new LinkedList<>();
                HtmlCheckbox checkbok;

                for (Zone zone : allZones) {
                    checkbok = new HtmlCheckbox("zones", "" + zone.getId(),
                            zone.getName());

                    if (campaign != null) {
                        if (!zonesByCampaign.isEmpty()) {
                            for (Long zone1 : zonesByCampaign) {
                                if (zone1 == zone.getId()) {
                                    checkbok.checked(true);

                                    break;
                                }
                            }
                        }
                    }

                    row.add(checkbok.getHtml());
                    col++;

                    if (col >= 3) {
                        table.addRow(new LinkedList<>(row));
                        row.clear();
                        col = 0;
                    }

                }

                table.addRow(row);
            }
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        form.addRowInOneCell(table);
    }

}
