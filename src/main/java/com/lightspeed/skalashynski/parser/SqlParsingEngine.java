package com.lightspeed.skalashynski.parser;

import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.parser.impl.*;

import java.util.ArrayList;
import java.util.List;

public class SqlParsingEngine {

    private static final List<ClauseParser> clauseParsers = List.of(
            new SelectClauseParser(),
            new FromClauseParser(),
            new WhereClauseParser(),
            new GroupByClauseParser(),
            new OrderByClauseParser(),
            new LimitClauseParser(),
            new OffsetClauseParser()
    );

    public static boolean isValidSql(String sql) {
        if (sql == null || sql.isBlank()) return false;

        String normalized = sql.trim().toUpperCase();

        if (!normalized.startsWith("SELECT") || !normalized.contains("FROM")) {
            return false;
        }

        int parenDepth = 0;
        for (char c : sql.toCharArray()) {
            if (c == '(') parenDepth++;
            if (c == ')') parenDepth--;
            if (parenDepth < 0) return false;
        }

        return parenDepth == 0;
    }


    public static Query parse(String sql) {
        if (!isValidSql(sql)) {
            throw new IllegalArgumentException("Invalid SQL syntax: " + sql);
        }

        sql = normalize(sql);
        List<String> clauses = splitClauses(sql);
        List<ParserBinding> bindings = bindParsers(clauses);
        Query query = new Query();

        for (ParserBinding binding : bindings) {
            binding.parser().parse(binding.clause(), query);
        }

        return query;
    }

    private static String normalize(String sql) {
        return sql.trim().replaceAll("\\s+", " ");
    }

    private static List<String> splitClauses(String sql) {
        var reservedKeys = List.of("SELECT", "FROM", "WHERE", "GROUP", "ORDER", "LIMIT", "OFFSET");
        List<String> clauses = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenDepth = 0;
        String[] tokens = sql.split(" ");

        for (String token : tokens) {
            if (reservedKeys.stream().anyMatch(e -> e.equalsIgnoreCase(token)) && parenDepth == 0 && !current.isEmpty()) {
                clauses.add(current.toString().trim());
                current.setLength(0);
            }
            current.append(token).append(" ");
            for (char c : token.toCharArray()) {
                if (c == '(') parenDepth++;
                else if (c == ')') parenDepth--;
            }
        }

        if (!current.isEmpty()) {
            clauses.add(current.toString().trim());
        }

        return clauses;
    }

    private static List<ParserBinding> bindParsers(List<String> clauses) {
        var bindings = new ArrayList<ParserBinding>();
        for (String clause : clauses) {
            clauseParsers.forEach(e -> {
                if (e.supports(clause)) {
                    bindings.add(new ParserBinding(e, clause));
                }
            });
        }
        return bindings;
    }

    public record ParserBinding(ClauseParser parser, String clause) {
    }
}

