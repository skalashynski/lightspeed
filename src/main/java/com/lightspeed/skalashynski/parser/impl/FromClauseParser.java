package com.lightspeed.skalashynski.parser.impl;


import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.model.Table;
import com.lightspeed.skalashynski.parser.ClauseParser;

import java.util.stream.Stream;

import static com.lightspeed.skalashynski.parser.impl.SelectClauseParser.SPLIT_BY_COMMA;

public class FromClauseParser implements ClauseParser {


    public static final String SPLIT_BY_SPACE = "\\s+";

    private static final JoinClauseParser JOIN_CLAUSE_PARSER = new JoinClauseParser();
    private static final SubSelectClauseParser SUB_SELECT_CLAUSE_PARSER = new SubSelectClauseParser();

    public boolean supports(String part) {
        return part.trim().toUpperCase().startsWith("FROM");
    }

    public void parse(String part, Query query) {
        var fromPart = part.substring(4).trim();
        if (SUB_SELECT_CLAUSE_PARSER.supports(fromPart)) {
            SUB_SELECT_CLAUSE_PARSER.parse(fromPart, query);
            return;
        }

        if (JOIN_CLAUSE_PARSER.supports(fromPart)) {
            JOIN_CLAUSE_PARSER.parse(fromPart, query);
        }
    }

    public static void parseBaseTables(String base, Query query) {
        for (String tbl : base.split(SPLIT_BY_COMMA)) {
            var parts = tbl.trim().split(SPLIT_BY_SPACE);
            var tableName = parts[0];
            var alias = parseAlias(parts);
            var table = new Table(tableName, alias, null);
            query.getFromSources().add(table);
        }
    }

    private static String parseAlias(String[] tokens) {
        if (tokens.length > 1) {
            /*
             * To avoid parsing 'OFFSET' as alias in the queries like: 'SELECT id FROM t OFFSET 10 LIMIT 5"
             * */
            return Stream.of("OFFSET", "LIMIT").noneMatch(e -> e.equalsIgnoreCase(tokens[1])) ? tokens[1] : null;
        }
        return null;
    }

}
