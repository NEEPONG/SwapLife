document.addEventListener("DOMContentLoaded", () => {
    const tabReceived = document.getElementById("tab-received");
    const tabSent = document.getElementById("tab-sent");
    const receivedSection = document.getElementById("received-section");
    const sentSection = document.getElementById("sent-section");

    // ✅ สลับแท็บ - แก้ไขให้ highlight ถูกต้อง
    if (tabReceived && tabSent) {
        tabReceived.addEventListener("click", () => {
            // เพิ่ม active class ให้ tab-received
            tabReceived.classList.add("active-tab", "text-green-600", "bg-green-50", "border-green-500");
            tabReceived.classList.remove("text-gray-500", "border-gray-300");
            
            // ลบ active class จาก tab-sent
            tabSent.classList.remove("active-tab", "text-green-600", "bg-green-50", "border-green-500");
            tabSent.classList.add("text-gray-500", "border-gray-300");
            
            // แสดง/ซ่อน section
            receivedSection.classList.remove("hidden");
            sentSection.classList.add("hidden");
        });

        tabSent.addEventListener("click", () => {
            // เพิ่ม active class ให้ tab-sent
            tabSent.classList.add("active-tab", "text-green-600", "bg-green-50", "border-green-500");
            tabSent.classList.remove("text-gray-500", "border-gray-300");
            
            // ลบ active class จาก tab-received
            tabReceived.classList.remove("active-tab", "text-green-600", "bg-green-50", "border-green-500");
            tabReceived.classList.add("text-gray-500", "border-gray-300");
            
            // แสดง/ซ่อน section
            sentSection.classList.remove("hidden");
            receivedSection.classList.add("hidden");
        });
    }

    // ✅ ยืนยันก่อนยอมรับ
    document.querySelectorAll(".accept-form").forEach((form) => {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            Swal.fire({
                title: "ยืนยันการยอมรับ?",
                text: "คุณต้องการยอมรับการแลกเปลี่ยนนี้หรือไม่?",
                icon: "question",
                showCancelButton: true,
                confirmButtonText: "✅ ยอมรับ",
                cancelButtonText: "ยกเลิก",
                confirmButtonColor: "#10B981",
                cancelButtonColor: "#6B7280",
            }).then((result) => {
                if (result.isConfirmed) {
                    form.submit();
                }
            });
        });
    });

    document.querySelectorAll("form[action*='/swap/reject']").forEach((form) => {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            Swal.fire({
                title: "ปฏิเสธคำร้องขอ?",
                text: "คุณแน่ใจหรือไม่ว่าต้องการปฏิเสธการแลกเปลี่ยนนี้?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "❌ ปฏิเสธ",
                cancelButtonText: "ยกเลิก",
                confirmButtonColor: "#EF4444",
                cancelButtonColor: "#6B7280",
            }).then((result) => {
                if (result.isConfirmed) {
                    form.submit();
                }
            });
        });
    });
});

function confirmCancelOffer(event, form) {
  event.preventDefault();
  Swal.fire({
    title: "ยืนยันการยกเลิก?",
    text: "คุณแน่ใจหรือไม่ที่จะยกเลิกคำร้องนี้",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "ยืนยัน",
    cancelButtonText: "ยกเลิก"
  }).then((result) => {
    if (result.isConfirmed) {
      form.submit();
    }
  });
  return false;
}