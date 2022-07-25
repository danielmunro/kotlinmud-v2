package kotlinmudv2.socket

import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobService
import kotlinmudv2.mob.PlayerMob

class AuthService (
    private val mobService: MobService,
) {
    private val auth = mutableMapOf<Client, Auth>()
    private val context = mutableMapOf<Client, MutableMap<String, Any>>()

    fun handleInput(client: Client) {
        ensureClientAddedToAuth(client)
        val input = client.shiftInput()
        auth[client]!!.let {
            when (it) {
                Auth.Name -> handleName(client, input)
                Auth.Password -> handlePassword(client, input)
            }
        }
    }

    private fun handleName(client: Client, input: String) {
        if (mobService.isPlayerMob(input)) {
            context[client] = mutableMapOf(
                Pair("mob", mobService.hydratePlayerMob(input))
            )
            client.writePrompt("Password: ")
            auth[client] = Auth.Password
            return
        }
        context[client] = mutableMapOf(
            Pair("mob", input)
        )
    }

    private fun handlePassword(client: Client, input: String) {
        val mob = context[client]!!["mob"]!! as PlayerMob
        if (input != mob.password) {
            client.writePrompt("wrong password")
            return
        }
        client.mob = mob
        client.writePrompt("login successful, welcome back!")
    }

    private fun ensureClientAddedToAuth(client: Client) {
        if (auth[client] == null) {
            auth[client] = Auth.Name
        }
    }
}