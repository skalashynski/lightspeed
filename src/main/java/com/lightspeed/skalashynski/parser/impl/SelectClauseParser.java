package com.lightspeed.skalashynski.parser.impl;


import com.lightspeed.skalashynski.model.Column;
import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.parser.ClauseParser;
import com.lightspeed.skalashynski.parser.SqlParsingEngine;

import java.util.ArrayList;


public class SelectClauseParser implements ClauseParser {

    public static final String SPLIT_BY_COMMA = "\\s*,\\s*";

    public boolean supports(String part) {
        return part.trim().toUpperCase().startsWith("SELECT");
    }

    @Override
    public void parse(String part, Query query) {
        var cols = part.substring(6).trim();
        var columnList = new ArrayList<Column>();
        for (String col : cols.split(SPLIT_BY_COMMA)) {
            if (col.matches("(?i).+\\s+AS\\s+.+")) {
                var split = col.split("(?i)\\s+AS\\s+");
                columnList.add(new Column(split[0].trim(), split[1].trim()));
            } else {
                columnList.add(new Column(col.trim(), null));
            }
        }
        query.setColumns(columnList);
    }

    public static Query parseFullQuery(String sql) {
        return SqlParsingEngine.parse(sql);
    }
}
