package com.vtnd.lus.shared.type

enum class HistoryType(val statusCode: Int) {
    PENDING(1), APPROVE(2), REJECT(3), FINISH(4)
}