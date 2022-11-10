package ee.solarlabs.ui

sealed class ConnectionState {
    object CheckPermission : ConnectionState()
    object AttemptingConnection : ConnectionState()
    object Done : ConnectionState()
}