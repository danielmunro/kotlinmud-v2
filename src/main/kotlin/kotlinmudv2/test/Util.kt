package kotlinmudv2.test

fun getIdentifyingWord(input: String): String? {
    return input.split(" ").find { it.length > 1 }
}
