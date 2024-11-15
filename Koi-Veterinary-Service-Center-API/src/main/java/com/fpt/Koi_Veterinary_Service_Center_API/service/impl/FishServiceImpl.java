package com.fpt.Koi_Veterinary_Service_Center_API.service.impl;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.fishRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.fishResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.serviceResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Fish;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import com.fpt.Koi_Veterinary_Service_Center_API.exception.AppException;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.FishRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.UserRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IFishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FishServiceImpl implements IFishService {
    @Autowired
    private FishRepository fishRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<fishResponse> getAllFish() {
        List<Fish> fishes = fishRepository.findAll();
        List<fishResponse> fishResponses = new ArrayList<>();
        for (Fish fish : fishes) {
            fishResponse response = new fishResponse();
            response.setDescribe(fish.getDescribe());
            response.setWeight(fish.getWeight());
            response.setLength(fish.getLength());
            response.setFishID(fish.getFishID());
            response.setMonth(fish.getMonth());
            response.setUserID(fish.getUser().getUserID());
            fishResponses.add(response);
        }
        return fishResponses;
    }

    @Override
    public fishResponse createFish(fishRequest fishRequest) {
        User user = userRepository.findByUserID(fishRequest.getUserID()).orElseThrow(()-> new AppException("User not found"));
        Fish fish = new Fish();
        fish.setDescribe(fishRequest.getDescribe());
        fish.setUser(user);
        fish.setWeight(fishRequest.getWeight());
        fish.setLength(fishRequest.getLength());
        fish.setMonth(fishRequest.getMonth());
        Fish savedFish = fishRepository.save(fish);

        fishResponse response = new fishResponse();
        response.setDescribe(savedFish.getDescribe());
        response.setWeight(savedFish.getWeight());
        response.setLength(savedFish.getLength());
        response.setFishID(savedFish.getFishID());
        response.setMonth(savedFish.getMonth());
        response.setUserID(savedFish.getUser().getUserID());
        return response;
    }

    @Override
    public List<fishResponse> getFishByUserId(String userId) {
        User user = userRepository.findByUserID(userId).orElseThrow(()-> new AppException("User not found"));
        List<Fish> fishes = user.getFish();
        List<fishResponse> fishResponses = new ArrayList<>();
        for (Fish fish : fishes) {
            fishResponse response = new fishResponse();
            response.setDescribe(fish.getDescribe());
            response.setWeight(fish.getWeight());
            response.setLength(fish.getLength());
            response.setFishID(fish.getFishID());
            response.setMonth(fish.getMonth());
            response.setUserID(fish.getUser().getUserID());
            fishResponses.add(response);
        }
        return fishResponses;
    }

    @Override
    public fishResponse updateFish(String fishId, fishRequest fishRequest) {
        User user = userRepository.findByUserID(fishRequest.getUserID()).orElseThrow(()-> new AppException("User not found"));
        Fish fish = fishRepository.findByFishID(fishId).orElseThrow(()-> new AppException("Fish not found"));
        fish.setDescribe(fishRequest.getDescribe());
        fish.setUser(user);
        fish.setWeight(fishRequest.getWeight());
        fish.setLength(fishRequest.getLength());
        fish.setMonth(fishRequest.getMonth());
        Fish savedFish = fishRepository.save(fish);

        fishResponse response = new fishResponse();
        response.setDescribe(savedFish.getDescribe());
        response.setWeight(savedFish.getWeight());
        response.setLength(savedFish.getLength());
        response.setFishID(savedFish.getFishID());
        response.setMonth(savedFish.getMonth());
        response.setUserID(savedFish.getUser().getUserID());
        return response;
    }

    @Override
    public void deleteFish(String fishId) {
        Fish fish = fishRepository.findByFishID(fishId).orElseThrow(()-> new AppException("Fish not found"));
        fishRepository.delete(fish);
    }
}
