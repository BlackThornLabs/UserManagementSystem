package com.github.BlackThornLabs.dto;

import com.github.BlackThornLabs.model.User;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotBlank
    @Size(min=2, max=50)
    private String name;

    @NotBlank
    @Email
    private String email;

    @Min(0)
    @Max(150)
    private Integer age;

    public void applyToUser(User user) {
        if (this.name != null && !this.name.isEmpty()) {
            user.setName(this.name);
        }
        if (this.email != null && !this.email.isEmpty()) {
            user.setEmail(this.email);
        }
        if (this.age != null && this.age > 0) {
            user.setAge(this.age);
        }
    }
}
