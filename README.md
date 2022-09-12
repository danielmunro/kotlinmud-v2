# kotlinmud-v2

A rewrite of [Kotlinmud](https://github.com/danielmunro/kotlinmud).

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=danielmunro_kotlinmudv2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=danielmunro_kotlinmudv2)

## Todo - M1 - Done

* Ticks - done
* Player persistence - done
* Web server - done
  * mob, item, rooms
* Auth - done
  * login
  * create mob

## Todo - M2 - Done

* Social - done
  * actions: say, gossip
* Potions - done
  * action: quaff
* Fight - done
  * actions: kill, flee
  * service

## Todo - M3 - done

* Furniture -- done
* Better action error handling - done
  * get - item not found
  * kill - mob not found
* Races - done
* Equipment - done
  * actions: wear, remove
  * attributes
* Movement cost - done

## Todo - M4 - done

* Affects - done
  * mobs
  * items
  * observer to count down timeout, remove affect
* Regen - done
* Dispositions - done

## Todo - M5 - done

* Moosehead - initial - done
  * rooms
  * mobs
  * items
    * rooms
    * mob inventory
    * mob equipped

## Todo - M6 - done

* Add level property to mob and items - done
* Item material - done
* Item type - done
* Shops - done

## Todo - M7 - done
* Leveling - done
* Doors - done
* Actions:
  * level - done
  * drop - done
  * open, door - done
  * close, door - done
  * open, direction - done
  * close, direction - done
  * open, error - done
  * close, error - done

## Todo - M8 - done
* Classes - done
* Skills - done
* Spells - done

## Uncategorized Todos
* damage and healing as events
* more skills
* more item, mob, room properties migrated from mhs source
* skills:
  * no list for roles - done
  * affect for not stacking
  * equipped weapon syntax