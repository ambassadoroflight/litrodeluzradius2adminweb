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
import net.comtor.radius.element.AdvertisingCampaign;
import net.comtor.radius.facade.AdvertisingCampaignDAOFacade;
import net.comtor.util.StringUtil;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 04, 2019
 */
public class AdvertisingCampaignWebFacade
        extends AbstractWebLogicFacade<AdvertisingCampaign, Long, AdvertisingCampaignDAOFacade> {

    private static final Logger LOG = Logger.getLogger(AdvertisingCampaignWebFacade.class.getName());

    private static final String[] VALID_IMAGE_FORMATS = {
        ".png",
        ".jpeg",
        ".jpg"
    };

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(AdvertisingCampaign campaign) {
        return validate(campaign);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(AdvertisingCampaign campaign) {
        return validate(campaign);
    }

    @Override
    public void insert(AdvertisingCampaign ad) throws BusinessLogicException {
        File file1 = getRequest().getMparser().getFile("banner_1_file");
        String banner1Filename = getBannerName(ad.getSponsor()) + "_horz." + getExtension(file1);
        File desFile = new File(GlobalWeb.ADS_DIRECTORY + File.separator + banner1Filename);

        try {
            byte[] bytes = Files.readAllBytes(file1.toPath());

            Files.write(desFile.toPath(), bytes);

            if (desFile.exists()) {
                file1.delete();
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        ad.setBanner_1(banner1Filename);

        File file2 = getRequest().getMparser().getFile("banner_2_file");
        String banner2Filename = getBannerName(ad.getSponsor()) + "_vert." + getExtension(file2);
        File desFile2 = new File(GlobalWeb.ADS_DIRECTORY + File.separator + banner2Filename);

        try {
            byte[] bytes = Files.readAllBytes(file2.toPath());

            Files.write(desFile2.toPath(), bytes);

            if (desFile2.exists()) {
                file2.delete();
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        ad.setBanner_2(banner2Filename);

        super.insert(ad);
    }

    public File getDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            Files.createDirectories(directory.toPath());
        }

        return directory;
    }

    public boolean uploadFile(String targetPath, String filename, File srcFile)
            throws IOException {
        File targetDirectory = getDirectory(targetPath);

        File targetFile = new File(targetDirectory.getPath() + File.separator + filename);

        byte[] bytes = Files.readAllBytes(srcFile.toPath());

        Files.write(targetFile.toPath(), bytes);

        return targetFile.exists();
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

    public static String getExtension(final File file) {
        final String name = file.getName();
        final String extension = name.substring(name.lastIndexOf('.') + 1);

        return extension;
    }

    private LinkedList<ObjectValidatorException> validate(AdvertisingCampaign campaign)
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
                if (file != null && !hasValidFormat(file)) {
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
            if (startDate.after(endDate)) {
                exceptions.add(new ObjectValidatorException("start_date",
                        "Fecha inicial debe ser anterior a la fecha final."));
                exceptions.add(new ObjectValidatorException("end_date",
                        "Fecha inicial debe ser anterior a la fecha final."));
            }
        }

        return exceptions;
    }
}
