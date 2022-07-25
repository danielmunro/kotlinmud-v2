package kotlinmudv2.socket

class ClientService {
    private val clients = mutableListOf<Client>()

    fun addClient(client: Client) {
        clients.add(client)
    }

    fun getClients(): List<Client> {
        return clients
    }
}
