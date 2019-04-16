package org.unlitrodeluzcolombia.radius.gui.advertising.microservices;

import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.unlitrodeluzcolombia.radius.gui.advertising.commons.QuestionFieldGenerator;
import org.unlitrodeluzcolombia.radius.gui.advertising.microservices.dto.MapResponse;

/**
 *
 * @author Guido Cafiel
 */
@Path("survey_service")
public class SurveyServices {

    @GET
    @Path("/question_tag/{question_type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MapResponse> getQuetionTags(@PathParam("question_type") String questionType) {
        List<MapResponse> response = new LinkedList<>();
        QuestionFieldGenerator questionFieldGenerator = new QuestionFieldGenerator();
        response.add(new MapResponse("questions_area", questionFieldGenerator.getQuestionTag(questionType).getHtml()));
        return response;
    }

}
