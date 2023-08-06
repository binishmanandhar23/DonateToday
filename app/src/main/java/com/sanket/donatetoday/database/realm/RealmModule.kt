package com.sanket.donatetoday.database.realm

import com.sanket.donatetoday.models.entity.CreditCardDataEntity
import com.sanket.donatetoday.models.entity.UserEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {
    @Provides
    @Singleton
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(UserEntity::class, CreditCardDataEntity::class)
        ).deleteRealmIfMigrationNeeded().build()
        return Realm.open(config)
    }
}