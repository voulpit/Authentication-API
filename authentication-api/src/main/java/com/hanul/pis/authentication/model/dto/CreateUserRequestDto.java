package com.hanul.pis.authentication.model.dto;

import com.hanul.pis.authentication.model.dto.shared.AddressDto;
import com.hanul.pis.authentication.utils.ErrorMessages;
import com.hanul.pis.authentication.validation.EmailValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateUserRequestDto {
    @NotNull(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    @NotBlank(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    private String firstName;

    @NotNull(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    @NotBlank(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    private String lastName;

    @NotNull(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    @NotBlank(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    @EmailValidation
    private String email;

    @NotNull(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    @NotBlank(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    private String password;

    private List<AddressDto> addresses;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        this.addresses = addresses;
    }
}
