package com.vtnd.lus.shared.liveData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class CombineLiveData<A, B>(
    liveDataA: LiveData<A>,
    liveDataB: LiveData<B>
) : MediatorLiveData<Pair<A?, B?>>() {
    var dataA: A? = null
    var dataB: B? = null

    init {
        addSource(liveDataA) { dataA ->
            this.dataA = dataA
            dataB?.let {
                value = dataA to it
            }
        }
        addSource(liveDataB) { dataB ->
            this.dataB = dataB
            dataA?.let {
                value = it to dataB
            }
        }
    }
}
