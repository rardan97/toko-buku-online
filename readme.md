## Sistem Toko Buku Online


## System Requirements

- Java openjdk : Version 17.0.2
- Spring Boot : version 3.4.1
- Database : PostgreSQL
- Maven : Apache Maven 3.9.3
- Editor : Intellij IDEA 2023.1.1 Community Edition
- Postman

## Dependencies

- spring-boot-starter-web
- spring-boot-starter-validation
- spring-boot-devtools
- spring-boot-starter-data-jpa
- postgresql
- jjwt-api
- jackson-databind
- jjwt-impl
- jjwt-jackson
- lombok


Aplikasi ini terdiri dari beberapa service/module utama:
- web-api
- user-service
- book-service
- category-service
- common-service
- order-service
- reports-service


Semua service di daftarkan pada pom.xml root sebagai module
```xml
<modules>
      <module>user-service</module>
      <module>web-api</module>
      <module>common-service</module>
      <module>categories-service</module>
      <module>books-service</module>
      <module>orders-service</module>
      <module>reports-service</module>
</modules>
```

### Arsitektur dan Integrasi

- web-api berfungsi sebagai lapisan controller yang menangani permintaan dari pengguna (HTTP request) dan meneruskan ke service terkait.
- Modul lain seperti user-service, book-service, category-service, order-service, dan reports-service berperan sebagai dependency yang menyediakan logika bisnis serta akses data.
- common-service berisi komponen bersama (shared utilities, constants, DTOs, dan konfigurasi umum) yang digunakan oleh seluruh service.

---

### Alur Komunikasi Antar Service

- Dalam arsitektur modular monolith ini, setiap modul dapat berinteraksi langsung dengan modul lain menggunakan dependency injection (Spring Bean)
- Setiap modul yang membutuhkan fungsionalitas dari modul lain harus mendaftarkan modul tersebut sebagai dependency di file pom.xml miliknya.
- Client mengirim request ke web-api. web-api meneruskan permintaan ke service terkait (misalnya user-service untuk login).


## DONE

### Authentication 
- Login
- Register
- Refresh Token
- Validasi Token Jwt
- Hash password
- expiry
- role-based access

### Category
- Get All Data Category 
  - Pagination
  - Access User & Admin
- Get Data Category By ID
  - Access User & Admin
- Create Data Category
    - Access Admin Only
- Update Data Category
    - Access Admin Only
- Delete Data Category
    - Access Admin Only

### Book
- Get All Data Book
    - Pagination
    - Access User & Admin
- Get Data Category By ID
    - Access User & Admin
- Create Data Category
    - Access Admin Only
- Update Data Category
    - Access Admin Only
- Delete Data Category
    - Access Admin Only


### Order
- Create Order
    - Transaksi order with multi item
    - Total Price Count automation
    - Validasi Stock
    - Update Stock Book
    - Status PENDING
- Payment Order
    - Status PAID
- Get All Order
    - Pagination
    - User
    - Admin (can view all order)
- Get Detail Order

### Error handling
- implementation Error handling

---
## In Progress
- Reporting
- unit testing
- Date faker





---


## Testing Admin

- Register

<p align="center">
  <img src="dokumentasi/register.jpg" alt="Register" width="700"/>
</p>

- Login Gagal

<p align="center">
  <img src="dokumentasi/login-gagal.jpg" alt="Login" width="700"/>
</p>

- Login Success
<p align="center">
  <img src="dokumentasi/login-gagal.jpg" alt="Login" width="700"/>
</p>

- Set Authorization Header Jwt Token
<p align="center">
  <img src="dokumentasi/catgoey-header.jpg" alt="Login" width="700"/>
</p>

- Create Category
<p align="center">
  <img src="dokumentasi/creaete-category.jpg" alt="Login" width="700"/>
</p>

- Get List Category with pagination
<p align="center">
  <img src="dokumentasi/getCategory.jpg" alt="Login" width="700"/>
</p>



- Create Book
<p align="center">
  <img src="dokumentasi/books-create.jpg" alt="Login" width="700"/>
</p>


- Get List Book with pagination
<p align="center">
  <img src="dokumentasi/get-Book.jpg" alt="Login" width="700"/>
</p>


- Create Order
<p align="center">
  <img src="dokumentasi/creaet-order.jpg" alt="Login" width="700"/>
</p>

- Create Order
<p align="center">
  <img src="dokumentasi/creaet-order.jpg" alt="Login" width="700"/>
</p>


- Stok Book Update
<p align="center">
  <img src="dokumentasi/stock-book-update.jpg" alt="Login" width="700"/>
</p>


- Create Order (Stock Failed)
<p align="center">
  <img src="dokumentasi/order-stock-failed.jpg" alt="Login" width="700"/>
</p>


- Payment
<p align="center">
  <img src="dokumentasi/payment.jpg" alt="Login" width="700"/>
</p>



- Get Order List And Check Status Payment
<p align="center">
  <img src="dokumentasi/status-payment.jpg" alt="Login" width="700"/>
</p>


- Payment Failed
<p align="center">
  <img src="dokumentasi/payment-gagal.jpg" alt="Login" width="700"/>
</p>

