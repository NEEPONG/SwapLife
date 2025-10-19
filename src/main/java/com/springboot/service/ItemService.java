package com.springboot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.model.Category;
import com.springboot.model.Item;
import com.springboot.model.ItemImage;
import com.springboot.model.User;
import com.springboot.model.enums.ItemCondition;
import com.springboot.model.enums.ItemStatus;
import com.springboot.model.enums.ListingType;
import com.springboot.repository.CategoryRepository;
import com.springboot.repository.ItemRepository;
import com.springboot.repository.SwapOfferRepository;

@Service
public class ItemService {
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private SwapOfferRepository swapOfferRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	@Transactional
	public void saveItemWithImages(Item item, MultipartFile thumbnail, List<MultipartFile> images) {
		List<ItemImage> imageList = new ArrayList<>();

		try {
			System.out.println("📁 [DEBUG] Upload directory: " + uploadDir);

			// ✅ สร้างโฟลเดอร์อัตโนมัติถ้ายังไม่มี
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
				System.out.println("✅ [DEBUG] Created upload directory");
			}

			// ✅ บันทึก Thumbnail
			if (thumbnail != null && !thumbnail.isEmpty()) {
				String originalFilename = thumbnail.getOriginalFilename();
				String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
				String fileName = UUID.randomUUID() + extension;
				Path filePath = uploadPath.resolve(fileName);

				Files.copy(thumbnail.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("✅ [DEBUG] Saved thumbnail: " + fileName);

				ItemImage thumbImg = new ItemImage();
				thumbImg.setImageUrl("/uploads/" + fileName);
				thumbImg.setThumbnail(true);
				thumbImg.setItem(item);
				imageList.add(thumbImg);
			}

			// ✅ บันทึกภาพเสริม
			if (images != null && !images.isEmpty()) {
				int savedCount = 0;
				for (MultipartFile file : images) {
					if (file != null && !file.isEmpty()) {
						String originalFilename = file.getOriginalFilename();
						String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
						String fileName = UUID.randomUUID() + extension;
						Path filePath = uploadPath.resolve(fileName);

						Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
						System.out.println("✅ [DEBUG] Saved image " + (++savedCount) + ": " + fileName);

						ItemImage img = new ItemImage();
						img.setImageUrl("/uploads/" + fileName);
						img.setThumbnail(false);
						img.setItem(item);
						imageList.add(img);
					}
				}
				System.out.println("✅ [DEBUG] Total images saved: " + savedCount);
			}

			// ✅ เชื่อมโยง images กับ item
			item.setImages(imageList);

			// ✅ บันทึกลง database
			Item savedItem = itemRepository.save(item);
			System.out.println("✅ [SUCCESS] Item saved with ID: " + savedItem.getItemId());
			System.out.println("✅ [SUCCESS] Total images in DB: " + savedItem.getImages().size());

		} catch (IOException e) {
			System.err.println("❌ [ERROR] Failed to upload files: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("เกิดข้อผิดพลาดในการอัปโหลดไฟล์: " + e.getMessage(), e);
		} catch (Exception e) {
			System.err.println("❌ [ERROR] Unexpected error: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("เกิดข้อผิดพลาดในการบันทึกข้อมูล: " + e.getMessage(), e);
		}
	}

	// ✅ แสดงทั้งหมด
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> searchAndFilter(String keyword, String type, String category, String condition) {
        keyword = (keyword == null) ? "" : keyword.trim().toLowerCase();

        return itemRepository.searchFilteredItems(keyword, type, category, condition, ItemStatus.ว่าง);
    }
    
    public List<Item> findItemsByUser(User user) {
        return itemRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    // ✅ ดึงเฉพาะของที่ "ว่าง"
    public List<Item> getAvailableItems(User user) {
        return itemRepository.findByUserAndStatusOrderByCreatedAtDesc(user, ItemStatus.ว่าง);
    }

    // ✅ ดึงเฉพาะของที่ "แลกแล้ว"
    public List<Item> getSwappedItems(User user) {
        return itemRepository.findByUserAndStatusOrderByCreatedAtDesc(user, ItemStatus.แลกแล้ว);
    }
    
    @Transactional(readOnly = true)
    public Item getItemById(Integer id) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isEmpty()) {
            throw new RuntimeException("ไม่พบข้อมูลสินค้า ID: " + id);
        }

        Item item = itemOpt.get();

        return item;
    }
    
    @Transactional
    public boolean deleteItemByUser(Integer itemId, User user) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("ไม่พบสิ่งของนี้"));

        // ✅ ตรวจสอบสิทธิ์
        if (!item.getUser().equals(user)) {
            return false;
        }

        // ✅ อนุญาตให้ลบเฉพาะ item ที่สถานะ “ว่าง”
        if (item.getStatus() != ItemStatus.ว่าง) {
            return false;
        }

        // ✅ ลบ SwapOffer ที่เกี่ยวข้องกับ item นี้ (ทั้งฝั่งเสนอ และฝั่งถูกเสนอ)
        swapOfferRepository.deleteAllByOfferedItemOrRequestedItem(item, item);

        // ✅ ลบรูปในโฟลเดอร์ (ถ้ามี)
        if (item.getImages() != null) {
            for (ItemImage img : item.getImages()) {
                try {
                    String filePath = img.getImageUrl().replaceFirst("^/", "");
                    Files.deleteIfExists(Paths.get(filePath));
                } catch (Exception e) {
                    System.err.println("⚠️ [DEBUG] ลบรูปไม่สำเร็จ: " + e.getMessage());
                }
            }
        }

        // ✅ ลบ Item จริง
        itemRepository.delete(item);
        return true;
    }
    
 // เพิ่ม Method นี้ลงใน ItemService.java

    @Transactional
    public boolean updateItem(Integer itemId, User user, String title, String description,
                              String itemConditionStr, Integer categoryId, String listingTypeStr,
                              String desiredItems, MultipartFile thumbnail, List<MultipartFile> images) {
        
        // ✅ ดึง Item จาก DB
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("ไม่พบสินค้านี้"));
        
        // ✅ ตรวจสอบสิทธิ์
        if (!item.getUser().equals(user)) {
            return false;
        }
        
        // ✅ ตรวจสอบสถานะ - อนุญาตแก้ไขเฉพาะสินค้าที่ "ว่าง"
        if (item.getStatus() != ItemStatus.ว่าง) {
            return false;
        }
        
        try {
            // ✅ อัปเดตข้อมูลพื้นฐาน
            item.setTitle(title.trim());
            item.setDescription(description.trim());
            item.setDesiredItems(desiredItems != null ? desiredItems.trim() : null);
            
            // ✅ แปลง Enum
            item.setItemCondition(ItemCondition.valueOf(itemConditionStr));
            item.setListingType(ListingType.valueOf(listingTypeStr));
            
            // ✅ อัปเดต Category
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("ไม่พบหมวดหมู่ที่เลือก"));
            item.setCategory(category);
            
            // ✅ จัดการรูปภาพใหม่ (ถ้ามีการอัปโหลด)
            if ((thumbnail != null && !thumbnail.isEmpty()) || 
                (images != null && images.stream().anyMatch(f -> !f.isEmpty()))) {
                
                // 🔹 เก็บรูปเก่าไว้ก่อน เพื่อลบไฟล์
                List<ItemImage> oldImages = new ArrayList<>(item.getImages());
                
                // 🔹 ลบรูปเก่าออกจาก List โดยใช้ removeIf แทน clear()
                item.getImages().removeIf(img -> true);
                
                // 🔹 Flush เพื่อให้ Hibernate ลบ records ก่อน
                itemRepository.flush();
                
                // 🔹 ลบไฟล์รูปเก่าจากระบบ
                for (ItemImage oldImg : oldImages) {
                    try {
                        String filePath = oldImg.getImageUrl().replaceFirst("^/", "");
                        Files.deleteIfExists(Paths.get(filePath));
                    } catch (Exception e) {
                        System.err.println("⚠️ ลบรูปเก่าไม่สำเร็จ: " + e.getMessage());
                    }
                }
                
                // 🔹 สร้างโฟลเดอร์ถ้ายังไม่มี
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // 🔹 บันทึก Thumbnail ใหม่
                if (thumbnail != null && !thumbnail.isEmpty()) {
                    String originalFilename = thumbnail.getOriginalFilename();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String fileName = UUID.randomUUID() + extension;
                    Path filePath = uploadPath.resolve(fileName);
                    
                    Files.copy(thumbnail.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    
                    ItemImage thumbImg = new ItemImage();
                    thumbImg.setImageUrl("/uploads/" + fileName);
                    thumbImg.setThumbnail(true);
                    thumbImg.setItem(item);
                    item.getImages().add(thumbImg);
                }
                
                // 🔹 บันทึกรูปเสริมใหม่
                if (images != null && !images.isEmpty()) {
                    for (MultipartFile file : images) {
                        if (file != null && !file.isEmpty()) {
                            String originalFilename = file.getOriginalFilename();
                            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                            String fileName = UUID.randomUUID() + extension;
                            Path filePath = uploadPath.resolve(fileName);
                            
                            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                            
                            ItemImage img = new ItemImage();
                            img.setImageUrl("/uploads/" + fileName);
                            img.setThumbnail(false);
                            img.setItem(item);
                            item.getImages().add(img);
                        }
                    }
                }
            }
            
            // ✅ บันทึกการเปลี่ยนแปลง
            itemRepository.save(item);
            System.out.println("✅ [SUCCESS] Item updated with ID: " + item.getItemId());
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ [ERROR] Failed to update files: " + e.getMessage());
            throw new RuntimeException("เกิดข้อผิดพลาดในการอัปโหลดไฟล์: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("❌ [ERROR] Failed to update item: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("เกิดข้อผิดพลาดในการอัปเดตข้อมูล: " + e.getMessage(), e);
        }
    }

}