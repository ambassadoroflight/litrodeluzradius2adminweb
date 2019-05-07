package org.unlitrodeluzcolombia.radius.web.facade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.radius.element.Campaign;
import net.comtor.radius.facade.CampaignDAOFacade;
import net.comtor.util.StringUtil;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 16, 2019
 */
public class CampaignWebFacade
        extends AbstractWebLogicFacade<Campaign, Long, CampaignDAOFacade> {

    private static final Logger LOG = Logger.getLogger(CampaignWebFacade.class.getName());

    private static final String[] VALID_IMAGE_FORMATS = {
        ".png",
        ".jpeg",
        ".jpg"
    };

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Campaign campaign) {
        return validate(campaign);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Campaign campaign) {
        return validate(campaign);
    }

    @Override
    public void insert(Campaign campaign) throws BusinessLogicException {
        File file1 = getRequest().getMparser().getFile("banner_1_file");
        String banner1Filename = getBannerName(campaign.getSponsor()) + "_horz."
                + getExtension(file1);
        File desFile = new File(GlobalWeb.ADS_DIRECTORY + File.separator + banner1Filename);

        try {
            writeFile(file1, desFile);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        campaign.setBanner_1(banner1Filename);

        File file2 = getRequest().getMparser().getFile("banner_2_file");
        String banner2Filename = getBannerName(campaign.getSponsor()) + "_vert."
                + getExtension(file2);
        File desFile2 = new File(GlobalWeb.ADS_DIRECTORY + File.separator + banner2Filename);

        try {
            writeFile(file2, desFile2);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        campaign.setBanner_2(banner2Filename);

        super.insert(campaign);
    }

    @Override
    public void update(Campaign campaign) throws BusinessLogicException {
        File file1 = getRequest().getMparser().getFile("banner_1_file");

        if (file1 != null) {
            File desFile = new File(GlobalWeb.ADS_DIRECTORY + File.separator
                    + campaign.getBanner_1());

            try {
                writeFile(file1, desFile);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        File file2 = getRequest().getMparser().getFile("banner_2_file");

        if (file2 != null) {
            File desFile2 = new File(GlobalWeb.ADS_DIRECTORY + File.separator
                    + campaign.getBanner_2());

            try {
                writeFile(file2, desFile2);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        super.update(campaign);
    }

    private void writeFile(File srcFile, File desFile) throws IOException {
        byte[] bytes = Files.readAllBytes(srcFile.toPath());

        Files.write(desFile.toPath(), bytes);

        if (desFile.exists()) {
            srcFile.delete();
        }
    }

    private LinkedList<ObjectValidatorException> validate(Campaign campaign)
            throws RuntimeException {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        if (campaign.getSponsor() <= 0) {
            exceptions.add(new ObjectValidatorException("sponsor", "Debe indicar "
                    + "a qué patrocinador pertenece esta campaña."));
        }

        File file;

        for (int i = 1; i <= 2; i++) {
            String bannerParam = "banner_" + i + "_file";

            file = getRequest().getMparser().getFile(bannerParam);

            if ((file == null)
                    && (!StringUtil.isValid(campaign.getBanner_1())
                    || !StringUtil.isValid(campaign.getBanner_2()))) {
                exceptions.add(new ObjectValidatorException(bannerParam,
                        "Debe subir un archivo."));
            } else {
                if ((file != null) && !hasValidFormat(file)) {
                    exceptions.add(new ObjectValidatorException(bannerParam,
                            "Debe subir una imagen en formato <b><i>png o jpeg.</i></b>"));
                }

            }
        }

        java.sql.Date startDate = campaign.getStart_date();

        if (startDate == null) {
            exceptions.add(new ObjectValidatorException("start_date",
                    "Debe ingresar una fecha inicial."));
        }

        java.sql.Date endDate = campaign.getEnd_date();

        if (endDate != null) {
            exceptions.add(new ObjectValidatorException("end_date",
                    "Debe ingresar una fecha final."));
        } else {
            if (startDate.after(endDate)) {
                exceptions.add(new ObjectValidatorException("start_date",
                        "Fecha inicial debe ser anterior a la fecha final."));
                exceptions.add(new ObjectValidatorException("end_date",
                        "Fecha inicial debe ser anterior a la fecha final."));
            }
        }

        return exceptions;
    }

    private boolean hasValidFormat(final File file) {
        for (String format : VALID_IMAGE_FORMATS) {
            if (file.getName().endsWith(format)) {
                return true;
            }
        }

        return false;
    }

    private String getBannerName(long sponsor) {
        return "sponsor_" + sponsor + "_"
                + UUID.randomUUID().toString().replace("-", "");
    }

    private static String getExtension(final File file) {
        final String name = file.getName();
        final String extension = name.substring(name.lastIndexOf('.') + 1);

        return extension;
    }
}
