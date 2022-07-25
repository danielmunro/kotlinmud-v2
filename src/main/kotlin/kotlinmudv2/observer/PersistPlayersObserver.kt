package kotlinmudv2.observer

import com.google.gson.GsonBuilder
import kotlinmudv2.event.Event
import kotlinmudv2.socket.ClientService
import java.io.File

class PersistPlayersObserver(private val clientService: ClientService) : Observer {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    override suspend fun <T> invokeAsync(event: Event<T>) {
        println("persist players")
        clientService.getClients().forEach {
            File("./players/${it.mob.name}.json").writeText(
                gson.toJson(it.mob)
            )
        }
    }
}