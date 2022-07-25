package kotlinmudv2.socket

import kotlinmudv2.mob.MobService

class AuthService (
    private val mobService: MobService,
) {
    private val auth = mutableMapOf<Client, Auth>()

    fun handleInput(client: Client) {
        ensureClientAddedToAuth(client)
        val input = client.shiftInput()
        auth[client]!!.let {
            when (it) {
                Auth.Name -> handleName(client, input)
                Auth.Password -> null
            }
        }
    }

    private fun handleName(client: Client, input: String) {
        if (mobService.isPlayerMob(input)) {
            client.mob = mobService.hydratePlayerMob(input)
        }
    }

    private fun ensureClientAddedToAuth(client: Client) {
        if (auth[client] == null) {
            auth[client] = Auth.Name
        }
    }
}