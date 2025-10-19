function confirmDeleteItem(event) {
    event.preventDefault();
    const form = event.target;

    Swal.fire({
        title: "ยืนยันการลบ?",
        text: "คุณแน่ใจหรือไม่ที่จะลบสิ่งของนี้? ข้อมูลจะไม่สามารถกู้คืนได้",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "ลบเลย",
        cancelButtonText: "ยกเลิก",
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
    }).then((result) => {
        if (result.isConfirmed) {
            form.submit();
        }
    });

    return false;
}

document.addEventListener("DOMContentLoaded", () => {
    const successMsg = document.querySelector("[data-success-msg]");
    if (successMsg) {
        const message = successMsg.dataset.successMsg;
        
        // ✅ ตรวจสอบว่าข้อความเป็นแบบไหน
        let title = "สำเร็จ!";
        let icon = "success";
        
        if (message.includes("ลบ")) {
            title = "✅ ลบสิ่งของสำเร็จ!";
        } else if (message.includes("แก้ไข")) {
            title = "✅ แก้ไขสำเร็จ!";
        } else if (message.includes("เพิ่ม") || message.includes("โพสต์")) {
            title = "✅ เพิ่มสินค้าสำเร็จ!";
        }
        
        Swal.fire({
            title: title,
            text: message,
            icon: icon,
            confirmButtonColor: "#10b981",
        });
    }
});