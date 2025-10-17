package com.springboot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.model.Item;
import com.springboot.model.ItemImage;
import com.springboot.repository.ItemRepository;

@Service
public class ItemService {
	@Autowired
	private ItemRepository itemRepository;

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

	// New: ดึงรายการ Item ทั้งหมดจากฐานข้อมูล และ initialize ความสัมพันธ์ที่เป็น LAZY
	@Transactional(readOnly = true)
	public List<Item> getAllItems() {
		List<Item> items = itemRepository.findAll();
		// บังคับให้ JPA โหลดความสัมพันธ์ LAZY ก่อนส่งกลับไปยัง view
		items.forEach(i -> {
			if (i.getImages() != null)
				i.getImages().size();
			if (i.getUser() != null)
				i.getUser().getUsername();
			if (i.getCategory() != null)
				i.getCategory().getName();
		});
		return items;
	}
}