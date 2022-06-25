package com.ramiyon.soulmath.domain.di

import com.ramiyon.soulmath.domain.usecase.leaderboard.LeaderboardImpl
import com.ramiyon.soulmath.domain.usecase.leaderboard.LeaderboardUseCase
import com.ramiyon.soulmath.domain.usecase.login.LoginUseCase
import com.ramiyon.soulmath.domain.usecase.login.LoginUseCaseImpl
import com.ramiyon.soulmath.domain.usecase.register.RegisterUseCase
import com.ramiyon.soulmath.domain.usecase.register.RegisterUseCaseImpl
import com.ramiyon.soulmath.domain.usecase.splash.SplashUseCase
import com.ramiyon.soulmath.domain.usecase.splash.SplashUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<SplashUseCase> { SplashUseCaseImpl(get()) }
    single<LoginUseCase> { LoginUseCaseImpl(get()) }
    single<RegisterUseCase> { RegisterUseCaseImpl(get()) }
    single<LeaderboardUseCase> { LeaderboardImpl(get()) }
}