package com.hse.cyber.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hse.cyber.dao.DAOUser
import com.hse.cyber.model.InvalidFormatException
import com.hse.cyber.model.User
import com.hse.cyber.model.UserAuth
import com.hse.cyber.model.UserNotFoundException
import com.hse.cyber.model.UserRegister
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.sql.SQLException

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var daoUser: DAOUser

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    private val sampleUser = User(
        userId = 1L,
        name = "Alice",
        login = "alice",
        secretWord = "secret",
        isAdmin = false,
    )

    @Test
    @DisplayName("POST /user/register — успешная регистрация возвращает 200 и userId")
    fun register_success_returns200AndUserId() {
        whenever(daoUser.registerUser(any())).thenReturn(42L)

        mockMvc.post("/user/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                UserRegister(name = "Alice", login = "alice", password = "pass", secretWord = "word")
            )
        }.andExpect {
            status { isOk() }
            jsonPath("$.value") { value(42) }
        }
    }

    @Test
    @DisplayName("POST /user/register — невалидный формат возвращает 400")
    fun register_invalidFormat_returns400() {
        doThrow(InvalidFormatException()).whenever(daoUser).registerUser(any())

        mockMvc.post("/user/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                UserRegister(name = "Al", login = "ab", password = "x", secretWord = "y")
            )
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @DisplayName("POST /user/register — запрос без тела возвращает 400")
    fun register_emptyBody_returns400() {
        mockMvc.post("/user/register") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @DisplayName("POST /user/register — ошибка SQL возвращает 500")
    fun register_sqlException_returns500() {
        doThrow(SQLException("DB error")).whenever(daoUser).registerUser(any())

        mockMvc.post("/user/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                UserRegister(name = "Alice", login = "alice", password = "pass", secretWord = "word")
            )
        }.andExpect {
            status { isInternalServerError() }
        }
    }

    @Test
    @DisplayName("POST /user/register — неизвестная ошибка возвращает 503")
    fun register_unknownException_returns503() {
        doThrow(RuntimeException("Unexpected error")).whenever(daoUser).registerUser(any())

        mockMvc.post("/user/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                UserRegister(name = "Alice", login = "alice", password = "pass", secretWord = "word")
            )
        }.andExpect {
            status { isServiceUnavailable() }
        }
    }

    @Test
    @DisplayName("GET /user/auth — успешная аутентификация возвращает 200 и пользователя")
    fun authenticate_success_returns200AndUser() {
        whenever(daoUser.authenticateUser(any())).thenReturn(sampleUser)

        mockMvc.get("/user/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(UserAuth(login = "alice", password = "pass"))
        }.andExpect {
            status { isOk() }
            jsonPath("$.value.userId") { value(1) }
            jsonPath("$.value.login") { value("alice") }
            jsonPath("$.value.name") { value("Alice") }
        }
    }

    @Test
    @DisplayName("GET /user/auth — пользователь не найден возвращает 404")
    fun authenticate_userNotFound_returns404() {
        doThrow(UserNotFoundException()).whenever(daoUser).authenticateUser(any())

        mockMvc.get("/user/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(UserAuth(login = "ghost", password = "pass"))
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @DisplayName("GET /user/auth — неверный формат данных возвращает 400")
    fun authenticate_invalidFormat_returns400() {
        doThrow(InvalidFormatException()).whenever(daoUser).authenticateUser(any())

        mockMvc.get("/user/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(UserAuth(login = "ab", password = "x"))
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @DisplayName("GET /user/auth — запрос без тела возвращает 400")
    fun authenticate_emptyBody_returns400() {
        mockMvc.get("/user/auth") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @DisplayName("GET /user/auth — ошибка SQL возвращает 500")
    fun authenticate_sqlException_returns500() {
        doThrow(SQLException("DB error")).whenever(daoUser).authenticateUser(any())

        mockMvc.get("/user/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(UserAuth(login = "alice", password = "pass"))
        }.andExpect {
            status { isInternalServerError() }
        }
    }

    @Test
    @DisplayName("GET /user/auth — неизвестная ошибка возвращает 503")
    fun authenticate_unknownException_returns503() {
        doThrow(RuntimeException("Unexpected error")).whenever(daoUser).authenticateUser(any())

        mockMvc.get("/user/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(UserAuth(login = "alice", password = "pass"))
        }.andExpect {
            status { isServiceUnavailable() }
        }
    }

    @Test
    @DisplayName("GET /user/get/id/{userId} — пользователь найден возвращает 200")
    fun getUserById_found_returns200() {
        whenever(daoUser.getUserById(1L)).thenReturn(sampleUser)

        mockMvc.get("/user/get/id/1").andExpect {
            status { isOk() }
            jsonPath("$.value.userId") { value(1) }
            jsonPath("$.value.login") { value("alice") }
        }
    }

    @Test
    @DisplayName("GET /user/get/id/{userId} — невалидный ID (строка) возвращает 400")
    fun getUserById_invalidId_returns400() {
        mockMvc.get("/user/get/id/not-a-number").andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @DisplayName("GET /user/get/all — возвращает список всех пользователей и 200")
    fun getAllUsers_returns200AndList() {
        whenever(daoUser.getAllUsers()).thenReturn(listOf(sampleUser))

        mockMvc.get("/user/get/all").andExpect {
            status { isOk() }
            jsonPath("$.value[0].userId") { value(1) }
            jsonPath("$.value[0].login") { value("alice") }
        }
    }
}
