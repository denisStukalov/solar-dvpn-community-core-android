package co.sentinel.cosmos.task

import co.sentinel.cosmos.base.BaseCosmosApp


abstract class CommonTask(val app: BaseCosmosApp) {
    protected var mResult: TaskResult = TaskResult()


    protected abstract suspend fun doInBackground(vararg strings: String): TaskResult
    suspend fun run(vararg params: String): TaskResult {
        return doInBackground(*params)
    }
}
