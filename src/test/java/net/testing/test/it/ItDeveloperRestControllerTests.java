package net.testing.test.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.testing.test.dto.DeveloperDto;
import net.testing.test.entity.DeveloperEntity;
import net.testing.test.repository.DeveloperRepository;
import net.testing.test.util.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ItDeveloperRestControllerTests extends AbstractRestControllerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // packet is Jackson
    @Autowired
    private DeveloperRepository developerRepository;

    @BeforeEach
    public void setUp() {
        developerRepository.deleteAll();
    }


    @Test
    @DisplayName("Test create developer functionality")
    public void givenDeveloperDto_whenCreateDeveloper_thenSuccessResponse() throws Exception {
        //given
        DeveloperDto dto = DataUtils.getMikeSmithDtoTransient(); // без айди дто

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/developers") // отрпавляю запрос  с дто
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue())) // проверка возращаемых данных
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("Mister")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")));
    }

    @Test
    @DisplayName("Test create developer with duplicate email functionality")
    public void givenDeveloperDtoDuplicateEmail_whenCreateDeveloper_thenResponseError() throws Exception {
        //given
        DeveloperDto dto = DataUtils.getMikeSmithDtoTransient(); // с айди
        developerRepository.save(dto.toEntity()); // пре
        //when
        ResultActions result = mockMvc.perform(post("/api/v1/developers") // отрпавляю запрос  с дто
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result.andDo(MockMvcResultHandlers.print()) // проверка возрващаемых данных Експешеона
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400))) // проверка возращаемых данных
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Developer with defined email is already exist")));

    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenDeveloperDto_whenUpdateDeveloper_thenSuccessResponse() throws Exception {
        //given
        DeveloperDto dto = DataUtils.getMikeSmithDtoTransient(); // c айди
        developerRepository.save(dto.toEntity());
        dto.setEmail("update@email.ru");

        //when
        ResultActions result = mockMvc.perform(put("/api/v1/developers") // отрпавляю запрос  с дто
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue())) // проверка возращаемых данных
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("Mister")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is("update@email.ru")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")));
    }

    @Test
    @DisplayName("Test update developer with incorrectID email functionality")
    public void givenDeveloperDtoIncorrectID_whenUpdateDeveloper_thenResponseError() throws Exception {
        //given
        DeveloperDto dto = DataUtils.getMikeSmithDtoTransient(); // c айди


        //when
        ResultActions result = mockMvc.perform(put("/api/v1/developers") // отрпавляю запрос  с дто
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))); // серилизуем дто для отправки в теле запроса

        //then
        result.andDo(MockMvcResultHandlers.print()) // проверка возрващаемых данных Експешеона
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400))) // проверка возращаемых данных
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Developer not found")));

    }

    @Test
    @DisplayName("Test get developer by id functional")
    public void givenId_whenGetByIdDeveloper_thenSuccessResponse() throws Exception {
        //given
        developerRepository.save(DataUtils.getMisterSmithDeveloperIsId_1());
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue())) // проверка возращаемых данных
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("Mister")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")));
    }

    @Test
    @DisplayName("Test get developer by incorrect id functional")
    public void givenInCorrectId_whenGetByIdDeveloper_thenErrorResponse() throws Exception {
        //given
        developerRepository.save(DataUtils.getMisterSmithDeveloperIsId_1());
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/developers/3333")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404))) // проверка возращаемых данных
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Developer not found")));

    }

    //
//    @Test
//    @DisplayName("Test is delete soft functional")
//    public void givenId_whenSoftDelete_thenSuccessResponse() throws Exception {
//        //given
//        BDDMockito.doNothing().when(developerService).softDeleteById(anyInt());
//        //when
//        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1")
//                .contentType(MediaType.APPLICATION_JSON));
//        //then
//        verify(developerService, times(1)).softDeleteById(anyInt());
//        result
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    @DisplayName("Test is delete by incorrect id soft functional")
//    public void giveIncorrectId_whenSoftDelete_thenSuccessResponse() throws Exception {
//        //given
//        BDDMockito.doThrow(new DeveloperNotFoundException("Developer not found"))
//                .when(developerService).softDeleteById(anyInt());
//        //when
//        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1")
//                .contentType(MediaType.APPLICATION_JSON));
//        //then
//        verify(developerService, times(1)).softDeleteById(anyInt());
//        result
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
    @Test
    @DisplayName("Test is delete hard functional")
    public void givenId_whenHardDelete_thenSuccessResponse() throws Exception {
        //given
        DeveloperEntity testEntity = developerRepository.save(DataUtils.getMisterSmithDeveloperIsId_1());
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1?isHard=true")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        DeveloperEntity entity = developerRepository.findById(testEntity.getId()).orElse(null);
        assertThat(entity).isNull();
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
