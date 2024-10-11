package com.example.group2_databasesql;

public class Member {
    private int memberId;
    private String email;
    private String companyName;
    private String city;
    private String country;
    private String password;

    public Member(int memberId, String email, String companyName, String city, String country, String password) {
        this.memberId = memberId;
        this.email = email;
        this.companyName = companyName;
        this.city = city;
        this.country = country;
        this.password = password;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
