package kotlinmudv2.socket

import kotlinmudv2.event.EventService
import kotlinmudv2.event.createClientConnectedEvent
import kotlinmudv2.log.logger
import kotlinmudv2.mob.MobService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.stream.Collectors

const val SELECT_TIMEOUT_MS: Long = 1
const val READ_BUFFER_SIZE_IN_BYTES = 1024

class SocketService(
    private val clientService: ClientService,
    private val eventService: EventService,
    private val mobService: MobService,
    private val port: Int = 0,
) {
    companion object {
        fun socketChannelFromKey(key: SelectionKey): SocketChannel {
            return key.channel() as SocketChannel
        }
    }

    private val selector: Selector = Selector.open()
    private val clients = mutableMapOf<SocketChannel, Client>()
    private val socket = ServerSocketChannel.open()
    private val logger = logger(this)

    init {
        val serverSocket = socket.socket()
        serverSocket.bind(InetSocketAddress(port))
        socket.configureBlocking(false)
        val ops = socket.validOps()
        socket.register(selector, ops, null)
    }

    suspend fun readIntoBuffers() {
        withContext(Dispatchers.IO) { selector.select(SELECT_TIMEOUT_MS) }
        val selectedKeys = selector.selectedKeys()
        selectedKeys.iterator().forEach {
            if (it.isAcceptable) {
                handleAccept(socket)
            } else if (it.isReadable) {
                handleRead(it)
            }
        }
        selectedKeys.clear()
    }

    fun getClientsWithBuffers(): List<Client> {
        return clients.values.stream()
            .filter { it.hasInput() }
            .collect(Collectors.toList())
    }

    private suspend fun handleAccept(newSocket: ServerSocketChannel) {
        configureAndAcceptSocket(newSocket)?.also {
            val testName = "foo"
            val client = if (mobService.isPlayerMob(testName)) {
                Client(it, mobService.hydratePlayerMob(testName))
            } else {
                Client(it, mobService.createMobEntity(testName, "bar", "baz", 1))
            }
            eventService.publish(createClientConnectedEvent(client))
            clients[it] = client
            clientService.addClient(client)
            logger.info("connection accepted :: {}", it.remoteAddress)
        }
    }

    private fun configureAndAcceptSocket(mySocket: ServerSocketChannel): SocketChannel? {
        return mySocket.accept()?.also {
            it.configureBlocking(false)
            it.register(selector, SelectionKey.OP_READ)
        }
    }

    private fun handleRead(key: SelectionKey) {
        val socket = socketChannelFromKey(key)
        try {
            readSocketIntoClient(socket)
        } catch (e: IOException) {
            logger.info("socket io exception, closing :: {}", socket.remoteAddress)
            socket.close()
        }
    }

    private fun readSocketIntoClient(socket: SocketChannel) {
        readSocket(socket).let {
            clients[socket]?.addInput(it)
            checkSocketForQuit(socket, it)
        }
    }

    private fun checkSocketForQuit(socket: SocketChannel, data: String) {
        if (data.equals("quit", ignoreCase = true)) {
            logger.info("connection closed :: ${socket.remoteAddress}")
            socket.close()
        }
    }

    private fun readSocket(socket: SocketChannel): String {
        val buffer = ByteBuffer.allocate(READ_BUFFER_SIZE_IN_BYTES)
        socket.read(buffer)
        return String(buffer.array()).trim { it <= ' ' }
    }
}
