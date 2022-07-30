package kotlinmudv2.mob

enum class Disposition {
    Standing,
    Fighting,
    Sitting,
    Sleeping,
}

fun anyDisposition(): List<Disposition> {
    return listOf(
        Disposition.Sleeping,
        Disposition.Standing,
        Disposition.Sitting,
        Disposition.Fighting,
    )
}

fun alertDisposition(): List<Disposition> {
    return listOf(
        Disposition.Fighting,
        Disposition.Standing,
        Disposition.Sitting,
    )
}
