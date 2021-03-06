package org.unlitrodeluzcolombia.radius.gui.advertising;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.ActionIcon;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.advanced.html.form.HtmlRadioGroup;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.html.administrable.ComtorAdministrableHtmlException;
import net.comtor.framework.html.administrable.ComtorAdministratorControllerHelperI18n;
import net.comtor.framework.html.administrable.ComtorMessageHelperI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.framework.request.validator.RequestBasicValidator;
import net.comtor.framework.util.security.SecurityHelper;
import net.comtor.html.HtmlBr;
import net.comtor.html.HtmlContainer;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlHr;
import net.comtor.html.HtmlTable;
import net.comtor.html.HtmlTd;
import net.comtor.html.HtmlText;
import net.comtor.html.HtmlTr;
import net.comtor.html.form.HtmlButton;
import net.comtor.html.form.HtmlCheckbox;
import net.comtor.html.form.HtmlInput;
import net.comtor.html.form.HtmlInputText;
import net.comtor.html.form.HtmlRadio;
import net.comtor.html.form.HtmlTextArea;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.i18n.html.DivFormI18n;
import net.comtor.radius.element.Campaign;
import org.ocelotframework.charts.ChartBar;
import org.ocelotframework.charts.ChartData;
import org.ocelotframework.charts.ChartDataset;
import org.ocelotframework.charts.ChartPie;
import org.ocelotframework.charts.ChartPieDataset;
import org.unlitrodeluzcolombia.radius.element.Answer;
import org.unlitrodeluzcolombia.radius.element.Question;
import org.unlitrodeluzcolombia.radius.element.Survey;
import org.unlitrodeluzcolombia.radius.enums.QuestionType;
import org.unlitrodeluzcolombia.radius.facade.AnswerDAOFacade;
import org.unlitrodeluzcolombia.radius.facade.QuestionDAOFacade;
import org.unlitrodeluzcolombia.radius.facade.SurveyDAOFacade;
import org.unlitrodeluzcolombia.radius.gui.advertising.commons.QuestionFieldGenerator;
import org.unlitrodeluzcolombia.radius.gui.finder.CampaignFinder;
import org.unlitrodeluzcolombia.radius.web.facade.CampaignWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.SurveyWebFacade;
import web.global.LitroDeLuzImages;
import web.gui.SurveyQuestionCard;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class SurveyController extends AbstractComtorFacadeAdministratorControllerI18n<Survey, Long> {

    private static final Logger LOG = Logger.getLogger(SurveyController.class.getName());

    @Override
    public String getEntityName() {
        return "Encuestas";
    }

    @Override
    public String getLogModule() {
        return "Encuestas";
    }

    @Override
    public WebLogicFacade<Survey, Long> getLogicFacade() {
        return new SurveyWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, Survey survey) throws BusinessLogicException {
        if (survey != null) {
            long surveyId = survey.getId();
            form.addInputHidden("id", surveyId);
        }

        HtmlTextArea description = new HtmlTextArea("description", 40, 5, 128);
        description.addAttribute("required", "required");
        form.addField("Descripci?n", description, null);

        HtmlFinder campaign = getCampaignFinder(survey);
        form.addField("Campa?a Publicitaria", campaign, "Indique a cu?l campa?a"
                + " publicitaria pertenece esta encuesta.", true);

        form.addSubTitle("Agregar Preguntas");

        HtmlDiv questions_area = new HtmlDiv("questions_area");

        if (survey != null) {
            List<Question> questions = new LinkedList<>();

            try {
                questions = new QuestionDAOFacade().findAllByProperty("survey", survey.getId());
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }

            QuestionFieldGenerator questionFieldGenerator = new QuestionFieldGenerator();
            questions.forEach((question) -> {
                questions_area.add(questionFieldGenerator.getQuestionTag(question.getType(),
                        question.getQuestion(), question.getOptions()));
            });
        }

        form.addRowInOneCell(questions_area);

        HtmlContainer container = new HtmlContainer();
        container.add(getAddQuestionButton("Agregar pregunta abierta",
                QuestionType.OPEN_QUESTION.toString(), "#FFFFFF"))
                .add(getAddQuestionButton("Agregar de selecci?n simple",
                                QuestionType.SINGLE.toString(), "#FFFFFF"))
                .add(getAddQuestionButton("Agregar de selecci?n m?ltiple",
                                QuestionType.MULTIPLE.toString(), "#FFFFFF"))
                .add(new HtmlHr());
        form.addRowInOneCell(container);

    }

    @Override
    public void initFormView(AdministrableForm form, Survey survey) {
        try {
            HtmlText field = new HtmlText(survey.getDescription());
            form.addField("Descripci?n", field, null);

            field = new HtmlText(survey.getCampaign_description());
            form.addField("Campa?a Publicitaria", field, null);

            form.addSubTitle("Preguntas");

            LinkedList<Question> questions = new QuestionDAOFacade()
                    .findAllByProperty("survey", survey.getId());

            if (!questions.isEmpty()) {
                int i = 1;

                for (Question question : questions) {
                    field = new HtmlText(question.getQuestion());
                    form.addField("Pregunta #" + i, field, null);

                    form.addField("", getOptionField(question), null);

                    i++;
                }
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    @Override
    public String getAddPrivilege() {
        return "ADD_SPONSOR";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_SPONSOR";
    }

    @Override
    public String getViewPrivilege() {
        return "VIEW_SPONSOR";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_SPONSOR";
    }

    @Override
    public String getFormName() {
        return "survey_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("description", "Descripci?n");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Survey survey) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(survey.getDescription());

        return row;
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.ADVERTISING_CONTROLLER;
    }

    @Override
    public String getAddFormLabel() {
        return "Nueva Encuesta";
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Crear Encuesta";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar Encuesta";
    }

    @Override
    public String getConfirmDeleteMessage(Survey survey) {
        return "?Est? seguro que desea eliminar la encuesta <b>[" + survey.getId()
                + "] " + survey.getDescription() + "</b>?";
    }

    @Override
    public String getAddedMessage(Survey survey) {
        return "La encuesta <b>[" + survey.getId() + "] " + survey.getDescription()
                + "</b> ha sido creada.";
    }

    @Override
    public String getDeletedMessage(Survey survey) {
        return "La encuesta <b>[" + survey.getId() + "] " + survey.getDescription()
                + "</b> ha sido eliminada.";
    }

    @Override
    public String getUpdatedMessage(Survey survey) {
        return "La encuesta <b>[" + survey.getId() + "] " + survey.getDescription()
                + "</b> ha sido actualizada.";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este m?dulo.";
    }

    @Override
    protected LinkedList<String> getBasicActionLinks(Survey survey) {
        LinkedList<String> actions = new LinkedList<>();

        if (isEditable(survey) && (getEditPrivilege() != null)
                && (SecurityHelper.can(getEditPrivilege(), getRequest()))) {
            actions.add(getEditIcon(getBaseUrl(), survey));
        }

        if ((getDeletePrivilege() != null)
                && (SecurityHelper.can(getDeletePrivilege(), getRequest()))) {
            actions.add(getDeleteIcon(getBaseUrl(), survey));
        }

        if ((getViewPrivilege() != null)
                && (SecurityHelper.can(getViewPrivilege(), getRequest()))) {
            actions.add(getViewIcon(getBaseUrl(), survey));
        }

        if ((getViewPrivilege() != null)
                && (SecurityHelper.can(getViewPrivilege(), getRequest())) && hasAnswers(survey)) {
            actions.add(getAnswersIcon(survey).getHtml());
        }

        return actions;
    }

    @Override
    public String getActionOther(String action, HttpServletMixedRequest request, HttpServletResponse response) {
        if (action.equals("surveyanswers")) {
            try {
                return getSurveyAnswersForm(request, response).getHtml();
            } catch (ComtorAdministrableHtmlException | BusinessLogicException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        return super.getActionOther(action, request, response);
    }

    public HtmlElement getSurveyAnswersForm(HttpServletMixedRequest request,
            HttpServletResponse response) throws ComtorAdministrableHtmlException, BusinessLogicException {
        long key = RequestBasicValidator.getLongFromRequest(request, "key");

        Survey survey = getLogicFacade().find(key);

        if (!hasAnswers(survey)) {
            return ComtorAdministratorControllerHelperI18n.continueFormMessageInfo(getBaseUrl(),
                    "No hay respuestas para mostrar de esta encuesta.", request);
        }

        Campaign campaign = new CampaignWebFacade().find(survey.getCampaign());

        AdministrableForm form = new DivFormI18n(getBaseUrl(), AdministrableForm.METHOD_POST,
                getRequest());
        form.setTitle("Respuestas de la Encuesta " + survey.getId());
        form.setFormName(getFormName());

        HtmlText field;
        field = new HtmlText(campaign.getSponsor_name());
        form.addField("Patrocinador", field, null);

        field = new HtmlText(campaign.getDescription());
        form.addField("Campa?a", field, null);

        field = new HtmlText(survey.getDescription());
        form.addField("Descripci?n Encuesta", field, null);

        try {
            LinkedList<Question> questions = new QuestionDAOFacade()
                    .findAllByProperty("survey", survey.getId());
            AnswerDAOFacade answerFacade = new AnswerDAOFacade();
            LinkedList<Answer> answers;
            int i = 1;

            for (Question question : questions) {
                answers = answerFacade.findAllByProperty("question", question.getId());

                HtmlElement content = null;

                if (question.getType().equals(QuestionType.OPEN_QUESTION.toString())) {
                    content = listOpenQuestionAnswers(answers);
                } else if (question.getType().equals(QuestionType.SINGLE.toString())) {
                    content = getPieChart(answers, question.getId());
                } else if (question.getType().equals(QuestionType.MULTIPLE.toString())) {
                    content = getBarChart(answers, question.getId());
                }

                SurveyQuestionCard box = new SurveyQuestionCard(i, question.getQuestion(), content);

                form.addRowInOneCell(box);

                i++;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return ComtorMessageHelperI18n.getErrorForm(getAddFormLabel(),
                    getBaseUrl(), ex, true, request);
        }

        form.addButton("cancel", "Regresar");

        return form;
    }

    private HtmlButton getAddQuestionButton(String title, String keyword, String color) {
        HtmlButton addQuestionButton = new HtmlButton(HtmlButton.SCRIPT_BUTTON, "add_question", title);
        addQuestionButton.addAttribute("class", "ajaxGet");
        addQuestionButton.addAttribute("title", title);
        addQuestionButton.addAttribute("style", "color:" + color);
        addQuestionButton.addAttribute("endpoint", "webservices/survey_service/question_tag/" + keyword);
        addQuestionButton.addAttribute("action_type", "append");

        return addQuestionButton;
    }

    private HtmlElement getOptionField(Question question) {
        if (question.getType().equals(QuestionType.OPEN_QUESTION.toString())) {

            switch (question.getOptions()) {
                case "text":
                    HtmlInputText inputText = new HtmlInputText("options");
                    inputText.setReadOnly(false);
                    return inputText;
                case "phone":
                    HtmlInput inputPhone = new HtmlInput("phone", "options", "");
                    inputPhone.setReadOnly(false);
                    return inputPhone;
                case "date":
                    HtmlInput inputDate = new HtmlInput("date", "options", "");
                    inputDate.setReadOnly(false);
                    inputDate.addAttribute("pattern", "[0-9]{4}-[0-9]{2}-[0-9]{2}");
                    return inputDate;
                case "email":
                    HtmlInput inputEmail = new HtmlInput("email", "options", "");
                    inputEmail.setReadOnly(false);
                    return inputEmail;
                case "number":
                    HtmlInput inputNumber = new HtmlInput("number", "options", "");
                    inputNumber.setReadOnly(false);
                    return inputNumber;
            }
        }

        if (question.getType().equals(QuestionType.SINGLE.toString())) {
            HtmlRadioGroup options = new HtmlRadioGroup();

            for (String option : question.getOptionsArray()) {
                options.addElement(new HtmlRadio("options", option, option));
                options.addElement(new HtmlBr());
            }

            return options;
        }

        HtmlContainer container = new HtmlContainer();

        for (String option : question.getOptionsArray()) {
            container.addElement(new HtmlCheckbox("options", option, option));
            container.addElement(new HtmlBr());
        }

        return container;
    }

    private HtmlFinder getCampaignFinder(Survey zone) {
        Campaign campaign = null;
        String valueToShow = "";

        try {
            campaign = ((zone == null)
                    ? null
                    : new CampaignWebFacade().find(zone.getCampaign()));
            valueToShow = ((campaign == null)
                    ? ""
                    : new CampaignFinder().getValueToShow(campaign));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return new HtmlFinder("campaign", CampaignFinder.class, valueToShow, 32);
    }

    private boolean isEditable(Survey survey) {
        return !hasAnswers(survey);
    }

    private boolean hasAnswers(Survey survey) {
        try {
            return new SurveyDAOFacade().hasAnswers(survey.getId());
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return false;
    }

    private ActionIcon getAnswersIcon(Survey survey) {
        return new ActionIcon(getBaseUrl() + "&amp;action=surveyanswers&amp;key="
                + getKey(survey), LitroDeLuzImages.SURVEY_ACTION, "Ver respuestas");
    }

    private HtmlTable listOpenQuestionAnswers(final LinkedList<Answer> answers) {
        HtmlTable table = new HtmlTable();
        HtmlTr row;
        int i = 1;

        for (Answer answer : answers) {
            row = new HtmlTr();
            row.addElement(new HtmlTd((i++) + ""));
            row.addElement(new HtmlTd(answer.getResponse()));

            table.addElement(row);
        }

        return table;
    }

    private HtmlDiv getPieChart(final LinkedList<Answer> answers,
            final long questionID) {
        Map<String, Integer> map = new HashMap<>();

        for (Answer answer : answers) {
            String key = answer.getResponse();

            if (map.containsKey(key)) {
                int value = map.get(key);
                ++value;

                map.put(key, value);
            } else {
                map.put(key, 1);
            }

        }

        LinkedList<ChartPieDataset> datasets = new LinkedList<>();
        ChartPieDataset dataset;
        int color = 1;

        for (Map.Entry<String, Integer> entrySet : map.entrySet()) {
            dataset = new ChartPieDataset(entrySet.getValue(), entrySet.getKey(), color++);
            datasets.add(dataset);

            if (color > 14) {
                color = 1;
            }
        }

        ChartPie pieChart = new ChartPie("canvas_question_" + questionID, datasets);
        pieChart.setDimentions(800, 400);

        HtmlDiv container = new HtmlDiv();
        container.addElement(pieChart.getHtml());

        return container;
    }

    private HtmlDiv getBarChart(final LinkedList<Answer> answers,
            final long questionID) {
        Map<String, Integer> map = new HashMap<>();

        for (Answer answer : answers) {
            String[] keys = answer.getResponse().split("#_#");

            for (String key : keys) {
                if (map.containsKey(key)) {
                    int value = map.get(key);
                    ++value;

                    map.put(key, value);
                } else {
                    map.put(key, 1);
                }
            }

        }

        LinkedList<ChartDataset> datasets = new LinkedList<>();
        ChartDataset dataset;
        int color = 1;

        for (Map.Entry<String, Integer> entrySet : map.entrySet()) {
            dataset = new ChartDataset(entrySet.getKey(), color, entrySet.getValue());
            datasets.add(dataset);
            ++color;

            if (color > 14) {
                color = 1;
            }
        }

        ChartData data = new ChartData(null, datasets);

        ChartBar barChart = new ChartBar("canvas_question_" + questionID, data);
        barChart.setDimentions(800, 400);

        HtmlDiv container = new HtmlDiv();
        container.addElement(barChart.getHtml());

        return container;
    }
}
