package com.ecommerce.ecommerce_app.dto;

public class AddressDto {
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String country;
    private String phone;

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public void setStreet(String s) { this.street = s; }
    public void setCity(String s) { this.city = s; }
    public void setState(String s) { this.state = s; }
}
