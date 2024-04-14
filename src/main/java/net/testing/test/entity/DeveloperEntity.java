package net.testing.test.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "developer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeveloperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String firstName;
    private String lastName;
    private String specialty;
    @Enumerated(EnumType.STRING)
    private Status status;

}
