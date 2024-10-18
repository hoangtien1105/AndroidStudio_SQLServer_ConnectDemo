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
    FOREIGN KEY (OrderId) REFERENCES [Order](OrderId),
    FOREIGN KEY (ProductId) REFERENCES Product(ProductId)
);

SET IDENTITY_INSERT Member ON;

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


SELECT * FROM MEMBER