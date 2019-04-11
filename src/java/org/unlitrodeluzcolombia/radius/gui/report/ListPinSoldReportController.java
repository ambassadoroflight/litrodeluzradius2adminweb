package org.unlitrodeluzcolombia.radius.gui.report;

import java.util.Calendar;
import java.util.LinkedList;
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
import org.unlitrodeluzcolombia.radius.gui.finder.SellerFinder;
import net.comtor.util.DateUtil;
import net.comtor.util.StringUtil;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 28, 2018
 */
public class ListPinSoldReportController extends ReportControllerJDBC {

    private static final int MAX_DAYS = 30;

    private static final String PARAM_SELLER = "seller";
    private static final String PARAM_START_DATE = "start_date";
    private static final String PARAM_END_DATE = "end_date";

    public ListPinSoldReportController() throws ReportException {
        super();
    }

    @Override
    protected ComtorJDBCDao createComtorJDBCDAO() throws ComtorDaoException {
        return new ApplicationDAO();
    }

    @Override
    public String[] getPrivileges() {
        return new String[]{"VIEW_SOLD_PIN_X_SELLER_REPORT"};
    }

    @Override
    public AdministrableForm createFilterForm(String action, HttpServletMixedRequest request) {
        AdministrableForm form = new DivFormI18n(action, AdministrableForm.METHOD_POST, request);
        form.setTitle("Establezca rango de fechas para el reporte");

        HtmlFinder seller = new HtmlFinder(PARAM_SELLER, SellerFinder.class, "", 32);
        form.addField("Vendedor", seller, null, true);

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

        String seller = RequestBasicValidator.getStringFromRequest(request,
                PARAM_SELLER, "");
        java.sql.Date start_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_START_DATE);
        java.sql.Date end_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_END_DATE);

        if (!StringUtil.isValid(seller)) {
            exceptions.add(new ObjectValidatorException(PARAM_SELLER,
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
        return "Pines Vendidos por Vendedor";
    }

    @Override
    public String getReportDescription(HttpServletMixedRequest request) {
        return "Pines Vendidos por Vendedor";
    }

    @Override
    public String getReportFileName(HttpServletMixedRequest request) {
        String seller = RequestBasicValidator.getStringFromRequest(request,
                PARAM_SELLER, "");
        String start_date = RequestBasicValidator.getStringFromRequest(request,
                PARAM_START_DATE);
        String end_date = RequestBasicValidator.getStringFromRequest(request,
                PARAM_END_DATE);

        return "PinesVendidos_" + seller + "_" + start_date + "_" + end_date + ".xlsx";
    }

    @Override
    public boolean isPaginated() {
        return true;
    }

    @Override
    protected ReportJDBCPreparedQuery createPreparedQuery(HttpServletMixedRequest request) {
        ReportJDBCPreparedQuery query = new ReportJDBCPreparedQuery("\n"
                + " SELECT \n"
                + "     prepaid_customer.login                          AS 'PIN', \n"
                + "     (prepaid_customer.purchased_time DIV 3600)      AS 'TIEMPO (HORAS)', \n"
                + "     prepaid_customer.creation_date                  AS 'FECHA DE CREACIÓN', \n"
                + "     prepaid_customer.end_session_date               AS 'FECHA DE FINALIZACIÓN', \n"
                + "     prepaid_customer.attr_1                         AS 'BENEFICIARIO' \n"
                + " FROM \n"
                + "     prepaid_customer \n"
                + " WHERE \n"
                + "     prepaid_customer.created_by = ? \n"
                + " AND (prepaid_customer.creation_ddate BETWEEN ? AND ?) \n"
                + " ORDER BY \n"
                + "     prepaid_customer.id DESC \n");

        final String seller = RequestBasicValidator.getStringFromRequest(request,
                PARAM_SELLER, "");
        final java.sql.Date start_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_START_DATE);
        final java.sql.Date end_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_END_DATE);

        query.addParameter(seller);
        query.addParameter(start_date);
        query.addParameter(end_date);

        System.out.println("net.comtor.radius.gui.report.ListPinSoldReportController.createPreparedQuery()\t" + query);

        return query;
    }

}
