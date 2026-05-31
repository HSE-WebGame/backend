package com.hse.cyber.controller

import com.hse.cyber.dao.DAOTask
import com.hse.cyber.model.Task
import com.hse.cyber.model.TaskRequest
import com.hse.cyber.utills.Logger
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
        val result = daoTask.getTaskList()
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @GetMapping("/get/solved")
    fun getSolvedTaskList(@RequestBody userId: Long): ResponseEntity<Result<List<Task>>> {
        val result = daoTask.getSolvedTask(userId)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @PostMapping("/solve")
    fun solveTask(@RequestBody taskId: Long): ResponseEntity<Result<Unit>> {
        val result = daoTask.solveTask(taskId)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @DeleteMapping("/delete")
    fun deleteTask(@RequestBody taskId: Long): ResponseEntity<Result<Unit>> {
        val result = daoTask.deleteTask(taskId)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @PostMapping("/create")
    fun createTask(@RequestBody taskRequest: TaskRequest): ResponseEntity<Result<Unit>> {
        val result = daoTask.createTask(taskRequest)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    private companion object {
        const val TAG = "TaskController"
    }
}