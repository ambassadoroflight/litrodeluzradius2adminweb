function calculateTotalTime(diffInSeconds) {
    if ((diffInSeconds >= 60) && (diffInSeconds < 3600)) {
        var minutes = (diffInSeconds / 60).toFixed(0);
        var seconds = (diffInSeconds % 60).toFixed(2);

        return  (getPlural(minutes, "minuto") + ", "
                + getPlural(seconds, "segundo"));
    } else if ((diffInSeconds >= 3600) && (diffInSeconds < 86400)) {
        var hours = (diffInSeconds / 3600).toFixed(0);
        var minutes = ((diffInSeconds % 3600) / 60).toFixed(0);

        return  (getPlural(hours, "hora")) + ", "
                + (getPlural(minutes, "minuto"));
    } else {
        var days = (diffInSeconds / 86400).toFixed(0);
        var hours = ((diffInSeconds % 86400) / 60).toFixed(0);

        return (getPlural(days, "día") + ", " + (getPlural(hours, "hora")));
    }
}

function getPlural(qty, singular) {
    return qty + " " + singular + ((qty == 1) ? "" : "s");
}

function showPinTime(obj) {
    $("#duration").text(calculateTotalTime(obj.value));
}

function addQuestionInput() {
    $("#questions_area").append("<div class='DivFormField' id='divformrow_question'><span id='question_error'></span><span>Preguntas</span><span><input type='text' name='question' value='' id='question' size='32' maxlength='512' required></span><span></span></div><div class='DivFormField' id='divformrow_options'><span id='options_error'></span><span>Opciones (separadas por barra vertical | )</span><span><textarea name='options' id='options' cols='40' rows='5' maxlength='128' onkeyup='return ismaxlength(this)' required></textarea></span><span></span></div>");
}

function addMultiInput(id) {
    $(id).prepend('<input type=\"text\" name=\"ip_address\" value="" style=\"margin: 5px 0;\"><br>');
}

function removeMultiInput(id) {
    $(id + " br:first").remove();
    $(id + " input:first").remove();
}

function getEndpoint(endpoint, action_type) {
    $.getJSON(endpoint, function (j) {
        for (var i = 0; i < j.length; i++) {

            $("#" + j[i].label).css("display", "none");
            if (action_type !== null || action_type === "append") {
                $("#" + j[i].label).append(j[i].value);
            } else {
                $("#" + j[i].label).html(j[i].value);
            }

            $("#" + j[i].label).fadeIn();

        }
    }).done(function (data) {

    }).fail(function (jqXHR) {

    });
}

function getHotspots() {
    var sponsor = $("select#sponsor").val();
    var url = "/litrodeluz/radius/webservices/hotspot_services/map/get_hotspots?sponsor=" + sponsor;
    
    addAjaxMarkerList(map_tracker, coordinates_tracker, markers_tracker, null, url);
}

$(document).ready(function () {
    var secs = $("#duration_in_seconds").val();
    showPinTime(parseInt(secs, 10));

    //Se adjuntan listeners al dom para eventos por ajax
    $(document).on("click", '.ajaxGet', function (event) {
        event.preventDefault();
        var endpoint = $(this).attr("endpoint");
        var action_type = $(this).attr("action_type");
        getEndpoint(endpoint, action_type);
    });

    $(document).on("click", '.deleteSection', function (event) {
        event.preventDefault();
        $(this).parent().remove();
    });



});


