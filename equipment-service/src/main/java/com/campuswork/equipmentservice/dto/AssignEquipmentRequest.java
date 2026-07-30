package com.campuswork.equipmentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignEquipmentRequest {
    @NotNull
    private Long equipmentId;
    @NotNull
    private Long studentId;
}