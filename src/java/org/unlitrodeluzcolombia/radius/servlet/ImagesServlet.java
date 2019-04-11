package org.unlitrodeluzcolombia.radius.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.request.validator.RequestBasicValidator;
import net.comtor.radius.element.AdvertisingCampaign;
import org.unlitrodeluzcolombia.radius.web.facade.AdvertisingCampaignWebFacade;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Abr 09, 2019
 */
public class ImagesServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ImagesServlet.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long id = RequestBasicValidator.getLongFromRequest(request, "id");
        int banner = RequestBasicValidator.getIntFromRequest(request, "banner");

        if ((id <= 0) || ((banner < 1) || (banner > 2))) {
            response.sendError(403, "Parámetro no válido");

            return;
        }

        AdvertisingCampaign campaign = null;

        try {
            campaign = new AdvertisingCampaignWebFacade().find(id);
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        if (campaign == null) {
            response.sendError(404, "Recurso no encontrado.");

            return;
        }

        String imageName = ((banner == 1)
                ? campaign.getBanner_1()
                : campaign.getBanner_2());

        String ext = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();
        String mime = "image/" + ext;

        response.setContentType(mime);

        try (ServletOutputStream os = response.getOutputStream()) {
            File file = new File(GlobalWeb.ADS_DIRECTORY + File.separator + imageName);

            if (!file.exists()) {
                LOG.log(Level.INFO, "Archivo " + imageName + " no existe.");

                response.sendError(404, "Recurso no encontrado");

                return;
            }

            byte[] bytes = Files.readAllBytes(file.toPath());

            os.write(bytes);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
