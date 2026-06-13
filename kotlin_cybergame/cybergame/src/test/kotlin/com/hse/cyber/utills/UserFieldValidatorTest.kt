package com.hse.cyber.utills

import com.hse.cyber.model.UserAuth
import com.hse.cyber.model.UserRegister
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserFieldValidatorTest {

    @Test
    @DisplayName("Регистрация — все поля в допустимой длине (3–9 символов) проходят валидацию")
    fun validateRegister_allFieldsValid_returnsTrue() {
        val user = UserRegister(
            name = "Alice",
            login = "alice",
            password = "pass123",
            secretWord = "secret",
        )

        assertTrue(UserFieldValidator.validateUserFieldsRegister(user))
    }

    @ParameterizedTest
    @ValueSource(strings = ["ab", "1234567890"])
    @DisplayName("Регистрация — слишком короткий или длинный login не проходит валидацию")
    fun validateRegister_invalidLogin_returnsFalse(login: String) {
        val user = UserRegister(
            name = "Alice",
            login = login,
            password = "pass123",
            secretWord = "secret",
        )

        assertFalse(UserFieldValidator.validateUserFieldsRegister(user))
    }

    @Test
    @DisplayName("Регистрация — null не проходит валидацию")
    fun validateRegister_nullUser_returnsFalse() {
        assertFalse(UserFieldValidator.validateUserFieldsRegister(null))
    }

    @Test
    @DisplayName("Аутентификация — корректные login и password проходят валидацию")
    fun validateAuth_validCredentials_returnsTrue() {
        val user = UserAuth(login = "alice", password = "pass123")

        assertTrue(UserFieldValidator.validateUserFieldsAuth(user))
    }

    @ParameterizedTest
    @ValueSource(strings = ["ab", "1234567890"])
    @DisplayName("Аутентификация — некорректная длина login или password не проходит валидацию")
    fun validateAuth_invalidCredentials_returnsFalse(invalidValue: String) {
        val user = UserAuth(login = invalidValue, password = "pass123")

        assertFalse(UserFieldValidator.validateUserFieldsAuth(user))
    }

    @Test
    @DisplayName("Аутентификация — null не проходит валидацию")
    fun validateAuth_nullUser_returnsFalse() {
        assertFalse(UserFieldValidator.validateUserFieldsAuth(null))
    }
}
