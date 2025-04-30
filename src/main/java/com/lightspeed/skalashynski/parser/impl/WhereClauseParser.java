package com.lightspeed.skalashynski.parser.impl;

import com.lightspeed.skalashynski.model.Condition;
import com.lightspeed.skalashynski.model.ConditionNode;
import com.lightspeed.skalashynski.model.Query;
import com.lightspeed.skalashynski.model.Where;
import com.lightspeed.skalashynski.parser.ClauseParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WhereClauseParser implements ClauseParser {

    private static final Pattern LOGICAL_OPERATOR_PATTERN = Pattern.compile("(?i)\\s+(AND|OR)\\s+");

    @Override
    public boolean supports(String part) {
        return part.trim().toUpperCase().startsWith("WHERE");
    }

    @Override
    public void parse(String part, Query query) {
        String expression = part.substring(5).trim();
        ConditionNode root = parseCondition(expression);
        query.setWhere(new Where(root));
    }

    public ConditionNode parseCondition(String expr) {
        List<String> tokens = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        var matcher = LOGICAL_OPERATOR_PATTERN.matcher(expr);
        int lastEnd = 0;

        while (matcher.find()) {
            tokens.add(expr.substring(lastEnd, matcher.start()).trim());
            operators.add(matcher.group(1).toUpperCase());
            lastEnd = matcher.end();
        }
        tokens.add(expr.substring(lastEnd).trim());

        if (operators.isEmpty()) {
            return ConditionNode.leaf(new Condition(expr.trim()));
        }

        var op = ConditionNode.LogicOperator.valueOf(operators.get(0));
        if (!operators.stream().allMatch(o -> o.equals(op.name()))) {
            throw new IllegalArgumentException("Mixed logical operators are not supported yet: " + expr);
        }

        var children = tokens.stream()
                .map(t -> ConditionNode.leaf(new Condition(t)))
                .toList();

        return new ConditionNode(op, children, null);
    }
}