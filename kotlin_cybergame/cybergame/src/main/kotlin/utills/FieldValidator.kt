package com.hse.cyber.utills

import com.hse.cyber.model.User

internal class FieldValidator {
    companion object {
        fun checkUserPassword(userPassword: String?): Boolean {
            userPassword?.let {
                if (userPassword.length in 3..9) {
                    return true
                }
            }
            return false
        }

        fun checkUserLogin(userLogin: String?): Boolean {
            userLogin?.let {
                if (userLogin.length in 3..9) {
                    return true
                }
            }
            return false
        }

        fun checkUserName(userName: String?): Boolean {
            userName?.let {
                if (userName.length in 3..9) {
                    return true
                }
            }
            return false
        }

        fun checkUserSecretWord(userSecretWord: String?): Boolean {
            userSecretWord?.let {
                if (userSecretWord.length in 3..9) {
                    return true
                }
            }
            return false
        }
    }
}

class UserFieldValidator {
    companion object {
        fun checkUserFieldsAuth(user: User?): Boolean {
            var result = false
            user?.let {
                result = FieldValidator.checkUserLogin(user.login)
                        && FieldValidator.checkUserPassword(user.password)
            }
            return result
        }

        fun checkUserFields(user: User?): Boolean {
            var result = false
            user?.let { user ->
                result = FieldValidator.checkUserLogin(user.login)
                        && FieldValidator.checkUserName(user.name)
                        && FieldValidator.checkUserPassword(user.password)
                        && FieldValidator.checkUserSecretWord(user.secretWord)
            }
            return result
        }
    }
}