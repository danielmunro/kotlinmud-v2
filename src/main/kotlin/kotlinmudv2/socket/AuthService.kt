package kotlinmudv2.socket

import kotlinmudv2.crypto.isExpectedPassword
import kotlinmudv2.mob.MobService
import kotlinmudv2.mob.PlayerMob
import kotlinmudv2.mob.Race

class AuthService(
    private val mobService: MobService,
) {
    private val auth = mutableMapOf<Client, Auth>()
    private val context = mutableMapOf<Client, MutableMap<String, Any>>()

    fun handleInput(client: Client) {
        ensureClientAddedToAuth(client)
        val input = client.shiftInput()
        auth[client]?.let {
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
            client.write("Password: ")
            auth[client] = Auth.Password
            return
        }
        context[client] = mutableMapOf(
            Pair("mob", input)
        )
        auth[client] = Auth.NewPassword
        client.write("new mob. What's your password? ")
    }

    private fun handlePassword(client: Client, input: String) {
        val mob = context[client]!!["mob"]!! as PlayerMob
        if (!isExpectedPassword(input, mob.salt, mob.password)) {
            client.write("wrong password")
            return
        }
        client.mob = mob
        client.write("login successful, welcome back!")
    }

    private fun handleNewPassword(client: Client, input: String) {
        context[client]!!["newPassword"] = input
        auth[client] = Auth.NewPasswordConfirm
        client.write("Again: ")
    }

    private fun handleNewPasswordConfirm(client: Client, input: String) {
        val pw = context[client]!!["newPassword"]
        if (pw != input) {
            client.write("passwords do not match")
            return
        }
        client.mob = mobService.createPlayerMob(
            context[client]!!["mob"]!! as String,
            input,
            Race.Human,
        )
        client.write("new character creation success!")
        client.persistPlayerMob()
    }

    private fun ensureClientAddedToAuth(client: Client) {
        if (auth[client] == null) {
            auth[client] = Auth.Name
        }
    }
}
