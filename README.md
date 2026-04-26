# OpenrhynnJavaServer

This repository contains all openrhynn java server sources.
Some parts of code belongs to macrolutions LTD and released under original license https://github.com/marlowe-fw/Rhynn/blob/master/LICENSE

This server is recreation of original C# version of openrhynn server, some parts of code were directly ported so code quality very ugly and should be rewritten.

## Neighbour repositories

[Openrhynn Website](https://github.com/steel-team/OpenrhynnWebsite) - a brand new `OpenRhynn` website, rewritten from scratch, made with `Nuxt` and `Nuxt UI`

[Openrhynn Server-side content](https://github.com/steel-team/OpenrhynnServerContent) - assets required to properly run server

[Openrhynn Client](https://github.com/steel-team/OpenrhynnClient) - fork of marlowe-fw/Rhynn, with removed original server and world builder (only client with OR modifications)

[Openrhynn Runner](https://github.com/steel-team/OpenRhynnRunner) - fork of zb3/freej2me-web which able to run `OpenRhynn` in browser with `WebSocket` transport

## Quick start

You may run server by

```
docker compose up
```

Please note, this compose file will download artifacts from github releases, if you'd like local development with docker, you have to modify it and add build step with maven compile.
Build is not reliable in podman env from my experience, so I decided to go with release download approach.

## Changes and notes:

- server now can handle 100+ players in one map (but you should modify client. change DEFAULT_PACKETPERLOOP from 8 to high value like 5000)
- server is stable (no crashes)
- all mobs work properly (no movement bugs like in C# version)
- this server still miss some features (quests, trading) but I'll try to write them when I have time
- code quality even in new parts of code is bad, sorry :)

Used libriries:

- netty
- dbcp2
- json

TO-DO:

- rewrite network send logic, it's messy(I done a lot of experiments to achive high perfomance, so that's the reason)
- rewrite MOBS/NPCS network logic on both client and server (legacy network format from C# assumes that mob is character with id > 100000 and npc is character with id > 200000)
- rewrite drop system
- add quests system
- add trading system
- fix friends bugs (rewrite network logic?)

## Compile:

```
mvn package
```

## Development:

`VSCodium` is recommended for development
