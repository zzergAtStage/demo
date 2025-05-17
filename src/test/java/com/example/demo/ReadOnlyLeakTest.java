package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Disabled
class ReadOnlyLeakTest {

    @Autowired MockMvc mockMvc;

    @Test
    void shouldFailOnReadOnlyLeak() throws Exception {
        // 1. «Заражаем» соединение
        mockMvc.perform(get("/probe"))
               .andExpect(status().isOk());

        // 2. Пытаемся обновить — ожидаем 5xx и PSQLException
        mockMvc.perform(put("/users/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"X\",\"email\":\"x@x.com\"}"))
               .andExpect(status().is5xxServerError())
               .andExpect(result -> 
                   assertTrue(result.getResolvedException()
                       .getMessage().contains("read-only transaction")));
    }
}