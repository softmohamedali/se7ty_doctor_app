package com.example.doctors_app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth()=FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideFirebaseFirestore()=FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideDataStorage()=FirebaseStorage.getInstance()

}