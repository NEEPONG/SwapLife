package com.springboot.service;

import com.springboot.model.Item;
import com.springboot.model.SwapOffer;
import com.springboot.model.User;
import com.springboot.model.enums.ItemStatus;
import com.springboot.model.enums.OfferStatus;
import com.springboot.repository.ItemRepository;
import com.springboot.repository.SwapOfferRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  
  public List<SwapOffer> getOffersByRequester(User user) {
	    return swapOfferRepository.findByRequester(user);
	}

  @Transactional
  public void createOffer(
    User user,
    Integer offeredItemId,
    Integer targetItemId,
    String message
  ) {
    Item offeredItem = itemRepository
      .findById(offeredItemId)
      .orElseThrow(() -> new IllegalArgumentException("ไม่พบสิ่งของที่เสนอ"));
    Item targetItem = itemRepository
      .findById(targetItemId)
      .orElseThrow(() -> new IllegalArgumentException("ไม่พบสิ่งของที่ถูกขอแลก"));

    // ตรวจสอบว่าไม่ใช่ของตัวเอง
    if (targetItem.getUser().getUserId().equals(user.getUserId())) {
      throw new IllegalStateException("ไม่สามารถแลกของของตนเองได้");
    }

    // ✅ ตรวจสอบว่า offeredItem เป็นของ user จริง
    if (!offeredItem.getUser().getUserId().equals(user.getUserId())) {
      throw new IllegalStateException("ไม่สามารถเสนอสิ่งของที่ไม่ใช่ของคุณได้");
    }

    // ✅ ตรวจสอบว่า offeredItem ยังว่างอยู่
    if (offeredItem.getStatus() != ItemStatus.ว่าง) {
      throw new IllegalStateException("สิ่งของที่เสนอไม่สามารถใช้แลกได้แล้ว");
    }

    // ✅ ตรวจสอบว่า targetItem ยังว่างอยู่
    if (targetItem.getStatus() != ItemStatus.ว่าง) {
      throw new IllegalStateException("สิ่งของที่ต้องการแลกไม่พร้อมใช้งานแล้ว");
    }

    // ✅ สร้าง SwapOffer
    SwapOffer offer = new SwapOffer();
    offer.setRequester(user);
    offer.setOfferedItem(offeredItem);
    offer.setRequestedItem(targetItem);
    offer.setMessage(message);
    offer.setStatus(OfferStatus.รอดำเนินการ);
    offer.setCreatedAt(LocalDateTime.now());

    swapOfferRepository.save(offer);

    // ✅ อัพเดตสถานะของสินค้าทั้งสองเป็น "มีคำร้องขอ"
    offeredItem.setStatus(ItemStatus.มีคำร้องขอ);
    targetItem.setStatus(ItemStatus.มีคำร้องขอ);

    itemRepository.save(offeredItem);
    itemRepository.save(targetItem);

    System.out.println("✅ [SUCCESS] Swap offer created with ID: " + offer.getOfferId());
    System.out.println(
      "✅ [SUCCESS] Item " + offeredItem.getItemId() + " status updated to: มีคำร้องขอ"
    );
    System.out.println(
      "✅ [SUCCESS] Item " + targetItem.getItemId() + " status updated to: มีคำร้องขอ"
    );
  }
  
}

