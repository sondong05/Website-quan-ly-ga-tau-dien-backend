package com.train_service.dto;


import com.train_service.model.TrainType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainResponse {
    private Long id;
    private String name;
    private TrainType type;
    private String status;
}