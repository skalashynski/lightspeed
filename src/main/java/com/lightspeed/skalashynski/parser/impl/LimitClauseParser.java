package com.lightspeed.skalashynski.parser.impl;

import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.parser.ClauseParser;


public class LimitClauseParser implements ClauseParser {
    public boolean supports(String part) {
        return part.trim().toUpperCase().startsWith("LIMIT");
    }

    public void parse(String part, Query query) {
        query.setLimit(Integer.parseInt(part.substring(5).trim()));
    }
}