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
                Auth.NewPassword -> handleNewPassword(client, input)
                Auth.NewPasswordConfirm -> handleNewPasswordConfirm(client, input)
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
        auth[client] = Auth.NewPassword
        client.writePrompt("new mob. What's your password? ")
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

    private fun handleNewPassword(client: Client, input: String) {
        context[client]!!["newPassword"] = input
        auth[client] = Auth.NewPasswordConfirm
        client.writePrompt("Again: ")
    }

    private fun handleNewPasswordConfirm(client: Client, input: String) {
        val pw = context[client]!!["newPassword"]
        if (pw != input) {
            client.writePrompt("passwords do not match")
            return
        }
        client.mob = mobService.createPlayerMob(
            context[client]!!["mob"]!! as String,
            input,
        )
        client.writePrompt("new character creation success!")
    }

    private fun ensureClientAddedToAuth(client: Client) {
        if (auth[client] == null) {
            auth[client] = Auth.Name
        }
    }
}