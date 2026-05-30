package com.hse.cyber.controller

import com.hse.cyber.dao.DAOUser
import com.hse.cyber.model.InvalidFormat
import com.hse.cyber.model.User
import com.hse.cyber.model.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.SQLException

@RestController
@RequestMapping("/user")
class UserController {

    private val daoUser = DAOUser()

    @GetMapping("/auth")
    fun authenticate(@RequestBody user: User): ResponseEntity<Result<User>> {
        return try {
            ResponseEntity(Result.success(daoUser.authenticateUser(user)), HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            ResponseEntity(Result.failure(e), HttpStatus.NOT_FOUND)
        } catch (i: InvalidFormat) {
            ResponseEntity(Result.failure(i), HttpStatus.BAD_REQUEST)
        } catch (s: SQLException) {
            ResponseEntity(Result.failure(s), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (f: Exception) {
            ResponseEntity(Result.failure(f), HttpStatus.SERVICE_UNAVAILABLE)
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody user: User): ResponseEntity<Result<User>> {
        return try {
            ResponseEntity(Result.success(daoUser.registerUser(user)), HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            ResponseEntity(Result.failure(e), HttpStatus.NOT_FOUND)
        } catch (i: InvalidFormat) {
            ResponseEntity(Result.failure(i), HttpStatus.BAD_REQUEST)
        } catch (s: SQLException) {
            ResponseEntity(Result.failure(s), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (f: Exception) {
            ResponseEntity(Result.failure(f), HttpStatus.SERVICE_UNAVAILABLE)
        }
    }

    @GetMapping("/get")
    fun getUserById() {

    }

    @GetMapping("/get/list")
    fun getAllUsers() {

    }
}