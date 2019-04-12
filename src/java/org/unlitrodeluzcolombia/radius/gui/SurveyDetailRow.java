package org.unlitrodeluzcolombia.radius.gui;

import net.comtor.advanced.html.HtmlAccordionJQuery;
import net.comtor.framework.jsptag.HtmlGuiInterface;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlSpan;

/**
 *
 * @author Guido Cafiel
 */
public class SurveyDetailRow extends HtmlGuiInterface{

    @Override
    public String getHtml() {
        
        HtmlDiv div = new HtmlDiv("divformrow_question", "DivFormField");
        div.addElement(new HtmlSpan("id", "HOLA "+getRequest().getParameter("name")));
        return div.getHtml();
        //<div class='' id='divformrow_question'><span id='question_error'></span><span>Preguntas</span><span><input type='text' name='question' value='' id='question' size='32' maxlength='512' required></span><span></span></div><div class='DivFormField' id='divformrow_options'><span id='options_error'></span><span>Opciones (separadas por barra vertical | )</span><span><textarea name='options' id='options' cols='40' rows='5' maxlength='128' onkeyup='return ismaxlength(this)' required></textarea></span><span></span></div>
    }

    @Override
    public boolean requireComtorSession() {
       return true;
    }
    
}
