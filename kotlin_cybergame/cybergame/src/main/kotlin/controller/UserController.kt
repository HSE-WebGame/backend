package com.hse.cyber.controller

import com.hse.cyber.dao.DAOUser
import com.hse.cyber.model.InvalidFormatException
import com.hse.cyber.model.User
import com.hse.cyber.model.UserAuth
import com.hse.cyber.model.UserNotFoundException
import com.hse.cyber.model.UserRegister
import com.hse.cyber.utills.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin // new
import java.sql.SQLException

@CrossOrigin(origins = ["http://localhost:5173"]) // new
@RestController
@RequestMapping("/user")
class UserController(
    private val daoUser: DAOUser
) {

    @PostMapping("/auth")
    fun authenticate(@RequestBody user: UserAuth): ResponseEntity<Result<User>> {
        return try {
            val result = daoUser.authenticateUser(user)
            Logger.log(TAG, result.toString())
            ResponseEntity(Result.success(result), HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            Logger.err(e)
            ResponseEntity(Result.failure(e), HttpStatus.NOT_FOUND)
        } catch (i: InvalidFormatException) {
            Logger.err(i)
            ResponseEntity(Result.failure(i), HttpStatus.BAD_REQUEST)
        } catch (s: SQLException) {
            Logger.err(s)
            ResponseEntity(Result.failure(s), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (f: Exception) {
            Logger.err(f)
            ResponseEntity(Result.failure(f), HttpStatus.SERVICE_UNAVAILABLE)
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody user: UserRegister): ResponseEntity<Result<Long>> {
        return try {
            val result = daoUser.registerUser(user)
            Logger.log(TAG, result.toString())
            ResponseEntity(Result.success(result), HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            Logger.err(e)
            ResponseEntity(Result.failure(e), HttpStatus.NOT_FOUND)
        } catch (i: InvalidFormatException) {
            Logger.err(i)
            ResponseEntity(Result.failure(i), HttpStatus.BAD_REQUEST)
        } catch (s: SQLException) {
            Logger.err(s)
            ResponseEntity(Result.failure(s), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (f: Exception) {
            Logger.err(f)
            ResponseEntity(Result.failure(f), HttpStatus.SERVICE_UNAVAILABLE)
        }
    }

    @GetMapping("/get/id/{userId}")
    fun getUserById(@PathVariable("userId") userId: Long): ResponseEntity<Result<User>> {
        val result = daoUser.getUserById(userId)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @GetMapping("/get/all")
    fun getAllUsers(): ResponseEntity<Result<List<User>>> {
        val result = daoUser.getAllUsers()
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    private companion object {
        const val TAG = "UserController"
    }
}