package com.ba.skhool.student.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceStatusDto {
    private Long classId;
    private String className;
    private String section;
    private int totalStudents;
    private int present;
    private int absent;
    private int unmarked;
}
