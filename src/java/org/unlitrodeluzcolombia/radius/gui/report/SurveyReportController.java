package org.unlitrodeluzcolombia.radius.gui.report;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.ajax.HtmlJavaScript;
import net.comtor.advanced.html.HtmlCalendarJQuery;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.report.ReportControllerJDBC;
import net.comtor.framework.report.ReportException;
import net.comtor.framework.report.ReportJDBCPreparedQuery;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.framework.request.validator.RequestBasicValidator;
import net.comtor.html.form.HtmlButton;
import net.comtor.html.form.HtmlSelect;
import net.comtor.i18n.html.DivFormI18n;
import net.comtor.util.StringUtil;
import org.unlitrodeluzcolombia.radius.gui.FormUtils;
import org.unlitrodeluzcolombia.radius.gui.finder.SurveyFinder;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 24, 2019
 */
public class SurveyReportController extends ReportControllerJDBC {

    private static final String PARAM_SURVEY = "survey";

    public SurveyReportController() throws ReportException {
        super();
    }

    @Override
    protected ComtorJDBCDao createComtorJDBCDAO() throws ComtorDaoException {
        return new ApplicationDAO();
    }

    @Override
    protected ReportJDBCPreparedQuery createPreparedQuery(HttpServletMixedRequest request) {
        long sponsor = RequestBasicValidator.getLongFromRequest(request, "sponsor", 0);
        String country = RequestBasicValidator.getStringFromRequest(request, "country");
        long zone = RequestBasicValidator.getLongFromRequest(request, "zone", 0);
        long survey = RequestBasicValidator.getLongFromRequest(request, "survey", 0);
        java.sql.Date start_date = RequestBasicValidator.getDateFromRequest(request, "start_date");
        java.sql.Date end_date = RequestBasicValidator.getDateFromRequest(request, "end_date");

        String sql = "\n"
                + " SELECT \n"
                + "     su.id										AS 'Encuesta', \n"
                + "     su.description								AS 'Descripción Encuesta', \n"
                + "     sp.name                                     AS 'Patrocinador', \n"
                + "     ca.description								AS 'Descripción Campaña', \n"
                + "     ca.start_date								AS 'Fecha Inicio Campaña', \n"
                + "     ca.end_date                                 AS 'Fecha Final Campaña', \n"
                + "     co.name                                     AS 'País', \n"
                + "     z.name										AS 'Zona', \n"
                + "     h.called_station_id                         AS 'Called Station ID Hotspot', \n"
                + "     h.name										AS 'Nombre Hotspot', \n"
                + "     h.what3words								AS 'What3Words', \n"
                + "     CONCAT(h.latitude, ', ', h.longitude)		AS 'Coordenadas', \n"
                + "     qu.question                          		AS 'Pregunta', \n"
                + "     REPLACE(an.response, '#_#', ', ')			AS 'Respuesta', \n"
                + "     an.answer_date                       		AS 'Fecha Respuesta' \n"
                + " FROM \n"
                + "     survey su \n"
                + " JOIN question qu            ON (qu.survey = su.id) \n"
                + " LEFT JOIN answer an         ON (an.question = qu.id) \n"
                + " JOIN campaign ca            ON (ca.id = su.campaign) \n"
                + " JOIN hotspot h              ON (h.id = an.hotspot) \n"
                + " JOIN zone z                 ON (z.id = h.zone) \n"
                + " JOIN country co             ON (co.iso = z.country) \n"
                + " JOIN sponsor sp             ON (sp.id = ca.sponsor) \n"
                + " WHERE \n"
                + "    1 = 1 \n";
        List<Object> params = new ArrayList<>();

        if (sponsor > 0) {
            sql += " AND sp.id = ? \n";
            params.add(sponsor);
        }

        if (StringUtil.isValid(country)) {
            sql += " AND co.iso = ? \n";
            params.add(country);
        }

        if (zone > 0) {
            sql += " AND z.id = ? \n";
            params.add(zone);
        }

        if (survey > 0) {
            sql += " AND su.id = ? \n";
            params.add(survey);
        }

        if (start_date != null) {
            sql += " AND ca.start_date >= ? \n";
            params.add(start_date);
        }

        if (end_date != null) {
            sql += " AND ca.end_date = ? \n";
            params.add(end_date);
        }

        ReportJDBCPreparedQuery query = new ReportJDBCPreparedQuery(sql);

        for (Object param : params) {
            query.addParameter(param);
        }

        return query;
    }

    @Override
    public AdministrableForm createFilterForm(String action, HttpServletMixedRequest request) {
        DivFormI18n form = new DivFormI18n(action, AdministrableForm.METHOD_POST, request);
        form.setTitle("Establezca rango de fechas para el reporte");

        HtmlSelect sponsor = FormUtils.getSponsorSelect();
        form.addField("Patrocinador", sponsor, null);

        HtmlSelect country = FormUtils.getCountrySelect();
        form.addField("Pais", country, null);

        HtmlSelect zone = new HtmlSelect("zone");
        zone.addOption("0", "Todas");
        form.addField("Zona", zone, null);

        HtmlFinder survey = new HtmlFinder(PARAM_SURVEY, SurveyFinder.class, "", 32);
        form.addField("Encuesta", survey, null, true);

        HtmlCalendarJQuery start_date = new HtmlCalendarJQuery("start_date");
        form.addField("Fecha Inicio Campaña", start_date, null);

        HtmlCalendarJQuery end_date = new HtmlCalendarJQuery("end_date");
        form.addField("Fecha Final Campaña", end_date, null);

        HtmlButton ok = new HtmlButton(HtmlButton.SUBMIT_BUTTON,
                "ok", "Aceptar");

        HtmlButton clear = new HtmlButton(HtmlButton.SCRIPT_BUTTON,
                "survey_report_clear_filter_button", "Limpiar", "clearSearch();");

        HtmlButton cancel = new HtmlButton(HtmlButton.SUBMIT_BUTTON,
                "cancel", "Cancelar");

        form.addButtons(ok, clear, cancel);

        form.addRowInOneCell(getJS());

        return form;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateForm(String option, HttpServletMixedRequest request) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        long sponsor = RequestBasicValidator.getLongFromRequest(request, "sponsor", 0);
        String country = RequestBasicValidator.getStringFromRequest(request, "country");
        long survey = RequestBasicValidator.getLongFromRequest(request, "survey", 0);
        java.sql.Date start_date = RequestBasicValidator.getDateFromRequest(request, "start_date");
        java.sql.Date end_date = RequestBasicValidator.getDateFromRequest(request, "end_date");

        if ((sponsor == 0)
                && !StringUtil.isValid(country)
                && (survey == 0)
                && (start_date == null)
                && (end_date == null)) {
            String message = "Debe indicar mínimo un parámetro de búsqueda";

            exceptions.add(new ObjectValidatorException("sponsor", message));
            exceptions.add(new ObjectValidatorException("country", message));
            exceptions.add(new ObjectValidatorException("survey", message));
            exceptions.add(new ObjectValidatorException("zone", message));
            exceptions.add(new ObjectValidatorException("start_date", message));
            exceptions.add(new ObjectValidatorException("end_date", message));
        }

        if (((start_date != null) && (end_date != null))
                && (start_date.after(end_date))) {
            exceptions.add(new ObjectValidatorException("start_date",
                    "Fecha inicial debe ser anterior a la fecha final."));
            exceptions.add(new ObjectValidatorException("end_date",
                    "Fecha inicial debe ser anterior a la fecha final."));
        }

        return exceptions;
    }

    @Override
    public String getReportName(HttpServletMixedRequest request) {
        return "Resultados de Encuestas";
    }

    @Override
    public String getReportDescription(HttpServletMixedRequest request) {
        return "Resultados de Encuestas";
    }

    @Override
    public String getReportFileName(HttpServletMixedRequest request) {
        long survey = RequestBasicValidator.getLongFromRequest(request, PARAM_SURVEY);

        return "resultados_encuesta_" + survey + ".xlsx";
    }

    @Override
    public boolean isPaginated() {
        return true;
    }

    @Override
    public String getReturnLabel() {
        return "Regresar";
    }

    @Override
    public String[] getPrivileges() {
        return new String[]{"VIEW_SURVEY_REPORT"};
    }

    private HtmlJavaScript getJS() {
        String js = "\n"
                + " $(document).ready(function() {\n"
                + "     var $country = $(\"select#country\"); \n"
                + "     var $zone = $(\"select#zone\"); \n\n"
                + ""
                + "     $country.change(function() { \n"
                + "         var iso = $(this).val(); \n"
                + "\n"
                + "    var settings = {\n"
                + "        \"url\": \"/litrodeluz/radius/webservices/hotspot_services/map/get_zones?country=\" + iso, \n"
                + "        \"method\": \"GET\",\n"
                + "    }\n\n"
                + ""
                + "     $zone.empty();\n"
                + "     $zone.append('<option value=\"\">Todas</option>');\n\n"
                + ""
                + "     $.ajax(settings) \n"
                + "         .done(function (response) { \n"
                + "             $.each(response, function(i, item) {\n"
                + "                 $zone.append('<option value=\"' + item.id + '\">' + item.name + '</option>');\n"
                + "             });\n"
                + "         }) \n"
                + "         .fail(function(jqXHR, textStatus){ \n"
                + "             console.error(textStatus);\n"
                + "         });\n"
                + "     });\n"
                + " }); \n";

        return new HtmlJavaScript(js);
    }

}
