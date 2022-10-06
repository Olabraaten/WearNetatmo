package solutions.silly.wearnetatmo.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import solutions.silly.wearnetatmo.MASTER_KEY_ALIAS
import solutions.silly.wearnetatmo.SHARED_PREFS_FILENAME
import solutions.silly.wearnetatmo.api.NetatmoService

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    fun provideSharedPreferences(@ApplicationContext applicationContext: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            SHARED_PREFS_FILENAME,
            MASTER_KEY_ALIAS,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    fun provideNetatmoService(): NetatmoService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.netatmo.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(NetatmoService::class.java)
    }
}