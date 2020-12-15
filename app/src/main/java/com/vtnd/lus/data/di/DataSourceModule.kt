package com.vtnd.lus.data.di

import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.TokenDataSource
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.local.RepoLocalImpl
import com.vtnd.lus.data.repository.source.local.TokenLocalImpl
import com.vtnd.lus.data.repository.source.local.UserLocalImpl
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefApiImpl
import com.vtnd.lus.data.repository.source.remote.RepoRemoteImpl
import com.vtnd.lus.data.repository.source.remote.UserRemoteImpl
import org.koin.dsl.module

//private fun appDatabase(context: Context): DatabaseManager =
//    Room.databaseBuilder(context, DatabaseManager::class.java, DatabaseConfig.DATABASE_NAME)
//        .addMigrations(MigrationManager.MIGRATION_1_2).build()

//private fun contentResolver(context: Context): ContentResolver =
//    context.contentResolver

val dataSourceModule = module {
    /**
     * Local setting module
     */
//    single { contentResolver(context = get()) }

//    single { appDatabase(context = get()) }

    single<SharedPrefApi> {
        SharedPrefApiImpl(context = get(), moshi = get())
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
            sharedPrefApi = get(),
            userJsonAdapter = get()
        )
    }
    single<UserDataSource.Remote> {
        UserRemoteImpl(
            apiService = get(),
            application = get()
        )
    }
    single<RepoDataSource.Local> {
        RepoLocalImpl(
//            databaseApi = get(),
            sharedPrefApi = get(),
            serviceJsonAdapter = get()
        )
    }
    single<RepoDataSource.Remote> {
        RepoRemoteImpl(
            apiService = get(),
            geocoderApiService = get()
        )
    }
    single<TokenDataSource.Local> {
        TokenLocalImpl(
//            databaseApi = get(),
            sharedPrefApi = get()
        )
    }
}
