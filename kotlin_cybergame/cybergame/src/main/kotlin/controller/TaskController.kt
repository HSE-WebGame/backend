package com.hse.cyber.controller

import com.hse.cyber.dao.DAOTask
import com.hse.cyber.model.Task
import com.hse.cyber.model.TaskAlreadySolvedException
import com.hse.cyber.model.TaskNotFoundException
import com.hse.cyber.model.TaskRequest
import com.hse.cyber.model.TaskSolveRequest
import com.hse.cyber.model.UserNotFoundException
import com.hse.cyber.model.WrongTaskAnswerException
import com.hse.cyber.utills.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin // new
import java.sql.SQLException

@RestController
@CrossOrigin(origins = ["http://localhost:5173"]) // new
@RequestMapping("/task")
class TaskController {

    private val daoTask = DAOTask()

    @GetMapping("/get")
    fun getTaskList(): ResponseEntity<Result<List<Task>>> {
        val result = daoTask.getTaskList()
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @GetMapping("/get/solved/{userId}")
    fun getSolvedTaskList(@PathVariable("userId") userId: Long): ResponseEntity<Result<List<Task>>> {
        val result = daoTask.getSolvedTask(userId)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @PostMapping("/solve")
    fun solveTask(@RequestBody request: TaskSolveRequest): ResponseEntity<Result<Unit>> {
        return try {
            daoTask.solveTask(request)
            Logger.log(TAG, "task solved: userId=${request.userId}, taskId=${request.taskId}")
            ResponseEntity(Result.success(Unit), HttpStatus.OK)
        } catch (e: WrongTaskAnswerException) {
            Logger.err(e)
            ResponseEntity(Result.failure(e), HttpStatus.BAD_REQUEST)
        } catch (e: TaskNotFoundException) {
            Logger.err(e)
            ResponseEntity(Result.failure(e), HttpStatus.NOT_FOUND)
        } catch (e: UserNotFoundException) {
            Logger.err(e)
            ResponseEntity(Result.failure(e), HttpStatus.NOT_FOUND)
        } catch (e: TaskAlreadySolvedException) {
            Logger.err(e)
            ResponseEntity(Result.failure(e), HttpStatus.CONFLICT)
        } catch (s: SQLException) {
            Logger.err(s)
            ResponseEntity(Result.failure(s), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (f: Exception) {
            Logger.err(f)
            ResponseEntity(Result.failure(f), HttpStatus.SERVICE_UNAVAILABLE)
        }
    }

    @DeleteMapping("/delete/{taskId}")
    fun deleteTask(@PathVariable("taskId") taskId: Long): ResponseEntity<Result<Unit>> {
        val result = daoTask.deleteTask(taskId)
        Logger.log(TAG, result.toString())
        return ResponseEntity(Result.success(result), HttpStatus.OK)
    }

    @PostMapping("/create")
    fun createTask(@RequestBody taskRequest: TaskRequest): ResponseEntity<Result<Long>> {
        val taskId = daoTask.createTask(taskRequest)
        Logger.log(TAG, taskId.toString())
        return ResponseEntity(Result.success(taskId), HttpStatus.OK)
    }

    private companion object {
        const val TAG = "TaskController"
    }
}