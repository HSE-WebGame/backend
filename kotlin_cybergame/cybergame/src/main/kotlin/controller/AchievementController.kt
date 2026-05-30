package com.hse.cyber.controller

import com.hse.cyber.dao.DAOAchievement
import com.hse.cyber.model.Achievement
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
        return ResponseEntity(Result.success(daoAchievement.getAchievementListByUserId(userId)), HttpStatus.OK)
    }

    @GetMapping("/{urlCode}/icon.svg")
    fun getAchievementIconByCode(@PathVariable urlCode: String): ResponseEntity<Result<String>> {
        return ResponseEntity
            .ok()
            .contentType(MediaType.valueOf("image/svg+xml"))
            .body(
                Result.success(
                    daoAchievement.getAchievementIconByUrlCode(urlCode)
                )
            )
    }
}