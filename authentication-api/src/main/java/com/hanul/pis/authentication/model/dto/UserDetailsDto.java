package com.hanul.pis.authentication.model.dto;

import com.hanul.pis.authentication.model.dto.shared.AddressDto;

import java.util.List;

public class UserDetailsDto {
    private String userId; // public user ID, different from the user's ID in the DB
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressDto> addresses;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        this.addresses = addresses;
    }
}
