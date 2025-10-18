/**
 * 
 */

document.addEventListener("DOMContentLoaded", function() {
	const urlParams = new URLSearchParams(window.location.search);

	// ✅ ตรวจสอบว่ามี success=offerSent หรือไม่
	if (urlParams.get("success") === "offerSent") {
		Swal.fire({
			title: '✅ ส่งคำขอแลกเปลี่ยนสำเร็จ!',
			text: 'คำร้องของคุณได้ถูกส่งไปยังเจ้าของสินค้าแล้ว',
			icon: 'success',
			confirmButtonColor: '#16a34a',
			confirmButtonText: 'ตกลง'
		}).then(() => {
			// 🔹 ล้างพารามิเตอร์ออกจาก URL หลังจากแสดง alert
			const newUrl = window.location.pathname;
			window.history.replaceState({}, document.title, newUrl);
		});
	}
});

function confirmOffer(event) {
	event.preventDefault();
	Swal.fire({
		title: 'ยืนยันการส่งคำร้อง?',
		text: "คุณต้องการส่งคำร้องขอแลกเปลี่ยนนี้หรือไม่",
		icon: 'question',
		showCancelButton: true,
		confirmButtonColor: '#16a34a',
		cancelButtonColor: '#d33',
		confirmButtonText: 'ใช่, ส่งเลย!',
		cancelButtonText: 'ยกเลิก'
	}).then((result) => {
		if (result.isConfirmed) {
			event.target.submit();
		}
	});
	return false;
}