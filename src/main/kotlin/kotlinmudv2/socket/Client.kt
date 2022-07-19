package kotlinmudv2.socket

import kotlinmudv2.mob.Mob
import java.nio.channels.SocketChannel

class Client(val socket: SocketChannel) {
    var connected = false
    var mob: Mob? = null
    private val buffers = mutableListOf<String>()

    fun hasInput(): Boolean {
        return buffers.isNotEmpty()
    }

    fun addInput(input: String) {
        buffers.add(input)
    }
}