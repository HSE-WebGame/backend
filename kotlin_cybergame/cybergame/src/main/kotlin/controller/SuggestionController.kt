package com.hse.cyber.controller

import com.hse.cyber.dao.DAOSuggestion
import com.hse.cyber.model.Suggestion
import com.hse.cyber.model.SuggestionRequest
import com.hse.cyber.model.Task
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/suggestion")
class SuggestionController {

    private val daoSuggestion = DAOSuggestion()

    @GetMapping("/get")
    fun getSuggestionList(): ResponseEntity<Result<List<Suggestion>>> {
        return ResponseEntity(Result.success(daoSuggestion.getSuggestionList()), HttpStatus.OK)
    }

    @PostMapping("/create")
    fun createSuggestion(@RequestBody suggestionRequest: SuggestionRequest): ResponseEntity<Result<Task>> {
        return ResponseEntity(Result.success(daoSuggestion.createSuggestion(suggestionRequest)), HttpStatus.OK)
    }

    @DeleteMapping("/delete")
    fun deleteSuggestion(@RequestBody suggestId: Long): ResponseEntity<Result<Unit>> {
        return ResponseEntity(Result.success(daoSuggestion.deleteSuggestion(suggestId)), HttpStatus.OK)
    }
}