package com.lightspeed.skalashynski.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    private String tableName;
    private String alias;
}
