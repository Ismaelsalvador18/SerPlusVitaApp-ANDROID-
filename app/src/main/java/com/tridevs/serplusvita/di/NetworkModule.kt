package com.tridevs.serplusvita.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.tridevs.serplusvita.data.api.AuthService
import com.tridevs.serplusvita.data.api.HabitoApi
import com.tridevs.serplusvita.data.api.HistorialHabitosApi
import com.tridevs.serplusvita.data.api.PesosApi
import com.tridevs.serplusvita.data.api.UsuarioApi
import com.tridevs.serplusvita.data.network.AuthInterceptor
import com.tridevs.serplusvita.utils.SesionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(sesionManager: SesionManager): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sesionManager))
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        // ✅  FIX: Configure Gson to translate snake_case to camelCase
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        return Retrofit.Builder()
            .baseUrl("http://10.144.253.59:3000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideUsuarioApi(retrofit: Retrofit): UsuarioApi =
        retrofit.create(UsuarioApi::class.java)

    @Provides
    @Singleton
    fun provideHabitoApi(retrofit: Retrofit): HabitoApi =
        retrofit.create(HabitoApi::class.java)

    @Provides
    @Singleton
    fun provideHistorialHabitosApi(retrofit: Retrofit): HistorialHabitosApi =
        retrofit.create(HistorialHabitosApi::class.java)

    @Provides
    @Singleton
    fun providePesosApi(retrofit: Retrofit): PesosApi =
        retrofit.create(PesosApi::class.java)
}
