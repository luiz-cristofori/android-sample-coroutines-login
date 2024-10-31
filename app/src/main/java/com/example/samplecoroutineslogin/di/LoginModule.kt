package com.example.samplecoroutineslogin.di

import android.content.Context
import android.content.SharedPreferences
import com.example.samplecoroutineslogin.data.repository.LoginRepositoryImpl
import com.example.samplecoroutineslogin.data.repository.UserPreferencesRepositoryImpl
import com.example.samplecoroutineslogin.domain.repository.LoginRepository
import com.example.samplecoroutineslogin.domain.repository.UserPreferencesRepository
import com.example.samplecoroutineslogin.domain.usecase.DeleteUserNameUseCase
import com.example.samplecoroutineslogin.domain.usecase.GetUserNameUseCase
import com.example.samplecoroutineslogin.domain.usecase.LoginUseCase
import com.example.samplecoroutineslogin.domain.usecase.SaveUserNameUseCase
import com.example.samplecoroutineslogin.presentation.viewmodel.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val USER_SHARED_PREFERENCE_NAME = "user_prefs"

val loginModule = module {
    single<SharedPreferences> {
        get<Context>().getSharedPreferences(USER_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    singleOf(::UserPreferencesRepositoryImpl) bind UserPreferencesRepository::class
    singleOf(::LoginRepositoryImpl) bind LoginRepository::class

    singleOf(::SaveUserNameUseCase)
    singleOf(::GetUserNameUseCase)
    singleOf(::DeleteUserNameUseCase)
    singleOf(::LoginUseCase)

    viewModelOf(::LoginViewModel)
}
