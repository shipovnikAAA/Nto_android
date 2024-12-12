package ru.myitschool.work.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.myitschool.work.core.Constants
import ru.myitschool.work.data.remote.LoginApi
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun provideHttpClient() = Retrofit.Builder()
    .baseUrl(Constants.SERVER_ADDRESS)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

  @Provides
  @Singleton
  fun provideLoginApi(retrofit: Retrofit) = retrofit.create(LoginApi::class.java)
}