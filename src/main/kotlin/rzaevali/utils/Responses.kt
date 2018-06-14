package rzaevali.utils

sealed class Meta

data class Success(val status: Int, val success: String): Meta()

data class Error(val status: Int, val error: String): Meta()

data class Schedule(val schedule: NestedList)

data class Response(val meta: Meta, val data: Schedule?)
