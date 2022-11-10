package co.sentinel.dvpn.hub.failure

const val REQUEST_BODY_VALIDATION_FAILED = 1
const val REQUEST_BODY_DATA_VALIDATION_FAILED = 2
const val SESSION_VALIDATION_FAILED = 3
const val SUBSCRIPTION_VALIDATION_FAILED = 4
const val NODE_VALIDATION_FAILED = 5
const val DUPLICATED_SESSION_REQUEST = 6
const val QUERY_SESSION_FAILED = 7
const val REMOVE_PEER_FAILED = 8
const val QUERY_QUOTA_FAILED = 9 // can mean quota for address does not exist
const val QUOTA_EXCEEDED = 10
const val MAX_PEERS_LIMIT_REACHED = 11