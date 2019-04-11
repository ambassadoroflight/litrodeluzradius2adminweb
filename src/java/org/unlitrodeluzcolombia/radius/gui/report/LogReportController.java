package org.unlitrodeluzcolombia.radius.gui.report;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlFinder;
import web.connection.ApplicationDAO;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.report.ReportControllerJDBC;
import net.comtor.framework.report.ReportException;
import net.comtor.framework.report.ReportJDBCPreparedQuery;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.framework.request.validator.RequestBasicValidator;
import net.comtor.i18n.html.DivFormI18n;
import net.comtor.i18n.html.HtmlCalendarJQueryI18n;
import net.comtor.radius.element.Hotspot;
import net.comtor.radius.facade.HotspotDAOFacade;
import org.unlitrodeluzcolombia.radius.gui.finder.HotspotFinder;
import net.comtor.util.DateUtil;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class LogReportController extends ReportControllerJDBC {

    private static final Logger LOG = Logger.getLogger(LogReportController.class.getName());

    private static final int MAX_DAYS = 30;

    private static final String PARAM_HOTSPOT = "hotspot";
    private static final String PARAM_START_DATE = "start_date";
    private static final String PARAM_END_DATE = "end_date";

    public LogReportController() throws ReportException {
        super();
    }

    @Override
    protected ComtorJDBCDao createComtorJDBCDAO() throws ComtorDaoException {
        return new ApplicationDAO();
    }

    @Override
    public String[] getPrivileges() {
        return new String[]{"VIEW_HISTORY_REPORT"};
    }

    @Override
    public AdministrableForm createFilterForm(String action, HttpServletMixedRequest request) {
        DivFormI18n form = new DivFormI18n(action, AdministrableForm.METHOD_POST, request);
        form.setTitle("Establezca rango de fechas para el reporte");

        HtmlFinder hotspot = new HtmlFinder(PARAM_HOTSPOT, HotspotFinder.class, "", 32);
        form.addField("Hotspot", hotspot, null, true);

        HtmlCalendarJQueryI18n start_date = new HtmlCalendarJQueryI18n(PARAM_START_DATE,
                request.getOriginalRequest());
        form.addField("Fecha Inicial", start_date, null, true);

        HtmlCalendarJQueryI18n end_date = new HtmlCalendarJQueryI18n(PARAM_END_DATE,
                request.getOriginalRequest());
        form.addField("Fecha Final", end_date, null, true);

        form.addBasicButtons("Aceptar", "Cancelar");

        return form;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateForm(String option,
            HttpServletMixedRequest request) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        long hotspot = RequestBasicValidator.getLongFromRequest(request,
                PARAM_HOTSPOT, 0);
        java.sql.Date start_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_START_DATE);
        java.sql.Date end_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_END_DATE);

        if (hotspot <= 0) {
            exceptions.add(new ObjectValidatorException(PARAM_HOTSPOT,
                    "Debe indicar el Hotspot."));
        }

        if (start_date == null) {
            exceptions.add(new ObjectValidatorException(PARAM_START_DATE,
                    "Debe ingresar una fecha inicial."));
        }

        if (end_date == null) {
            exceptions.add(new ObjectValidatorException(PARAM_END_DATE,
                    "Debe ingresar una fecha final."));
        }

        if (start_date != null && end_date != null) {
            if (start_date.after(end_date)) {
                exceptions.add(new ObjectValidatorException(PARAM_START_DATE,
                        "Fecha inicial debe ser anterior a la fecha final."));
                exceptions.add(new ObjectValidatorException(PARAM_END_DATE,
                        "Fecha inicial debe ser anterior a la fecha final."));
            }

            if (!DateUtil.add(start_date, Calendar.DATE, MAX_DAYS).after(end_date)) {
                exceptions.add(new ObjectValidatorException(PARAM_END_DATE,
                        "Debe ingresar un margen de fechas menor a " + MAX_DAYS
                        + " días"));
            }
        }

        return exceptions;
    }

    @Override
    public String getReportName(HttpServletMixedRequest request) {
        return "Reporte Histórico";
    }

    @Override
    public String getReportDescription(HttpServletMixedRequest request) {
        return "Reporte Histórico";
    }

    @Override
    public String getReportFileName(HttpServletMixedRequest request) {
        String start_date = RequestBasicValidator.getStringFromRequest(request,
                PARAM_START_DATE);
        String end_date = RequestBasicValidator.getStringFromRequest(request,
                PARAM_END_DATE);

        return "ReporteHistorico_" + start_date + "_" + end_date + ".xlsx";
    }

    @Override
    public boolean isPaginated() {
        return true;
    }

    @Override
    protected ReportJDBCPreparedQuery createPreparedQuery(HttpServletMixedRequest request) {
        ReportJDBCPreparedQuery query = new ReportJDBCPreparedQuery("\n"
                + " SELECT \n"
                + "     radius_log.called_mac_address       AS 'HOTSPOT (IPK)', \n"
                + "     radius_log.user                     AS 'PIN', \n"
                + "     radius_log.message                  AS 'MENSAJE', \n"
                + "     radius_log.date                     AS 'FECHA', \n"
                + "     radius_log.hour                     AS 'Hora', \n"
                + "     radius_log.calling_mac_address      AS 'MAC' \n"
                + " FROM \n"
                + "     radius_log \n"
                + " WHERE \n"
                + "     radius_log.called_mac_address = ? \n"
                + " AND (radius_log.date BETWEEN ? AND ?) \n"
                + " ORDER BY \n"
                + "     radius_log.id DESC \n");

        final long hotspot_id = RequestBasicValidator.getLongFromRequest(request,
                PARAM_HOTSPOT);
        final java.sql.Date start_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_START_DATE);
        final java.sql.Date end_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_END_DATE);

        try {
            Hotspot hotspot = new HotspotDAOFacade().find(hotspot_id);
            query.addParameter(hotspot.getCalled_station_id());
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        query.addParameter(start_date);
        query.addParameter(end_date);

        return query;
    }

}
