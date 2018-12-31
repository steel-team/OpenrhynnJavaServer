/*
MIT License
-----------

Copyright (c) 2019 Ivan Yurkov (MB "Stylo tymas" http://steel-team.net)
Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/
package com.steelteam.openrhynn.logic;

import com.steelteam.openrhynn.data.DataSource;
import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.entities.Mob;
import com.steelteam.openrhynn.entities.Npc;
import com.steelteam.openrhynn.network.messages.server.GameCharacterHighscoreListEntry;
import com.steelteam.openrhynn.scripting.ScriptManager;
import com.steelteam.openrhynn.tickers.GCTicker;
import com.steelteam.openrhynn.tickers.NetworkTicker;
import com.steelteam.openrhynn.tickers.RespawnTicker;
import com.steelteam.openrhynn.tickers.VitalityRestoreTicker;
import com.steelteam.openrhynn.tickers.core.Ticker;
import com.steelteam.openrhynn.utilits.Time;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class PostInitializator {

    public static ArrayList<Ticker> sharedTickers = new ArrayList<>();

    public static void initializeServer() {
        /* initialize shared tickers */
        VitalityRestoreTicker vitalityRestoreTicker = new VitalityRestoreTicker();
        GCTicker gcTicker = new GCTicker();
        RespawnTicker respawnTicker = new RespawnTicker();
        NetworkTicker networkTicker = new NetworkTicker();


        vitalityRestoreTicker.createTicker(1000);
        gcTicker.createTicker(ServerConfig.gcTime);
        respawnTicker.createTicker(1000);
        networkTicker.createTicker(33); /* flush rate = 30 FPS*/


        sharedTickers.add(vitalityRestoreTicker);
        sharedTickers.add(gcTicker);
        sharedTickers.add(respawnTicker);
        sharedTickers.add(networkTicker);


        Random random = new Random();

        /* load mobs from DB and put on map */
        Connection conn = null;
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT object_id, tpl_id, world_id, spawn_x, spawn_y, respawn_delay FROM mob_spawning;");

            while (resultSet.next()) {
                Mob mob = new Mob();

                mob.objectId = resultSet.getInt(1);
                mob.connectedModel = ServerConfig.mob_templates.get(resultSet.getInt(2));
                mob.worldId = resultSet.getInt(3);
                mob.spawnX = resultSet.getInt(4);
                mob.spawnY = resultSet.getInt(5);
                mob.respawnDelay = resultSet.getInt(6);

                mob.x = mob.spawnX;
                mob.y = mob.spawnY;

                mob.talkTime = Time.getUnixTimeMillis() + random.nextInt(1000);

                mob.fillFromModel();

                World world = World.registeredWorlds.get(mob.worldId);
                world.addEntity(mob);
            }

            resultSet.close();
            state.close();

        } catch (Exception ex) {ex.printStackTrace(); } finally { try { conn.close(); } catch (Exception e) {} }



        /* load npcs from DB and put on map */
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT object_id, tpl_id, world_id, spawn_x, spawn_y FROM npc_spawning;");

            while (resultSet.next()) {
                Npc npc = new Npc();

                npc.objectId = resultSet.getInt(1);
                npc.connectedModel = ServerConfig.npc_templates.get(resultSet.getInt(2));
                npc.worldId = resultSet.getInt(3);
                npc.spawnX = resultSet.getInt(4);
                npc.spawnY = resultSet.getInt(5);

                npc.x = npc.spawnX;
                npc.y = npc.spawnY;

                npc.talkTime = Time.getUnixTimeMillis() + random.nextInt(1000);

                npc.fillFromModel();

                World world = World.registeredWorlds.get(npc.worldId);
                world.addEntity(npc);
            }

            resultSet.close();
            state.close();

        } catch (Exception ex) {ex.printStackTrace(); } finally { try { conn.close(); } catch (Exception e) {} }
    }
}
