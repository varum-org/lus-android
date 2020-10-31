package com.vtnd.lus.data

interface RepoRepository {

    //Local
    fun isOpenFirstApp(): Boolean?

    fun setOpenFirstApp(isFirst: Boolean)
}
