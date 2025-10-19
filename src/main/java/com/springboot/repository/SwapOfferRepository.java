package com.springboot.repository;

import com.springboot.model.SwapOffer;
import com.springboot.model.User;
import com.springboot.model.enums.OfferStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SwapOfferRepository extends JpaRepository<SwapOffer, Integer> {
    // Spring Data JPA จะสร้างเมธอดพื้นฐาน (save, findById, findAll, delete) ให้เอง
    // เราสามารถเพิ่มเมธอดค้นหาที่ซับซ้อนขึ้นได้เอง เช่น
    // User findByUsername(String username);
	
	@Query("""
		    SELECT so FROM SwapOffer so
		    JOIN FETCH so.requestedItem ri
		    JOIN FETCH so.offeredItem oi
		    WHERE ri.user = :user
		      AND so.status = com.springboot.model.enums.OfferStatus.รอดำเนินการ
		""")
		List<SwapOffer> findRequestsForUser(@Param("user") User user);
	
	@Query("""
		    SELECT so FROM SwapOffer so
		    JOIN FETCH so.requestedItem ri
		    JOIN FETCH so.offeredItem oi
		    WHERE so.requester = :requester
		      AND so.status = com.springboot.model.enums.OfferStatus.รอดำเนินการ
		""")
		List<SwapOffer> findByRequester(@Param("requester") User requester);
	
	List<SwapOffer> findByRequesterAndStatusNot(User requester, OfferStatus status);

}
