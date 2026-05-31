package com.hse.cyber.constants

object UserFiledName {
    const val USER_ID = "user_id"
    const val NAME = "name"
    const val PASSWORD = "password"
    const val LOGIN = "login"
    const val SECRET_WORD = "secret_word"
    const val IS_ADMIN = "is_admin"
}

object TaskFieldName {
    const val TASK_ID = "task_id"
    const val HEADER = "header"
    const val DESCRIPTION = "description"
    const val ANSWER = "answer"
    const val POINTS = "points"
    const val HINT = "hint"
}

object AchievementFieldName {
    const val ACHIEVEMENT_ID = "achievement_id"
    const val HEADER = "header"
    const val URL_CODE = "url_code"
}

object SuggestionFieldName {
    const val SUGGESTION_ID = "suggestion_id"
    const val HEADER = "header"
    const val DESCRIPTION = "description"
    const val ANSWER = "answer"
    const val POINTS = "points"
    const val HINT = "hint"
    const val AUTHOR_ID = "author_id"
}

object TaskSolvedFieldName {
    const val USER_ID = UserFiledName.USER_ID
    const val TASK_ID = TaskFieldName.TASK_ID
    const val TOTAL_USER_POINTS = "total_user_points"
}

object UserAchievementFieldName {
    const val USER_ID = UserFiledName.USER_ID
    const val ACHIEVEMENT_LIST_ID = "achievement_list_id"
}