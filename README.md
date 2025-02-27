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

<details>
    <summary><b>Modul 3</b></summary>

### 1) Prinsip-prinsip SOLID yang Diterapkan dalam Proyek
#### Single Responsibility Principle (SRP)
Dalam modifikasi kode, saya memisahkan CarController dari ProductController sehingga setiap kelas memiliki role sendiri, contohnya:

- CarController sekarang hanya menangani operasi HTTP terkait mobil
- CarRepository khusus mengelola penyimpanan data mobil
- CarServiceImpl fokus pada logika bisnis terkait mobil

#### Open/Closed Principle (OCP)
Saya memperkenalkan interface generik yang memungkinkan ekstensi tanpa memodifikasi kode yang sudah ada:

- Interface GenericRepository<T> memungkinkan pembuatan repository baru tanpa mengubah kode yang ada
- Model Car mengimplementasikan interface Identifiable, membuatnya terbuka untuk ekstensi

#### Liskov Substitution Principle (LSP)
Saya menghilangkan inheritance yang tidak tepat antara CarController dan ProductController:

- Sebelumnya, CarController inherit dari ProductController namun tidak berperilaku sebagai subtipe yang tepat
- Desain baru memungkinkan substitusi yang benar melalui interface

#### Interface Segregation Principle (ISP)
Saya membuat interface yang terfokus dengan metode spesifik:

- Interface Identifiable hanya memiliki metode yang dibutuhkan untuk identifikasi objek
- CarService hanya mendeklarasikan metode yang dibutuhkan untuk operasi mobil

#### Dependency Inversion Principle (DIP)
Saya menerapkan prinsip ini dengan membuat high dan low level modul bergantung pada abstraksi:

- CarServiceImpl bergantung pada interface CarRepository, bukan implementasi konkret
- CarController bergantung pada interface CarService


### 2) Keuntungan Menerapkan Prinsip SOLID dalam Proyek
Penerapan prinsip SOLID dalam proyek ini secara signifikan meningkatkan kualitas dan maintainability kode. Dengan memisahkan tanggung jawab antar kelas, kode menjadi lebih terorganisir dan mudah dipahami. Interface generik seperti GenericRepository dan Identifiable memungkinkan pengembangan fitur baru tanpa memodifikasi kode yang sudah ada, mengurangi risiko bug dan mempercepat proses pengembangan. Penghapusan inheritance yang tidak tepat antara controller membuat sistem lebih stabil dan dapat diprediksi, sementara penggunaan interface kecil dan terfokus mengurangi ketergantungan yang tidak perlu. Dengan memastikan komponen bergantung pada abstraksi bukan implementasi konkret, sistem menjadi lebih fleksibel dan mudah diuji.

### 3) Kerugian Tidak Menerapkan Prinsip SOLID dalam Proyek
Tidak menerapkan prinsip SOLID dalam proyek dapat menyebabkan berbagai masalah jangka panjang yang serius. Code menjadi sulit dipahami dan maintained karena classes memiliki terlalu banyak tanggung jawab (too many responsibilities) dan tightly coupled. Pengembangan new features menjadi berisiko tinggi karena sering kali memerlukan modifikasi existing code, meningkatkan kemungkinan bugs. Inheritance yang tidak tepat, seperti antara CarController dan ProductController, menciptakan confusing class hierarchy dengan perilaku yang sulit diprediksi. Large interfaces memaksa classes untuk mengimplementasikan metode yang tidak relevan, sementara ketergantungan pada concrete implementations membuat sistem rigid dan sulit diuji (difficult to test). Akibatnya, pengembangan menjadi lebih slow, kode lebih rentan terhadap bugs, dan biaya maintenance meningkat seiring waktu karena setiap perubahan berpotensi menyebabkan ripple effects ke seluruh sistem.
</details>
