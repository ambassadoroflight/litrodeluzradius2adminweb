<%-- 
    LITRO DE LUZ RADIUS 2 ADMIN WEB
    Document   : index
    Created on : Jan 24, 2019, 09:57
    Author     : juandiego@comtor.net
--%>
<%@page import="web.global.LitroDeLuzImages"%>
<%@page import="net.comtor.framework.images.Images"%>
<%@page import="net.comtor.util.connection.ConnectionType"%>
<%@page import="net.comtor.dao.ComtorDaoException"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="net.comtor.framework.global.ComtorGlobal"%>
<%@page import="net.comtor.framework.images.Images"%>
<%@page import="net.comtor.aaa.ComtorPrivilege"%>
<%@page import="net.comtor.framework.jsptag.*"%>
<%@page import="net.comtor.aaa.ComtorUser"%>
<%@page import="web.global.GlobalWeb"%>
<%@page import="i18n.WebFrameWorkTranslationHelper"%>
<%@page contentType="text/html"  buffer="500kb" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="WEB-INF/comtortags.tld" prefix="comtor"%>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String title = GlobalWeb.PROJECT_NAME;

    switch (GlobalWeb.CONNECTION_TYPE) {
        case DEVELOPMENT:
            title += " (Desarrollo)";
            break;
        case TEST:
            title += " (Pruebas)";
            break;
    }
%>
<html>
    <head>
        <title><%=title%></title>
        <!--<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">-->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href='framework/images/favicon.ico' rel="shortcut icon" >
        <comtor:cssmain></comtor:cssmain>
            <link type="text/css" rel="stylesheet" href="framework/css/simple-grid.min.css"/>
            <link type="text/css" rel="stylesheet" href="framework/css/radius.css"/>
        <comtor:jsjquery></comtor:jsjquery>
        <comtor:jscomtorframework></comtor:jscomtorframework>
            <script src="framework/js/chart.js/Chart.bundle.min.js"></script>
        </head>

        <body>
        <comtor:ifsessionexists>
            <div id="top">
                <div id ="logo">
                    <a href="index.jsp">
                        <img src="<%=LitroDeLuzImages.LOGO_LOGIN%>" alt="<comtor:keytranslation key="html.client.logo.alt"></comtor:keytranslation>"/>
                        </a>
                    </div>
                    <div id="header">
                        <span id="title"><%=title%></span>
                </div>

                <div id="comtorlogo">
                    <img src="<%=Images.COMTOR_LOGO%>" alt="COMTOR LTDA">
                </div>    

                <div class="menu">
                    <span id="classicMenu">
                        <comtor:menuli internationalized="true" menuid="sysMenu"></comtor:menuli>
                        </span>

                    <comtor:guielement clazz="web.gui.userInfo.HtmlUserSpecificMenu"></comtor:guielement>
                    </div>

                    <div class="clear"></div>
                </div>
        </comtor:ifsessionexists>

        <div id="content">
            <comtor:content defaultpagefactory="web.gui.Index" 
                            defaultloginfactory="web.gui.login.Login" />
        </div>

        <div id="footer">
            <a href="<%=ComtorGlobal.COMTOR_WEBPAGE%>" target="_blank" title="Ir al sitio web de Comtor">
                <img src="<%=Images.IMG_LOGO_COMTOR_WORD%>" alt="Comtor Ltda" />
            </a>

            <span id="disclaimer">
                <span class="disclaimerText">COMTOR LTDA. - TODOS LOS DERECHOS RESERVADOS © <%=ComtorGlobal.CURRENT_YEAR%></span>
                <span class="disclaimerText"><a href="<%=ComtorGlobal.COMTOR_MAP_LINK%>" target="_blank" id="mapLink"><%=ComtorGlobal.COMTOR_ADDRESS%></a></span>
                <span class="disclaimerText">Tels: <%=ComtorGlobal.printPhones()%></span>
                <span class="disclaimerText"><a href="mailto:<%=ComtorGlobal.COMTOR_EMAIL%>"><%=ComtorGlobal.COMTOR_EMAIL%></a></span>
            </span>
        </div>

        <comtor:jsprj></comtor:jsprj>
        <script src="https://assets.what3words.com/sdk/v3/what3words.js?key=<%=GlobalWeb.W3W_API_KEY%>"></script>
    </body>
</html>
