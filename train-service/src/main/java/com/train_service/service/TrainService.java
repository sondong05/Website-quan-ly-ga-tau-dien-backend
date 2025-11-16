package com.train_service.service;


import com.train_service.dto.TrainRequest;
import com.train_service.dto.TrainResponse;
import com.train_service.exception.ResourceNotFoundException; // THÊM
import com.train_service.model.Train;
import com.train_service.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepo;

    public List<TrainResponse> getAllTrains() {
        return trainRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TrainResponse getTrain(Long id) {
        // SỬA: Ném exception 404
        Train train = trainRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
        return mapToResponse(train);
    }

    public TrainResponse createTrain(TrainRequest req) {
        Train train = Train.builder()
                .name(req.getName())
                .type(req.getType())
                .status(req.getStatus() == null ? "ACTIVE" : req.getStatus())
                .build();
        return mapToResponse(trainRepo.save(train));
    }

    public TrainResponse updateTrain(Long id, TrainRequest req) {
        // SỬA: Ném exception 404
        Train train = trainRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
        train.setName(req.getName());
        train.setType(req.getType());
        train.setStatus(req.getStatus());
        return mapToResponse(trainRepo.save(train));
    }

    public void deleteTrain(Long id) {
        // SỬA: Thêm kiểm tra tồn tại
        if (!trainRepo.existsById(id)) {
            throw new ResourceNotFoundException("Train not found with id: " + id);
        }
        trainRepo.deleteById(id);
    }

    public TrainResponse updateStatus(Long id, String status) {
        // SỬA: Ném exception 404
        Train train = trainRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
        train.setStatus(status);
        return mapToResponse(trainRepo.save(train));
    }

    private TrainResponse mapToResponse(Train train) {
        // ... (Không đổi) ...
        return TrainResponse.builder()
                .id(train.getId())
                .name(train.getName())
                .type(train.getType())
                .status(train.getStatus())
                .build();
    }
}