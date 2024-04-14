package net.testing.test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.testing.test.entity.DeveloperEntity;
import net.testing.test.entity.Status;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperDto {


    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String specialty;
    private Status status;


    public DeveloperEntity toEntity() {

        return DeveloperEntity.builder().
                id(id).
                firstName(firstName)
                .lastName(lastName)
                .specialty(specialty)
                .email(email)
                .status(status).
                build();
    }

    public static DeveloperDto fromEntity(DeveloperEntity entity) {
        return DeveloperDto.
                builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .specialty(entity.getSpecialty())
                .email(entity.getEmail())
                .status(entity.getStatus())
                .build();


    }

}
