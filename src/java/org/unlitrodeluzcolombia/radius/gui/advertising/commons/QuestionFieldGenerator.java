package org.unlitrodeluzcolombia.radius.gui.advertising.commons;

import net.comtor.html.HtmlBr;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlHr;
import net.comtor.html.HtmlSpan;
import net.comtor.html.form.HtmlButton;
import net.comtor.html.form.HtmlInputHidden;
import net.comtor.html.form.HtmlInputText;
import net.comtor.html.form.HtmlOption;
import net.comtor.html.form.HtmlSelect;
import net.comtor.html.form.HtmlTextArea;
import org.unlitrodeluzcolombia.radius.enums.QuestionType;

/**
 *
 * @author Guido Cafiel
 */
public class QuestionFieldGenerator {

    public HtmlElement getQuestionTag(String questionType) {
        return getQuestionTag(questionType, null, null);
    }

    public HtmlElement getQuestionTag(String questionType, String question, String options) {

        String optionMessage = QuestionType.OPEN_QUESTION.toString().equals(questionType) 
                ? "Seleccione el tipo de campo para la respuesta" 
                : "Opciones (separadas por saltos de linea)";

        HtmlInputText inputText = new HtmlInputText("question", 32, 512);
        inputText.addAttribute("required", "required");

        if (question != null) {
            inputText.setValue(question);
        }

        HtmlDiv divForm = new HtmlDiv();
        divForm.setClass("DivFormField");
        divForm.add(new HtmlSpan("question_error"))
                .add(new HtmlSpan("sp1", "Enunciado"))
                .add(new HtmlSpan().add(inputText))
                .add(new HtmlSpan());

        HtmlElement myElement = null;

        if (QuestionType.OPEN_QUESTION.toString().equals(questionType)) {
            myElement = getSelect(options);
        } else {
            HtmlTextArea textArea = new HtmlTextArea("options", "", 40, 5, 128);
            textArea.addAttribute("onkeyup", "return ismaxlength(this)");
            textArea.addAttribute("required", "required");

            if (options != null) {
                textArea.setValue(options);
            }

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
                .add(new HtmlInputHidden("type", questionType))
                .add(new HtmlHr());
    }

    private HtmlSelect getSelect(String options) {
        HtmlSelect select = new HtmlSelect();
        select.setName("options");
        HtmlOption option = new HtmlOption("text", "Libre");
        if (options != null && "text".equals(options)) {
            option.selected();
        }
        select.add(option);

        option = new HtmlOption("phone", "Teléfono");
        if (options != null && "phone".equals(options)) {
            option.selected();
        }
        select.add(option);

        option = new HtmlOption("date", "Fecha");
        if (options != null && "date".equals(options)) {
            option.selected();
        }
        select.add(option);

        option = new HtmlOption("email", "Correo electronico");
        if (options != null && "email".equals(options)) {
            option.selected();
        }
        select.add(option);

        option = new HtmlOption("number", "Número");
        if (options != null && "number".equals(options)) {
            option.selected();
        }
        select.add(option);

        return select;
    }

}
