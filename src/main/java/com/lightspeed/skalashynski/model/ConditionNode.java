package com.lightspeed.skalashynski.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionNode {
    private LogicOperator operator;
    private List<ConditionNode> children;
    private Condition condition;

    public static ConditionNode leaf(Condition condition) {
        return new ConditionNode(LogicOperator.NONE, null, condition);
    }

    public static ConditionNode of(LogicOperator op, ConditionNode... nodes) {
        return new ConditionNode(op, Arrays.asList(nodes), null);
    }

    public enum LogicOperator {
        AND, OR, NONE
    }
}