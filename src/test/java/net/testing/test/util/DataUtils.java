package net.testing.test.util;

import net.testing.test.dto.DeveloperDto;
import net.testing.test.entity.DeveloperEntity;
import net.testing.test.entity.Status;

import java.util.Arrays;
import java.util.List;

public class DataUtils {

    public static DeveloperEntity getMisterSmith() {

        return DeveloperEntity.builder()
                .firstName("Mister")
                .lastName("Smith")
                .email("mike.smith@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();

    }

    public static DeveloperEntity getMisterBob() {

        return DeveloperEntity.builder()
                .firstName("Mister")
                .lastName("Bob")
                .email("bob@mail.ru")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();

    }

    public static DeveloperEntity getMisterCarl() {

        return DeveloperEntity.builder()
                .firstName("Mister")
                .lastName("Carl")
                .email("carlo@mail.ru")
                .specialty("Java")
                .status(Status.DELETED)
                .build();

    }


    public static List<DeveloperEntity> getDevelopersAllActive() {
        return Arrays.asList(
                DeveloperEntity.builder()
                        .firstName("Mister")
                        .lastName("Smith")
                        .email("smith@mail.ru")
                        .specialty("Java")
                        .status(Status.ACTIVE)
                        .build(),
                DeveloperEntity.builder()
                        .firstName("Mister")
                        .lastName("Bob")
                        .email("bob@mail.ru")
                        .specialty("Java")
                        .status(Status.ACTIVE)
                        .build(),
                DeveloperEntity.builder()
                        .firstName("Mister")
                        .lastName("Carl")
                        .email("carlo@mail.ru")
                        .specialty("Java")
                        .status(Status.ACTIVE)
                        .build()
        );
    }


    public static DeveloperEntity getMisterSmithDeveloperIsId_1() {
        return DeveloperEntity.builder()
                .id(1)
                .firstName("Mister")
                .lastName("Smith")
                .email("mike.smith@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();


    }

    public static DeveloperDto getJohnDoeDtoTransient() {
        return DeveloperDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getMikeSmithDtoTransient() {
        return DeveloperDto.builder()
                .id(1)
                .firstName("Mister")
                .lastName("Smith")
                .email("mike.smith@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getMikeSmithDtoPersistedWith_Id() {
        return DeveloperDto.builder()
                .id(1)
                .firstName("Mister")
                .lastName("Smith")
                .email("mike.smith@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getFrankJonesDtoTransient() {
        return DeveloperDto.builder()
                .firstName("Frank")
                .lastName("Jones")
                .email("frank.jones@mail.com")
                .specialty("Java")
                .status(Status.DELETED)
                .build();
    }


}
