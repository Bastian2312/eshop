Nama: Bastian Adiputra Siregar\
NPM: 2306245005\
Kelas: Pemrograman Lanjut B

<details>
    <summary><b>Tutorial 1</b></summary>

## Refleksi 1

Pada tutorial ini, saya menerapkan dua fitur baru menggunakan Spring Boot dan menerapkan berbagai prinsip clean code dan secure coding practices. Berikut adalah refleksi utama dari implementasinya:

### **Prinsip Clean Code yang Diterapkan**

1. **Meaningful Names (Penamaan yang Bermakna)**
    - Setiap nama variabel, metode, dan kelas diberi nama yang jelas dan deskriptif, sehingga mudah dipahami tanpa perlu komentar tambahan. Contoh:
    
    ```java
    @GetMapping("/edit/{productId}")
    public String editProductPage(@PathVariable String productId, Model model) {
        Product product = service.findById(productId);
        if (product != null) {
            model.addAttribute("product", product);
            return "editProduct";
        }
        return "redirect:/product/list";
    }
    ```
   Nama metode dan parameternya memudahkan untuk memahami tujuannya tanpa memerlukan komentar tambahan.

2. **Single Responsibility Principle (SRP)**
    - Kelas dipisahkan berdasarkan tanggung jawabnya masing-masing:
      - `ProductController` menangani HTTP request.
      - `ProductServiceImpl` mengatur logika bisnis.
      - `ProductRepository` menangani penyimpanan data.

3. **Avoiding Code Duplication (Menghindari Duplikasi Kode)**
    - Menggunakan metode kode yang sudah terbuat berulang-kali, misal findById

    ```java
    public Product findById(String productId) {
        for (Product product : productData) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
    ```
Metode kode ini hanya perlu dibuat sekali saja dan dapat digunakan berkali-kali sehingga menghindari duplikasi kode

### **Praktik Secure Coding yang Diterapkan**

1. **Menggunakan UUID untuk ID Produk**
    - Menghindari penggunaan ID yang dapat ditebak dengan menerapkan UUID:
    
    ```java
    public Product() {
        this.productId = UUID.randomUUID().toString();
    }
    ```

2. **Validasi Input untuk Mencegah Data Tidak Valid**
    - Di ProductRepository, metode update memastikan bahwa hanya nilai bukan nol yang diperbarui:
    
    ```java
    public void update(Product updatedProduct) {
        Product existingProduct = findById(updatedProduct.getProductId());
        if (existingProduct != null) {
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setProductQuantity(updatedProduct.getProductQuantity());
        }
    }
    ```
    Ini mencegah penyimpanan nilai null.

3. **Enkapsulasi Data**
    - Daftar productData dalam ProductRepository bersifat private, mencegah modifikasi yang tidak sah dari luar kelas.
    - Getter dan setter digunakan untuk mengontrol akses ke properti produk.

### **Area yang Perlu Ditingkatkan**
1. **Meningkatkan Validasi Input**
Saat ini, tidak ada validasi pada create atau edit produk. Menambahkan validasi seperti nama product tidak boleh null atau quantity harus dalam suatu range dapat mencegah data yang tidak valid.

2. **Improve Error Handling**
Metode findById saat ini mengembalikan null jika produk tidak ditemukan. Sebagai gantinya, melemparkan exception khusus akan lebih baik. Ini memungkinkan penanganan exception dengan pesan error yang lebih bermakna.

Dengan menerapkan prinsip-prinsip di atas, kode saya menjadi lebih bersih, aman, dan mudah dipelihara. Ke depannya, saya akan lebih memperhatikan aspek keamanan dan clean code untuk meningkatkan kualitas pengembangan software saya.

</details>

