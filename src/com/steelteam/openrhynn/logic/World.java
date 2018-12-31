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

import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.entities.Entity;
import com.steelteam.openrhynn.entities.Mob;
import com.steelteam.openrhynn.enums.EntityType;
import com.steelteam.openrhynn.models.*;
import com.steelteam.openrhynn.network.messages.ORMessage;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.server.*;
import com.steelteam.openrhynn.scrolls.core.BaseScroll;
import com.steelteam.openrhynn.tickers.AITicker;
import com.steelteam.openrhynn.tickers.AITickerNpc;
import com.steelteam.openrhynn.tickers.EventTicker;
import com.steelteam.openrhynn.tickers.MovementTicker;
import com.steelteam.openrhynn.utilits.Time;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class World {
    public static ConcurrentHashMap<Integer, World> registeredWorlds = new ConcurrentHashMap<>();


    /* map cells */
    public Cell[][] cells = new Cell[1][1];
    public static final int blockTolerance = 3;

    /* world info */
    public int id = 0;
    public String name = "";
    public int width = 0;
    public int height = 0;
    public boolean pvp_zone = false;
    public int spawnX = 0;
    public int spawnY = 0;

    /* world data object, MapFormat analog */
    public byte[] data = null;

    /* required graphics */
    public ArrayList<Integer> graphicsBackground = new ArrayList<>();
    public ArrayList<Integer> graphicsCharacter = new ArrayList<>();
    /* entities */
    private ConcurrentHashMap<Integer, Entity> entities = new ConcurrentHashMap<>();
    /* pickupable */
    public ConcurrentHashMap<Integer, Pickupable> registeredPickupables = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Pickupable> pickupables = new ConcurrentHashMap<>();
    /* ai ticker */
    private AITicker aiTicker = null;
    private AITickerNpc aiTickerNpc = null;
    /* event ticker */
    private EventTicker eventTicker = null;
    public ArrayList<BaseScroll> scrolls = new ArrayList<>();
    public ConcurrentHashMap<String, Firewall> firewalls = new ConcurrentHashMap<>();
    /* portals */
    public ConcurrentHashMap<String, Portal> portals = new ConcurrentHashMap<>();
    /* triggers */
    public ConcurrentHashMap<String, Trigger> triggers = new ConcurrentHashMap<>();
    /* flood messages ticker */
    public MovementTicker movementTicker = null;

    public void initialize() {
        aiTicker = new AITicker(this);
        aiTicker.createTicker(100);

        aiTickerNpc = new AITickerNpc(this);
        aiTickerNpc.createTicker(100);

        eventTicker = new EventTicker(this);
        eventTicker.createTicker(100);

        //uncomment lines below to enable threaded movement sending
        /*movementTicker = new MovementTicker(this);
        movementTicker.createTicker(150);
        */

        /* parse data */
        cells = new Cell[width][height];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cells[x][y] = new Cell();
                cells[x][y].x = x;
                cells[x][y].y = y;

                int dataIndex = (y * width + x) * 2;

                int triggerAndFunctionData = data[dataIndex] & 0xFF;
                int trigger = ((triggerAndFunctionData & 0xE0) >> 5);
                int function = (triggerAndFunctionData & 0x1F);

                if(function == 0x01) {
                    cells[x][y].blocked = true;
                } else {
                    cells[x][y].blocked = false;
                }

                if(function == 0x02) {
                    cells[x][y].peaceful = true;
                } else {
                    cells[x][y].peaceful = false;
                }
            }
        }
    }

    /* system */

    public Pickupable getPickupable(int objectId) {
        if(pickupables.containsKey(objectId))
            return pickupables.get(objectId);
        return null;
    }

    public Pickupable[] getPickupables() {
        return pickupables.values().toArray(new Pickupable[pickupables.size()]);
    }

    public void registerPickupable(Pickupable obj) {
        registeredPickupables.put(obj.objectId, obj);
    }

    public void spawnPickupable(int objectId) {
        if(!pickupables.containsKey(objectId) && registeredPickupables.containsKey(objectId)) {
            Pickupable obj = registeredPickupables.get(objectId);
            obj.pickupTime = 0;
            obj.present = true;
            pickupables.put(obj.objectId, obj);
            /* notice everyone */
            broadcastMessage(new GameItemAdd(obj).getData(), 0);
        }
    }

    public int getPickupableId() {
        if(pickupables.size() > 0)
            return Collections.max(pickupables.keySet()) + 1;
        else
            return 0;
    }

    public void addPickupable(Pickupable obj) {
        obj.present = true;
        pickupables.put(obj.objectId, obj);
        /* notice everyone */
        broadcastMessage(new GameItemAdd(obj).getData(), 0);
    }

    public void removePickupable(int objectId) {
        if(pickupables.containsKey(objectId)) {
            Pickupable obj = pickupables.get(objectId);
            pickupables.remove(objectId);
            obj.present = false;
            obj.pickupTime = Time.getUnixTimeMillis();
            /* notice everyone */
            broadcastMessage(new GameItemRemove(objectId).getData(), 0);
        }
    }


    /* functions */

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public ArrayList<Entity> getMobs() {
        ArrayList<Entity> ents = new ArrayList<>();
        for(Entity ent : entities.values()) {
            if(ent.entityType == EntityType.MOB)
                ents.add(ent);
        }
        return ents;
    }

    public ArrayList<Entity> getNpcs() {
        ArrayList<Entity> ents = new ArrayList<>();
        for(Entity ent : entities.values()) {
            if(ent.entityType == EntityType.NPC)
                ents.add(ent);
        }
        return ents;
    }

    public ArrayList<Entity> getCharacters() {
        ArrayList<Entity> ents = new ArrayList<>();
        for(Entity ent : entities.values()) {
            if(ent.entityType == EntityType.CHARACTER)
                ents.add(ent);
        }
        return ents;
    }


    public void addEntity(Entity entity) {
        /* if entityType = CHARACTER, tell character about all entities, on map skills, on map items */
        if(entity.entityType == EntityType.CHARACTER) {
            Character addedCharacter = (Character)entity;
            for (Entity ent : entities.values()) {
                addedCharacter.connectedClient.writeMessage(new GameCharacterAdd(ent).getData());
            }
            for(Firewall fire : firewalls.values()) {
                addedCharacter.connectedClient.writeMessage(new GameFirewall(fire.attackerId, fire.cellX, fire.cellY).getData());
            }
        }
        /* add entity */
        entities.put(entity.objectId, entity);
        /* tell everyone about new entity */
        broadcastMessage(new GameCharacterAdd(entity).getData(), entity.objectId);
    }

    public void respawnEntity(Entity entity) {

        entity.deathTime = 0;
        entity.dead = false;

        broadcastMessage(new GameCharacterAdd(entity).getData(), entity.objectId);
    }

    public void chat(int issuer, String message) {
        broadcastMessage(new GameCharacterChatAllInfo(issuer, message).getData(), 0);
    }

    public void movement(Entity entity) {
        //entity.movementRequired = true;
        broadcastMessage(new GameCharacterMoveInfo(entity.x, entity.y, entity.direction, entity.objectId).getData(), entity.objectId); //comment this line and uncomment line above to enable threaded movement sending
    }


    public void removeEntity(Entity entity, boolean isDead) {
        /* tell everyone about removed entity */
        entities.remove(entity.objectId);
        broadcastMessage(new GameCharacterRemove(entity.objectId).getData(), 0);
    }

    public boolean hasEntity(int entityId) {
        if(entities.containsKey(entityId))
            return true;
        return false;
    }

    public Entity getEntity(int entityId) {
        if(hasEntity(entityId))
            return entities.get(entityId);
        return null;
    }

    public void attack(int attackerId, int targetId) {
        Entity attacker = null;
        Entity target = null;
        if(entities.containsKey(attackerId) && entities.containsKey(targetId)) {
            attacker = entities.get(attackerId);
            target = entities.get(targetId);
            if(attacker.dead || target.dead)
                return;

            if(!checkAttackConditions(attacker, target))
                return;

            int frequency = attacker.attackCharge;
            long currentTime = Time.getUnixTimeMillis();

            if(attacker.lastAttackTime + frequency > currentTime)
                return;

            if(checkRange(attacker, target) && checkZone(attacker, target)) {
                int attacker_damageMax = attacker.getDamageMax();
                int attacker_attackMax = attacker.getAttackMax();
                int attacker_defenseMax = attacker.getDefenseMax();

                int target_defenseMax = target.getDefenseMax();
                int target_skillMax = target.getSkillMax();


                boolean dead = false;

                attacker.lastAttackTime = currentTime;
                target.lastReceivedDamageTime = currentTime;

                if(Formulas.isMiss(attacker_attackMax, attacker_defenseMax, target_skillMax)) {
                    broadcastMessage(new GameCharacterHitMissInfo(attackerId, targetId).getData(), 0);
                } else {
                    int damage = Formulas.calculateDamage(attacker_damageMax, target_defenseMax);
                    dead = clearDamage(attacker, target, damage, false);
                }

                if(target.entityType == EntityType.MOB && !dead) {
                    Mob m = (Mob)target;
                    m.onAttackReceived(attacker);
                }
            }
        }
    }

    /* returns death */
    public boolean clearDamage(Entity attacker, Entity target, int damage, boolean fakeAttacker) {

        if(!checkAttackConditions(attacker, target))
            return false;

        boolean dead = false;
        target.healthCurrent -= damage;
        int attackerObjectId = -1;
        if(attacker != null)
            attackerObjectId = attacker.objectId;
        if(fakeAttacker)
            attackerObjectId = -1;
        if(target.healthCurrent <= 0) {
            //death
            target.setDead();
            target.healthCurrent = (int)(target.getHealthMax() * 0.7);
            target.manaCurrent = (int)(target.getManaMax() * 0.7);

            int coordX = target.x;
            int coordY = target.y;

            if(target.entityType == EntityType.CHARACTER) {
                //find nearest portal.
                int[] cords = findNearestPortalFor(target);
                target.x = cords[0];
                target.y = cords[1];
            } else {
                target.x = this.spawnX;
                target.y = this.spawnY;
            }

            dead = true;

            if(target.entityType == EntityType.MOB) {
                Mob m = (Mob)target;
                m.onDeath(attacker);
                if(attacker.entityType == EntityType.CHARACTER) {
                    Character chr = (Character)attacker;
                                /* drop items/gold */
                                /* check quest progress */
                    chr.addExperience(this, m.connectedModel.drop_exp);
                }
                DropCore.beginDrop(this, m, coordX, coordY);
            }

            if(attacker.entityType == EntityType.MOB) {
                Mob m = (Mob)attacker;
                m.onTargetDeath(target);
            }

            broadcastMessage(new GameCharacterKilled(target.objectId).getData(), 0);


            //entities.remove(target.objectId);
        } else {
            broadcastMessage(new GameCharacterHitInfo(attackerObjectId, target.objectId, damage, target.healthCurrent).getData(), 0);
        }
        return dead;
    }


    public boolean checkAttackConditions(Entity attacker, Entity target) {
        if(attacker != null) {
            //some checks?
        }
        if(target == null)
            return false;
        if(target.entityType == EntityType.NPC)
            return false;
        if(target.invincible) {
            return false;
        }
        return true;
    }


    private boolean checkRange(Entity attacker, Entity target) {
        int attacker_x = attacker.x;
        int attacker_y = attacker.y;

        int target_x = target.x;
        int target_y = target.y;


        return WorldTools.withinRange(attacker_x, attacker_y, target_x, target_y, attacker.attackRange);
    }

    private boolean checkZone(Entity attacker, Entity target) {
        if(!WorldTools.isPeaceful(this, attacker.x, attacker.y, attacker.graphicsDim) && !WorldTools.isPeaceful(this, target.x, target.y, target.graphicsDim))
            return true;
        return false;
    }

    private int[] findNearestPortalFor(Entity entity) {
        int[] cords = new int[2];

        cords[0] = spawnX;
        cords[1] = spawnY;

        int previousLen = 0;
        Portal previousPortal = null;

        for(Portal portal : portals.values()) {
            //Point
            int len = (int)Math.hypot((double)(entity.x - portal.portalCell.x * Cell.cellSize), (double)(entity.y - portal.portalCell.y * Cell.cellSize));
            if(len < previousLen || previousPortal == null) {
                previousLen = len;
                previousPortal = portal;
            }
        }

        if(previousPortal != null) {
            int y_up = previousPortal.portalCell.y - 1;
            int y_down = previousPortal.portalCell.y + 1;
            int x_right = previousPortal.portalCell.x + 1;
            int x_left = previousPortal.portalCell.x - 1;
            if(y_up >= 0 && !cells[previousPortal.portalCell.x][y_up].blocked) {
                cords[0] = previousPortal.portalCell.x * Cell.cellSize;
                cords[1] = y_up * Cell.cellSize - 3;

            } else if(y_down <= height && !cells[previousPortal.portalCell.x][y_down].blocked) {
                cords[0] = previousPortal.portalCell.x * Cell.cellSize;
                cords[1] = y_down * Cell.cellSize + 3;

            } else if(x_left >= 0 && !cells[x_left][previousPortal.portalCell.y].blocked) {
                cords[0] = x_left * Cell.cellSize - 3 /*entity.graphicsDim*/;
                cords[1] = previousPortal.portalCell.y * Cell.cellSize;

            } else if(x_right <= width && !cells[x_right][previousPortal.portalCell.y].blocked) {
                cords[0] = x_right * Cell.cellSize + 3;
                cords[1] = previousPortal.portalCell.y * Cell.cellSize;
            }
        }

        return cords;
    }



    public void broadcastMessage(ORMessage msg, int exclude) {
        for(Entity ent : entities.values()) {
            if(ent.entityType == EntityType.CHARACTER && ent.objectId != exclude) {
                try {
                    Character character = (Character) ent;
                    if(character.connectedClient.context.channel().isActive() && character.connectedClient.context.channel().isWritable()) {
                        //character.connectedClient.context.writeAndFlush(msg, character.connectedClient.context.voidPromise());
                        character.connectedClient.writeMessage(msg);
                    }
                } catch (Exception ex) {

                }
            }
        }
    }
}
