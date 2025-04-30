package com.lightspeed.skalashynski.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sort {
    private Column column;
    private SortType sortType;

    public enum SortType {
        ASC,
        DESC
    }
}
