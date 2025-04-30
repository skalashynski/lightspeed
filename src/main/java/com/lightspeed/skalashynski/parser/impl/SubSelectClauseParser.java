package com.lightspeed.skalashynski.parser.impl;

import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.model.Table;
import com.lightspeed.skalashynski.parser.ClauseParser;

import java.util.regex.Pattern;

import static com.lightspeed.skalashynski.parser.impl.SelectClauseParser.parseFullQuery;

public class SubSelectClauseParser implements ClauseParser {
    public static final Pattern SUB_SELECT_PATTERN = Pattern.compile("^\\((SELECT.+)\\)\\s+(\\w+)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    @Override
    public boolean supports(String part) {
        return SUB_SELECT_PATTERN.matcher(part).matches();
    }

    @Override
    public void parse(String part, Query query) {
        var subSelectMatcher = SUB_SELECT_PATTERN.matcher(part);
        if (subSelectMatcher.matches()) {
            var subquerySql = subSelectMatcher.group(1);
            var alias = subSelectMatcher.group(2);
            var subquery = parseFullQuery(subquerySql);
            var subqueryTable = new Table(null, alias, subquery);
            query.getFromSources().add(subqueryTable);
        }
    }
}
