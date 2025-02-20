Nama: Bastian Adiputra Siregar\
NPM: 2306245005\
Kelas: Pemrograman Lanjut B

<details>
    <summary><b>Modul 1</b></summary>

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

## Refleksi 2

Membuat kelas pengujian fungsional baru yang mirip dengan CreateProductFunctionalTest.java dengan setup dan variabel instans yang sama dapat menurunkan kualitas kode karena menyebabkan duplikasi kode (code duplication). Duplikasi ini meningkatkan kompleksitas maintenance karena setiap perubahan dalam prosedur setup harus diterapkan di beberapa tempat, berisiko menciptakan inkonsistensi. Selain itu, kurangnya pemisahan tanggung jawab membuat struktur kode menjadi kurang modular dan sulit diperluas. 

Untuk meningkatkan kebersihan kode, kita bisa terapkan BaseFunctionalTest sebagai base class yang menangani setup umum, sehingga class test baru dapat mewarisinya tanpa menulis ulang kode yang sama contoh:

```java
public abstract class BaseFunctionalTest {
    protected WebDriver driver;
    protected String baseUrl;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        baseUrl = "http://example.com";
        driver.get(baseUrl);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
```

Class test baru seperti VerifyProductCountTest dan CreateProductFunctionalTest bisa extend (inherit) kelas ini sehingga menghindari duplikasi misal,

```java
public class VerifyProductCountTest extends BaseFunctionalTest {
    @Test
    public void testProductCount() {
        List<WebElement> products = driver.findElements(By.className("product-item"));
        assertEquals(expectedCount, products.size());
    }
}
```
</details>

<details>
    <summary><b>Modul 2</b></summary>

### Code quality Issue(s) fixed
Selama exercise, saya menemukan beberapa code quality issues, seperti repetisi kode dalam test suite, kurangnya modularitas dalam setup test, dan kurangnya validasi pada beberapa bagian kode. Strategi utama yang saya gunakan untuk memperbaiki ini sebagai berikut:
   - **Repetisi kode pada unit test**: Beberapa test case memiliki setup yang sama, yang melanggar prinsip DRY (Don't Repeat Yourself). Saya mengatasinya dengan membuat base test class yang menyimpan setup umum agar dapat digunakan kembali oleh berbagai functional test.
   - **Pengecekan edge case**: Beberapa unit test hanya menguji skenario umum tanpa mempertimbangkan input ekstrem atau kondisi batas. Saya menambahkan boundary test untuk memastikan kode menangani skenario yang tidak biasa dengan baik.
   - **Peringatan code linting**: Beberapa bagian kode memiliki peringatan dari static analysis tool terkait formatting dan naming conventions. Saya memperbaikinya dengan mengikuti standar yang disarankan oleh linter PMD.

### CI/CD workflows Implementation
Menurut saya, implementasi CI/CD saat ini sudah memenuhi definisi Continuous Integration (CI) dan Continuous Deployment (CD) dengan cukup baik. Continuous Integration telah terpenuhi karena setiap commit dan merge request secara otomatis menjalankan unit test dan analisis kode untuk memastikan bahwa perubahan yang dilakukan tidak merusak fungsionalitas yang ada. Selain itu, linting dan code quality checks yang diterapkan membantu menjaga standar kode tetap tinggi.

Dari sisi Continuous Deployment, proses yang diterapkan memungkinkan aplikasi untuk secara otomatis dideploy ke PaaS setelah semua tahap pengujian berhasil dilewati. Ini berarti setiap perubahan yang telah diverifikasi dapat langsung tersedia tanpa intervensi manual, sehingga mempersingkat waktu pengiriman fitur baru ke pengguna.

Namun, untuk lebih meningkatkan CI/CD pipeline ini, bisa dilakukan optimasi seperti caching dependencies agar pipeline berjalan lebih cepat atau menambahkan end-to-end testing untuk memastikan bahwa seluruh aplikasi bekerja dengan baik setelah deployment.

</details>
