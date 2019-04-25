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
                + "    qu.question                          AS 'Pregunta', \n"
                + "    REPLACE(an.response, '#_#', ', ')    AS 'Respuesta', \n"
                + "    an.answer_date                       AS 'Fecha' \n"
                + "FROM \n"
                + "    survey su \n"
                + "JOIN question qu ON (qu.survey = su.id) \n"
                + "LEFT JOIN answer an ON (an.question = qu.id) \n"
                + "WHERE \n"
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

}
