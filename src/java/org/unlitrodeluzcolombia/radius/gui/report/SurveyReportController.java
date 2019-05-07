package org.unlitrodeluzcolombia.radius.gui.report;

import java.util.LinkedList;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.report.ReportControllerJDBC;
import net.comtor.framework.report.ReportException;
import net.comtor.framework.report.ReportJDBCPreparedQuery;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.framework.request.validator.RequestBasicValidator;
import net.comtor.i18n.html.DivFormI18n;
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
        ReportJDBCPreparedQuery query = new ReportJDBCPreparedQuery("\n"
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
                + "    su.id = ? \n");

        long survey = RequestBasicValidator.getLongFromRequest(request,
                PARAM_SURVEY);

        query.addParameter(survey);

        return query;
    }

    @Override
    public AdministrableForm createFilterForm(String action, HttpServletMixedRequest request) {
        DivFormI18n form = new DivFormI18n(action, AdministrableForm.METHOD_POST, request);
        form.setTitle("Establezca rango de fechas para el reporte");

        HtmlFinder survey = new HtmlFinder(PARAM_SURVEY, SurveyFinder.class, "", 32);
        form.addField("Encuesta", survey, null, true);

        form.addBasicButtons("Aceptar", "Cancelar");

        return form;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateForm(String option, HttpServletMixedRequest request) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        long survey = RequestBasicValidator.getLongFromRequest(request,
                PARAM_SURVEY, 0);

        if (survey <= 0) {
            exceptions.add(new ObjectValidatorException(PARAM_SURVEY,
                    "Debe indicar la encuesta."));
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

}
