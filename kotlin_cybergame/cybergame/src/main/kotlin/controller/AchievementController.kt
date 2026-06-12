package com.hse.cyber.controller

import com.hse.cyber.dao.DAOAchievement
import com.hse.cyber.model.Achievement
import com.hse.cyber.model.AchievementGrantRequest
import com.hse.cyber.utills.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.CrossOrigin // new
import org.springframework.web.bind.annotation.RestController // new

@RestController // new
@CrossOrigin(origins = ["http://localhost:5173"]) // new
@RequestMapping("/achievement")
class AchievementController {

    private val daoAchievement = DAOAchievement()

    @GetMapping("/get")
    fun getAchievementListByUserId(@RequestBody userId: Long): ResponseEntity<Result<List<Achievement>>> {
        val result = daoAchievement.getAchievementListByUserId(userId)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @PostMapping("/grant")
    fun grantAchievementToUser(@RequestBody achievementGrantRequest: AchievementGrantRequest): ResponseEntity<Result<Unit>> {
        val result = daoAchievement.grantAchievement(achievementGrantRequest)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    private companion object {
        const val TAG = "AchievementController"
    }
}