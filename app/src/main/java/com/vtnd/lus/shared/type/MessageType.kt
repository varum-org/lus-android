package com.vtnd.lus.shared.type

import com.vtnd.lus.shared.TYPE_FOOTER
import com.vtnd.lus.shared.TYPE_HEADER

enum class MessageType(val type: Int) {
    CHAT_MINE(TYPE_FOOTER), CHAT_PARTNER(TYPE_HEADER)
}
