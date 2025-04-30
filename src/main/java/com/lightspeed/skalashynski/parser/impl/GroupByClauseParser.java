package com.lightspeed.skalashynski.parser.impl;


import com.lightspeed.skalashynski.model.Column;
import com.lightspeed.skalashynski.model.ConditionNode;
import com.lightspeed.skalashynski.model.GroupBy;
import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.parser.ClauseParser;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class GroupByClauseParser implements ClauseParser {
    private static final Pattern HAVING_PATTERN = Pattern.compile("HAVING", Pattern.CASE_INSENSITIVE);

    public boolean supports(String part) {
        return part.startsWith("GROUP BY");
    }

    public void parse(String part, Query query) {
        var segments = HAVING_PATTERN.split(part, 2);
        var groupColumns = parseGroupBy(segments);
        var having = parseHaving(segments);
        query.setGroupBy(new GroupBy(groupColumns, having));
    }

    private static List<Column> parseGroupBy(String[] segments) {
        return Arrays.stream(segments[0].substring(8).trim().split(","))
                .map(e -> new Column(e.trim(), null)).toList();
    }


    private static GroupBy.Having parseHaving(String[] segments) {
        GroupBy.Having having = null;
        if (segments.length > 1) {
            var havingExpr = segments[1].trim()
                    .replaceAll("^\\(+", "")
                    .replaceAll("\\)+$", "");
            var rootNode = new WhereClauseParser().parseCondition(havingExpr);
            having = new GroupBy.Having(rootNode);
        }
        return having;
    }
}