package com.lightspeed.skalashynski.parser.impl;


import com.lightspeed.skalashynski.model.Column;
import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.model.Sort;
import com.lightspeed.skalashynski.parser.ClauseParser;

public class OrderByClauseParser implements ClauseParser {
    public boolean supports(String part) {
        return part.startsWith("ORDER BY");
    }

    public void parse(String part, Query query) {
        var orderBySequences = part.substring(8).trim().split(",");
        for (String orderBy : orderBySequences) {
            var orderByTokens = orderBy.trim().split(" ");
            var columnName = orderByTokens[0];
            var sortType = (orderByTokens.length == 1 || orderByTokens[1].equalsIgnoreCase("ASC")) ? Sort.SortType.ASC : Sort.SortType.DESC;
            query.getSortColumns().add(new Sort(new Column(columnName, null), sortType));
        }
    }
}