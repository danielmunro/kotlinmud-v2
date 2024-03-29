package kotlinmudv2.socket

import com.google.gson.GsonBuilder
import kotlinmudv2.mob.PlayerMob
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.NotYetConnectedException
import java.nio.channels.SocketChannel

class Client(private val socket: SocketChannel, var mob: PlayerMob? = null) {
    var delay = 0
    private var connected = true
    private val buffers = mutableListOf<String>()
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun write(message: String) {
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

    fun persistPlayerMob() {
        mob?.let {
            File("./players/${it.name}.json").writeText(gson.toJson(it))
        }
    }
}
