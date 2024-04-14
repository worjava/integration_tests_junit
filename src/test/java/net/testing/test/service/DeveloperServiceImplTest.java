package net.testing.test.service;

import static org.mockito.ArgumentMatchers.*;

import net.testing.test.exceptiom.DeveloperNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.testing.test.entity.DeveloperEntity;
import net.testing.test.exceptiom.DeveloperWithDuplicateExecution;
import net.testing.test.repository.DeveloperRepository;
import net.testing.test.util.DataUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // для того чтобы заинжектились моки
public class DeveloperServiceImplTest {
    @InjectMocks
    private DeveloperServiceImpl developerService;
    @Mock
    private DeveloperRepository developerRepository;

    @Test
    @DisplayName("Test save developer functionality")
    public void givenDeveloperToSave_whenSaveDeveloper_thenRepositoryIsCalled() {
        //given
        DeveloperEntity developerSAVE = DataUtils.getMisterSmith();
        BDDMockito.given(developerRepository.findByEmail(anyString()))
                .willReturn(null);
        BDDMockito.given(developerRepository.save(any(DeveloperEntity.class)))
                .willReturn(DataUtils.getMisterSmith());
        //when
        DeveloperEntity saveDeveloper = developerService.saveDeveloper(developerSAVE);
        //then
        assertThat(saveDeveloper).isNotNull();

    }

    @Test
    @DisplayName("Test save developer with duplicate function")
    public void givenDeveloperToSaveWithDuplicateEmail_thenExceptionIsThrow() {
        //give
        DeveloperEntity developer_toSave = DataUtils.getMisterSmith();
        BDDMockito.given(developerRepository.findByEmail(anyString()))
                .willReturn(DataUtils.getMisterSmith());
        //when
        assertThrows(DeveloperWithDuplicateExecution.class, () -> // проверка на наличие выброса исключения
                developerService.saveDeveloper(developer_toSave));
        //then
        verify(developerRepository, never()).save(any(DeveloperEntity.class)); // проверка вызовы метода сохранения
    }


    @Test
    @DisplayName(" Test update developer function")
    public void givenDeveloperToUpdate_whenUpdateDeveloper_thenRepositoryIsCalled() {
        //given
        DeveloperEntity developerToUpdate = DataUtils.getMisterSmithDeveloperIsId_1();
        BDDMockito.given(developerRepository.existsById(anyInt()))
                .willReturn(true);

        BDDMockito.given(developerRepository.save(any(DeveloperEntity.class))) // задаем поведение чтобы отратобал сервис
                .willReturn(developerToUpdate);

        //when
        DeveloperEntity developerUpdate = developerService.updateDeveloper(developerToUpdate);
        //then
        assertThat(developerUpdate).isNotNull();
        verify(developerRepository, times(1)).save(any(DeveloperEntity.class));
    }

    @Test
    @DisplayName("Test update developer with incorrect id functional ")
    public void givenDeveloperToUpdateWithIncorrect_whenUpdateDeveloper_thenExceptionIsThrow() {
        //given
        DeveloperEntity developerToUpdate = DataUtils.getMisterSmithDeveloperIsId_1();
        BDDMockito.given(developerRepository.existsById(anyInt())).
                willReturn(false);

        //when
        assertThrows(
                DeveloperNotFoundException.class, () -> developerService.updateDeveloper(developerToUpdate)
        );
        //then
        verify(developerRepository, never()).save(any(DeveloperEntity.class));

    }

    @Test
    @DisplayName("Test get developer by id functionality")
    public void givenDeveloperId_whenGetById_thenDeveloperIsReturned() {
        //given
        BDDMockito.given(developerRepository.findById(anyInt()))
                .willReturn(Optional.of(DataUtils.getMisterSmithDeveloperIsId_1()));
        // when
        DeveloperEntity obtainDeveloper = developerService.getDeveloperById(444);
        //then
        assertThat(obtainDeveloper).isNotNull();
    }

    @Test
    @DisplayName("Test get developer by id functionality")
    public void givenDeveloperIsNotCorrectId_whenGetById_thenExceptionThrow() {
        //given
        BDDMockito.given(developerRepository.findById(anyInt())) // задаем поведвение
                .willThrow(DeveloperNotFoundException.class);
        // when
        assertThrows(DeveloperNotFoundException.class, () -> developerService.getDeveloperById(777)); // и вызов метода
        //then
    }

    @Test
    @DisplayName("Test get developer by email ")
    public void getEmail_whenFindDeveloperByEmail_thenReturnedDeveloper() {
        //given
        String email = "любой@mai.ru";
        //when
        BDDMockito.given(developerRepository.findByEmail(anyString()))
                .willThrow(DeveloperNotFoundException.class);
        //then
        assertThrows(DeveloperNotFoundException.class, () -> developerService.getDeveloperByEmail(email));
    }

    @Test
    @DisplayName("Test gets all developers ")
    public void getCreatedAllDevelopers_whenGetsDevelopers_thenReturnedDevelopers() {
        //give
        List<DeveloperEntity> developerEntities = DataUtils.getDevelopersAllActive();
        BDDMockito.given(developerRepository.findAll()). // задаем поведение моку
                willReturn(developerEntities);
        //when
        List<DeveloperEntity> newDevelopersEntities = developerService.getAllDeveloper(); // вызываем метод

        //then
        assertThat(newDevelopersEntities).isNotNull();
        assertThat(newDevelopersEntities).isEqualTo(developerEntities);
    }

    @Test
    @DisplayName("Test gets just active status and speciality  developers ")
    public void getCreateDeveloperActiveStatusAndNotActive_whenGetDevelopersActiveStatus_thenReturned() {
        //give
        String speciality = "Java";
        List<DeveloperEntity> developerEntities = DataUtils.getDevelopersAllActive();
        BDDMockito.given(developerRepository.findByAllActiveBySpecialty(anyString())).
                willReturn(developerEntities);
        //when
        List<DeveloperEntity> activeStatusDevelopers = developerService.getDeveloperActiveBySpecialty(speciality);

        //then
        assertThat(activeStatusDevelopers.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test softDelete by id Developers")
    public void givenId_whenSoftDeleteByID_thenRepositoryIsCalled() {
        //given
        BDDMockito.given(developerRepository.findById(anyInt())).  // задем поведение на возращение пользоватлея с id 1
                willReturn(Optional.of(DataUtils.getMisterSmithDeveloperIsId_1()));
        //when
        developerService.softDeleteById(1);
        //then
        verify(developerRepository, times(1)).save(any(DeveloperEntity.class));
        verify(developerRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Test hard delete by id functional")
    public void givenCorrectId_whenHardDeleteDeveloper_thenDeleteRepoMethodIsCalled() {
        //given
        BDDMockito.given(developerRepository.findById(anyInt())).  // задем поведение на возращение пользоватлея с id 1
                willReturn(Optional.of(DataUtils.getMisterSmithDeveloperIsId_1()));
        //when
        developerService.hardDeleteById(1);
        //then
        verify(developerRepository, times(1)).deleteById(anyInt());


    }

    @Test
    @DisplayName("Test hard delete by id functional")
    public void givenCorrectId_whenHardDeleteDeveloper_thenException() {
        //given
        BDDMockito.given(developerRepository.findById(anyInt())).  // задем поведение на возращение пользоватлея с id 1
                willReturn(Optional.empty());
        //when
        assertThrows (DeveloperNotFoundException.class, () -> developerService.hardDeleteById(1));
        //then
        verify(developerRepository, never()).deleteById(anyInt());

    }

}
