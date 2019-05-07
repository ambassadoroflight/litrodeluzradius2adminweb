package web.gui;

import net.comtor.framework.common.auth.gui.aaa.UserController;
import net.comtor.framework.common.auth.gui.aaa.ProfileController;
import java.util.LinkedList;
import net.comtor.aaa.helper.UserHelper;
import net.comtor.framework.common.auth.element.User;
import net.comtor.framework.common.auth.gui.aaa.ProfileAdmin;
import net.comtor.framework.common.auth.gui.aaa.UserAdmin;
import net.comtor.framework.global.ComtorGlobal;
import net.comtor.framework.util.security.SecurityHelper;
import net.comtor.framework.html.administrable.AbstractComtorFacadeAdministratorController;
import net.comtor.framework.html.advanced.ComtorLinkIconFish;
import net.comtor.framework.pagefactory.index.TableIndexFactory;
import net.comtor.i18n.html.ComtorLinkIconFishI18n;
import org.unlitrodeluzcolombia.radius.gui.advertising.CampaignAdmin;
import org.unlitrodeluzcolombia.radius.gui.advertising.CampaignController;
import org.unlitrodeluzcolombia.radius.gui.advertising.SponsorAdmin;
import org.unlitrodeluzcolombia.radius.gui.advertising.SponsorController;
import org.unlitrodeluzcolombia.radius.gui.advertising.SurveyAdmin;
import org.unlitrodeluzcolombia.radius.gui.advertising.SurveyController;
import org.unlitrodeluzcolombia.radius.gui.happyhour.HappyHourAdmin;
import org.unlitrodeluzcolombia.radius.gui.happyhour.HappyHourController;
import org.unlitrodeluzcolombia.radius.gui.hotspot.HotspotAdmin;
import org.unlitrodeluzcolombia.radius.gui.hotspot.HotspotController;
import org.unlitrodeluzcolombia.radius.gui.hotspot.HotspotsMapPage;
import org.unlitrodeluzcolombia.radius.gui.hotspot.ZoneAdmin;
import org.unlitrodeluzcolombia.radius.gui.hotspot.ZoneController;
import org.unlitrodeluzcolombia.radius.gui.kiosk.KioskAdmin;
import org.unlitrodeluzcolombia.radius.gui.kiosk.KioskController;
import org.unlitrodeluzcolombia.radius.gui.rate.PrepaidRateAdmin;
import org.unlitrodeluzcolombia.radius.gui.rate.PrepaidRateController;
import org.unlitrodeluzcolombia.radius.gui.report.SurveyReportPage;
import org.unlitrodeluzcolombia.radius.gui.seller.SellerAdmin;
import org.unlitrodeluzcolombia.radius.gui.seller.SellerController;
import web.global.LitroDeLuzImages;

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
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(UserAdmin.class),
                    LitroDeLuzImages.USER_INDEX, "Usuarios", getRequest()));
        }

        if (can(new ProfileController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(ProfileAdmin.class),
                    LitroDeLuzImages.PROFILE_INDEX, "Perfiles", getRequest()));
        }

        if (can(new ZoneController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(ZoneAdmin.class),
                    LitroDeLuzImages.HOTSPOT_INDEX, "Zonas", getRequest()));
        }

        if (can(new HotspotController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(HotspotAdmin.class),
                    LitroDeLuzImages.HOTSPOT_INDEX, "Hotspots", getRequest()));
        }

        if (can(new HotspotController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(HotspotsMapPage.class),
                    LitroDeLuzImages.MAP_INDEX, "Mapa de Hotspots", getRequest()));
        }

        if (can(new SponsorController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(SponsorAdmin.class),
                    LitroDeLuzImages.ADVERTISING_INDEX, "Patrocinadores", getRequest()));
        }

        if (can(new CampaignController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(CampaignAdmin.class),
                    LitroDeLuzImages.ADVERTISING_INDEX, "Campañas Publicitarias", getRequest()));
        }

        if (can(new SurveyController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(SurveyAdmin.class),
                    LitroDeLuzImages.ADVERTISING_INDEX, "Encuestas", getRequest()));
        }

        if (can(new KioskController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(KioskAdmin.class),
                    LitroDeLuzImages.KIOSK_INDEX, "Kioscos", getRequest()));
        }

        if (can(new SellerController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(SellerAdmin.class),
                    LitroDeLuzImages.SELLER_INDEX, "Gestores", getRequest()));
        }

        if (can(new PrepaidRateController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(PrepaidRateAdmin.class),
                    LitroDeLuzImages.RATE_INDEX, "Tarifas", getRequest()));
        }

        if (can(new HappyHourController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(HappyHourAdmin.class),
                    LitroDeLuzImages.HAPPYHOUR_INDEX, "Pines Happy Hour", getRequest()));
        }

        if (can("VIEW_SURVEY_REPORT")) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(SurveyReportPage.class),
                    LitroDeLuzImages.REPORT_INDEX, "Reporte de Resultados por Encuesta", getRequest()));
        }

        return linkIcons;
    }

    @Override
    public int getCols() {
        return 4;
    }

    @Override
    public String getWelcomeMessage() {
        User user = (User) UserHelper.getCurrentUser(getRequest()).getUser();

        return "Bienvenido, " + user.getName();
    }

    private boolean can(AbstractComtorFacadeAdministratorController controller) {
        return SecurityHelper.can(controller.getDeletePrivilege(), getRequest())
                || SecurityHelper.can(controller.getEditPrivilege(), getRequest())
                || SecurityHelper.can(controller.getAddPrivilege(), getRequest())
                || SecurityHelper.can(controller.getViewPrivilege(), getRequest());
    }

    private boolean can(String privilege) {
        return SecurityHelper.can(privilege, getRequest());
    }
}
