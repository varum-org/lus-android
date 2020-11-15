package com.vtnd.lus.data.repository

import com.vtnd.lus.base.BaseRepository
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.repository.source.RepoDataSource

class RepoRepositoryImpl(
//    private val remote: RepoDataSource.Remote,
    private val local: RepoDataSource.Local
) : BaseRepository(), RepoRepository {

    override fun isOpenFirstApp() =
        local.isOpenFirstApp()

    override fun setOpenFirstApp() =
        local.setOpenFirstApp()
}
