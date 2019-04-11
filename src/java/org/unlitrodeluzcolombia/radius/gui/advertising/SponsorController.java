package org.unlitrodeluzcolombia.radius.gui.advertising;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.Sponsor;
import org.unlitrodeluzcolombia.radius.web.facade.SponsorWebFacade;
import web.Images;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 03, 2019
 */
public class SponsorController extends AbstractComtorFacadeAdministratorControllerI18n<Sponsor, Long> {

    private static final Logger LOG = Logger.getLogger(SponsorController.class.getName());

    @Override
    public String getEntityName() {
        return "Patrocinador";
    }

    @Override
    public WebLogicFacade<Sponsor, Long> getLogicFacade() {
        return new SponsorWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, Sponsor sponsor)
            throws BusinessLogicException {
        if (sponsor != null) {
            final long sponsorId = sponsor.getId();

            form.addInputHidden("id", sponsorId);

            HtmlText id = new HtmlText(sponsorId);
            form.addField("ID", id, null);
        }

        HtmlInputText name = new HtmlInputText("name", 32, 128);
        form.addField("Nombre", name, null, true);

        HtmlInputText contact = new HtmlInputText("contact", 32, 64);
        form.addField("Contacto", contact, null);

        HtmlInputText phone = new HtmlInputText("phone", 16, 32);
        form.addField("Teléfono", phone, null);

        HtmlInputText email = new HtmlInputText("email", 32, 128);
        form.addField("Correo Electrónico", email, null);
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
    public String getDeletePrivilege() {
        return "DELETE_SPONSOR";
    }

    @Override
    public String getFormName() {
        return "sponsor_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("name", "Nombre");
        headers.put("contact", "Contacto");
        headers.put("phone", "Teléfono");
        headers.put("email", "Correo Electrónico");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Sponsor sponsor) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(sponsor.getId());
        row.add(sponsor.getName());
        row.add(sponsor.getContact());
        row.add(sponsor.getPhone());
        row.add(sponsor.getEmail());

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return Images.HOTSPOTS_WHITE_32;
    }

    @Override
    public String getLogModule() {
        return "Patrocionadores";
    }

    @Override
    public String getAddFormLabel() {
        return "Nuevo Patrocinador";
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Crear Patrocinador";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar Patrocinador";
    }

    @Override
    public String getConfirmDeleteMessage(Sponsor sponsor) {
        return "¿Está seguro que desea eliminar el patrocinador <b>[" + sponsor.getId()
                + "] " + sponsor.getName() + "</b>?";
    }

    @Override
    public String getAddedMessage(Sponsor sponsor) {
        return "El patrocinador <b>[" + sponsor.getId() + "] " + sponsor.getName()
                + "</b> ha sido creado.";
    }

    @Override
    public String getDeletedMessage(Sponsor sponsor) {
        return "El patrocinador <b>[" + sponsor.getId() + "] " + sponsor.getName()
                + "</b> ha sido eliminado.";
    }

    @Override
    public String getUpdatedMessage(Sponsor hotspot) {
        return "El patrocinador <b>[" + hotspot.getId() + "] " + hotspot.getName()
                + "</b> ha sido actualizado.";
    }

}
