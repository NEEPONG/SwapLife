package com.springboot.model;

import com.springboot.model.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "swap_offers")
public class SwapOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer offerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status = OfferStatus.รอดำเนินการ;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // --- ความสัมพันธ์ (Relationships) ---

    // ผู้ยื่นข้อเสนอ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // ของที่เขาต้องการ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_item_id", nullable = false)
    private Item requestedItem;

    // ของที่เขานำมาเสนอ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offered_item_id", nullable = false)
    private Item offeredItem;
}
