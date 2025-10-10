<%-- File: src/main/webapp/WEB-INF/jsp/index.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- JSP Standard Tag Library (JSTL) สำหรับการวนลูป --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<!DOCTYPE html>
<html lang="th">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SwapLife - ให้ของที่ไม่ได้ใช้ กลับไปมีคุณค่าอีกครั้ง</title>
    
    <%-- เรียกใช้งาน Tailwind CSS ผ่าน CDN --%>
    <script src="https://cdn.tailwindcss.com"></script>
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=IBM+Plex+Sans+Thai:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'IBM Plex Sans Thai', sans-serif;
        }
    </style>
</head>
<body class="bg-gray-50">

    <nav class="bg-white shadow-md">
        <div class="max-w-6xl mx-auto px-4">
            <div class="flex justify-between">
                <div class="flex space-x-7">
                    <div>
                        <a href="/" class="flex items-center py-4 px-2">
                            <span class="font-bold text-gray-700 text-2xl">SwapLife 🌍</span>
                        </a>
                    </div>
                </div>
                <div class="hidden md:flex items-center space-x-3">
                    <a href="/" class="py-4 px-2 text-green-500 border-b-4 border-green-500 font-semibold">🏠 หน้าแรก</a>
                    <a href="/swap" class="py-4 px-2 text-gray-500 font-semibold hover:text-green-500 transition duration-300">🔁 แลกของ</a>
                    <a href="/post" class="py-4 px-2 text-gray-500 font-semibold hover:text-green-500 transition duration-300">➕ โพสต์ของ</a>
                    <a href="/profile" class="py-4 px-2 text-gray-500 font-semibold hover:text-green-500 transition duration-300">👤 โปรไฟล์</a>
                </div>
                <div class="md:hidden flex items-center">
                    <button class="outline-none mobile-menu-button">
                        <svg class="w-6 h-6 text-gray-500" fill="none" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" viewBox="0 0 24 24" stroke="currentColor">
                            <path d="M4 6h16M4 12h16m-7 6h7"></path>
                        </svg>
                    </button>
                </div>
            </div>
        </div>
    </nav>

    <header class="bg-green-600 text-white text-center py-20">
        <div class="container mx-auto px-4">
            <h1 class="text-5xl font-bold mb-4">ของเก่าคุณ = ของใหม่ของใครอีกคน</h1>
            <p class="text-xl mb-8">ให้ของที่ไม่ได้ใช้ กลับไปมีคุณค่าอีกครั้งกับ SwapLife</p>
            <div class="space-x-4">
                <%-- ปุ่ม CTA (Call-to-Action) --%>
                <a href="/browse" class="bg-white text-green-700 font-bold py-3 px-8 rounded-full text-lg hover:bg-gray-200 transition duration-300">
                    เริ่มแลกของเลย
                </a>
                <a href="/post" class="bg-green-800 text-white font-bold py-3 px-8 rounded-full text-lg hover:bg-green-900 transition duration-300">
                    โพสต์ของของคุณ
                </a>
            </div>
        </div>
    </header>

    <section class="py-16">
        <div class="container mx-auto px-4">
            <h2 class="text-3xl font-bold text-center text-gray-800 mb-10">หมวดหมู่ยอดนิยม</h2>
            
            <%-- JSTL Loop: วนลูปแสดงข้อมูลหมวดหมู่ที่ส่งมาจาก Controller --%>
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-8">
                <c:forEach var="category" items="${popularCategories}">
                    <div class="bg-white rounded-lg shadow-lg overflow-hidden transform hover:scale-105 transition duration-300">
                        <img src="${category.imageUrl}" alt="${category.name}" class="w-full h-48 object-cover">
                        <div class="p-4 text-center">
                            <h3 class="font-bold text-xl text-gray-700">${category.name}</h3>
                        </div>
                    </div>
                </c:forEach>
            </div>
            
        </div>
    </section>

    <footer class="bg-gray-800 text-white py-8">
        <div class="container mx-auto text-center">
            <p>&copy; 2025 SwapLife. All Rights Reserved.</p>
        </div>
    </footer>

</body>
</html>