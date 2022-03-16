package com.example.lululemonassessment.hilt

import android.app.Application
import com.example.lululemonassessment.LuluApplication
import com.example.lululemonassessment.database.GarmentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    @Singleton
    fun provideRepo(app: Application): GarmentRepository = GarmentRepository(app)

}
