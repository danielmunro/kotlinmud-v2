package kotlinmudv2.game

import io.github.serpro69.kfaker.Faker
import kotlinmudv2.action.ActionService
import kotlinmudv2.action.Command
import kotlinmudv2.action.ContextService
import kotlinmudv2.action.actions.combat.createFleeAction
import kotlinmudv2.action.actions.combat.createKillAction
import kotlinmudv2.action.actions.combat.createKillErrorAction
import kotlinmudv2.action.actions.commerce.createBuyAction
import kotlinmudv2.action.actions.commerce.createBuyErrorAction
import kotlinmudv2.action.actions.commerce.createListAction
import kotlinmudv2.action.actions.commerce.createSellAction
import kotlinmudv2.action.actions.commerce.createSellErrorAction
import kotlinmudv2.action.actions.createDownAction
import kotlinmudv2.action.actions.createEastAction
import kotlinmudv2.action.actions.createLevelAction
import kotlinmudv2.action.actions.createNorthAction
import kotlinmudv2.action.actions.createRecallAction
import kotlinmudv2.action.actions.createSouthAction
import kotlinmudv2.action.actions.createUpAction
import kotlinmudv2.action.actions.createWestAction
import kotlinmudv2.action.actions.informational.createExitsAction
import kotlinmudv2.action.actions.informational.createInventoryAction
import kotlinmudv2.action.actions.informational.createLookAction
import kotlinmudv2.action.actions.informational.createLookAtItemInInventoryAction
import kotlinmudv2.action.actions.informational.createLookAtItemInRoomAction
import kotlinmudv2.action.actions.informational.createLookAtMobInRoomAction
import kotlinmudv2.action.actions.manipulate.createCloseDirectionAction
import kotlinmudv2.action.actions.manipulate.createCloseErrorAction
import kotlinmudv2.action.actions.manipulate.createCloseExitAction
import kotlinmudv2.action.actions.manipulate.createDropAction
import kotlinmudv2.action.actions.manipulate.createDropErrorAction
import kotlinmudv2.action.actions.manipulate.createGetAction
import kotlinmudv2.action.actions.manipulate.createGetErrorAction
import kotlinmudv2.action.actions.manipulate.createOpenDirectionAction
import kotlinmudv2.action.actions.manipulate.createOpenErrorAction
import kotlinmudv2.action.actions.manipulate.createOpenExitAction
import kotlinmudv2.action.actions.manipulate.createQuaffAction
import kotlinmudv2.action.actions.manipulate.createQuaffErrorAction
import kotlinmudv2.action.actions.manipulate.createRemoveAction
import kotlinmudv2.action.actions.manipulate.createRemoveErrorAction
import kotlinmudv2.action.actions.manipulate.createSacrificeAction
import kotlinmudv2.action.actions.manipulate.createSacrificeErrorAction
import kotlinmudv2.action.actions.manipulate.createWearAction
import kotlinmudv2.action.actions.manipulate.createWearErrorAction
import kotlinmudv2.action.actions.social.createGossipAction
import kotlinmudv2.action.actions.social.createSayAction
import kotlinmudv2.action.skills.createCastAction
import kotlinmudv2.action.skills.createSkillAction
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.item.ItemService
import kotlinmudv2.mob.DeathService
import kotlinmudv2.mob.FightService
import kotlinmudv2.mob.MobService
import kotlinmudv2.mob.races.createDwarfRace
import kotlinmudv2.mob.races.createElfRace
import kotlinmudv2.mob.races.createHalflingRace
import kotlinmudv2.mob.races.createHumanRace
import kotlinmudv2.mob.races.createOrcRace
import kotlinmudv2.observer.AffectDecayObserver
import kotlinmudv2.observer.ClientConnectedObserver
import kotlinmudv2.observer.FightObserver
import kotlinmudv2.observer.HitObserver
import kotlinmudv2.observer.Observer
import kotlinmudv2.observer.PersistPlayersObserver
import kotlinmudv2.observer.ProcessClientBufferObserver
import kotlinmudv2.observer.ReadClientsObserver
import kotlinmudv2.observer.RegenObserver
import kotlinmudv2.observer.RespawnObserver
import kotlinmudv2.room.RoomService
import kotlinmudv2.skill.skills.cleric.createHealSkill
import kotlinmudv2.skill.skills.cleric.createLayOnHandsSkill
import kotlinmudv2.skill.skills.cleric.createSanctuarySkill
import kotlinmudv2.skill.skills.mage.createFireballSkill
import kotlinmudv2.skill.skills.mage.createMagicMissileSkill
import kotlinmudv2.skill.skills.thief.createBackStabSkill
import kotlinmudv2.skill.skills.thief.createHamstringSkill
import kotlinmudv2.skill.skills.warrior.createBashSkill
import kotlinmudv2.skill.skills.warrior.createDirtKickSkill
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
        bindSingleton { DeathService(instance(), instance()) }
        bindSingleton { FightService(instance(), instance(), instance(), instance()) }
        bindSingleton { RoomService(instance()) }
        bindSingleton { MobService(instance()) }
        bindSingleton { AuthService(instance()) }
        bindSingleton { WebServerService(instance(), instance(), instance()) }
        bindSingleton { ContextService(instance(), instance(), instance(), instance()) }
        bindSingleton { Faker() }
        bindSingleton {
            ActionService(
                instance(),
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
                createExitsAction(),
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
                createSkillAction(Command.Bash),
                createSkillAction(Command.Backstab),
                createCastAction(),
            )
        }

        // skills
        bindSingleton {
            listOf(
                // warrior
                createBashSkill(),
                createDirtKickSkill(),

                // thief
                createBackStabSkill(),
                createHamstringSkill(),

                // cleric
                createHealSkill(),
                createLayOnHandsSkill(),
                createSanctuarySkill(),

                // mage
                createMagicMissileSkill(),
                createFireballSkill(),
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
                instance(),
            )
        }
        bindProvider(tag = "fight") { FightObserver(instance()) }
        bindProvider(tag = "regen") { RegenObserver(instance()) }
        bindProvider(tag = "affectDecay") { AffectDecayObserver(instance(), instance()) }
        bindProvider(tag = "respawn") { RespawnObserver(instance()) }
        bindProvider(tag = "hit") { HitObserver(instance(), instance()) }

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
                Pair(
                    EventType.Hit,
                    listOf(
                        instance(tag = "hit"),
                    ),
                ),
            )
        }
    }
}
