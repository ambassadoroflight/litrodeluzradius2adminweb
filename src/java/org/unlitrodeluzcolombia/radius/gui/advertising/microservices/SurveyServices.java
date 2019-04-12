package org.unlitrodeluzcolombia.radius.gui.advertising.microservices;

import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.comtor.html.HtmlBr;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlHr;
import net.comtor.html.HtmlSpan;
import net.comtor.html.form.HtmlButton;
import net.comtor.html.form.HtmlInputHidden;
import net.comtor.html.form.HtmlInputText;
import net.comtor.html.form.HtmlSelect;
import net.comtor.html.form.HtmlTextArea;
import org.unlitrodeluzcolombia.radius.enums.QuestionType;
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
        response.add(new MapResponse("questions_area", getQuestionTag(questionType).getHtml()));
        return response;
    }

    public HtmlElement getQuestionTag(String questionType) {

        String optionMessage = QuestionType.OPEN_QUESTION.toString().equals(questionType) ? "Seleccione el tipo de campo para la respuesta" : "Opciones (separadas por saltos de linea| )";

        HtmlInputText inputText = new HtmlInputText("question", 32, 512);
        inputText.addAttribute("required", "required");

        HtmlDiv divForm = new HtmlDiv();
        divForm.setClass("DivFormField");
        divForm.add(new HtmlSpan("question_error"))
                .add(new HtmlSpan("sp1", "Preguntas"))
                .add(new HtmlSpan().add(inputText))
                .add(new HtmlSpan());

        HtmlElement myElement = null;

        if (QuestionType.OPEN_QUESTION.toString().equals(questionType)) {
            myElement = getSelect();
        } else {
            HtmlTextArea textArea = new HtmlTextArea("options", "", 40, 5, 128);
            textArea.addAttribute("onkeyup", "return ismaxlength(this)");
            textArea.addAttribute("required", "required");

            myElement = textArea;
        }

        HtmlDiv divTxA = new HtmlDiv();
        divTxA.setClass("DivFormField")
                .add(new HtmlSpan("option_error"))
                .add(new HtmlSpan("", optionMessage))
                .add(myElement);

        HtmlButton button = new HtmlButton(HtmlButton.SCRIPT_BUTTON, "delete_question", "Eliminar");
        button.addAttribute("class", "deleteSection");

        return new HtmlDiv().addAttribute("class", "div-answer-content")
                .add(new HtmlBr())
                .add(divForm)
                .add(divTxA)
                .add(button)
                .add(new HtmlInputHidden("question_type", questionType))
                .add(new HtmlHr());
    }

    private HtmlSelect getSelect() {
        HtmlSelect select = new HtmlSelect();
        select.addOption("text", "Libre");
        select.addOption("phone", "Teléfono");
        select.addOption("date", "Fecha");
        select.addOption("email", "Correo electronico");
        select.addOption("number", "Número");
        return select;
    }

}
