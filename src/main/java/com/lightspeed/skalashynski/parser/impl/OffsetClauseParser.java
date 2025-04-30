package com.lightspeed.skalashynski.parser.impl;


import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.parser.ClauseParser;

public class OffsetClauseParser implements ClauseParser {
    public boolean supports(String part) {
        return part.trim().toUpperCase().startsWith("OFFSET");
    }

    public void parse(String part, Query query) {
        query.setOffset(Integer.parseInt(part.substring(6).trim()));
    }
}
