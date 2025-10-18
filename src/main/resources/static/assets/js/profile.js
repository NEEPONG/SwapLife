document.addEventListener("DOMContentLoaded", () => {
	// ✅ ==========================
	// Preview รูปโปรไฟล์ก่อนบันทึก
	// ✅ ==========================
	const input = document.getElementById("profileInput");
	const preview = document.getElementById("profilePreview");

	if (input) {
		input.addEventListener("change", (event) => {
			const file = event.target.files[0];
			if (file) {
				// ตรวจสอบประเภทไฟล์
				if (!file.type.startsWith("image/")) {
					Swal.fire("ผิดพลาด", "กรุณาเลือกรูปภาพเท่านั้น (JPEG หรือ PNG)", "error");
					input.value = "";
					return;
				}

				// ตรวจสอบขนาด (ไม่เกิน 1MB)
				if (file.size > 1048576) {
					Swal.fire("ไฟล์ใหญ่เกินไป", "ขนาดสูงสุดที่อนุญาตคือ 1MB", "warning");
					input.value = "";
					return;
				}

				// แสดง preview
				const reader = new FileReader();
				reader.onload = (e) => {
					preview.src = e.target.result;
					preview.classList.remove("hidden");
				};
				reader.readAsDataURL(file);
			}
		});
	}

	// ✅ ==========================
	// SweetAlert แจ้งผลเปลี่ยนรหัสผ่าน / อัปเดตข้อมูล
	// ✅ ==========================
	const urlParams = new URLSearchParams(window.location.search);

	if (urlParams.has("success")) {
		Swal.fire({
			icon: "success",
			title: "สำเร็จ!",
			text: "เปลี่ยนรหัสผ่านเรียบร้อยแล้ว",
			confirmButtonColor: "#10B981",
		});
	}

	if (urlParams.has("error")) {
		Swal.fire({
			icon: "error",
			title: "เกิดข้อผิดพลาด",
			text: decodeURIComponent(urlParams.get("error")),
			confirmButtonColor: "#EF4444",
		});
	}

	// ✅ แก้ไขข้อมูลส่วนตัวสำเร็จ
	if (urlParams.has("updateSuccess")) {
		Swal.fire({
			icon: "success",
			title: "บันทึกสำเร็จ!",
			text: "ข้อมูลส่วนตัวของคุณถูกอัปเดตเรียบร้อยแล้ว",
			confirmButtonColor: "#10B981",
		});
	}

	// ❌ แก้ไขข้อมูลส่วนตัวล้มเหลว
	if (urlParams.has("updateError")) {
		Swal.fire({
			icon: "error",
			title: "บันทึกล้มเหลว",
			text: decodeURIComponent(urlParams.get("updateError")),
			confirmButtonColor: "#EF4444",
		});
	}

	// ✅ ==========================
	// Optional: Password Strength Meter
	// ✅ ==========================
	const newPassword = document.querySelector('input[name="newPassword"]');
	const strengthBar = document.createElement("div");

	if (newPassword) {
		strengthBar.className = "h-2 w-full mt-2 rounded bg-gray-200 overflow-hidden";
		const barFill = document.createElement("div");
		barFill.className = "h-full w-0 bg-red-400 transition-all duration-300";
		strengthBar.appendChild(barFill);
		newPassword.insertAdjacentElement("afterend", strengthBar);

		newPassword.addEventListener("input", () => {
			const val = newPassword.value;
			let strength = 0;
			if (val.length >= 6) strength++;
			if (/[A-Z]/.test(val)) strength++;
			if (/[0-9]/.test(val)) strength++;
			if (/[^A-Za-z0-9]/.test(val)) strength++;

			const colors = ["bg-red-400", "bg-yellow-400", "bg-lime-400", "bg-green-500"];
			barFill.className = `h-full transition-all duration-300 ${colors[strength - 1] || "bg-gray-300"}`;
			barFill.style.width = `${(strength / 4) * 100}%`;
		});
	}
});