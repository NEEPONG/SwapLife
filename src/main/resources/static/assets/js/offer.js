/**
 * 
 */

async function confirmOffer(event) {
  event.preventDefault();
  const form = event.target;

  const offeredItemId = form.querySelector("[name='offeredItemId']").value;
  if (!offeredItemId) {
    Swal.fire({
      title: "⚠️ โปรดเลือกสิ่งของของคุณก่อน!",
      icon: "warning",
      confirmButtonColor: "#10b981",
    });
    return false;
  }

  // ✅ แสดง SweetAlert ยืนยัน
  const result = await Swal.fire({
    title: "ยืนยันการส่งคำร้องขอ?",
    text: "คุณต้องการส่งคำร้องขอแลกเปลี่ยนนี้หรือไม่",
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "ส่งคำร้อง",
    cancelButtonText: "ยกเลิก",
    confirmButtonColor: "#10b981",
    cancelButtonColor: "#d33",
  });

  if (result.isConfirmed) {
    // ✅ ส่งข้อมูลฟอร์มจริง
    const formData = new FormData(form);

    try {
      const response = await fetch(form.action, {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        // ✅ แสดง Alert สำเร็จ
        Swal.fire({
          title: "🎉 ส่งคำร้องสำเร็จ!",
          text: "คำร้องของคุณถูกส่งเรียบร้อยแล้ว",
          icon: "success",
          showConfirmButton: false,
          timer: 1500,
        });

        // ✅ หน่วงเวลาแล้ว redirect
        setTimeout(() => {
          window.location.href = "/items/mine?status=requested";
        }, 1600);
      } else {
        Swal.fire({
          title: "เกิดข้อผิดพลาด!",
          text: "ไม่สามารถส่งคำร้องได้ กรุณาลองใหม่อีกครั้ง",
          icon: "error",
          confirmButtonColor: "#d33",
        });
      }
    } catch (error) {
      Swal.fire({
        title: "❌ ข้อผิดพลาด!",
        text: "เกิดปัญหาในการเชื่อมต่อกับเซิร์ฟเวอร์",
        icon: "error",
        confirmButtonColor: "#d33",
      });
    }
  }

  return false; // ❌ ป้องกัน form ส่งซ้ำ
}