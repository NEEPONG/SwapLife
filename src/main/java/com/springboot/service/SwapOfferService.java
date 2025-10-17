package com.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.model.SwapOffer;
import com.springboot.model.User;
import com.springboot.repository.SwapOfferRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SwapOfferService {

    private final SwapOfferRepository swapOfferRepository;

    public List<SwapOffer> getRequestsForUser(User user) {
        return swapOfferRepository.findRequestsForUser(user);
    }
}
