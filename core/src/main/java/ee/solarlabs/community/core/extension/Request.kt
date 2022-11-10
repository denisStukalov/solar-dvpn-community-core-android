package ee.solarlabs.community.core.extension

fun String.anyToNull() = if (this.equals("any", true)) null else this