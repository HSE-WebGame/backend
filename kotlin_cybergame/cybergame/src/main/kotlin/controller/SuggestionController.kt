package com.hse.cyber.controller

import com.hse.cyber.dao.DAOSuggestion
import com.hse.cyber.model.Suggestion
import com.hse.cyber.model.SuggestionRequest
import com.hse.cyber.model.Task
import com.hse.cyber.utills.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/suggestion")
class SuggestionController {

    private val daoSuggestion = DAOSuggestion()

    @GetMapping("/get")
    fun getSuggestionList(): ResponseEntity<Result<List<Suggestion>>> {
        val result = daoSuggestion.getSuggestionList()
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @PostMapping("/create")
    fun createSuggestion(@RequestBody suggestionRequest: SuggestionRequest): ResponseEntity<Result<Long>> {
        val result = daoSuggestion.createSuggestion(suggestionRequest)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @DeleteMapping("/delete")
    fun deleteSuggestion(@RequestBody suggestId: Long): ResponseEntity<Result<Unit>> {
        val result = daoSuggestion.deleteSuggestion(suggestId)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    private companion object {
        const val TAG = "SuggestionController"
    }
}