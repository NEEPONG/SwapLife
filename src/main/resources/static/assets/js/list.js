/**
 * 
 */

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("searchForm");
    const input = form.querySelector("input[name='q']");

    form.addEventListener("submit", function (e) {
        if (!input.value.trim()) {
            e.preventDefault();
            Swal.fire({
                icon: "warning",
                title: "กรุณากรอกคำค้นหา",
                text: "ลองพิมพ์ชื่อสิ่งของที่คุณต้องการ เช่น เสื้อผ้า หรือ หนังสือ",
                confirmButtonColor: "#16a34a",
                confirmButtonText: "ตกลง"
            });
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("filterForm");

    // ตัวกรองประเภท
    document.querySelectorAll(".filter-btn").forEach(btn => {
        btn.addEventListener("click", e => {
            e.preventDefault();
            form.querySelector("[name='type']").value = btn.dataset.type;
            form.submit();
        });
    });

    // ตัวกรองหมวดหมู่
    document.querySelectorAll(".category-pill").forEach(btn => {
        btn.addEventListener("click", e => {
            e.preventDefault();
            form.querySelector("[name='category']").value = btn.dataset.category;
            form.submit();
        });
    });

    // ตัวกรองสภาพ
    document.querySelectorAll(".condition-btn").forEach(btn => {
        btn.addEventListener("click", e => {
            e.preventDefault();
            form.querySelector("[name='condition']").value = btn.dataset.condition;
            form.submit();
        });
    });
});