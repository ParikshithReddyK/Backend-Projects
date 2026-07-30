package com.campuswork.equipmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEquipmentRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @NotBlank
    private String serialNumber;
}