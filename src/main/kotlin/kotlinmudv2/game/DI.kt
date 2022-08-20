package kotlinmudv2.game

import kotlinmudv2.action.ActionService
import kotlinmudv2.action.ContextService
import kotlinmudv2.action.actions.createBuyAction
import kotlinmudv2.action.actions.createBuyErrorAction
import kotlinmudv2.action.actions.createCloseDirectionAction
import kotlinmudv2.action.actions.createCloseErrorAction
import kotlinmudv2.action.actions.createCloseExitAction
import kotlinmudv2.action.actions.createDownAction
import kotlinmudv2.action.actions.createDropAction
import kotlinmudv2.action.actions.createDropErrorAction
import kotlinmudv2.action.actions.createEastAction
import kotlinmudv2.action.actions.createFleeAction
import kotlinmudv2.action.actions.createGetAction
import kotlinmudv2.action.actions.createGetErrorAction
import kotlinmudv2.action.actions.createGossipAction
import kotlinmudv2.action.actions.createInventoryAction
import kotlinmudv2.action.actions.createKillAction
import kotlinmudv2.action.actions.createKillErrorAction
import kotlinmudv2.action.actions.createLevelAction
import kotlinmudv2.action.actions.createListAction
import kotlinmudv2.action.actions.createLookAction
import kotlinmudv2.action.actions.createLookAtItemInInventoryAction
import kotlinmudv2.action.actions.createLookAtItemInRoomAction
import kotlinmudv2.action.actions.createLookAtMobInRoomAction
import kotlinmudv2.action.actions.createNorthAction
import kotlinmudv2.action.actions.createOpenDirectionAction
import kotlinmudv2.action.actions.createOpenErrorAction
import kotlinmudv2.action.actions.createOpenExitAction
import kotlinmudv2.action.actions.createQuaffAction
import kotlinmudv2.action.actions.createQuaffErrorAction
import kotlinmudv2.action.actions.createRecallAction
import kotlinmudv2.action.actions.createRemoveAction
import kotlinmudv2.action.actions.createRemoveErrorAction
import kotlinmudv2.action.actions.createSacrificeAction
import kotlinmudv2.action.actions.createSacrificeErrorAction
import kotlinmudv2.action.actions.createSayAction
import kotlinmudv2.action.actions.createSellAction
import kotlinmudv2.action.actions.createSellErrorAction
import kotlinmudv2.action.actions.createSouthAction
import kotlinmudv2.action.actions.createUpAction
import kotlinmudv2.action.actions.createWearAction
import kotlinmudv2.action.actions.createWearErrorAction
import kotlinmudv2.action.actions.createWestAction
import kotlinmudv2.action.skills.createBackstabAction
import kotlinmudv2.action.skills.createBashAction
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.fight.FightService
import kotlinmudv2.item.ItemService
import kotlinmudv2.mob.MobService
import kotlinmudv2.mob.races.createDwarfRace
import kotlinmudv2.mob.races.createElfRace
import kotlinmudv2.mob.races.createHalflingRace
import kotlinmudv2.mob.races.createHumanRace
import kotlinmudv2.mob.races.createOrcRace
import kotlinmudv2.observer.AffectDecayObserver
import kotlinmudv2.observer.ClientConnectedObserver
import kotlinmudv2.observer.FightObserver
import kotlinmudv2.observer.Observer
import kotlinmudv2.observer.PersistPlayersObserver
import kotlinmudv2.observer.ProcessClientBufferObserver
import kotlinmudv2.observer.ReadClientsObserver
import kotlinmudv2.observer.RegenObserver
import kotlinmudv2.observer.RespawnObserver
import kotlinmudv2.room.RoomService
import kotlinmudv2.skill.skills.createBackStabSkill
import kotlinmudv2.skill.skills.createBashSkill
import kotlinmudv2.skill.skills.createHealSkill
import kotlinmudv2.skill.skills.createMagicMissileSkill
import kotlinmudv2.socket.AuthService
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.SocketService
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

fun createContainer(port: Int): DI {
    return DI {
        // services
        bindSingleton { EventService() }
        bindSingleton { ClientService() }
        bindSingleton { ItemService() }
        bindSingleton { FightService(instance(), instance()) }
        bindSingleton { RoomService(instance()) }
        bindSingleton { MobService(instance()) }
        bindSingleton { AuthService(instance()) }
        bindSingleton { WebServerService(instance(), instance(), instance()) }
        bindSingleton { ContextService(instance(), instance(), instance(), instance()) }
        bindSingleton {
            ActionService(
                instance(),
                instance(),
                instance(),
                instance(),
            )
        }
        bindSingleton { SocketService(instance(), instance(), instance(), port) }
        bindSingleton { GameService(instance()) }

        // races
        bindSingleton {
            listOf(
                createHumanRace(),
                createOrcRace(),
                createElfRace(),
                createHalflingRace(),
                createDwarfRace(),
            )
        }

        // actions
        bindSingleton {
            listOf(
                createNorthAction(),
                createSouthAction(),
                createEastAction(),
                createWestAction(),
                createUpAction(),
                createDownAction(),
                createLookAtMobInRoomAction(),
                createLookAtItemInRoomAction(),
                createLookAtItemInInventoryAction(),
                createLookAction(),
                createQuaffAction(),
                createQuaffErrorAction(),
                createSayAction(),
                createGossipAction(),
                createKillAction(),
                createKillErrorAction(),
                createFleeAction(),
                createGetAction(),
                createGetErrorAction(),
                createDropAction(),
                createDropErrorAction(),
                createWearAction(),
                createWearErrorAction(),
                createRemoveAction(),
                createRemoveErrorAction(),
                createOpenExitAction(),
                createOpenDirectionAction(),
                createOpenErrorAction(),
                createCloseExitAction(),
                createCloseDirectionAction(),
                createCloseErrorAction(),
                createRecallAction(),
                createSacrificeAction(),
                createSacrificeErrorAction(),
                createListAction(),
                createBuyAction(),
                createBuyErrorAction(),
                createSellAction(),
                createSellErrorAction(),
                createInventoryAction(),
                createLevelAction(),
                createBashAction(),
                createBackstabAction(),
            )
        }

        // skills
        bindSingleton {
            listOf(
                createBashSkill(),
                createBackStabSkill(),
                createHealSkill(),
                createMagicMissileSkill(),
            )
        }

        // observers
        bindProvider(tag = "persistPlayers") { PersistPlayersObserver(instance()) }
        bindProvider(tag = "clientConnected") { ClientConnectedObserver() }
        bindProvider(tag = "readClients") { ReadClientsObserver(instance()) }
        bindProvider(tag = "processClientBuffer") {
            ProcessClientBufferObserver(
                instance(),
                instance(),
                instance(),
                instance(),
            )
        }
        bindProvider(tag = "fight") { FightObserver(instance()) }
        bindProvider(tag = "regen") { RegenObserver(instance()) }
        bindProvider(tag = "affectDecay") { AffectDecayObserver(instance(), instance()) }
        bindProvider(tag = "respawn") { RespawnObserver(instance()) }

        // list of observers
        bindSingleton<Map<EventType, List<Observer>>>(tag = "observers") {
            mapOf(
                Pair(
                    EventType.ClientConnected,
                    listOf(
                        instance(tag = "clientConnected"),
                    ),
                ),
                Pair(
                    EventType.GameLoop,
                    listOf(
                        instance(tag = "readClients"),
                        instance(tag = "processClientBuffer"),
                    ),
                ),
                Pair(
                    EventType.Tick,
                    listOf(
                        instance(tag = "persistPlayers"),
                        instance(tag = "regen"),
                        instance(tag = "affectDecay"),
                        instance(tag = "respawn"),
                    ),
                ),
                Pair(
                    EventType.Pulse,
                    listOf(
                        instance(tag = "fight"),
                    ),
                ),
            )
        }
    }
}
