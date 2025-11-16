package com.train_service.dto;


import com.train_service.model.TrainType;
import lombok.Data;

@Data
public class TrainRequest {
    private String name;
    private TrainType type;
    private String status;
}
