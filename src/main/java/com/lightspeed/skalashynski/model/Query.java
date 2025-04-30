package com.lightspeed.skalashynski.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Query {
    private List<Column> columns = new ArrayList<>();
    private List<Table> fromSources = new ArrayList<>();
    private List<Join> joins = new ArrayList<>();
    private Where where;
    private List<GroupBy> groupByColumns = new ArrayList<>();
    private List<Sort> sortColumns = new ArrayList<>();
    private Integer limit;
    private Integer offset;
}