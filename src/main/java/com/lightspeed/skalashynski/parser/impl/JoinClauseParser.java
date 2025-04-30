package com.lightspeed.skalashynski.parser.impl;

import com.lightspeed.skalashynski.model.Condition;
import com.lightspeed.skalashynski.model.Join;
import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.model.Table;
import com.lightspeed.skalashynski.parser.ClauseParser;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lightspeed.skalashynski.parser.impl.SelectClauseParser.parseFullQuery;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class JoinClauseParser implements ClauseParser {

    private static final Pattern JOIN_PATTERN = Pattern.compile(
            "(LEFT|RIGHT|FULL|INNER|CROSS)?\\s*JOIN\\s+(\\([^)]+\\)|\\S+)(?:\\s+(\\w+))?(?:\\s+ON\\s+(.*))?", CASE_INSENSITIVE
    );

    private record JoinToken(String type, String target, String alias, String onClause) {
    }

    @Override
    public boolean supports(String fromClause) {
        return !tokenizeJoins(fromClause).isEmpty();
    }

    public void parse(String fromClause, Query query) {
        List<String> tokens = tokenizeJoins(fromClause);
        if (tokens.isEmpty()) return;

        String base = tokens.removeFirst();
        FromClauseParser.parseBaseTables(base, query);
        Table currentSource = getLastFromSource(query);

        for (String joinToken : tokens) {
            var token = parseJoinToken(joinToken);
            if (token == null) {
                throw new IllegalArgumentException("Invalid JOIN token: " + joinToken);
            }
            Table target = parseJoinTarget(token.target(), token.alias());
            Condition onCondition = parseJoinCondition(token.onClause());
            Join.JoinType type = parseJoinType(token.type());
            query.getJoins().add(new Join(type, currentSource, target, onCondition));
            currentSource = target;
        }
    }

    private JoinToken parseJoinToken(String joinToken) {
        Matcher matcher = JOIN_PATTERN.matcher(joinToken);
        if (!matcher.matches()) return null;

        return new JoinToken(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
    }

    private Join.JoinType parseJoinType(String typeStr) {
        return (typeStr == null) ? Join.JoinType.INNER : Join.JoinType.valueOf(typeStr.toUpperCase());
    }

    private Table parseJoinTarget(String targetExpr, String alias) {
        if (StringUtils.startsWithIgnoreCase(targetExpr.trim(), "(SELECT")) {
            String subquerySql = unwrapParentheses(targetExpr);
            return new Table(null, alias, parseFullQuery(subquerySql));
        } else {
            return new Table(targetExpr.trim(), alias, null);
        }
    }

    private Condition parseJoinCondition(String onExpr) {
        if (onExpr == null) return null;
        return new Condition(unwrapParentheses(onExpr));
    }

    private String unwrapParentheses(String text) {
        text = text.trim();
        if (text.startsWith("(") && text.endsWith(")")) {
            return text.substring(1, text.length() - 1).trim();
        }
        return text;
    }

    private Table getLastFromSource(Query query) {
        return query.getFromSources().getLast();
    }

    private List<String> tokenizeJoins(String fromClause) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = JOIN_PATTERN.matcher(fromClause);
        int lastIndex = 0;
        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                tokens.add(fromClause.substring(lastIndex, matcher.start()).trim());
            }
            tokens.add(fromClause.substring(matcher.start(), matcher.end()).trim());
            lastIndex = matcher.end();
        }
        if (lastIndex < fromClause.length()) {
            tokens.add(fromClause.substring(lastIndex).trim());
        }
        return tokens;
    }
}
