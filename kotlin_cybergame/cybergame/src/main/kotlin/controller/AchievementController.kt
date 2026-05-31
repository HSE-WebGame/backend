package com.hse.cyber.controller

import com.hse.cyber.dao.DAOAchievement
import com.hse.cyber.model.Achievement
import com.hse.cyber.utills.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/achievement")
class AchievementController {

    private val daoAchievement = DAOAchievement()

    @GetMapping("/get")
    fun getAchievementListByUserId(@RequestBody userId: Long): ResponseEntity<Result<List<Achievement>>> {
        val result = daoAchievement.getAchievementListByUserId(userId)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @GetMapping("/{urlCode}/icon.svg")
    fun getAchievementIconByCode(@PathVariable("urlCode") urlCode: String): ResponseEntity<Result<String>> {
        return ResponseEntity
            .ok()
            .contentType(MediaType.valueOf("image/svg+xml"))
            .body(
                Result.success(
                    daoAchievement.getAchievementIconByUrlCode(urlCode)
                )
            )
    }

    private companion object {
        const val TAG = "AchievementController"
    }
}