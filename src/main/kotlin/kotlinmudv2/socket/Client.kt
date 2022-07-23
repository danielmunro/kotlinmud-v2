package kotlinmudv2.socket

import kotlinmudv2.mob.Mob
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.NotYetConnectedException
import java.nio.channels.SocketChannel

class Client(private val socket: SocketChannel, val mob: Mob) {
    var delay = 0
    private var connected = true
    private val buffers = mutableListOf<String>()

    fun writePrompt(message: String) {
        val buffer = ByteBuffer.allocate(1024)
        buffer.put("$message\n".toByteArray())
        buffer.flip()
        try {
            socket.write(buffer)
        } catch (e: ClosedChannelException) {
            connected = false
        } catch (e: IOException) {
            connected = false
        } catch (e: NotYetConnectedException) {
            connected = false
        }
    }

    fun hasInput(): Boolean {
        return buffers.isNotEmpty()
    }

    fun addInput(input: String) {
        buffers.add(input)
    }

    fun isDelayed(): Boolean {
        return delay > 0
    }

    fun shiftInput(): String {
        return buffers.removeAt(0)
    }
}
