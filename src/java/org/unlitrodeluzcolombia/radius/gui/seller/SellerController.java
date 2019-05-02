package org.unlitrodeluzcolombia.radius.gui.seller;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.radius.element.Seller;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlText;
import org.unlitrodeluzcolombia.radius.gui.finder.KioskFinder;
import net.comtor.html.form.HtmlCheckbox;
import net.comtor.html.form.HtmlFormElement;
import net.comtor.html.form.HtmlInputPassword;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.Kiosk;
import org.unlitrodeluzcolombia.radius.web.facade.KioskWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.SellerWebFacade;
import web.Images;
import web.global.LitroDeLuzImages;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class SellerController
        extends AbstractComtorFacadeAdministratorControllerI18n<Seller, String> {

    private static final Logger LOG = Logger.getLogger(SellerController.class.getName());

    @Override
    public String getEntityName() {
        return "Gestores";
    }

    @Override
    public WebLogicFacade<Seller, String> getLogicFacade() {
        return new SellerWebFacade();
    }
    
    @Override
    public void initForm(AdministrableForm form, Seller seller) throws BusinessLogicException {
        if (seller == null) {
            HtmlInputText login = new HtmlInputText("login", 32, 64);
            form.addField("Usuario", login, "Indique el nombre de "
                    + "usuario con el cual el gestor ingresará a la plataforma "
                    + "de venta de pines.", true);
        } else {
            final String seller_login = seller.getLogin();

            form.addInputHidden("login", seller_login);

            HtmlText login = new HtmlText(seller_login);
            form.addField("Usuario", login, null);
        }

        HtmlInputPassword password = new HtmlInputPassword("password", 32, 128);
        form.addField("Contraseña", password, "Ingrese la contraseña con el cual "
                + "el gestor ingresará a la plataforma de venta de pines.", true);

        HtmlInputPassword confirm_password = new HtmlInputPassword("confirm_password", 32, 128);
        form.addField("Confirmar Contraseña", confirm_password, null, true);

        HtmlInputText name = new HtmlInputText("name", "", 32, 128);
        form.addField("Nombre", name, null, true);

        HtmlInputText document_id = new HtmlInputText("document_id", 32, 32);
        form.addField("Documento de Identidad", document_id, null);

        HtmlInputText email = new HtmlInputText("email", 32, 128);
        form.addField("E-mail", email, null);

        HtmlInputText phone = new HtmlInputText("phone", 16, 32);
        form.addField("Teléfono", phone, null);

        HtmlInputText address = new HtmlInputText("address", 32, 128);
        form.addField("Dirección", address, null);

        HtmlFinder kiosk = getKioskFinder(seller);
        form.addField("Kiosco", kiosk, null, true);

        HtmlCheckbox active = new HtmlCheckbox("active", "active");
        form.addField("Activo", active, "Si marca la casilla, el gestor estará "
                + "activo para ingresar a la plataforma de venta de pines.");
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_SELLER";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_SELLER";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_SELLER";
    }

    @Override
    public String getFormName() {
        return "seller_form";
    }

    @Override
    protected void fillFormOnEdit(Seller object, AdministrableForm form) {
        super.fillFormOnEdit(object, form);

        HtmlFormElement formElement1 = (HtmlFormElement) form.getHtmlElement("password");
        formElement1.setValue("nochange");

        HtmlFormElement formElement2 = (HtmlFormElement) form.getHtmlElement("confirm_password");
        formElement2.setValue("nochange");
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("login", "Usuario");
        headers.put("name", "Nombre");
        headers.put("kiosk", "Kiosco");
        headers.put("active", "Estado");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Seller seller) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(seller.getLogin());
        row.add(seller.getName());
        row.add("[" + seller.getKiosk() + "] " + seller.getKiosk_name());
        row.add(seller.isActive() ? "Activo" : "Inactivo");

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.SELLER_CONTROLLER;
    }

    @Override
    public String getLogModule() {
        return "Gestores";
    }

    @Override
    public String getAddFormLabel() {
        return "Nuevo Gestor";
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Crear Gestor";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar Gestor";
    }

    @Override
    public String getConfirmDeleteMessage(Seller seller) {
        return "¿Está seguro que desea eliminar al gestor <b>[" + seller.getLogin()
                + "] " + seller.getName() + "</b>?";
    }

    @Override
    public String getAddedMessage(Seller seller) {
        return "El gestor <b>[" + seller.getLogin() + "] " + seller.getName()
                + "</b> ha sido creado.";
    }

    @Override
    public String getDeletedMessage(Seller seller) {
        return "El gestor <b>[" + seller.getLogin() + "] " + seller.getName()
                + "</b> ha sido eliminado.";
    }

    @Override
    public String getUpdatedMessage(Seller seller) {
        return "El gestor <b>[" + seller.getLogin() + "] " + seller.getName()
                + "</b> ha sido actualizado.";
    }

    private HtmlFinder getKioskFinder(final Seller seller) {
        Kiosk hotspot = null;
        String valueToShow = "";

        try {
            hotspot = ((seller == null)
                    ? null
                    : new KioskWebFacade().find(seller.getKiosk()));
            valueToShow = ((hotspot == null)
                    ? ""
                    : new KioskFinder().getValueToShow(hotspot));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return new HtmlFinder("kiosk", KioskFinder.class, valueToShow, 32);
    }

}
