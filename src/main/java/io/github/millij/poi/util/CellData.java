package io.github.millij.poi.util;

import io.github.millij.poi.ss.model.CellType;

public class CellData {
    private Object value;
    private CellType cellType;

    public CellData(Object value, CellType cellType) {
        this.value = value;
        this.cellType = cellType;
    }

    public Object getValue() {
        return value;
    }

    public CellType getCellType() {
        return cellType;
    }
}
