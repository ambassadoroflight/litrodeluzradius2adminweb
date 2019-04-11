package org.unlitrodeluzcolombia.radius.gui.report;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import net.comtor.i18n.html.HtmlCalendarJQueryI18n;
import net.comtor.radius.element.Seller;
import net.comtor.radius.facade.SellerDAOFacade;
import org.unlitrodeluzcolombia.radius.gui.finder.KioskFinder;
import org.unlitrodeluzcolombia.radius.gui.finder.SellerFinder;
import net.comtor.util.DateUtil;
import net.comtor.util.StringUtil;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 28, 2018
 */
public class SellerStatsReportController extends ReportControllerJDBC {

    private static final Logger LOG = Logger.getLogger(SellerStatsReportController.class.getName());

    private static final int MAX_DAYS = 30;

    private static final String PARAM_KIOSK = "kiosk";
    private static final String PARAM_SELLER = "seller";
    private static final String PARAM_START_DATE = "start_date";
    private static final String PARAM_END_DATE = "end_date";

    public SellerStatsReportController() throws ReportException {
        super();
    }

    @Override
    protected ComtorJDBCDao createComtorJDBCDAO() throws ComtorDaoException {
        return new ApplicationDAO();
    }

    @Override
    public String[] getPrivileges() {
        return new String[]{"VIEW_SOLD_PIN_X_KIOSK_REPORT"};
    }

    @Override
    public AdministrableForm createFilterForm(String action, HttpServletMixedRequest request) {
        AdministrableForm form = new DivFormI18n(action, AdministrableForm.METHOD_POST, request);
        form.setTitle("Establezca rango de fechas para el reporte");

        HtmlFinder kiosk = new HtmlFinder(PARAM_KIOSK, KioskFinder.class, "", 32);
        form.addField("Kiosco", kiosk, null, true);

        HtmlFinder seller = new HtmlFinder(PARAM_SELLER, SellerFinder.class, "", 32);
        form.addField("Vendedor", seller, null);

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

        long kiosk = RequestBasicValidator.getLongFromRequest(request,
                PARAM_KIOSK, 0);
        String seller_login = RequestBasicValidator.getStringFromRequest(request,
                PARAM_SELLER, "");
        java.sql.Date start_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_START_DATE);
        java.sql.Date end_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_END_DATE);

        if (kiosk <= 0) {
            exceptions.add(new ObjectValidatorException(PARAM_KIOSK,
                    "Debe indicar el Kiosco."));
        }

        if (StringUtil.isValid(seller_login)) {
            try {
                Seller seller = new SellerDAOFacade().find(seller_login);

                if (seller.getKiosk() != kiosk) {
                    exceptions.add(new ObjectValidatorException(PARAM_SELLER,
                            "El vendedor <b>" + seller + "</b> no pertenece al "
                            + "kiosco <b>" + kiosk + "</b>."));
                }
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
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
//FIXME: ARREGLAR ESTE QUERY
    @Override
    protected ReportJDBCPreparedQuery createPreparedQuery(HttpServletMixedRequest request) {
        ReportJDBCPreparedQuery query = new ReportJDBCPreparedQuery("\n"
                + " SELECT \n"
                + "     seller_stats.seller_login AS Vendedor, "
                + "     seller_stats.commerce.name AS Kiosco, "
                + "     seller_stats.date AS Fecha, "
                + "     seller_stats.time AS Tiempo, "
                + "     seller_stats.quantity AS Cantidad "
                + " FROM \n"
                + "     seller_stats \n"
                + " LEFT JOIN kiosk ON seller_stats.commerce_id = kiosk.commerce.id \n"
                + " WHERE ( \n"
                + "     seller_login = ? OR commerce_id = ? \n"
                + " ) \n"
                + " AND (date BETWEEN ?  AND ?) \n"
                + " ORDER BY date DESC ");

        final long kiosk = RequestBasicValidator.getLongFromRequest(request,
                PARAM_KIOSK, 0);
        final String seller_login = RequestBasicValidator.getStringFromRequest(request,
                PARAM_SELLER, "");
        final java.sql.Date start_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_START_DATE);
        final java.sql.Date end_date = RequestBasicValidator.getDateFromRequest(request,
                PARAM_END_DATE);

        query.addParameter(seller_login);
        query.addParameter(kiosk);
        query.addParameter(start_date);
        query.addParameter(end_date);

        return query;
    }

    @Override
    public String getReportName(HttpServletMixedRequest request) {
        return "Historico PIN venditos";
    }

    @Override
    public String getReportDescription(HttpServletMixedRequest request) {
        return "Reporte";
    }

    @Override
    public String getReportFileName(HttpServletMixedRequest request) {
        return "ReportePinVendidos.xlsx";
    }

    @Override
    public boolean isPaginated() {
        return true;
    }

}
