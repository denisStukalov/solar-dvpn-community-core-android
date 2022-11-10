package co.sentinel.cosmos.task

import co.sentinel.cosmos.base.BaseConstant


class TaskResult {
    var taskType = 0
    var isSuccess = false
    var errorCode: Int = BaseConstant.ERROR_CODE_UNKNOWN
    var errorMsg: String = ""
    var resultData: Any? = null
    var resultData2: String? = null
    var resultData3: String? = null
}