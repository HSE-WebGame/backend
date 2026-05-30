package com.hse.cyber.controller

import com.hse.cyber.dao.DAOTask
import com.hse.cyber.model.Task
import com.hse.cyber.model.TaskRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/task")
class TaskController {

    private val daoTask = DAOTask()

    @GetMapping("/get")
    fun getTaskList(): ResponseEntity<Result<List<Task>>> {
        return ResponseEntity(Result.success(daoTask.getTaskList()), HttpStatus.OK)
    }

    @GetMapping("/get/solved")
    fun getSolvedTaskList(@RequestBody userId: Long): ResponseEntity<Result<List<Task>>> {
        return ResponseEntity(Result.success(daoTask.getSolvedTask(userId)), HttpStatus.OK)
    }

    @PostMapping("/solve")
    fun solveTask(@RequestBody taskId: Long): ResponseEntity<Result<Unit>> {
        return ResponseEntity(Result.success(daoTask.solveTask(taskId)), HttpStatus.OK)
    }

    @DeleteMapping("/delete")
    fun deleteTask(@RequestBody taskId: Long): ResponseEntity<Result<Unit>> {
        return ResponseEntity(Result.success(daoTask.deleteTask(taskId)), HttpStatus.OK)
    }

    @PostMapping("/create")
    fun createTask(@RequestBody taskRequest: TaskRequest): ResponseEntity<Result<Unit>> {
        return ResponseEntity(Result.success(daoTask.createTask(taskRequest)), HttpStatus.OK)
    }
}