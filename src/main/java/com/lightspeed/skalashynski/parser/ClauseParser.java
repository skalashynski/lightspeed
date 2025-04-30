package com.lightspeed.skalashynski.parser;


import com.lightspeed.skalashynski.model.Query;

public interface ClauseParser {
    boolean supports(String part);

    void parse(String part, Query query);
}