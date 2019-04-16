package org.unlitrodeluzcolombia.radius.gui;

import net.comtor.framework.jsptag.HtmlGuiInterface;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlSpan;

/**
 *
 * @author Guido Cafiel
 */
public class SurveyDetailRow extends HtmlGuiInterface {

    @Override
    public String getHtml() {

        HtmlDiv div = new HtmlDiv("divformrow_question", "DivFormField");
        div.addElement(new HtmlSpan("id", "HOLA " + getRequest().getParameter("name")));
        return div.getHtml();
    }

    @Override
    public boolean requireComtorSession() {
        return true;
    }

}
