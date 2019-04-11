package org.unlitrodeluzcolombia.radius.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.radius.element.Seller;
import org.unlitrodeluzcolombia.radius.web.facade.SellerWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class SellerFinder extends AbstractComtorFinderFactoryI18n<Seller, String> {

    @Override
    protected String getValueToHide(Seller seller) {
        return seller.getLogin();
    }

    @Override
    protected String getValueToShow(Seller seller) {
        return "[" + seller.getLogin() + "] " + seller.getName();
    }

    @Override
    public WebLogicFacade<Seller, String> getFacade() {
        return new SellerWebFacade();
    }

    @Override
    protected LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("login", "Usuario");
        headers.put("name", "Nombre");
        headers.put("kiosk", "Kiosco");

        return headers;
    }

    @Override
    protected LinkedList<Object> getRow(Seller seller) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(seller.getLogin());
        row.add(seller.getName());
        row.add("[" + seller.getKiosk() + "] " + seller.getKiosk_name());
        
        return row;
    }

}
