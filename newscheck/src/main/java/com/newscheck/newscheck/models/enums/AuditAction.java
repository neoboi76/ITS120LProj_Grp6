package com.newscheck.newscheck.models.enums;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//enum class delimiting the range of possible audit actions

public enum AuditAction {
    USER_LOGIN,
    USER_LOGOUT,
    USER_REGISTER,
    PASSWORD_RESET,
    LOGIN_FAILED,
    VERIFICATION_SUBMITTED,
    VERIFICATION_COMPLETED,
    VERIFICATION_FAILED,
    VERIFICATION_VIEWED,
    USER_PROFILE_UPDATED,
    USER_PROFILE_VIEWED,
    SYSTEM_ERROR,
    API_KEY_USED,
    SEARCH_PERFORMED
}
