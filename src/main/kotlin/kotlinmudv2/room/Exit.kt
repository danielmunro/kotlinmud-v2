package kotlinmudv2.room

class Exit(
    val direction: Direction,
    val roomId: Int,
    val keyword: String? = null,
    val status: ExitStatus? = null,
    val lockId: Int? = null,
    val keyId: Int? = null,
)
