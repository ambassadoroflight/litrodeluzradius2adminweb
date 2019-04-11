package org.unlitrodeluzcolombia.radius.ws;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.comtor.aaa.helper.PasswordHelper2;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.radius.element.HappyHour;
import net.comtor.radius.element.Kiosk;
import net.comtor.radius.element.PrepaidCustomer;
import net.comtor.radius.element.PrepaidRate;
import net.comtor.radius.element.Seller;
import net.comtor.radius.element.SellerAuthToken;
import net.comtor.radius.facade.HappyHourDAOFacade;
import net.comtor.radius.facade.PrepaidCustmerDAOFacade;
import net.comtor.radius.facade.PrepaidRateDAOFacade;
import net.comtor.radius.facade.SellerDAOFacade;
import org.unlitrodeluzcolombia.radius.web.facade.HappyHourWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.KioskWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.SellerWebFacade;
import org.unlitrodeluzcolombia.radius.ws.io.PinSellInput;
import net.comtor.util.StringUtil;
import org.json.JSONArray;
import org.unlitrodeluzcolombia.radius.ws.io.PinInput;

/**
 * REST Web Service
 *
 * @author juandiego@comtor.net
 * @since Jan 28, 2019
 */
@Path("main")
public class LitroDeLuzRadiusResource {

    private static final Logger LOG = Logger.getLogger(LitroDeLuzRadiusResource.class.getName());

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MainWebServiceResource
     */
    public LitroDeLuzRadiusResource() {
    }

    /**
     * WS para autenticación del vendedor/gestor.
     *
     * @param input
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/v1/login")
    public Response login(LoginInput input) throws BusinessLogicException {
        final String username = input.getUsername();
        final String password = input.getPassword();

        if ((input == null) || !StringUtil.isValid(username)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new LoginOutput("Vendedor no válido"))
                    .build();
        }

        Seller seller;

        try {
            seller = new SellerDAOFacade().find(username);
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return Response.serverError()
                    .entity(new LoginOutput("Error interno"))
                    .build();
        }

        if (seller == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new LoginOutput("Usuario o contraseña incorrectas"))
                    .build();
        }

        if (!seller.isActive()) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new LoginOutput("Vendedor inactivo"))
                    .build();
        }

        String encryptedPassword = PasswordHelper2.getHelper()
                .encryption(seller.getSalt() + password);

        if (!seller.getPassword().equals(encryptedPassword)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new LoginOutput("Usuario o contraseña incorrectas"))
                    .build();
        }

        Kiosk kiosk = new KioskWebFacade().find(seller.getKiosk());

        SellerAuthToken token = new SellerAuthToken(username, username, null);

//        new SellerAuthTokenDAOFacade().
        LinkedList<PrepaidRate> rates;

        try {
            rates = new PrepaidRateDAOFacade().findAll();
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return Response.serverError()
                    .entity(new LoginOutput("Error interno"))
                    .build();
        }

        JSONArray ratesArray = new JSONArray(rates);

        return Response.ok(new LoginOutput("OK", username, seller.getName(),
                kiosk.getName(), ratesArray))
                .build();
    }

    /**
     * WS para obtener las tarifas prepago de pines
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/v1/prepaidrates")
    public Response prepaidRates() {
        try {
            LinkedList<PrepaidRate> rates = new PrepaidRateDAOFacade().findAll();

            GenericEntity<LinkedList<PrepaidRate>> list
                    = new GenericEntity<LinkedList<PrepaidRate>>(rates) {
                    };

            return Response.ok()
                    .entity(list)
                    .build();
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return Response.serverError()
                    .entity(new LoginOutput("Error interno"))
                    .build();
        }
    }

    /**
     * WS para guardar pines vendidos por el gestor desde cliente PHP.
     *
     * @param soldPin
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/v1/prepaidcustomer")
    public Response processPinSell(PinSellInput soldPin) {
        String pin = soldPin.getPin();
        long purchased_time = soldPin.getPurchased_time();
        String pin_type = soldPin.getPin_type();
        String seller = soldPin.getSeller();
        Timestamp creation_date = new Timestamp(soldPin.getCreation_date());
        String name = soldPin.getCustomer_name();

        PrepaidCustomer prepaidCustomer = new PrepaidCustomer(pin, purchased_time,
                pin_type, seller);
        prepaidCustomer.setCreation_date(creation_date);
        prepaidCustomer.setAttr_1(name);

        try {
            new PrepaidCustmerDAOFacade().create(prepaidCustomer);

            return Response
                    .ok(prepaidCustomer)
                    .build();
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return Response.serverError()
                    .entity(new LoginOutput("Error interno"))
                    .build();
        }
    }

    //TODO: OBTENER EL KIOSCO O HOTSPOT EN LUGAR DEL VENDEDOR
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/v1/happyhour")
    public Response happyHours(@QueryParam("seller") String seller_login) {
        try {
            if (!StringUtil.isValid(seller_login)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new LoginOutput("Vendedor no válido"))
                        .build();
            }

            Seller seller = new SellerWebFacade().find(seller_login);
            long kioskId = seller.getKiosk();

            Kiosk kiosk = new KioskWebFacade().find(kioskId);

            LinkedList<HappyHour> happyHours = new HappyHourWebFacade()
                    .findAllByProperty("hotspot", kiosk.getHotspot());

            GenericEntity<LinkedList<HappyHour>> list
                    = new GenericEntity<LinkedList<HappyHour>>(happyHours) {
                    };

            return Response.ok()
                    .entity(list)
                    .build();
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return Response.serverError()
                    .entity(new LoginOutput("Error interno"))
                    .build();
        }
    }

    /**
     * WS para autenticación del vendedor/gestor.
     *
     * @param input
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/v1/validar")
    public Response validatePin(PinInput input) throws BusinessLogicException {
        final String pin = input.getPin();
        final long hotspot = input.getHotspot();

        if ((input == null) || !StringUtil.isValid(pin)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Pin no válido.")
                    .build();
        }

        if (isHappyHourPin(pin)) {
            return validateHappyHourPin(pin, hotspot);
        }

        return validateFullPin(pin, hotspot);
    }

    private Response validateHappyHourPin(final String pin, final long hotspot) {
        HappyHour happyHour;

        try {
            happyHour = new HappyHourDAOFacade().findByPinAndHotspot(pin, hotspot);
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return Response.serverError()
                    .entity("Error interno")
                    .build();
        }

        if (happyHour == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Pin hapno válido.")
                    .build();
        }

        Calendar calendar = Calendar.getInstance();

        int currentTime = calendar.get(Calendar.HOUR_OF_DAY) * 60
                + calendar.get(Calendar.MINUTE);

        if (isHappyHourDay(happyHour, calendar)
                && isHappyHourTime(happyHour, currentTime)) {
            return Response.ok()
                    .entity("+OK")
                    .build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("El pin de Happy Hour '" + pin + "' se encuentra fuera de horario")
                .build();
    }

    private Response validateFullPin(final String login, long hotspotId) {
        PrepaidCustomer customer = null;

        try {
            final PrepaidCustmerDAOFacade facade = new PrepaidCustmerDAOFacade();

            customer = facade.findByProperty("login", login);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return Response.serverError()
                    .entity("Error interno")
                    .build();
        }

        if (customer == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Pin full no válido.")
                    .build();
        }

        final String pin = customer.getLogin();

        // Valida si el cliente tiene un hotspot asignado.
        if ((customer.getHotspot_joined() == 1)
                && (customer.getHotspot() != hotspotId)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(String.format("El pin '%s' está ligado al Hotspot %d",
                                    pin, customer.getHotspot()))
                    .build();
        }

        // Valida si el pin está inactivo (o sea, ya venció por tiempo).
        if (!customer.isActive()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(String.format("El pin '%s' no se encuentra activo.", pin))
                    .build();
        }

        // Valida si el pin ya tiene fecha de primer uso.
        if (customer.getFirst_use_date() == null) {
            return Response.ok()
                    .entity("+OK")
                    .build();
        } else {
            long now = System.currentTimeMillis();
            long time = ((customer.getEnd_session_date().getTime() - now) / 1000);

            // Valida si el pin ya venció por tiempo.
            if (time <= 0) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(String.format("El tiempo del pin '" + pin + "' se ha agotado.", pin))
                        .build();
            }

            return Response.ok()
                    .entity("+OK")
                    .build();
        }

    }

    private boolean isHappyHourPin(final String pin) {
        return pin.matches("HAP-[A-Z]{3}-[A-Z]{3}");
    }

    private boolean isHappyHourDay(final HappyHour happyHour, Calendar calendar) {
        int currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK);

        return (happyHour.isSunday() && (currentWeekDay == 1))
                || (happyHour.isMonday() && (currentWeekDay == 2))
                || (happyHour.isTuesday() && (currentWeekDay == 3))
                || (happyHour.isWednesday() && (currentWeekDay == 4))
                || (happyHour.isThursday() && (currentWeekDay == 5))
                || (happyHour.isFriday() && (currentWeekDay == 6))
                || (happyHour.isSaturday() && (currentWeekDay == 7));
    }

    private boolean isHappyHourTime(final HappyHour happyHour, final int currentTime) {
        return (currentTime >= happyHour.getStart_time())
                && (currentTime <= happyHour.getEnd_time());
    }
    /*
     private String generateAuthToken(String login) throws ComtorDaoException {
     long now = System.currentTimeMillis();
     //        String token = ClaroUtil.generateToken(6) + "-" + now;
     String token = "";
     long expirationDate = now + (1000 * 60 * 60 * AuthToken.TOKEN_VALID_HOURS);

     SellerAuthToken authToken = new SellerAuthToken(login, token, expirationDate);

     new SellerAuthTokenDAOFacade().createOrEdit(authToken);

     return token;
     }*/
}
