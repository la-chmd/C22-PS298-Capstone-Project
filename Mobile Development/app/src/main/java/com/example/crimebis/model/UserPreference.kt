package com.example.crimebis.model


import android.content.Context

internal class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME_KEY = "name"
        private const val STATE_KEY = "state"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: User) {
        val editor = preferences.edit()
        editor.putString(NAME_KEY, value.username)
        editor.putBoolean(STATE_KEY, value.isLogin)
        editor.apply()
    }

    fun getUser(): User {
        val model = User()
        model.username = preferences.getString(NAME_KEY, "")
        model.isLogin = preferences.getBoolean(STATE_KEY, false)
        return model
    }

    fun getLogin(): Boolean {
        return preferences.getBoolean(STATE_KEY, false)
    }

    fun logout() {
        val editor = preferences.edit()
        editor.putString(NAME_KEY, "")
        editor.putBoolean(STATE_KEY, false)
        editor.apply()
    }
}