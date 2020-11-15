package com.vtnd.lus.data.repository.source

interface RepoDataSource {

    interface Local {
        fun isOpenFirstApp(): String?

        fun setOpenFirstApp()
    }

    interface Remote
}
