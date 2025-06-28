package com.ba.skhool.student.dto;

import java.util.List;

public interface SortableRequest {
    List<SortField> getSortFields();

    class SortField {
        private String field;
        private String direction; // "asc" or "desc"

        public SortField() {}

        public SortField(String field, String direction) {
            this.field = field;
            this.direction = direction;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }
}
