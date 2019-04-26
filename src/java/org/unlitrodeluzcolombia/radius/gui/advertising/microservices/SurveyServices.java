package org.unlitrodeluzcolombia.radius.gui.advertising.microservices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.comtor.dao.ComtorDaoException;
import net.comtor.radius.element.Campaign;
import net.comtor.radius.element.CampaignXZone;
import net.comtor.radius.element.Hotspot;
import net.comtor.radius.facade.CampaignDAOFacade;
import net.comtor.radius.facade.CampaignXZoneDAOFacade;
import net.comtor.radius.facade.HotspotDAOFacade;
import org.unlitrodeluzcolombia.radius.element.Answer;
import org.unlitrodeluzcolombia.radius.element.Question;
import org.unlitrodeluzcolombia.radius.element.Survey;
import org.unlitrodeluzcolombia.radius.facade.AnswerDAOFacade;
import org.unlitrodeluzcolombia.radius.facade.QuestionDAOFacade;
import org.unlitrodeluzcolombia.radius.facade.SurveyDAOFacade;
import org.unlitrodeluzcolombia.radius.gui.advertising.commons.QuestionFieldGenerator;
import org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto.HotspotCampaign;
import org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto.HotspotQuestion;
import org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto.HotspotSurvey;
import org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto.MapResponse;
import org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto.SurveyQuestion;
import org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto.SurveyResponse;
import org.unlitrodeluzcolombia.radius.utils.Base64Util;

/**
 *
 * @author Guido Cafiel
 */
@Path("survey_service")
public class SurveyServices {

    private static final Logger LOG = Logger.getLogger(SurveyServices.class.getName());

    @GET
    @Path("/question_tag/{question_type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MapResponse> getQuetionTags(@PathParam("question_type") String questionType) {
        List<MapResponse> response = new LinkedList<>();
        QuestionFieldGenerator questionFieldGenerator = new QuestionFieldGenerator();
        response.add(new MapResponse("questions_area", questionFieldGenerator.getQuestionTag(questionType).getHtml()));
        return response;
    }

    /**
     * WS que recibe un JSON con las respuestas de una encuesta y las guarda.
     *
     * @param surveyAnswer
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/v1/save_survey")
    public Response saveSurveyAnswer(SurveyResponse surveyAnswer) {
        if (surveyAnswer == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        
        java.sql.Date answerDate = new java.sql.Date(surveyAnswer.getDate().getTime());
        Long hotspot = surveyAnswer.getHotspot();

        AnswerDAOFacade answerFacade = new AnswerDAOFacade();
        Answer answer;

        try {
            for (SurveyQuestion question : surveyAnswer.getAnswers()) {
                answer = new Answer(hotspot, answerDate);
                answer.setResponse(question.getAnswer());
                answer.setQuestion(question.getQuestionId());

                answerFacade.create(answer);
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return Response
                    .serverError()
                    .build();
        }

        return Response
                .ok()
                .build();
    }

    /**
     * WS que recibe un JSON con las respuestas de una encuesta y las guarda.
     *
     * @param surveyAnswer
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/v1/surveys")
    public Response saveSurveyAnswer(@QueryParam("hotspot") long hotspotId) {
        try {
            Hotspot hotspot = new HotspotDAOFacade().find(hotspotId);

            if (hotspot == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }

            long zone = hotspot.getZone();

            
            //TODO: ESTO SE PUEDE HACER DESDE UN SÓLO QUERY
            CampaignXZone cxz = new CampaignXZoneDAOFacade().findByProperty("zone", zone);

            Campaign campaign = new CampaignDAOFacade().find(cxz.getCampaign());

            Survey survey = new SurveyDAOFacade().findByProperty("campaign", campaign.getId());

            LinkedList<Question> questions = new QuestionDAOFacade().findAllByProperty("survey", survey.getId());

            HotspotSurvey jsonSurvey = new HotspotSurvey(survey);

            HotspotQuestion jsonQuestion;

            for (Question question : questions) {
                jsonQuestion = new HotspotQuestion(question);

                jsonSurvey.addQuestion(jsonQuestion);
            }

            HotspotCampaign jsonCampaign = new HotspotCampaign(campaign);
            jsonCampaign.setSurvey(jsonSurvey);
            jsonCampaign.setBanner_1(campaign.getBanner_1());
            jsonCampaign.setBanner_2(campaign.getBanner_2());

            return Response
                    .ok(jsonCampaign)
                    .build();
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return Response
                    .serverError()
                    .build();
        }
    }

    private String getBase64(final File file, final Base64Util base64Util) throws IOException {
        try {
            return base64Util.encodeBase64(file);
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return "";
        }
    }

}
