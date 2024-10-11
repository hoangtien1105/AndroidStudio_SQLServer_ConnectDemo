Create database SaleManagement
use SaleManagement

CREATE TABLE Member (
    MemberId INT IDENTITY(1,1) PRIMARY KEY,
    Email VARCHAR(100) NOT NULL,
    CompanyName NVARCHAR(40) NOT NULL,
    City NVARCHAR(15) NOT NULL,
    Country NVARCHAR(15) NOT NULL,
    Password NVARCHAR(30) NOT NULL
);

CREATE TABLE Product (
    ProductId INT IDENTITY(1,1) PRIMARY KEY,
    CategoryId INT NOT NULL,
    ProductName NVARCHAR(40) NOT NULL,
    Weight NVARCHAR(20) NOT NULL,
    UnitPrice MONEY NOT NULL,
    UnitsInStock INT NOT NULL
);

CREATE TABLE [Order] (
    OrderId INT IDENTITY(1,1) PRIMARY KEY,
    MemberId INT NOT NULL,
    OrderDate DATETIME NOT NULL,
    RequiredDate DATETIME,
    ShippedDate DATETIME,
    Freight MONEY,
    FOREIGN KEY (MemberId) REFERENCES Member(MemberId)
);

CREATE TABLE OrderDetail (
    OrderId INT  IDENTITY(1,1) PRIMARY KEY,
    ProductId INT,
    UnitPrice MONEY NOT NULL,
    Quantity INT NOT NULL,
    Discount FLOAT NOT NULL,
    PRIMARY KEY (OrderId, ProductId),
    FOREIGN KEY (OrderId) REFERENCES [Order](OrderId),
    FOREIGN KEY (ProductId) REFERENCES Product(ProductId)
);

SET IDENTITY_INSERT Member ON;

SET IDENTITY_INSERT Product ON;

SET IDENTITY_INSERT OrderDetail ON;


SET IDENTITY_INSERT [Order] ON;


INSERT INTO Member (MemberId, Email, CompanyName, City, Country, Password)
VALUES
    (1, N'nguyenvana@gmail.com', N'Công ty ABC', N'Hà Nội', N'Việt Nam', N'matkhau123'),
    (2, N'tranthib@yahoo.com', N'Công ty XYZ', N'Hồ Chí Minh', N'Việt Nam', N'password456'),
    (3, N'lethic@hotmail.com', N'TNHH MTV', N'Đà Nẵng', N'Việt Nam', N'securepass'),
    (4, N'phamvand@outlook.com', N'Cổ phần 123', N'Cần Thơ', N'Việt Nam', N'strongpass'),
    (5, N'nguyennhee@gmail.com', N'Doanh nghiệp tư nhân', N'Hải Phòng', N'Việt Nam', N'complexpass'),
    (6, N'tranthif@yahoo.com', N'Công ty TNHH', N'Nha Trang', N'Việt Nam', N'hashedpass'),
    (7, N'lethig@hotmail.com', N'Cơ sở sản xuất', N'Huế', N'Việt Nam', N'encryptedpass'),
    (8, N'phamvanh@outlook.com', N'Tập đoàn lớn', N'Vũng Tàu', N'Việt Nam', N'randompass');

INSERT INTO Product (ProductId, CategoryId, ProductName, Weight, UnitPrice, UnitsInStock)
VALUES
    (1, 1, N'Sản phẩm A', N'500g', 10.99, 50),
    (2, 2, N'Sản phẩm B', N'1kg', 19.99, 30),
    (3, 3, N'Sản phẩm C', N'250g', 5.99, 80),
    (4, 1, N'Sản phẩm D', N'750g', 15.99, 25),
    (5, 2, N'Sản phẩm E', N'1.5kg', 24.99, 15),
    (6, 3, N'Sản phẩm F', N'300g', 7.99, 60),
    (7, 1, N'Sản phẩm G', N'900g', 18.99, 40),
    (8, 2, N'Sản phẩm H', N'2kg', 29.99, 10);

	INSERT INTO [Order] (OrderId, MemberId, OrderDate, RequiredDate, ShippedDate, Freight)
VALUES
    (1, 1, '2024-06-10', '2024-06-15', '2024-06-12', 5.99),
    (2, 3, '2024-06-05', '2024-06-10', '2024-06-08', 8.99),
    (3, 5, '2024-05-28', '2024-06-02', '2024-05-30', 4.99),
    (4, 2, '2024-05-20', '2024-05-25', '2024-05-23', 7.99)