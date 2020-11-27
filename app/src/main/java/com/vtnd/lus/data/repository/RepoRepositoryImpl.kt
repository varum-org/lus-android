package com.vtnd.lus.data.repository

import com.vtnd.lus.base.BaseRepository
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.repository.source.RepoDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

class RepoRepositoryImpl(
    private val remote: RepoDataSource.Remote,
    private val local: RepoDataSource.Local
) : BaseRepository(), RepoRepository {

    override fun isOpenFirstApp() =
        local.isOpenFirstApp()

    override fun setOpenFirstApp() =
        local.setOpenFirstApp()

    override suspend fun services() = local.services()

    @ExperimentalCoroutinesApi
    override fun servicesObservable() = local.servicesObservable()
        .distinctUntilChanged()
        .buffer(1).let {
            Timber.i("userObservable")
            it
        }

    override suspend fun getServices() = withResultContext {
        val services = remote.getServices().data
        local.saveServices(services ?: emptyList())
    }
}
