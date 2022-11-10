package co.sentinel.dvpn.domain.core.extension

import java.nio.ByteBuffer

fun bytesToUnsignedShort(byte1: Byte, byte2: Byte, bigEndian: Boolean): Int {
    if (bigEndian)
        return (((byte1.toInt() and 255) shl 8) or (byte2.toInt() and 255))
    return (((byte2.toInt() and 255) shl 8) or (byte1.toInt() and 255))
}

fun Short.reverseBytes(): Short {
    val v0 = ((this.toInt() ushr 0) and 0xFF)
    val v1 = ((this.toInt() ushr 8) and 0xFF)
    return ((v1 and 0xFF) or (v0 shl 8)).toShort()
}

fun Int.reverseBytes(): Int {
    val v0 = ((this ushr 0) and 0xFF)
    val v1 = ((this ushr 8) and 0xFF)
    val v2 = ((this ushr 16) and 0xFF)
    val v3 = ((this ushr 24) and 0xFF)
    return (v0 shl 24) or (v1 shl 16) or (v2 shl 8) or (v3 shl 0)
}

fun Long.reverseBytes(): Long {
    val v0 = (this ushr 0).toInt().reverseBytes().toLong() and 0xFFFFFFFFL
    val v1 = (this ushr 32).toInt().reverseBytes().toLong() and 0xFFFFFFFFL
    return (v0 shl 32) or (v1 shl 0)
}

fun Long.toByteArray(): ByteArray {
    return ByteBuffer.allocate(8).putLong(this).array()
}