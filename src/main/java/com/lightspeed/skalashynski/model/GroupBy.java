package com.lightspeed.skalashynski.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupBy {
    private List<Column> columns;
    private Having having;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Having {
        private ConditionNode conditionNode;
    }
}
