/**
 * ✅ Enhanced Post Form JavaScript with Better UX
 */
let selectedImages = [];
let thumbnailFile = null;

// Initialize form enhancements
document.addEventListener('DOMContentLoaded', function() {
	initializeFormValidation();
	initializeDragAndDrop();
	initializeCharacterCounters();
	initializeFormProgress();
});

/**
 * Preview Thumbnail with enhanced animations
 */
function previewThumbnail(event) {
	const preview = document.getElementById('thumbnailPreview');
	const file = event.target.files[0];
	
	if (file) {
		// Validate file size (max 5MB)
		if (file.size > 5 * 1024 * 1024) {
			Swal.fire({
				icon: 'error',
				title: 'ไฟล์ใหญ่เกินไป!',
				text: 'กรุณาเลือกรูปภาพที่มีขนาดไม่เกิน 5MB',
				confirmButtonColor: '#16a34a',
			});
			event.target.value = '';
			return;
		}

		// Validate file type
		if (!file.type.startsWith('image/')) {
			Swal.fire({
				icon: 'error',
				title: 'ไฟล์ไม่ถูกต้อง!',
				text: 'กรุณาเลือกไฟล์รูปภาพเท่านั้น',
				confirmButtonColor: '#16a34a',
			});
			event.target.value = '';
			return;
		}

		thumbnailFile = file;
		preview.innerHTML = '';
		
		const wrapper = document.createElement('div');
		wrapper.className = 'relative inline-block';
		wrapper.style.animation = 'fadeInScale 0.4s ease';
		
		const img = document.createElement('img');
		img.src = URL.createObjectURL(file);
		img.className = 'w-full h-48 object-cover rounded-xl border-2 border-green-200 shadow-lg';
		img.onload = () => URL.revokeObjectURL(img.src);
		
		const removeBtn = document.createElement('button');
		removeBtn.innerHTML = '❌';
		removeBtn.type = 'button';
		removeBtn.className = 'absolute top-2 right-2 bg-red-500 text-white text-sm rounded-full w-8 h-8 hover:bg-red-600 transition-all duration-300 shadow-lg hover:scale-110';
		removeBtn.onclick = () => removeThumbnail();
		
		const fileInfo = document.createElement('div');
		fileInfo.className = 'mt-2 text-sm text-gray-600';
		fileInfo.innerHTML = `
			<div class="flex items-center justify-between bg-green-50 rounded-lg p-2">
				<span class="flex items-center">
					<span class="mr-2">✅</span>
					${file.name}
				</span>
				<span class="text-xs text-gray-500">${formatFileSize(file.size)}</span>
			</div>
		`;
		
		wrapper.appendChild(img);
		wrapper.appendChild(removeBtn);
		preview.appendChild(wrapper);
		preview.appendChild(fileInfo);
		
		// Show success toast
		showToast('success', 'อัปโหลดรูปหลักสำเร็จ!');
	}
}

/**
 * Remove thumbnail
 */
function removeThumbnail() {
	const preview = document.getElementById('thumbnailPreview');
	const input = document.getElementById('thumbnail');
	
	preview.innerHTML = '';
	input.value = '';
	thumbnailFile = null;
	
	showToast('info', 'ลบรูปหลักแล้ว');
}

/**
 * Preview multiple images with enhanced UI
 */
function previewImages(event) {
	const previewContainer = document.getElementById('imagesPreview');
	const files = Array.from(event.target.files);
	
	// Validate number of images
	if (files.length > 4) {
		Swal.fire({
			icon: 'warning',
			title: 'รูปภาพเกินจำนวน!',
			text: 'สามารถอัปโหลดได้สูงสุด 4 รูปเท่านั้น',
			confirmButtonColor: '#16a34a',
		});
		return;
	}
	
	previewContainer.innerHTML = '';
	selectedImages = [];
	
	files.forEach((file, index) => {
		// Validate file size
		if (file.size > 5 * 1024 * 1024) {
			showToast('error', `${file.name} มีขนาดใหญ่เกิน 5MB`);
			return;
		}
		
		// Validate file type
		if (!file.type.startsWith('image/')) {
			showToast('error', `${file.name} ไม่ใช่ไฟล์รูปภาพ`);
			return;
		}
		
		const reader = new FileReader();
		reader.onload = e => {
			const wrapper = document.createElement('div');
			wrapper.className = 'image-preview-item relative';
			
			const img = document.createElement('img');
			img.src = e.target.result;
			img.className = 'w-full h-32 object-cover rounded-xl border-2 border-blue-200 shadow-md';
			
			const removeBtn = document.createElement('button');
			removeBtn.innerHTML = '❌';
			removeBtn.type = 'button';
			removeBtn.className = 'remove-btn absolute top-2 right-2 bg-red-500 text-white text-xs rounded-full w-7 h-7 hover:bg-red-600 transition-all duration-300 shadow-lg hover:scale-110';
			removeBtn.onclick = () => removeImage(index);
			
			const badge = document.createElement('div');
			badge.className = 'absolute bottom-2 left-2 bg-blue-500 text-white text-xs font-bold px-2 py-1 rounded-full shadow-lg';
			badge.textContent = `${index + 1}/${files.length}`;
			
			wrapper.appendChild(img);
			wrapper.appendChild(removeBtn);
			wrapper.appendChild(badge);
			previewContainer.appendChild(wrapper);
			selectedImages.push(file);
		};
		reader.readAsDataURL(file);
	});
	
	if (files.length > 0) {
		showToast('success', `อัปโหลด ${files.length} รูปสำเร็จ!`);
	}
}

/**
 * Remove specific image
 */
function removeImage(index) {
	selectedImages.splice(index, 1);
	
	const dataTransfer = new DataTransfer();
	selectedImages.forEach(file => dataTransfer.items.add(file));
	document.getElementById('images').files = dataTransfer.files;
	
	const event = { target: { files: selectedImages } };
	previewImages(event);
	
	showToast('info', 'ลบรูปภาพแล้ว');
}

/**
 * Enhanced form validation with detailed checks
 */
function validateImages(event) {
	event.preventDefault();
	
	const thumbnail = document.getElementById("thumbnail").files;
	const images = document.getElementById("images").files;
	const form = event.target;
	
	// Validate all required fields
	const title = document.getElementById("title").value.trim();
	const description = document.getElementById("description").value.trim();
	const itemCondition = document.getElementById("itemCondition").value;
	const category = document.getElementById("category_id").value;
	const listingType = document.querySelector('input[name="listingType"]:checked');
	
	// Check title
	if (!title) {
		showValidationError('กรุณากรอกชื่อสิ่งของ', 'title');
		return false;
	}
	
	if (title.length < 5) {
		showValidationError('ชื่อสิ่งของควรมีความยาวอย่างน้อย 5 ตัวอักษร', 'title');
		return false;
	}
	
	// Check description
	if (!description) {
		showValidationError('กรุณากรอกคำอธิบายสิ่งของ', 'description');
		return false;
	}
	
	if (description.length < 20) {
		showValidationError('คำอธิบายควรมีความยาวอย่างน้อย 20 ตัวอักษร', 'description');
		return false;
	}
	
	// Check thumbnail
	if (thumbnail.length === 0) {
		Swal.fire({
			icon: 'warning',
			title: 'ยังไม่ได้อัปโหลดภาพหลัก!',
			text: 'กรุณาเพิ่มภาพหลัก (Thumbnail) ก่อนส่งข้อมูล',
			confirmButtonColor: '#16a34a',
			confirmButtonText: 'ตกลง'
		});
		scrollToElement('thumbnail');
		return false;
	}
	
	// Check additional images
	if (images.length === 0) {
		Swal.fire({
			icon: 'warning',
			title: 'ยังไม่มีภาพเสริม!',
			text: 'กรุณาเพิ่มภาพเสริมอย่างน้อย 1 รูป',
			confirmButtonColor: '#16a34a',
			confirmButtonText: 'ตกลง'
		});
		scrollToElement('images');
		return false;
	}
	
	// Check item condition
	if (!itemCondition) {
		showValidationError('กรุณาเลือกสภาพสิ่งของ', 'itemCondition');
		return false;
	}
	
	// Check category
	if (!category) {
		showValidationError('กรุณาเลือกหมวดหมู่', 'category_id');
		return false;
	}
	
	// Check listing type
	if (!listingType) {
		Swal.fire({
			icon: 'warning',
			title: 'ยังไม่ได้เลือกประเภท!',
			text: 'กรุณาเลือกประเภทการลงประกาศ (แลกเปลี่ยนหรือบริจาค)',
			confirmButtonColor: '#16a34a',
			confirmButtonText: 'ตกลง'
		});
		scrollToElement('listingType1');
		return false;
	}
	
	// Show confirmation dialog
	Swal.fire({
		title: 'ยืนยันการเพิ่มสิ่งของ?',
		html: `
			<div class="text-left space-y-2">
				<p class="text-gray-700"><strong>ชื่อ:</strong> ${title}</p>
				<p class="text-gray-700"><strong>ประเภท:</strong> ${listingType.value}</p>
				<p class="text-gray-700"><strong>รูปภาพ:</strong> ${thumbnail.length + images.length} รูป</p>
				<p class="text-sm text-gray-500 mt-3">กรุณาตรวจสอบข้อมูลให้ถูกต้องก่อนส่ง</p>
			</div>
		`,
		icon: 'question',
		showCancelButton: true,
		confirmButtonColor: '#16a34a',
		cancelButtonColor: '#d33',
		confirmButtonText: '✅ เพิ่มสิ่งของ',
		cancelButtonText: '❌ ยกเลิก',
		reverseButtons: true,
	}).then((result) => {
		if (result.isConfirmed) {
			// Show loading
			Swal.fire({
				title: 'กำลังบันทึกข้อมูล...',
				html: '<div class="text-gray-600">กรุณารอสักครู่</div>',
				allowOutsideClick: false,
				allowEscapeKey: false,
				didOpen: () => {
					Swal.showLoading();
				}
			});
			
			// Submit form
			form.submit();
		}
	});
	
	return false;
}

/**
 * Show validation error
 */
function showValidationError(message, fieldId) {
	Swal.fire({
		icon: 'warning',
		title: 'ข้อมูลไม่ครบถ้วน!',
		text: message,
		confirmButtonColor: '#16a34a',
		confirmButtonText: 'ตกลง'
	});
	
	if (fieldId) {
		scrollToElement(fieldId);
		const field = document.getElementById(fieldId);
		if (field) {
			field.focus();
			field.classList.add('border-red-500');
			setTimeout(() => {
				field.classList.remove('border-red-500');
			}, 2000);
		}
	}
}

/**
 * Initialize drag and drop functionality
 */
function initializeDragAndDrop() {
	const thumbnailZone = document.querySelector('[onclick*="thumbnail"]');
	const imagesZone = document.querySelector('[onclick*="images"]');
	
	if (thumbnailZone) {
		setupDragAndDrop(thumbnailZone, 'thumbnail');
	}
	
	if (imagesZone) {
		setupDragAndDrop(imagesZone, 'images');
	}
}

function setupDragAndDrop(zone, inputId) {
	['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
		zone.addEventListener(eventName, preventDefaults, false);
	});
	
	function preventDefaults(e) {
		e.preventDefault();
		e.stopPropagation();
	}
	
	['dragenter', 'dragover'].forEach(eventName => {
		zone.addEventListener(eventName, () => {
			zone.classList.add('drag-over');
		}, false);
	});
	
	['dragleave', 'drop'].forEach(eventName => {
		zone.addEventListener(eventName, () => {
			zone.classList.remove('drag-over');
		}, false);
	});
	
	zone.addEventListener('drop', (e) => {
		const dt = e.dataTransfer;
		const files = dt.files;
		const input = document.getElementById(inputId);
		
		input.files = files;
		
		if (inputId === 'thumbnail') {
			previewThumbnail({ target: input });
		} else {
			previewImages({ target: input });
		}
	}, false);
}

/**
 * Initialize character counters for text inputs
 */
function initializeCharacterCounters() {
	const title = document.getElementById('title');
	const description = document.getElementById('description');
	
	if (title) {
		addCharacterCounter(title, 100);
	}
	
	if (description) {
		addCharacterCounter(description, 500);
	}
}

function addCharacterCounter(element, maxLength) {
	const counter = document.createElement('div');
	counter.className = 'text-sm text-gray-500 mt-1 text-right';
	element.parentNode.appendChild(counter);
	
	function updateCounter() {
		const length = element.value.length;
		counter.textContent = `${length}/${maxLength} ตัวอักษร`;
		
		if (length > maxLength * 0.9) {
			counter.classList.add('text-orange-500', 'font-semibold');
		} else {
			counter.classList.remove('text-orange-500', 'font-semibold');
		}
	}
	
	element.addEventListener('input', updateCounter);
	updateCounter();
}

/**
 * Initialize form progress tracking
 */
function initializeFormProgress() {
	const form = document.querySelector('form');
	if (!form) return;
	
	const requiredFields = form.querySelectorAll('[required]');
	
	requiredFields.forEach(field => {
		field.addEventListener('input', updateFormProgress);
		field.addEventListener('change', updateFormProgress);
	});
}

function updateFormProgress() {
	const form = document.querySelector('form');
	const requiredFields = form.querySelectorAll('[required]');
	let filledFields = 0;
	
	requiredFields.forEach(field => {
		if (field.type === 'radio') {
			if (document.querySelector(`input[name="${field.name}"]:checked`)) {
				filledFields++;
			}
		} else if (field.type === 'file') {
			if (field.files.length > 0) {
				filledFields++;
			}
		} else if (field.value.trim() !== '') {
			filledFields++;
		}
	});
	
	const progress = (filledFields / requiredFields.length) * 100;
	
	// You can add a progress bar here if needed
	console.log(`Form progress: ${progress.toFixed(0)}%`);
}

/**
 * Show toast notification
 */
function showToast(type, message) {
	const Toast = Swal.mixin({
		toast: true,
		position: 'top-end',
		showConfirmButton: false,
		timer: 3000,
		timerProgressBar: true,
		didOpen: (toast) => {
			toast.addEventListener('mouseenter', Swal.stopTimer);
			toast.addEventListener('mouseleave', Swal.resumeTimer);
		}
	});
	
	const icons = {
		success: 'success',
		error: 'error',
		warning: 'warning',
		info: 'info'
	};
	
	Toast.fire({
		icon: icons[type] || 'info',
		title: message
	});
}

/**
 * Format file size
 */
function formatFileSize(bytes) {
	if (bytes === 0) return '0 Bytes';
	const k = 1024;
	const sizes = ['Bytes', 'KB', 'MB', 'GB'];
	const i = Math.floor(Math.log(bytes) / Math.log(k));
	return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
}

/**
 * Smooth scroll to element
 */
function scrollToElement(elementId) {
	const element = document.getElementById(elementId);
	if (element) {
		element.scrollIntoView({ behavior: 'smooth', block: 'center' });
	}
}

/**
 * Initialize real-time validation
 */
function initializeFormValidation() {
	const form = document.querySelector('form');
	if (!form) return;
	
	// Add input validation listeners
	const inputs = form.querySelectorAll('input[type="text"], textarea');
	inputs.forEach(input => {
		input.addEventListener('blur', function() {
			validateField(this);
		});
	});
}

function validateField(field) {
	const value = field.value.trim();
	const minLength = field.id === 'title' ? 5 : 20;
	
	if (field.hasAttribute('required') && !value) {
		field.classList.add('border-red-500');
		return false;
	} else if (value && value.length < minLength && field.id !== 'desiredItems') {
		field.classList.add('border-orange-500');
		return false;
	} else {
		field.classList.remove('border-red-500', 'border-orange-500');
		field.classList.add('border-green-500');
		setTimeout(() => {
			field.classList.remove('border-green-500');
		}, 1000);
		return true;
	}
}