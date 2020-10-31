package com.vtnd.lus.data.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.vtnd.lus.data.repository.source.local.api.db.MigrationManager
import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.local.RepoLocalImpl
import com.vtnd.lus.data.repository.source.local.UserLocalImpl
import com.vtnd.lus.data.repository.source.local.api.DatabaseApi
import com.vtnd.lus.data.repository.source.local.api.DatabaseConfig
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi
import com.vtnd.lus.data.repository.source.local.api.db.DatabaseApiImpl
import com.vtnd.lus.data.repository.source.local.api.db.DatabaseManager
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefApiImpl
import com.vtnd.lus.data.repository.source.remote.RepoRemoteImpl
import com.vtnd.lus.data.repository.source.remote.UserRemoteImpl
import org.koin.dsl.module

//private fun appDatabase(context: Context): DatabaseManager =
//    Room.databaseBuilder(context, DatabaseManager::class.java, DatabaseConfig.DATABASE_NAME)
//        .addMigrations(MigrationManager.MIGRATION_1_2).build()

private fun contentResolver(context: Context): ContentResolver =
    context.contentResolver

val dataSourceModule = module {
    /**
     * Local setting module
     */
    single { contentResolver(context = get()) }

//    single { appDatabase(context = get()) }

    single<SharedPrefApi> {
        SharedPrefApiImpl(context = get())
    }

//    single<DatabaseApi> {
//        DatabaseApiImpl(databaseManager = get())
//    }

    /**
     * Data source module
     */

    single<UserDataSource.Local> {
        UserLocalImpl(
//            databaseApi = get(),
            sharedPrefApi = get()
        )
    }
    single<UserDataSource.Remote> {
        UserRemoteImpl(
            apiService = get()
        )
    }
    single<RepoDataSource.Local> {
        RepoLocalImpl(
//            databaseApi = get(),
            sharedPrefApi = get()
        )
    }
    single<RepoDataSource.Remote> {
        RepoRemoteImpl(
            apiService = get()
        )
    }
}
