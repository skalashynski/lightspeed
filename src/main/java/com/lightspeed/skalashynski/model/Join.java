package com.lightspeed.skalashynski.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Join {
    private JoinType type;
    private Table source;
    private Table target;
    private Condition on;

    public enum JoinType {
        INNER, LEFT, RIGHT, FULL, CROSS, IMPLICIT
    }
}
