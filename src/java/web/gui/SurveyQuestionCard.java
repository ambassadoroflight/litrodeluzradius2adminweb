package web.gui;

import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 24, 2019
 */
public class SurveyQuestionCard extends HtmlDiv {

    private int questionNumber;
    private String title;
    private HtmlElement content;

 public SurveyQuestionCard(int questionNumber, String title, HtmlElement content) {
        super("question_box_" + questionNumber, "card my-3");

        this.questionNumber = questionNumber;
        this.title = title;
        this.content = content;
    }

    private void init() {

        HtmlDiv card_header = new HtmlDiv(null, "card-header");
        card_header.addString("Pregunta #" + questionNumber + ": " + title);

        add(card_header);

        HtmlDiv card_body = new HtmlDiv(null, "card-body");
        card_body.addElement(content);

        add(card_body);
    }

    @Override
    public String getHtml() {
        init();
        return super.getHtml();
    }
}
