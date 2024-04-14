package net.testing.test.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.testing.test.dto.DeveloperDto;
import net.testing.test.entity.DeveloperEntity;
import net.testing.test.exceptiom.DeveloperNotFoundException;
import net.testing.test.exceptiom.DeveloperWithDuplicateExecution;
import net.testing.test.service.DeveloperService;
import net.testing.test.util.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class DeveloperRestControllerV1Tests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // packet is Jackson

    @MockBean
    private DeveloperService developerService;


    @Test
    @DisplayName("Test create developer functionality")
    public void givenDeveloperDto_whenCreateDeveloper_thenSuccessResponse() throws Exception {
        //given
        DeveloperDto dto = DataUtils.getMikeSmithDtoTransient(); // без айди дто
        DeveloperEntity entity = DataUtils.getMisterSmithDeveloperIsId_1(); // полноценная сущность
        BDDMockito.given(developerService.saveDeveloper(any(DeveloperEntity.class))) // мокирую поведение на возраст сущности
                .willReturn(entity);

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
        DeveloperDto dto = DataUtils.getMikeSmithDtoTransient(); // без айди дто
        BDDMockito.given(developerService.saveDeveloper(any(DeveloperEntity.class))) // мокирую поведение на возраст сущности
                .willThrow(new DeveloperWithDuplicateExecution("Developer duplicate with email"));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/developers") // отрпавляю запрос  с дто
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result.andDo(MockMvcResultHandlers.print()) // проверка возрващаемых данных Експешеона
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400))) // проверка возращаемых данных
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Developer duplicate with email")));

    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenDeveloperDto_whenUpdateDeveloper_thenSuccessResponse() throws Exception {
        //given
        DeveloperDto dto = DataUtils.getMikeSmithDtoTransient(); // c айди
        DeveloperEntity entity = DataUtils.getMisterSmithDeveloperIsId_1(); // полноценная сущность с айди
        BDDMockito.given(developerService.updateDeveloper(any(DeveloperEntity.class))) // мокирую поведение на возраст сущности
                .willReturn(entity);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")));
    }

    @Test
    @DisplayName("Test update developer with incorrectID email functionality")
    public void givenDeveloperDtoIncorrectID_whenUpdateDeveloper_thenResponseError() throws Exception {
        //given
        DeveloperDto dto = DataUtils.getMikeSmithDtoTransient(); // без айди дто
        BDDMockito.given(developerService.updateDeveloper(any(DeveloperEntity.class))) //аргумент для апдейта любой тип класса девеолепора
                .willThrow(new DeveloperNotFoundException("Developer not found"));

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
        BDDMockito.given(developerService.getDeveloperById(anyInt())).
                willReturn(DataUtils.getMisterSmithDeveloperIsId_1());
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
        BDDMockito.given(developerService.getDeveloperById(anyInt())).
                willThrow(new DeveloperNotFoundException("Developer not found"));
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404))) // проверка возращаемых данных
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Developer not found")));

    }

    @Test
    @DisplayName("Test is delete soft functional")
    public void givenId_whenSoftDelete_thenSuccessResponse() throws Exception {
        //given
        BDDMockito.doNothing().when(developerService).softDeleteById(anyInt());
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(developerService, times(1)).softDeleteById(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test is delete by incorrect id soft functional")
    public void giveIncorrectId_whenSoftDelete_thenSuccessResponse() throws Exception {
        //given
        BDDMockito.doThrow(new DeveloperNotFoundException("Developer not found"))
                .when(developerService).softDeleteById(anyInt());
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(developerService, times(1)).softDeleteById(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Test is delete hard functional")
    public void givenId_whenHardDelete_thenSuccessResponse() throws Exception {
        //given
        BDDMockito.doNothing().when(developerService).hardDeleteById(anyInt());
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1?isHard=true")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(developerService, times(1)).hardDeleteById(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}


