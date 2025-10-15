/**
 * ✅ แก้ไขปัญหาการ submit form
 */
let selectedImages = [];

function previewThumbnail(event) {
	const preview = document.getElementById('thumbnailPreview');
	preview.innerHTML = '';
	const file = event.target.files[0];
	if (file) {
		const img = document.createElement('img');
		img.src = URL.createObjectURL(file);
		img.className = 'w-48 h-48 object-cover rounded-lg border';
		preview.appendChild(img);
	}
}

function previewImages(event) {
	const previewContainer = document.getElementById('imagesPreview');
	const files = Array.from(event.target.files);
	previewContainer.innerHTML = '';
	selectedImages = [];

	files.slice(0, 4).forEach((file, index) => {
		const reader = new FileReader();
		reader.onload = e => {
			const wrapper = document.createElement('div');
			wrapper.className = 'relative inline-block';

			const img = document.createElement('img');
			img.src = e.target.result;
			img.className = 'w-32 h-32 object-cover rounded-lg border';

			const removeBtn = document.createElement('button');
			removeBtn.innerHTML = '❌';
			removeBtn.type = 'button';
			removeBtn.className =
				'absolute top-1 right-1 bg-red-500 text-white text-xs rounded-full px-1 hover:bg-red-600';
			removeBtn.onclick = () => removeImage(index);

			wrapper.appendChild(img);
			wrapper.appendChild(removeBtn);
			previewContainer.appendChild(wrapper);
			selectedImages.push(file);
		};
		reader.readAsDataURL(file);
	});
}

function removeImage(index) {
	selectedImages.splice(index, 1);

	const dataTransfer = new DataTransfer();
	selectedImages.forEach(file => dataTransfer.items.add(file));
	document.getElementById('images').files = dataTransfer.files;

	const event = { target: { files: selectedImages } };
	previewImages(event);
}

// ✅ แก้ไขฟังก์ชัน validateImages ให้ submit ถูกต้อง
function validateImages(event) {
	event.preventDefault(); // ป้องกัน submit ทันที

	const thumbnail = document.getElementById("thumbnail").files;
	const images = document.getElementById("images").files;
	const form = event.target; // เก็บ form reference

	// 🔸 ตรวจสอบภาพหลัก
	if (thumbnail.length === 0) {
		Swal.fire({
			icon: 'warning',
			title: 'ยังไม่ได้อัปโหลดภาพหลัก!',
			text: 'กรุณาเพิ่มภาพหลัก (Thumbnail) ก่อนส่งข้อมูล',
			confirmButtonColor: '#3085d6',
			confirmButtonText: 'ตกลง'
		});
		return false;
	}

	// 🔸 ตรวจสอบภาพเสริม
	if (images.length === 0) {
		Swal.fire({
			icon: 'warning',
			title: 'ยังไม่มีภาพเสริม!',
			text: 'กรุณาเพิ่มภาพเสริมอย่างน้อย 1 รูป',
			confirmButtonColor: '#3085d6',
			confirmButtonText: 'ตกลง'
		});
		return false;
	}

	// ✅ แสดงกล่องยืนยันก่อนส่ง
	Swal.fire({
		title: 'ยืนยันการเพิ่มสิ่งของ?',
		text: "ตรวจสอบข้อมูลให้ถูกต้องก่อนส่งนะครับ",
		icon: 'question',
		showCancelButton: true,
		confirmButtonColor: '#16a34a',
		cancelButtonColor: '#d33',
		confirmButtonText: 'เพิ่มสิ่งของ ✅',
		cancelButtonText: 'ยกเลิก'
	}).then((result) => {
		if (result.isConfirmed) {
			// ✅ แสดง loading ระหว่างส่งข้อมูล
			Swal.fire({
				title: 'กำลังบันทึกข้อมูล...',
				allowOutsideClick: false,
				didOpen: () => {
					Swal.showLoading();
				}
			});

			// ✅ Submit form จริงๆ
			form.submit();
		}
	});

	return false;
}