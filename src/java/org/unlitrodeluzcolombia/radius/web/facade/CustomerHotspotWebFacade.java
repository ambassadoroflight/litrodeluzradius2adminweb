/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.StringTokenizer;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;
import net.comtor.radius.element.CustomerHotspot;
import net.comtor.radius.facade.CustomerHotspotDAOFacade;
//TODO: REVISAR
/**
 *
 * @author ericson
 */
public class CustomerHotspotWebFacade extends AbstractWebLogicFacade<CustomerHotspot, Long, CustomerHotspotDAOFacade> {

    @Override
    public String getWhere(ComtorObjectCriterions criterions, LinkedList<Object> params) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1");
        LinkedList<ComtorObjectListFilter> filters = criterions.getFilters();

        for (ComtorObjectListFilter filter : filters) {
            String id = filter.getId();
            Object value = filter.getValue();
            if (id.equals(ComtorFilterHelper.FILTER_NAME) && value != null) {
                StringTokenizer st = new StringTokenizer(value.toString());
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().replaceAll("'", " ");
                    String likeToken = "%" + token + "%";

                    where.append("").
                            append("    AND (   \n").
                            append("        customer_hotspot.hotspot   LIKE    ? OR  \n").
                            append("        customer_hotspot.login   LIKE    ? OR  \n").
                            append("        customer_hotspot.creation_date   LIKE    ? OR  \n").
                            append("        customer_hotspot.created_by   LIKE    ? OR  \n").
                            append("        customer_hotspot.active   LIKE    ?   \n").
                            append("    )   \n");

                    params.add(likeToken);
                    params.add(likeToken);
                    params.add(likeToken);
                    params.add(likeToken);
                    params.add(likeToken);
                }
            }
            if (id.equals(ComtorFilterHelper.PARAMETER_NAME) && value != null && !value.toString().trim().isEmpty()) {
                where.append(" AND customer_hotspot.hotspot = ?");
                params.add(value.toString());
            }
        }

        return where.toString();
    }

}
