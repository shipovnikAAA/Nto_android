package ru.myitschool.work.di

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

  private val Context.dataStore by preferencesDataStore("settings")

  @Singleton
  class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val settingsDataStore = appContext.dataStore
    private val lastUsernameKey = stringPreferencesKey("last_username")
    val lastUsername: Flow<String>
      get() = settingsDataStore.data.map { it[lastUsernameKey].orEmpty() }

    suspend fun setLastUsername(username: String) =
      settingsDataStore.edit { it[lastUsernameKey] = username }
  }
}