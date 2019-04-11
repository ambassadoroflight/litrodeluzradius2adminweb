package web.gui;

import net.comtor.framework.common.auth.gui.aaa.UserController;
import net.comtor.framework.common.auth.gui.aaa.ProfileController;
import java.util.LinkedList;
import net.comtor.framework.global.ComtorGlobal;
import net.comtor.framework.util.security.SecurityHelper;
import net.comtor.framework.html.administrable.AbstractComtorFacadeAdministratorController;
import net.comtor.framework.html.advanced.ComtorLinkIconFish;
import net.comtor.framework.pagefactory.index.TableIndexFactory;
import net.comtor.i18n.I18n;
import net.comtor.i18n.html.ComtorLinkIconFishI18n;
import org.unlitrodeluzcolombia.radius.gui.kiosk.KioskAdmin;
import org.unlitrodeluzcolombia.radius.gui.seller.SellerAdmin;
import web.Images;

/**
 *
 * @author jorgegarcia@comtor.net
 * @since Sep 14, 2011
 * @version 1.0.0
 */
public class Index extends TableIndexFactory {

    @Override
    public LinkedList<ComtorLinkIconFish> getIconLinks() {
        LinkedList<ComtorLinkIconFish> linkIcons = new LinkedList<ComtorLinkIconFish>();

        if (can(new UserController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(SellerAdmin.class),
                    Images.SELLERS_128, "seller.entityname.plural", getRequest()));
        }

        if (can(new ProfileController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(KioskAdmin.class),
                    Images.KIOSKS_128, "commerce.entityname.plural", getRequest()));
        }

        return linkIcons;
    }

    /**
     * Whether the logged user supports all of the privileges for a given
     * controller or not
     */
    private boolean can(AbstractComtorFacadeAdministratorController controller) {
        return SecurityHelper.can(controller.getDeletePrivilege(), getRequest())
                || SecurityHelper.can(controller.getEditPrivilege(), getRequest())
                || SecurityHelper.can(controller.getAddPrivilege(), getRequest())
                || SecurityHelper.can(controller.getViewPrivilege(), getRequest());
    }

    @Override
    public int getCols() {
        return 4;
    }

    @Override
    public String getWelcomeMessage() {
        return I18n.tr(getLang(), "tableindex.welcomeMessage");
    }
}
