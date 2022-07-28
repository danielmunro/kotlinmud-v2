package kotlinmudv2.socket

import kotlinmudv2.mob.PlayerMob

class ClientService {
    private val clients = mutableListOf<Client>()

    fun addClient(client: Client) {
        clients.add(client)
    }

    fun getClients(): List<Client> {
        return clients
    }

    fun persistPlayerMobs() {
        clients.forEach { it.persistPlayerMob() }
    }

    fun sendToRoom(roomMessage: RoomMessage) {
        val mobsInRoom = mutableMapOf<PlayerMob, Client>()
        clients.filter { it.mob?.roomId == roomMessage.actionCreator.roomId }.forEach {
            mobsInRoom[it.mob as PlayerMob] = it
        }

        // send to action creator
        mobsInRoom[roomMessage.actionCreator]?.write(roomMessage.toActionCreator)

        // send to target
        roomMessage.target?.also { mobsInRoom[roomMessage.target]?.write(roomMessage.toTarget!!) }

        // send to observers
        mobsInRoom.filterValues {
            it.mob != roomMessage.actionCreator && it.mob != roomMessage.target
        }.forEach {
            it.value.write(roomMessage.toObservers)
        }
    }
}
