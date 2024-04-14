package net.testing.test.repository;

import net.testing.test.entity.DeveloperEntity;

import net.testing.test.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeveloperRepositoryTest {
    @Autowired
    private DeveloperRepository developerRepository;

    // каждый тест должен быть уникальным, чтобы удалить данные перед каждым тестом
    @BeforeEach
    public void setUp() {
        developerRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save developer functionality")
    public void givenDeveloperObject_whenSave_thenDeveloperIsCreated() {
        // given - условие
        DeveloperEntity developerToSave = DataUtils.getMisterSmith();

        // when - действие
        DeveloperEntity savedDeveloper = developerRepository.save(developerToSave);

        // then - проверка
        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test update developer function")
    public void givenDeveloperToUpdate_whenSave_thenEmailIsChanged() {
        //given
        String updateEmail = "update@mail.ru";
        DeveloperEntity developerToCreat = DataUtils.getMisterSmith();
        developerRepository.save(developerToCreat);

        //when
        DeveloperEntity developerUpdate = developerRepository.findById(developerToCreat.getId()).
                orElse(null);
        Objects.requireNonNull(developerUpdate).setEmail(updateEmail);
        DeveloperEntity developerUpdated = developerRepository.save(developerUpdate);
        //then
        assertThat(developerUpdated).isNotNull();
        assertThat(developerUpdated.getEmail()).isEqualTo(updateEmail);

    }

    @Test
    @DisplayName("Test get Developer by id function")
    public void given_DeveloperCreated_whenGetById_then_return() {
        //given
        DeveloperEntity developerToSave = DataUtils.getMisterSmith();
        developerRepository.save(developerToSave);

        // when
        DeveloperEntity obtainedDeveloper = developerRepository.findById(developerToSave.getId()).
                orElse(null); // получаем
        //then
        assertThat(obtainedDeveloper).isNotNull(); // проверяем
        assertThat(obtainedDeveloper.getEmail()).isEqualTo("mike.smith@mail.com");
    }

    @Test
    @DisplayName("Test developer not found")
    public void givenDeveloperIsNotCreated_whenGetById_thenOptionalIsEmpty() {
        //given
        //when
        DeveloperEntity obtainDeveloper = developerRepository.findById(1)
                .orElse(null);
        //then
        assertThat(obtainDeveloper).isNull();

    }

    @Test
    @DisplayName("Test get all developers functionality")
    public void givenThreeDeveloper_whenFindAll_thenDeveloperAreReturned() {
        //given
        DeveloperEntity developer1 = DataUtils.getMisterBob();
        DeveloperEntity developer2 = DataUtils.getMisterSmith();
        DeveloperEntity developer3 = DataUtils.getMisterCarl();
        developerRepository.saveAll(List.of(developer1, developer2, developer3));
        //when
        List<DeveloperEntity> entityList = developerRepository.findAll();
        //then
        assertThat(CollectionUtils.isEmpty(entityList)).isFalse(); // проверка что не пустая


    }

    @Test
    @DisplayName("Test find developer by email")
    public void givenDeveloperSaved_whenGetByEmail_thenDeveloperReturned() {
        //given
        DeveloperEntity developer = DataUtils.getMisterSmith();
        developerRepository.save(developer);

        //when
        DeveloperEntity findDeveloper = developerRepository.findByEmail(developer.getEmail());

        //then
        assertThat(findDeveloper.getEmail()).isEqualTo(developer.getEmail());
    }

    @Test
    @DisplayName("Test get all active developer by specialty")
    public void givenDeveloperSaver_whenAllActiveBySpecialty_thenReturned() {
        //given
        developerRepository.saveAll(DataUtils.getDevelopersAllActive());

        // when
        List<DeveloperEntity> developerEntities = developerRepository.findByAllActiveBySpecialty("Java");

        //then
        assertThat(CollectionUtils.isEmpty(developerEntities)).isFalse();
        assertThat(developerEntities.size()).isEqualTo(3);

    }

    @Test
    @DisplayName("Test delete developer bu id functioanal")
    public void givenDeveloper_whenDeleteById_thenIsRemovedFromDB() {
        //given
        DeveloperEntity developer1 = DataUtils.getMisterSmith();
        developerRepository.save(developer1);

        //when
        developerRepository.deleteById(developer1.getId());
        //then
        DeveloperEntity obtainDeveloper = developerRepository.findById(developer1.getId())
                .orElse(null);
        assertThat(obtainDeveloper).isNull();
    }

}