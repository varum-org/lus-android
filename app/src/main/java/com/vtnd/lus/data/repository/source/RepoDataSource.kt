package com.vtnd.lus.data.repository.source

interface RepoDataSource {

    interface Local {
        fun isOpenFirstApp(): Boolean?

        fun setOpenFirstApp(isFirst: Boolean)
    }

    interface Remote
}
