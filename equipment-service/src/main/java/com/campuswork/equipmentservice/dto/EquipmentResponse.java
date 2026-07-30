package com.campuswork.equipmentservice.dto;

import com.campuswork.equipmentservice.model.EquipmentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EquipmentResponse {
    private Long id;
    private String name;
    private String category;
    private String serialNumber;
    private EquipmentStatus status;
}