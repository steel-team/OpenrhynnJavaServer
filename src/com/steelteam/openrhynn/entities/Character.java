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
package com.steelteam.openrhynn.entities;

import com.steelteam.openrhynn.data.DataSource;
import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.enums.*;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.*;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.server.*;
import com.steelteam.openrhynn.utilits.Encryption;
import org.omg.CORBA.BAD_CONTEXT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class Character extends Entity {
    public static HashMap<Integer, Character> characters = new HashMap<>();

    public static String getDisplayName(String name, UserType type) {
        String n = name;
        if(type == UserType.GM) {
            n = "[GM]" + n;
        }
        return n;
    }


    public CharacterModel connectedModel = null;
    public ORClient connectedClient = null;
    public HashMap<Integer, Item> inventory = new HashMap<>();
    public boolean skipBlocked = false;
    public int movementSpeed = ServerConfig.defaultPlayerSpeed;
    public HashMap<Integer, Friend> friends = new HashMap<>();


    public void setVariable(int var_id, int value) {
        int val = getVariable(var_id);
        String query = null;
        if(val == -1) {
            query = "INSERT INTO vars (char_id, var_id, val) VALUES ('" + objectId + "', '" + var_id + "', '" + value + "');";
        } else {
            query = "UPDATE vars SET val='" + value + "' WHERE char_id='" + objectId+ "' AND var_id='" + var_id + "'";
        }
        Connection conn = null;
        try {
            conn = DataSource.getInstance().getConnection();
            Statement state = conn.createStatement();
            state.executeUpdate(query);
            conn.commit();
            state.close();

        } catch (Exception ex) { ex.printStackTrace();} finally { try { conn.close(); } catch (Exception e) {} }

    }

    public int getVariable(int var_id) {
        int var_val = -1;

        Connection conn = null;
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT val FROM vars WHERE char_id='" + objectId+ "' AND var_id='" + var_id + "'");
            if(resultSet.next()) {
                var_val = resultSet.getInt(1);
            }


            resultSet.close();
            state.close();

        } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }

        return var_val;
    }


    public void withdrawItem(Item item, int units, YesNo showDialog) {
        Connection conn = null;
        String query = null;
        try {
            conn = DataSource.getInstance().getConnection();

            if(item.units == units) {
                query = "DELETE FROM inventory WHERE id='" + item.id + "'";
                inventory.remove(item.id);
                connectedClient.writeMessage(new GameRemoveInv(item.id).getData());
            } else {
                /* fix units_sell */
                item.units -= units;
                query = "UPDATE inventory SET units='" + item.units + "', units_sell='" + item.units_sell + "' WHERE id='" + item.id + "'";
                connectedClient.writeMessage(new GameItemSetunits(item.id, item.units, showDialog).getData());
            }

            Statement state = conn.createStatement();
            state.executeUpdate(query);
            conn.commit();
            state.close();

        } catch (Exception ex) { ex.printStackTrace();} finally { try { conn.close(); } catch (Exception e) {} }
    }

    public void teleport(int targetWorldId, int targetX, int targetY) {
        if(World.registeredWorlds.containsKey(connectedModel.worldId) && World.registeredWorlds.containsKey(targetWorldId)) {
            World world = World.registeredWorlds.get(connectedModel.worldId);
            World destinationWorld = World.registeredWorlds.get(targetWorldId);
            int _targetX = destinationWorld.spawnX;
            int _targetY = destinationWorld.spawnY;
            if(targetX != -1 || targetY != -1) {
                _targetX = targetX;
                _targetY = targetY;
            }

            connectedClient.writeMessage(new GameTriggerUnlock().getData());
            if(world.id == destinationWorld.id) {
                updatePosition(world, _targetX, _targetY, Direction.UP, true, skipBlocked);
                connectedClient.writeMessage(new GameUpdateCords(x, y, direction).getData());
            } else {
                connectedClient.writeMessage(new GameTriggerUse(TriggerType.PORTAL).getData());
                world.removeEntity(this, false);
                updatePosition(world, _targetX, _targetY, Direction.UP, true, skipBlocked);
                forceCoordinates(targetWorldId, _targetX, _targetY);
                connectedModel.worldId = targetWorldId;

                connectedClient.worldFlagLocked = true;

                connectedClient.writeMessage(new GameUpdateCords(x, y, direction).getData());
                connectedClient.writeMessage(new GameUsePortal().getData());
            }
            //connectedClient.context.flush();
        }
    }

    public void unequipItemByType(ItemType type, boolean sendMessage) {

        Item it = null;
        for(Item item : inventory.values()) {
            if(item.type == type && item.equiped == 1) {
                it = item;
                break;
            }
        }
        if(it != null)
            unequipItemById(it.id, sendMessage);
    }

    public void sendSilentUpdate() {
        connectedClient.writeMessage(new GameUpdateAllSilent(
                objectId,
                connectedModel.classId,
                clanId,
                connectedModel.worldId,
                graphicsId,
                graphicsX,
                graphicsY,
                graphicsDim,
                x,
                y,
                level,
                connectedModel.levelPoints,
                connectedModel.experience,
                connectedModel.gold,

                healthBase,
                healthEffectsExtra,
                healthCurrent,

                manaBase,
                manaEffectsExtra,
                manaCurrent,

                attackBase,
                attackEffectsExtra,

                defenseBase,
                defenseEffectsExtra,

                damageBase,
                damageEffectsExtra,

                skillBase,
                skillEffectsExtra,

                magicBase,
                magicEffectsExtra,

                connectedModel.healthregenerateBase,
                connectedModel.healthregenerateEffectsExtra,

                connectedModel.manaregenerateBase,
                connectedModel.manaregenerateEffectsExtra,

                name,
                connectedModel.ownStatus
        ).getData());
    }

    public void unequipItemById(int item_id, boolean sendMessage) {
        Item it = inventory.get(item_id);
        if(it.equiped == 1) {
            it.equiped = 0;
            /* update DB item */
            Connection conn = null;
            try {
                conn = DataSource.getInstance().getConnection();
                Statement state = conn.createStatement();
                state.executeUpdate("UPDATE inventory SET equiped='0' WHERE id='" + it.id + "'");
                conn.commit();
                state.close();

            } catch (Exception ex) { ex.printStackTrace();} finally { try { conn.close(); } catch (Exception e) {} }


            /* update character */
            attackEffectsExtra -= it.attack_effect;
            damageEffectsExtra -= it.damage_effect;
            defenseEffectsExtra -= it.defense_effect;
            healthEffectsExtra -= it.health_effect;
            connectedModel.healthregenerateEffectsExtra -= it.healthregenerate_effect;
            magicEffectsExtra -= it.magic_effect;
            manaEffectsExtra -= it.mana_effect;
            connectedModel.manaregenerateEffectsExtra -= it.manaregenerate_effect;
            skillEffectsExtra -= it.skill_effect;

            /* frequency + attack_range */
            if(it.type == ItemType.WEAPON_1) {
                attackRange = ServerConfig.defaultWeaponRange;
                attackCharge = ServerConfig.defaultWeaponCharge;
            }
            saveModelToDb();
        }

        if(sendMessage) {
            //update all silent
            sendSilentUpdate();
        }
    }


    /* remove connection with pickupable */
    public void addItem(ItemTemplate itemTemplate, int units) {
        /* insert into database */

        //ItemTemplate itemTemplate = pickupable.itemTemplate;

        /* if we have same tpl_id try unite */

        Item item = new Item();
        item.char_id = objectId;
        item.action_effect_1 = itemTemplate.action_effect_1;
        item.action_effect_1_data = itemTemplate.action_effect_1_data;
        item.attack_effect = itemTemplate.attack_effect;
        item.available_status = itemTemplate.available_status;
        item.can_drop = itemTemplate.can_drop;
        item.can_sell = itemTemplate.can_sell;
        item.damage_effect = itemTemplate.damage_effect;
        item.defense_effect = itemTemplate.defense_effect;
        item.description = itemTemplate.description;
        item.effect_duration = itemTemplate.effect_duration;
        item.frequency = itemTemplate.frequency;
        item.graphics_id = itemTemplate.graphics_id;
        item.graphics_x = itemTemplate.graphics_x;
        item.graphics_y = itemTemplate.graphics_y;
        item.health_effect = itemTemplate.health_effect;
        item.healthregenerate_effect = itemTemplate.healthregenerate_effect;
        item.magic_effect = itemTemplate.magic_effect;
        item.mana_effect = itemTemplate.mana_effect;
        item.manaregenerate_effect = itemTemplate.manaregenerate_effect;
        item.max_units = itemTemplate.max_units;
        item.name = itemTemplate.name;
        item.premium = itemTemplate.premium;
        item.price = itemTemplate.price;
        item.range = itemTemplate.range;
        item.required_magic = itemTemplate.required_magic;
        item.required_skill = itemTemplate.required_skill;
        item.set_id = itemTemplate.set_id;
        item.skill_effect = itemTemplate.skill_effect;
        item.type = itemTemplate.type;
        item.usage_type = itemTemplate.usage_type;
        item.tpl_id = itemTemplate.id;
        item.units = units;//pickupable.units;

        Connection conn = null;
        String query = null;
        try {
            conn = DataSource.getInstance().getConnection();

            boolean joined = false;
            Item itupdate = null;

            for(Item itm : inventory.values()) {
                if(itm.tpl_id == item.tpl_id && itm.units + item.units < itm.max_units) {
                    itupdate = itm;
                    joined = true;
                    itupdate.units += item.units;
                    query = "UPDATE inventory SET units='" + itupdate.units + "' WHERE id='" + itupdate.id + "';";
                    break;
                }
            }

            if(!joined && inventory.size() < ServerConfig.maxInventorySize)
                query = "INSERT INTO inventory(char_id" +
                        ",tpl_id" +
                        ",type" +
                        ",set_id" +
                        ",graphics_id" +
                        ",graphics_x" +
                        ",graphics_y" +
                        ",name" +
                        ",description" +
                        ",can_sell" +
                        ",can_drop" +
                        ",units" +
                        ",max_units" +
                        ",price" +
                        ",health_effect" +
                        ",mana_effect" +
                        ",attack_effect" +
                        ",defense_effect" +
                        ",damage_effect" +
                        ",skill_effect" +
                        ",magic_effect" +
                        ",healthregenerate_effect" +
                        ",manaregenerate_effect" +
                        ",action_effect_1" +
                        ",action_effect_1_data" +
                        ",effect_duration" +
                        ",required_skill" +
                        ",required_magic" +
                        ",frequency" +
                        ",`range`" +
                        ",usage_type" +
                        ") VALUES ('" + objectId + "'" +
                        ", '" + item.tpl_id + "'" +
                        ", '" + ItemType.toInt(item.type) + "'" +
                        ", '" + item.set_id + "'" +
                        ", '" + item.graphics_id + "'" +
                        ", '" + item.graphics_x + "'" +
                        ", '" + item.graphics_y + "'" +
                        ", '" + item.name + "'" +
                        ", '" + item.description + "'" +
                        ", '" + item.can_sell + "'" +
                        ", '" + item.can_drop + "'" +
                        ", '" + item.units + "'" +
                        ", '" + item.max_units + "'" +
                        ", '" + item.price + "'" +
                        ", '" + item.health_effect + "'" +
                        ", '" + item.mana_effect + "'" +
                        ", '" + item.attack_effect + "'" +
                        ", '" + item.defense_effect + "'" +
                        ", '" + item.damage_effect + "'" +
                        ", '" + item.skill_effect + "'" +
                        ", '" + item.magic_effect + "'" +
                        ", '" + item.healthregenerate_effect + "'" +
                        ", '" + item.manaregenerate_effect + "'" +
                        ", '" + item.action_effect_1 + "'" +
                        ", '" + item.action_effect_1_data + "'" +
                        ", '" + item.effect_duration + "'" +
                        ", '" + item.required_skill + "'" +
                        ", '" + item.required_magic + "'" +
                        ", '" + item.frequency + "'" +
                        ", '" + item.range + "'" +
                        ", '" + UsageType.toInt(item.usage_type) + "'" +
                        ")";


            Statement state = conn.createStatement();
            if(!joined && query != null) {
                state.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = state.getGeneratedKeys();
                if (rs.next()){
                    item.id = rs.getInt(1);
                }
                rs.close();
            } else {
                state.executeUpdate(query);
            }
            conn.commit();

            state.close();


            if(!joined) {
                if(inventory.size() < ServerConfig.maxInventorySize) {
                    inventory.put(item.id, item);
                    //send inventory_add
                    connectedClient.writeMessage(new GameItemInventoryAdd(item).getData());
                } else {
                    //send inventory_add_error
                    connectedClient.writeMessage(new GameItemInventoryAddFail(YesNo.YES).getData());
                }
            } else {
                //send update units
                connectedClient.writeMessage(new GameItemSetunits(itupdate.id, itupdate.units, YesNo.YES).getData());
            }

        } catch (Exception ex) { ex.printStackTrace(); System.out.println(query); } finally { try { conn.close(); } catch (Exception e) {} }
    }



    public Character() {
        entityType = EntityType.CHARACTER;
    }
    public void fillFromModel() {
        this.objectId = connectedModel.id;
        this.clanId = connectedModel.clanId;
        this.graphicsId = connectedModel.graphicsId;
        this.graphicsX = connectedModel.graphicsX;
        this.graphicsY = connectedModel.graphicsY;
        this.graphicsDim = connectedModel.graphicsDim;
        this.level = connectedModel.level;
        this.x = connectedModel.x;
        this.y = connectedModel.y;
        this.direction = Direction.UP;

        this.healthBase = connectedModel.healthBase/* + connectedModel.healthEffectsExtra*/;
        this.healthEffectsExtra = connectedModel.healthEffectsExtra;
        this.healthCurrent = connectedModel.healthCurrent;

        this.name = connectedModel.displayName;



        this.manaBase = connectedModel.manaBase;
        this.manaEffectsExtra = connectedModel.manaEffectsExtra;
        this.manaCurrent = connectedModel.manaCurrent;

        this.damageBase = connectedModel.damageBase;
        this.damageEffectsExtra = connectedModel.damageEffectsExtra;

        this.defenseBase = connectedModel.defenseBase;
        this.defenseEffectsExtra = connectedModel.defenseEffectsExtra;

        this.attackBase = connectedModel.attackBase;
        this.attackEffectsExtra = connectedModel.attackEffectsExtra;

        this.skillBase = connectedModel.skillBase;
        this.skillEffectsExtra = connectedModel.skillEffectsExtra;

        this.magicBase = connectedModel.magicBase;
        this.magicEffectsExtra = connectedModel.magicEffectsExtra;
    }


    public void addExperience(World world, int exp) {
        connectedModel.experience += exp;

        int experiencePlusForNextLevel = 100 * (level + 2) * (level + 1);
        if(connectedModel.experience >= experiencePlusForNextLevel) {
            if (level < ServerConfig.maxLevel) {
                connectedModel.levelPoints += ServerConfig.pointsPerLevel;
                level += 1;
            }
        }

        world.broadcastMessage(new GameCharacterUpdateData(level, connectedModel.experience, connectedModel.gold, connectedModel.levelPoints, objectId).getData(), 0);
    }

    public void saveModelToDb() {
        //regenerate health/mana saved from model
        //other vars from character object

        /*
            clan_id
            world_id
            x
            y
            direct

            level
            points
            exp
            gold

            health_base
            health_effect_extra
            health_current

            mana_base
            mana_effect_extra
            mana_current

            attack_base
            attack_effect_extra

            defense_base
            defense_effect_extra

            damage_base
            damage_effect_extra

            skill_base
            skill_effect_extra

            magic_base
            magic_effect_extra


            healthregenerate_base
            healthregenerate_effect_extra

            manaregenerate_base
            manaregenerate_effect_extra

            custom_status_msg
         */
        Connection conn = null;
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            state.executeUpdate("UPDATE characters SET world_id = '" + connectedModel.worldId + "', x = '" + x + "', y = '" + y + "'" +
                    ", direct='" + direction + "'" +
                    ", clan_id='" + clanId + "'" +
                    ", level='" + level + "'" +
                    ", points='" + connectedModel.levelPoints + "'" +
                    ", exp='" + connectedModel.experience + "'" +
                    ", gold='" + connectedModel.gold + "'" +

                    ", health_base='" + healthBase + "'" +
                    ", health_effect_extra='" + healthEffectsExtra + "'" +
                    ", health_current='" + healthCurrent + "'" +

                    ", mana_base='" + manaBase + "'" +
                    ", mana_effect_extra='" + manaEffectsExtra + "'" +
                    ", mana_current='" + manaCurrent + "'" +

                    ", attack_base='" + attackBase + "'" +
                    ", attack_effect_extra='" + attackEffectsExtra + "'" +

                    ", defense_base='" + defenseBase + "'" +
                    ", defense_effect_extra='" + defenseEffectsExtra + "'" +

                    ", damage_base='" + damageBase + "'" +
                    ", damage_effect_extra='" + damageEffectsExtra + "'" +

                    ", skill_base='" + skillBase + "'" +
                    ", skill_effect_extra='" + skillEffectsExtra + "'" +

                    ", magic_base='" + magicBase + "'" +
                    ", magic_effect_extra='" + magicEffectsExtra + "'" +

                    ", healthregenerate_base='" + connectedModel.healthregenerateBase + "'" +
                    ", healthregenerate_effect_extra='" + connectedModel.healthregenerateEffectsExtra + "'" +

                    ", manaregenerate_base='" + connectedModel.manaregenerateBase + "'" +
                    ", manaregenerate_effect_extra='" + connectedModel.manaregenerateEffectsExtra + "'" +

                    ", custom_status_msg='" + connectedModel.ownStatus + "'" +

                    " WHERE id='" + objectId + "'");
            conn.commit();

            state.close();

        } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }
    }

    public void forceCoordinates(int _worldId, int _x, int _y) {
        connectedModel.worldId = _worldId;
        x = _x;
        y = _y;
        Connection conn = null;
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            state.executeUpdate("UPDATE characters SET direct='" + direction + "', world_id = '" + _worldId + "', x = '" + x + "', y = '" + y + "' WHERE id='" + objectId + "'");
            conn.commit();

            state.close();

        } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }
    }

    public void saveToModel() {
        //model should be read only and saved only on user exit
    }
}
