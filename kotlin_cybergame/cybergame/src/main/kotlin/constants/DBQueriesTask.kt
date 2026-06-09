package com.hse.cyber.constants

object DBQueriesTask {
    const val TASKS_GET_ALL = "SELECT * FROM task_get_all();"
    const val TASK_CREATE = "SELECT task_create(?, ?, ?, ?, ?);"
    const val TASK_GET_SOLVED = "SELECT * FROM task_get_solved(?);"
    const val TASK_SOLVE = "CALL task_solve(?, ?, ?);"
    const val TASK_DELETE = "CALL task_delete(?);"
}