package web.gui;

import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlSpan;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 24, 2019
 */
public class SurveyQuestionBox extends HtmlDiv {

    private int questionNumber;
    private String title;
    private HtmlElement content;

    public SurveyQuestionBox(int questionNumber, String title, HtmlElement content) {
        super("question_box_" + questionNumber, "row question_box mx-auto my-3");

        this.questionNumber = questionNumber;
        this.title = title;
        this.content = content;
    }

    private void init() {

        HtmlDiv titlePanel = new HtmlDiv(null, "m-0 col-md-4 question_box_title_panel");
        titlePanel.addElement(new HtmlSpan(null, "Pregunta # " + questionNumber));
        titlePanel.addElement(new HtmlSpan(null, title));

        add(titlePanel);

        HtmlDiv contentPanel = new HtmlDiv(null, "m-0 col-md-8 question_box_content_panel");
        contentPanel.addElement(content);

        add(contentPanel);
    }

    @Override
    public String getHtml() {
        init();
        return super.getHtml();
    }

}
