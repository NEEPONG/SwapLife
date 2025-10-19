package com.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.model.Item;
import com.springboot.model.SwapOffer;
import com.springboot.model.User;
import com.springboot.service.ItemService;
import com.springboot.service.SwapOfferService;
import com.springboot.service.UserService;

@Controller
public class OfferController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private SwapOfferService swapOfferService;
	
	@GetMapping("/swap/offer")
	public String showSwapOfferForm(@RequestParam("itemId") Integer targetItemId,
	                                Authentication authentication,
	                                Model model) {

	    String username = authentication.getName();
	    User user = userService.findByUsername(username);

	    Item targetItem = itemService.getItemById(targetItemId);
	    if (targetItem.getUser().getUserId().equals(user.getUserId())) {
	        return "redirect:/items/" + targetItemId + "?error=selfSwap";
	    }

	    List<Item> userItems = itemService.getAvailableItems(user);
	    model.addAttribute("targetItem", targetItem);
	    model.addAttribute("userItems", userItems);
	    model.addAttribute("swapOffer", new SwapOffer());
	    return "swap-offer";
	}
	
	@PostMapping("/swap/offer")
	public String submitSwapOffer(@RequestParam Integer targetItemId,
	                              @RequestParam Integer offeredItemId,
	                              @RequestParam(required = false) String message,
	                              Authentication authentication) {
	    String username = authentication.getName();
	    User user = userService.findByUsername(username);

	    swapOfferService.createOffer(user, offeredItemId, targetItemId, message);

	    return "redirect:/items/" + targetItemId + "?success=offerSent";
	}
	
	@PostMapping("/swap/accept")
    public String acceptOffer(@RequestParam Integer offerId, Authentication authentication) {
        swapOfferService.acceptOffer(offerId);
        return "redirect:/items/mine?status=requested&success=accepted";
    }

    @PostMapping("/swap/reject")
    public String rejectOffer(@RequestParam Integer offerId, Authentication authentication) {
        swapOfferService.rejectOffer(offerId);
        return "redirect:/items/mine?status=requested&success=rejected";
    }
    
    @GetMapping("/offers/history")
    public String viewOfferHistory(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        // ดึงเฉพาะคำร้องที่ฉันส่ง และมีสถานะ != รอดำเนินการ
        List<SwapOffer> sentOffers = swapOfferService.getOfferHistoryByRequester(user);

        model.addAttribute("sentOffers", sentOffers);
        model.addAttribute("user", user);
        return "profile-items-history";
    }
    
    @PostMapping("/swap/cancel")
    public String cancelOffer(@RequestParam Integer offerId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        swapOfferService.cancelOffer(user, offerId);

        return "redirect:/offers/history?success=cancelled";
    }

}

