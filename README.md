# OpenrhynnJavaServer

This repository contains all openrhynn java server sources.
Some parts of code belongs to macrolutions LTD and released under original license https://github.com/marlowe-fw/Rhynn/blob/master/LICENSE

This server is recreation of original C# version of openrhynn server, some parts of code were directly ported so code quality very ugly and should be rewritten.

Changes and notes:
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
- configure netty properly to achive maximum perfomance
- rewrite network send logic, it's messy(I done a lot of experiments to achive high perfomance, so that's the reason)
- rewrite MOBS/NPCS network logic on both client and server (legacy network format from C# assumes that mob is character with id > 100000 and npc is character with id > 200000)
- rewrite drop system
- add quests system
- add trading system
- fix friends bugs (rewrite network logic?)
