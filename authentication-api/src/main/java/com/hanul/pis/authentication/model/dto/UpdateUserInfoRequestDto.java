package com.hanul.pis.authentication.model.dto;

import com.hanul.pis.authentication.utils.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateUserInfoRequestDto {
    @NotNull(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    @NotBlank(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    private String firstName;

    @NotNull(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    @NotBlank(message = ErrorMessages.MISSING_REQUIRED_FIELD)
    private String lastName;

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
}
