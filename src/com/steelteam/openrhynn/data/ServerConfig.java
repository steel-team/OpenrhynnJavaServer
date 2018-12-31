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
package com.steelteam.openrhynn.data;

import com.steelteam.openrhynn.enums.AIType;
import com.steelteam.openrhynn.enums.GraphicsType;
import com.steelteam.openrhynn.enums.ItemType;
import com.steelteam.openrhynn.enums.UsageType;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.*;
import com.steelteam.openrhynn.scripting.ScriptManager;
import com.steelteam.openrhynn.utilits.Time;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ServerConfig {
    /* master server (servers list) */
    public static ArrayList<ServerEntry> servers = new ArrayList<>();

    /* server version */
    public static int versionHigh = 1;
    public static int versionLow = 4;
    public static int versionLowSub = 5;
    public static int revision = 4;
    public static int gcTime = 60000;

    /* */
    public static int port = 23179;
    public static int messageEncodeNumber = 0;
    public static HashMap<String, Language> languages = new HashMap<>();
    public static ArrayList<CharacterClassModel> classes = new ArrayList<>();
    public static HashMap<Integer, CharacterPointData> pointDatas = new HashMap<>();
    public static HashMap<Integer, MobTemplate> mob_templates = new HashMap<>();
    public static HashMap<Integer, NpcTemplate> npc_templates = new HashMap<>();


    /* game config */
    public static int vitalityIncrease = 0;
    public static int maxLevel = 200;
    public static int pointsPerLevel = 6;
    public static int maxInventorySize = 22;
    public static int defaultWeaponRange = 36;
    public static int defaultWeaponCharge = 1400;
    public static int vitalityIncreaseDamageWaitTime = 5000;
    public static int defaultPlayerSpeed = 3;
    public static int unstuckWorldId = 100132;
    public static int prisonWorldId = 100146;
    public static boolean pickupableAutoRemove = true;
    public static int pickupableRemoveTime = 120000; //2 minutes
    public static int maxFriends = 24;


    /* network */
    public static int netBufferSize = 2048;


    /* character create */
    public static int maxCharacters = 5;

    public static void loadConfig() {
        try {
            Path path = Paths.get("./configs/servers.json");
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(path)));
            JSONArray arr = obj.getJSONArray("servers");
            for(int i = 0; i < arr.length(); i++) {
                ServerConfig.servers.add(new ServerEntry(arr.getJSONObject(i).getString("name"), arr.getJSONObject(i).getString("ip")));
            }


            /* app.json */
            path = Paths.get("./configs/app.json");
            obj = new JSONObject(new String(Files.readAllBytes(path)));
            port = obj.getInt("port");
            String[] v = obj.getString("version").split("\\.");
            versionHigh = Integer.parseInt(v[0]);
            versionLow = Integer.parseInt(v[1]);
            versionLowSub = Integer.parseInt(v[2]);
            revision = obj.getInt("revision");
            gcTime = obj.getInt("gc_time");

            /* game.json */
            path = Paths.get("./configs/game.json");
            obj = new JSONObject(new String(Files.readAllBytes(path)));
            vitalityIncrease = obj.getInt("vitality_increase");



            path = Paths.get("./localization/definition.json");
            obj = new JSONObject(new String(Files.readAllBytes(path)));
            arr = obj.getJSONArray("languages");
            for(int i = 0; i < arr.length(); i++) {
                Language lang = new Language();
                lang.prepareStrings(arr.getString(i));
            }

            path = Paths.get("./data/character_classes.json");
            obj = new JSONObject(new String(Files.readAllBytes(path)));
            arr = obj.getJSONArray("classes");
            for(int i = 0; i < arr.length(); i++) {
                JSONObject cobj = arr.getJSONObject(i);
                CharacterClassModel model = new CharacterClassModel();
                model.displayName = cobj.getString("display_name");
                model.manaregenerateModifier = cobj.getInt("manaregenerate_modifier");
                model.manaregenerateBase = cobj.getInt("manaregenerate_base");
                model.healthregenerateModifier = cobj.getInt("healthregenerate_modifier");
                model.healthregenerateBase = cobj.getInt("healthregenerate_base");
                model.magicModifier = cobj.getInt("magic_modifier");
                model.magicBase = cobj.getInt("magic_base");
                model.skillModifier = cobj.getInt("skill_modifier");
                model.skillBase = cobj.getInt("skill_base");
                model.damageModifier = cobj.getInt("damage_modifier");
                model.damageBase = cobj.getInt("damage_base");
                model.defenseModifier = cobj.getInt("defense_modifier");
                model.defenseBase = cobj.getInt("defense_base");
                model.attackModifier = cobj.getInt("attack_modifier");
                model.attackBase = cobj.getInt("attack_base");
                model.manaModifier = cobj.getInt("mana_modifier");
                model.manaBase = cobj.getInt("mana_base");
                model.healthModifier = cobj.getInt("health_modifier");
                model.healthBase = cobj.getInt("health_base");
                model.graphicsDim = cobj.getInt("graphics_dim");
                model.graphicsY = cobj.getInt("graphics_y");
                model.graphicsX = cobj.getInt("graphics_x");
                model.graphicsId = cobj.getInt("graphics_id");
                model.premiumOnly = cobj.getBoolean("premium_only");
                model.classId = cobj.getInt("id");
                classes.add(model);
            }


            /* point data */
            path = Paths.get("./data/character_point_data.json");
            obj = new JSONObject(new String(Files.readAllBytes(path)));
            arr = obj.getJSONArray("point_data");
            for(int i = 0; i < arr.length(); i++) {
                JSONObject cobj = arr.getJSONObject(i);
                CharacterPointData model = new CharacterPointData();
                model.id = cobj.getInt("id");
                model.health_base = cobj.getInt("health_base");
                model.mana_base = cobj.getInt("attack_base");
                model.attack_base = cobj.getInt("attack_base");
                model.defense_base = cobj.getInt("defense_base");
                model.damage_base = cobj.getInt("damage_base");
                model.skill_base = cobj.getInt("skill_base");
                model.magic_base = cobj.getInt("magic_base");
                pointDatas.put(model.id, model);
            }

            /* load graphics */
            path = Paths.get("./data/tiles/definition.json");
            obj = new JSONObject(new String(Files.readAllBytes(path)));
            arr = obj.getJSONArray("tiles");
            for(int i = 0; i < arr.length(); i++) {
                JSONObject cobj = arr.getJSONObject(i);
                Graphics graphics = new Graphics();
                graphics.id = cobj.getInt("id");
                graphics.path = cobj.getString("path");
                graphics.graphicsType = GraphicsType.fromString(cobj.getString("type"));
                Path p = Paths.get("./data/tiles/" + GraphicsType.toString(graphics.graphicsType) + "/" + graphics.path);
                graphics.data = Files.readAllBytes(p);
                graphics.length = graphics.data.length;

                Graphics.registeredGraphics.put(graphics.id, graphics);
            }

            /* load item templates */
            File file = new File("./data/item_templates");
            String[] items = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isFile();
                }
            });
            for(String item : items) {
                path = Paths.get("./data/item_templates/" + item);
                obj = new JSONObject(new String(Files.readAllBytes(path)));

                ItemTemplate itpl = new ItemTemplate();
                itpl.id = obj.getInt("id");
                itpl.type = ItemType.fromInt(obj.getInt("type"));
                itpl.set_id = obj.getInt("set_id");
                itpl.graphics_id = obj.getInt("graphics_id");
                itpl.graphics_x = obj.getInt("graphics_x");
                itpl.graphics_y = obj.getInt("graphics_y");
                itpl.name = obj.getString("name");
                itpl.description = obj.getString("description");
                itpl.available_status = obj.getString("available_status");
                itpl.can_sell = obj.getInt("can_sell");
                itpl.can_drop = obj.getInt("can_drop");
                itpl.max_units = obj.getInt("max_units");
                itpl.price = obj.getInt("price");
                itpl.health_effect = obj.getInt("health_effect");
                itpl.mana_effect = obj.getInt("mana_effect");
                itpl.attack_effect = obj.getInt("attack_effect");
                itpl.defense_effect = obj.getInt("defense_effect");
                itpl.damage_effect = obj.getInt("damage_effect");
                itpl.skill_effect = obj.getInt("skill_effect");
                itpl.magic_effect = obj.getInt("magic_effect");
                itpl.healthregenerate_effect = obj.getInt("healthregenerate_effect");
                itpl.manaregenerate_effect = obj.getInt("manaregenerate_effect");
                itpl.action_effect_1 = obj.getInt("action_effect_1");
                itpl.action_effect_1_data = obj.getString("action_effect_1_data");
                itpl.effect_duration = obj.getInt("effect_duration");
                itpl.required_skill = obj.getInt("required_skill");
                itpl.required_magic = obj.getInt("required_magic");
                itpl.frequency = obj.getInt("frequency");
                itpl.range = obj.getInt("range");
                itpl.premium = obj.getInt("premium");
                itpl.usage_type = UsageType.fromInt(obj.getInt("usage_type"));

                ItemTemplate.itemTemplates.put(itpl.id, itpl);
            }


            /* load worlds */
            file = new File("./maps");
            String[] worlds = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isDirectory();
                }
            });
            for (String wldName : worlds) {
                Integer worldId = Integer.parseInt(wldName);

                path = Paths.get("./maps/" + wldName + "/info.json");
                obj = new JSONObject(new String(Files.readAllBytes(path)));



                JSONObject w_info = obj.getJSONObject("info");
                World world = new World();
                world.id = worldId;
                world.name = w_info.getString("map_name");
                world.width = w_info.getInt("width");
                world.height = w_info.getInt("height");
                world.pvp_zone = w_info.getBoolean("pvp_zone");
                JSONObject w_info_spawn = w_info.getJSONObject("spawn");
                world.spawnX = w_info_spawn.getInt("x");
                world.spawnY = w_info_spawn.getInt("y");
                path = Paths.get("./maps/" + wldName + "/data.bin");
                world.data = Files.readAllBytes(path);

                /* graphics */
                arr = obj.getJSONArray("graphics");
                for(int i = 0; i < arr.length(); i++) {
                    int g_id = arr.getInt(i);
                    if(Graphics.registeredGraphics.containsKey(g_id) && Graphics.registeredGraphics.get(g_id).graphicsType == GraphicsType.BACKGROUND)
                        world.graphicsBackground.add(g_id);
                    else if(Graphics.registeredGraphics.containsKey(g_id) && Graphics.registeredGraphics.get(g_id).graphicsType == GraphicsType.CHARACTER)
                        world.graphicsCharacter.add(g_id);
                }

                /* register tickers */
                world.initialize();
                World.registeredWorlds.put(world.id, world);
            }
            for (String wldName : worlds) {
                /* portals */
                Integer worldId = Integer.parseInt(wldName);
                World world = World.registeredWorlds.get(worldId);
                path = Paths.get("./maps/" + wldName + "/info.json");
                obj = new JSONObject(new String(Files.readAllBytes(path)));
                arr = obj.getJSONArray("portals");
                for(int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    JSONObject cell = job.getJSONObject("cell");
                    JSONObject dest = job.getJSONObject("dest");

                    Portal portal = new Portal();
                    portal.portalCell.x = cell.getInt("x");
                    portal.portalCell.y = cell.getInt("y");
                    if(World.registeredWorlds.containsKey(dest.getInt("world_id"))) {
                        portal.destinationWorld = World.registeredWorlds.get(dest.getInt("world_id"));
                    }
                    portal.destinationX = dest.getInt("x");
                    portal.destinationY = dest.getInt("y");
                    portal.required_quest = job.getString("required_quest");
                    portal.required_level = job.getInt("required_level");

                    world.portals.put(portal.portalCell.x + "_" + portal.portalCell.y, portal);
                }
                /* pickupable */
                arr = obj.getJSONArray("items");
                for(int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);

                    int item_tpl_id = job.getInt("tpl_id");
                    int x = job.getInt("x");
                    int y = job.getInt("y");
                    int respawn_delay = job.getInt("respawn_delay");
                    int units = job.getInt("units");

                    Pickupable pickupable = new Pickupable();
                    pickupable.objectId = i;
                    pickupable.x = x;
                    pickupable.y = y;
                    pickupable.respawnDelay = respawn_delay;
                    pickupable.units = units;
                    if(ItemTemplate.itemTemplates.containsKey(item_tpl_id)) {
                        pickupable.itemTemplate = ItemTemplate.itemTemplates.get(item_tpl_id);

                        /* graphicsId, graphicsX, graphicsY, usageType */
                        pickupable.graphicsId = pickupable.itemTemplate.graphics_id;
                        pickupable.graphicsX = pickupable.itemTemplate.graphics_x;
                        pickupable.graphicsY = pickupable.itemTemplate.graphics_y;
                        pickupable.usageType = pickupable.itemTemplate.usage_type;
                        /* end */

                        world.registerPickupable(pickupable);
                        world.spawnPickupable(i);
                    }
                }

                /* triggers */
                arr = obj.getJSONArray("triggers");
                for(int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    JSONObject cell = job.getJSONObject("cell");

                    Trigger trigger = new Trigger();
                    trigger.triggerCell.x = cell.getInt("x");
                    trigger.triggerCell.y = cell.getInt("y");
                    trigger.script = job.getString("script");

                    world.triggers.put(trigger.triggerCell.x + "_" + trigger.triggerCell.y, trigger);
                }
            }


            /* load mobs */
            file = new File("./data/mob_templates");
            String[] mobs = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isFile();
                }
            });
            for(String m : mobs) {
                Integer mobTemplateId = Integer.parseInt(m.replace(".json", ""));

                path = Paths.get("./data/mob_templates/" + mobTemplateId + ".json");
                obj = new JSONObject(new String(Files.readAllBytes(path)));

                JSONObject graphics = obj.getJSONObject("graphics");

                MobTemplate mobTemplate = new MobTemplate();

                mobTemplate.id = obj.getInt("id");
                mobTemplate.name = obj.getString("name");
                mobTemplate.clan_id = obj.getInt("clan_id");
                mobTemplate.graphics_id = graphics.getInt("id");
                mobTemplate.graphics_x = graphics.getInt("x");
                mobTemplate.graphics_y = graphics.getInt("y");
                mobTemplate.graphics_dim = graphics.getInt("dim");
                mobTemplate.level = obj.getInt("level");
                mobTemplate.health = obj.getInt("health");
                mobTemplate.damage = obj.getInt("damage");
                mobTemplate.attack = obj.getInt("attack");
                mobTemplate.defense = obj.getInt("defense");
                mobTemplate.skill = obj.getInt("skill");
                mobTemplate.move_range = obj.getInt("move_range");
                mobTemplate.move_speed = obj.getInt("move_speed");
                mobTemplate.attack_range = obj.getInt("attack_range");
                mobTemplate.ai = AIType.fromString(obj.getString("ai"));
                mobTemplate.aggresive = obj.getBoolean("aggresive");
                mobTemplate.drop_exp = obj.getInt("drop_exp");
                mobTemplate.drop_gold = obj.getInt("drop_gold");
                mobTemplate.peaceful = obj.getBoolean("peaceful");
                mobTemplate.drop_chance = obj.getInt("drop_chance");
                mobTemplate.talk_script = obj.getString("talk_script");
                if("".equals(mobTemplate.talk_script)) {
                    mobTemplate.talk_script = null;
                } else {
                    File f2 = new File(mobTemplate.talk_script);
                    mobTemplate.talk_script = f2.getName();
                    File f = new File("./data/scripts/talk/" + mobTemplate.talk_script);
                    if(!f.exists())
                        mobTemplate.talk_script = null;
                    else {
                        mobTemplate.talkDelay = ScriptManager.getDelay(mobTemplate.talk_script);
                        if(mobTemplate.talkDelay == 0)
                            mobTemplate.talk_script = null;
                    }
                }
                mobTemplate.legendary = obj.getBoolean("legendary");

                mob_templates.put(mobTemplateId, mobTemplate);
            }

            /* load npcs */
            file = new File("./data/npc_templates");
            String[] npcs = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isFile();
                }
            });
            for(String n : npcs) {
                Integer npcTemplateId = Integer.parseInt(n.replace(".json", ""));

                path = Paths.get("./data/npc_templates/" + npcTemplateId + ".json");
                obj = new JSONObject(new String(Files.readAllBytes(path)));

                JSONObject graphics = obj.getJSONObject("graphics");

                NpcTemplate npcTemplate = new NpcTemplate();
                npcTemplate.id = obj.getInt("id");
                npcTemplate.name = obj.getString("name");
                npcTemplate.clan_id = obj.getInt("clan_id");
                npcTemplate.graphics_id = graphics.getInt("id");
                npcTemplate.graphics_x = graphics.getInt("x");
                npcTemplate.graphics_y = graphics.getInt("y");
                npcTemplate.graphics_dim = graphics.getInt("dim");
                npcTemplate.level = obj.getInt("level");
                npcTemplate.health = obj.getInt("health");
                npcTemplate.move_range = obj.getInt("move_range");
                npcTemplate.move_speed = obj.getInt("move_speed");
                npcTemplate.ai = AIType.fromString(obj.getString("ai"));
                npcTemplate.talk_script = obj.getString("talk_script");
                if("".equals(npcTemplate.talk_script)) {
                    npcTemplate.talk_script = null;
                } else {
                    File f2 = new File(npcTemplate.talk_script);
                    npcTemplate.talk_script = f2.getName();
                    File f = new File("./data/scripts/talk_npcs/" + npcTemplate.talk_script);
                    if(!f.exists())
                        npcTemplate.talk_script = null;
                    else {
                        npcTemplate.talkDelay = ScriptManager.getDelay(npcTemplate.talk_script);
                        if(npcTemplate.talkDelay == 0)
                            npcTemplate.talk_script = null;
                    }
                }

                npcTemplate.dialog_script = obj.getString("dialog_script");
                if("".equals(npcTemplate.dialog_script)) {
                    npcTemplate.dialog_script = null;
                } else {
                    File f2 = new File(npcTemplate.dialog_script);
                    npcTemplate.dialog_script = f2.getName();
                    File f = new File("./data/scripts/dialogs/" + npcTemplate.dialog_script);
                    if(!f.exists())
                        npcTemplate.dialog_script = null;
                }

                /* items */
                arr = obj.getJSONArray("items");
                for(int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    Buyable buyable = new Buyable();
                    buyable.tpl_id = job.getInt("tpl_id");
                    buyable.amount = job.getInt("amount");
                    npcTemplate.items.add(buyable);
                }


                npc_templates.put(npcTemplateId, npcTemplate);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
