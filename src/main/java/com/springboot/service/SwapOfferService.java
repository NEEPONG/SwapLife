package com.springboot.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.model.Item;
import com.springboot.model.SwapOffer;
import com.springboot.model.User;
import com.springboot.model.enums.OfferStatus;
import com.springboot.repository.ItemRepository;
import com.springboot.repository.SwapOfferRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SwapOfferService {

	@Autowired
    private SwapOfferRepository swapOfferRepository;
    
	@Autowired
    private ItemRepository itemRepository;

    public List<SwapOffer> getRequestsForUser(User user) {
        return swapOfferRepository.findRequestsForUser(user);
    }
    
    @Transactional
    public void createOffer(User user, Integer offeredItemId, Integer targetItemId, String message) {
        Item offeredItem = itemRepository.findById(offeredItemId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบสิ่งของที่เสนอ"));
        Item targetItem = itemRepository.findById(targetItemId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบสิ่งของที่ถูกขอแลก"));

        if (targetItem.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalStateException("ไม่สามารถแลกของของตนเองได้");
        }

        SwapOffer offer = new SwapOffer();
        offer.setRequester(user);
        offer.setOfferedItem(offeredItem);
        offer.setRequestedItem(targetItem);
        offer.setMessage(message);
        offer.setStatus(OfferStatus.รอดำเนินการ);
        offer.setCreatedAt(LocalDateTime.now());

        swapOfferRepository.save(offer);
    }

}
