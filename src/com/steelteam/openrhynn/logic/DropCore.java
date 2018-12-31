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

import com.steelteam.openrhynn.entities.Mob;
import com.steelteam.openrhynn.enums.ItemType;
import com.steelteam.openrhynn.enums.UsageType;
import com.steelteam.openrhynn.models.ItemTemplate;
import com.steelteam.openrhynn.models.Pickupable;
import com.steelteam.openrhynn.utilits.AdvRandom;
import com.steelteam.openrhynn.utilits.ChanceCalculator;
import com.steelteam.openrhynn.utilits.Time;

import java.util.Hashtable;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/*
*  PORTED FROM C# VERSION!
*  CODE QUALITY ALWAYS SAME, BECAUSE I HAVEN'T TIME
*  TO REWRITE THIS PART OF CODE
*  ORIGINAL FILE: https://openrhynn.net/sources/publ/Code/Items/DropCoreV3.cs
* */

/*
    Some parts of code belongs to community (read openrhynn.net forums & other sources for formulas)
    But this file still released under MIT license
*/

public class DropCore {
    private static AdvRandom srand = new AdvRandom();
    public static void beginDrop(World world, Mob target, int coordX, int coordY) {
        try {
            int legendaryChance = target.level / 8;
            if(target.connectedModel.legendary)
                legendaryChance += 20;

            int itemChance = 60;
            int goldChance = 50;//80%
            int potionChance = 45;//80%
            int scrollChance = 20;

            boolean isLegendary = ChanceCalculator.calculate(legendaryChance);
            boolean isItem = ChanceCalculator.calculate(itemChance);
            boolean isGold = ChanceCalculator.calculate(goldChance);
            boolean isPotion = ChanceCalculator.calculate(potionChance);
            boolean isScroll = ChanceCalculator.calculate(scrollChance);

            if(isItem) {
                int rar = 5;
                if(isLegendary)
                    rar = 10;
                dropItem(world, coordX, coordY, target.level, rar);
            }
            if(isGold)
                dropGold(world, target.connectedModel.drop_gold, coordX, coordY);
            if(isPotion)
                dropPotion(world, coordX, coordY);
            if(isScroll)
                dropScroll(world, coordX, coordY);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void dropGold(World world, int amount, int coordX, int coordY) {
        Pickupable pickupable = new Pickupable();
        pickupable.present = true;
        pickupable.respawnDelay = 0;
        pickupable.pickupTime = 0;
        pickupable.units = amount;
        pickupable.usageType = UsageType.GOLD;
        pickupable.graphicsId = 100006;
        pickupable.graphicsX = 45;
        pickupable.graphicsY = 90;
        pickupable.objectId = world.getPickupableId();
        pickupable.x = coordX;
        pickupable.y = coordY;
        pickupable.autoRemoveSpawnTime = Time.getUnixTimeMillis();

        world.addPickupable(pickupable);
    }

    private static void dropScroll(World world, int coordX, int coordY) {
        int itemID = srand.nextIntImproved(0,36);
        if(ItemTemplate.itemTemplates.containsKey(itemID)) {
            ItemTemplate itpl = ItemTemplate.itemTemplates.get(itemID);


            Pickupable pickupable = new Pickupable();
            pickupable.present = true;
            pickupable.respawnDelay = 0;
            pickupable.pickupTime = 0;
            pickupable.units = 3;
            pickupable.usageType = itpl.usage_type;
            pickupable.graphicsId = itpl.graphics_id;
            pickupable.graphicsX = itpl.graphics_x;
            pickupable.graphicsY = itpl.graphics_y;
            pickupable.objectId = world.getPickupableId();
            pickupable.x = coordX;
            pickupable.y = coordY;
            pickupable.itemTemplate = itpl;
            pickupable.autoRemoveSpawnTime = Time.getUnixTimeMillis();

            world.addPickupable(pickupable);
        }
    }

    private static void dropPotion(World world, int coordX, int coordY) {
        int itemID = 37;

        if(srand.nextIntImproved(0, 2) == 0)
            itemID = 37;
        else
            itemID = 39;

        if(ItemTemplate.itemTemplates.containsKey(itemID)) {
            ItemTemplate itpl = ItemTemplate.itemTemplates.get(itemID);


            Pickupable pickupable = new Pickupable();
            pickupable.present = true;
            pickupable.respawnDelay = 0;
            pickupable.pickupTime = 0;
            pickupable.units = 3;
            pickupable.usageType = itpl.usage_type;
            pickupable.graphicsId = itpl.graphics_id;
            pickupable.graphicsX = itpl.graphics_x;
            pickupable.graphicsY = itpl.graphics_y;
            pickupable.objectId = world.getPickupableId();
            pickupable.x = coordX;
            pickupable.y = coordY;
            pickupable.itemTemplate = itpl;
            pickupable.autoRemoveSpawnTime = Time.getUnixTimeMillis();

            world.addPickupable(pickupable);
        }
    }
















    /* drop item */
    private static int swap(int type) {
        //1 - оружие,2 - щит,3 - нагрудник,4 - шлем,5 - боты,6 - перчи
        //items types:
        // 1 - helm
        // 2 - armor
        // 3 - weapon
        // 4 - shield
        // 5 - glove
        // 6 - boot
        if(type == 1)
            return 3;
        else if(type == 2)
            return 4;
        else if(type == 3)
            return 2;
        else if(type == 4)
            return 1;
        else if(type == 5)
            return 6;
        else if(type == 6)
            return 5;
        return 0;
    }


    public static String[] generateWeaponName(int wtype) {
        String[] result = new String[2];
        //0 - арбалеты,1 - дубинки,2 - звездочки,3 - копья
        //4 - луки,5 - мечи,6 - ножи,7 - палки,8 - посохи,9 - топоры
        int check = 0;
        switch(wtype)
        {
            case 0:
                result[0] = "Knight crossbow";
                result[1] = "Weak crossbow";
                break;
            case 1:
                check = srand.nextIntImproved(0, 7);

                if(check == 0) {
                    result[0] = "Club";
                    result[1] = "Weak Mace";
                } else if(check == 1) {
                    result[0] = "Morning Star";
                    result[1] = "Quality Mace";
                } else if(check == 2) {
                    result[0] = "Scull Mace";
                    result[1] = "Better Mace";
                } else if(check == 3) {
                    result[0] = "Spiked Club";
                    result[1] = "Normal Mace";
                } else if(check == 4) {
                    result[0] = "Flail";
                    result[1] = "Fine Mace";
                } else if(check == 5) {
                    result[0] = "Orc Club";
                    result[1] = "Valuable Mace";
                } else if(check == 6) {
                    result[0] = "Strong Club";
                    result[1] = "Normal Mace";
                }
                break;
            case 2:
                result[0] = "Throwing stars";
                result[1] = "Weak stars";
                break;
            case 3:
                check = srand.nextIntImproved(0, 2);

                if(check == 0) {
                    result[0] = "Heavy spear";
                    result[1] = "Weak spear";
                } else {
                    result[0] = "Light spear";
                    result[1] = "Weak spear";
                }
                break;
            case 4:
                check = srand.nextIntImproved(0,4);

                if(check == 0) {
                    result[0] = "Short Bow";
                    result[1] = "Simple Bow";
                } else if(check == 1) {
                    result[0] = "Bow";
                    result[1] = "Common Bow";
                } else if(check == 2) {
                    result[0] = "Rain Bow";
                    result[1] = "Weird Bow";
                } else if(check == 3) {
                    result[0] = "Heavy Bow";
                    result[1] = "Better Bow";
                }
                break;
            case 5:
                check = srand.nextIntImproved(0,6);

                if(check == 0) {
                    result[0] = "Sharp Blade";
                    result[1] = "Fine Sword";
                } else if(check == 1) {
                    result[0] = "Heavy sword";
                    result[1] = "Weak sword";
                } else if(check == 2) {
                    result[0] = "Pirate sword";
                    result[1] = "Weak sword";
                } else if(check == 3) {
                    result[0] = "Crystal sword";
                    result[1] = "Weak sword";
                } else if(check == 4) {
                    result[0] = "Ice sword";
                    result[1] = "Weak sword";
                } else if(check == 5) {
                    result[0] = "Elf Blade";
                    result[1] = "Weak Sword";
                }
                break;
            case 6:
                check = srand.nextIntImproved(0,3);

                if(check == 0) {
                    result[0] = "Knife";
                    result[1] = "Common Knife";
                } else if(check == 1) {
                    result[0] = "Sharp Knife";
                    result[1] = "Enhanced Knife";
                } else if(check == 2) {
                    result[0] = "Wolf Claw";
                    result[1] = "Better Knife";
                }

                break;
            case 7:
                check = srand.nextIntImproved(0,4);

                if(check == 0) {
                    result[0] = "Dead Staff";
                    result[1] = "Magic Staff";
                } else if(check == 1) {
                    result[0] = "Gold staff";
                    result[1] ="Weak staff";
                } else if(check == 2) {
                    result[0] = "Light staff";
                    result[1] = "Weak staff";
                } else if(check == 3) {
                    result[0] = "Heavy spear";
                    result[1] = "Weak spear";
                }

                break;
            case 8:
                check = srand.nextIntImproved(0,3);

                if(check == 0) {
                    result[0] = "Mage Scepter";
                    result[1] ="Magic Scepter";
                } else if(check == 1) {
                    result[0] = "Ancient Scepter";
                    result[1] = "Superior Scepter";
                } else if(check == 2) {
                    result[0] = "Power scepter";
                    result[1] = "Weak scepter";
                }
                break;
            case 9:
                check = srand.nextIntImproved(0,4);

                if(check == 0) {
                    result[0] = "Dwarf Axe";
                    result[1] = "Exeptional Axe";
                } else if(check == 1) {
                    result[0] = "Heavy Axe";
                    result[1] = "Weak axe";
                } else if(check == 2) {
                    result[0] = "Trees axe";
                    result[1] = "Weak axe";
                } else if(check == 3) {
                    result[0] = "Dark Axe";
                    result[1] = "Weak Axe";
                }
                break;
        }
        return result;
    }



    public static void dropItem(World world, int coordX, int coordY, int level, int rarity) {
        boolean legendary = false;
        if(rarity == 10)
            legendary = true;

        int gold = 0; //стоимость шмотки
        int lv1_stat_atck = 2;//атака
        int lv1_stat_dmg = 1;//дамаг
        int lv1_stat_def = 2;//защита
        int lv1_stat_heal = 3;//хп
        int lv1_stat_mana = 2;//мана
        int lv1_stat_skill = 0;//скилл
        int lv1_stat_magic = 0;//магия
        int hregen = 0;
        int mregen = 0;
        int reqSkill = 0;
        int reqMagic = 0;

        int frequency = 120;
        int range = 7;


        int rndCof = 0;

        int GraphicsID = 100006;
        int GraphicsX  = 0;
        int GraphicsY  = 0;

        String selectedName = "Thing";
        String selectedDescr = "Just thing...";


        //generate item
        //1 - оружие,2 - щит,3 - нагрудник,4 - шлем,5 - боты,6 - перчи
        int type = srand.nextIntImproved(1,7);
			/* here we should generate item */
        Hashtable<String, Integer> result = item_drop(swap(type), level, rarity);
        reqSkill = result.get("rskill");
        reqMagic = result.get("rmagic");
        lv1_stat_heal = result.get("hp");
        lv1_stat_mana = result.get("mana");
        lv1_stat_dmg = result.get("dmg");
        lv1_stat_atck = result.get("atk");
        lv1_stat_def = result.get("def");
        lv1_stat_skill = result.get("skill");
        lv1_stat_magic = result.get("magic");
        hregen = result.get("hregen");
        mregen = result.get("mregen");


			/*
 			  * next lines of code maked only for test!!!
			  * in release version we replace it with template system
			  * so it's add ability to do items look closer to 1.3.x version
			  * of rhynn
			  * <begining temp code>
			*/
        //graphics id generator
        switch(type) {
            case 1:
                //дальнее,или нет?
                boolean rangeWeapon = false;//T - значит дальнее :)
                if(ChanceCalculator.calculate(50))
                    rangeWeapon = true;
                if(rangeWeapon) {
                    rndCof = srand.nextIntImproved(1,25);
                    range = 64 + rndCof;
                    int gen = srand.nextIntImproved(1, 5);
                    switch(gen) {
                        case 1: {
                            //лук
                            String[] data = generateWeaponName(4);
                            selectedName = data[0];
                            selectedDescr = data[1];
                            GraphicsX = 30;
                            GraphicsY = 45;
                        }
                        break;
                        case 2: {
                            //лук
                            String[] data = generateWeaponName(4);
                            selectedName = data[0];
                            selectedDescr = data[1];
                            GraphicsX = 30;
                            GraphicsY = 60;
                        }
                        break;
                        case 3: {
                            //лук
                            String[] data = generateWeaponName(4);
                            selectedName = data[0];
                            selectedDescr = data[1];
                            GraphicsX = 30;
                            GraphicsY = 75;
                        }
                        break;
                        case 4: {
                            //арбалет
                            String[] data = generateWeaponName(0);
                            selectedName = data[0];
                            selectedDescr = data[1];
                            GraphicsX = 30;
                            GraphicsY = 90;
                        }
                        break;
                        case 5: {
                            //звезда
                            String[] data = generateWeaponName(2);
                            selectedName = data[0];
                            selectedDescr = data[1];
                            GraphicsX = 30;
                            GraphicsY = 105;
                        }
                        break;
                    }
                }
                else
                {
                    rndCof = srand.nextIntImproved(1,level / 2);
                    frequency += rndCof;
                    int gen = srand.nextIntImproved(1, 21);
                    switch(gen) {
                        //0 - арбалеты,1 - дубинки,2 - звездочки,3 - копья
                        //4 - луки,5 - мечи,6 - ножи,7 - палки,8 - посохи,9 - топоры
                        case 1: {
                            //меч
                            String[] data = generateWeaponName(5);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 0;
                            GraphicsY = 90;
                        }
                        break;
                        case 2: {
                            //посох
                            String[] data = generateWeaponName(8);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 0;
                            GraphicsY = 45;
                        }
                        break;
                        case 3: {
                            //меч
                            String[] data = generateWeaponName(5);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 0;
                            GraphicsY = 105;
                        }
                        break;
                        case 4: {
                            //дубинка
                            String[] data = generateWeaponName(1);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 0;
                            GraphicsY = 30;
                        }
                        break;
                        case 5: {
                            //дубинка
                            String[] data = generateWeaponName(1);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 0;
                            GraphicsY = 15;
                        }
                        break;
                        case 6: {
                            //меч
                            String[] data = generateWeaponName(5);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 15;
                            GraphicsY = 0;
                        }
                        break;
                        case 7: {
                            //топор
                            String[] data = generateWeaponName(9);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 15;
                            GraphicsY = 75;
                        }
                        break;
                        case 8: {
                            //дубинка
                            String[] data = generateWeaponName(1);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 0;
                            GraphicsY = 0;
                        }
                        break;
                        case 9: {
                            //нож
                            String[] data = generateWeaponName(6);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 0;
                            GraphicsY = 60;
                        }
                        break;
                        case 10: {
                            //нож
                            String[] data = generateWeaponName(6);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 0;
                            GraphicsY = 75;
                        }
                        break;
                        case 11: {
                            //топор
                            String[] data = generateWeaponName(9);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 15;
                            GraphicsY = 60;
                        }
                        break;
                        case 12: {
                            //топор
                            String[] data = generateWeaponName(9);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 15;
                            GraphicsY = 90;
                        }
                        break;
                        case 13: {
                            //посох
                            String[] data = generateWeaponName(8);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 15;
                            GraphicsY = 105;
                        }
                        break;
                        case 14: {
                            //посох
                            String[] data = generateWeaponName(8);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 30;
                            GraphicsY = 0;
                        }
                        break;
                        case 15: {
                            //копьё
                            String[] data = generateWeaponName(3);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 30;
                            GraphicsY = 15;
                        }
                        break;
                        case 16: {
                            //топор
                            String[] data = generateWeaponName(9);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 30;
                            GraphicsY = 30;
                        }
                        break;
                        case 17: {
                            //посох
                            String[] data = generateWeaponName(8);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 45;
                            GraphicsY = 0;
                        }
                        break;
                        case 18: {
                            //меч
                            String[] data = generateWeaponName(5);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 15;
                            GraphicsY = 15;
                        }
                        break;
                        case 19: {
                            //меч
                            String[] data = generateWeaponName(5);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 15;
                            GraphicsY = 30;
                        }
                        break;
                        case 20: {
                            //меч
                            String[] data = generateWeaponName(5);
                            selectedName = data[0];
                            selectedDescr = data[1];

                            GraphicsX = 15;
                            GraphicsY = 45;
                        }
                        break;
                    }
                }
                break;
            case 2:
                int rnd0 = srand.nextIntImproved(0,8);

                if(rnd0 == 0) {
                    selectedName = "Blood Shield";
                    selectedDescr = "Enhanced Shield";
                } else if(rnd0 == 1) {
                    selectedName = "Leather shield";
                    selectedDescr = "Weak shield";
                } else if(rnd0 == 2) {
                    selectedName = "Tomb shield";
                    selectedDescr = "Weak shield";
                } else if(rnd0 == 3) {
                    selectedName = "Knight Shield";
                    selectedDescr = "Good Shield";
                } else if(rnd0 == 4) {
                    selectedName = "Great Shield";
                    selectedDescr = "Exellent Shield";
                } else if(rnd0 == 5) {
                    selectedName = "Battle Shield";
                    selectedDescr = "Better Shield";
                } else if(rnd0 == 6) {
                    selectedName = "Dead Wall";
                    selectedDescr = "Sacred Shield";
                } else if(rnd0 == 7) {
                    selectedName = "Heavy Shield";
                    selectedDescr = "Normal Shield";
                } else if(rnd0 == 8) {
                    selectedName = "Power shield";
                    selectedDescr = "Weak shield";
                }

                GraphicsID = 100007;
                int gen2 = srand.nextIntImproved(1,4);
                switch(gen2) {
                    case 1:
                        GraphicsX = 15;
                        GraphicsY = 105;
                        break;
                    case 2:
                        GraphicsX = 30;
                        GraphicsY = 0;
                        break;
                    case 3:
                        GraphicsX = 30;
                        GraphicsY = 15;
                        break;
                }
                break;
            case 3:
                int rnd2 = srand.nextIntImproved(0,13);
                if(rnd2 == 0) {
                    selectedName = "Armor";
                    selectedDescr = "CommonArmor";
                } else if(rnd2 == 1) {
                    selectedName = "LightArmor";
                    selectedDescr = "WeakArmor";
                } else if(rnd2 == 2) {
                    selectedName = "WolfSkin";
                    selectedDescr = "WeakArmor";
                } else if(rnd2 == 3) {
                    selectedName = "LeatherArmor";
                    selectedDescr = "WeakArmor";
                } else if(rnd2 == 4) {
                    selectedName = "ChitinPlate";
                    selectedDescr = "BodyArmor";
                } else if(rnd2 == 5) {
                    selectedName = "AverageArmor";
                    selectedDescr = "BodyArmor";
                } else if(rnd2 == 6) {
                    selectedName = "BreastPlate";
                    selectedDescr = "GoodArmor";
                } else if(rnd2 == 7) {
                    selectedName = "KnightCloak";
                    selectedDescr = "WeakArmor";
                } else if(rnd2 == 8) {
                    selectedName = "GhostArmor";
                    selectedDescr = "GodlyArmor";
                } else if(rnd2 == 9) {
                    selectedName = "ChainMail";
                    selectedDescr = "GoodArmor";
                } else if(rnd2 == 10) {
                    selectedName = "HeavyArmor";
                    selectedDescr = "SuperiorArmor";
                } else if(rnd2 == 11) {
                    selectedName = "MageArmor";
                    selectedDescr = "MagicArmor";
                } else if(rnd2 == 12) {
                    selectedName = "DwarfMail";
                    selectedDescr = "QualityArmor";
                }


                GraphicsID = 100007;
                int gen3 = srand.nextIntImproved(1,6);
                switch(gen3) {
                    case 1:
                        GraphicsX = 0;
                        GraphicsY = 60;
                        break;
                    case 2:
                        GraphicsX = 0;
                        GraphicsY = 75;
                        break;
                    case 3:
                        GraphicsX = 0;
                        GraphicsY = 90;
                        break;
                    case 4:
                        GraphicsX = 0;
                        GraphicsY = 105;
                        break;
                    case 5:
                        GraphicsX = 15;
                        GraphicsY = 0;
                        break;
                }
                break;
            case 4:
                int rnd1 = srand.nextIntImproved(0,10);

                if(rnd1 == 0) {
                    selectedName = "Helm";
                    selectedDescr = "Common Helm";
                } else if(rnd1 == 1) {
                    selectedName = "Light Helm";
                    selectedDescr = "Weak Helm";
                } else if(rnd1 == 2) {
                    selectedName = "Cold Face";
                    selectedDescr = "Better Helm";
                } else if(rnd1 == 3) {
                    selectedName = "Dead Crown";
                    selectedDescr = "Sacred Helm";
                } else if(rnd1 == 4) {
                    selectedName = "War Helm";
                    selectedDescr = "Exellent Helm";
                } else if(rnd1 == 5) {
                    selectedName = "Magic Cap";
                    selectedDescr = "Magic Helm";
                } else if(rnd1 == 6) {
                    selectedName = "Full Helm";
                    selectedDescr = "Valuable Helm";
                } else if(rnd1 == 7) {
                    selectedName = "Protector";
                    selectedDescr = "Regular Helm";
                } else if(rnd1 == 8) {
                    selectedName = "Spiked Helm";
                    selectedDescr = "Good Helm";
                } else if(rnd1 == 9) {
                    selectedName = "Knight Helm";
                    selectedDescr = "Quality Helm";
                }

                GraphicsID = 100007;
                int gen4 = srand.nextIntImproved(1,5);
                switch(gen4) {
                    case 1:
                        GraphicsX = 0;
                        GraphicsY = 0;
                        break;
                    case 2:
                        GraphicsX = 0;
                        GraphicsY = 15;
                        break;
                    case 3:
                        GraphicsX = 0;
                        GraphicsY = 30;
                        break;
                    case 4:
                        GraphicsX = 0;
                        GraphicsY = 45;
                        break;
                }
                break;
            case 5:
                int rnd3 = srand.nextIntImproved(0,8);

                if(rnd3 == 0) {
                    selectedName = "Boots";
                    selectedDescr = "NormalBoots";
                } else if(rnd3 == 1) {
                    selectedName = "LightBoots";
                    selectedDescr = "WeakBoots";
                } else if(rnd3 == 2) {
                    selectedName = "LeatherBoots";
                    selectedDescr = "PlainBoots";
                } else if(rnd3 == 3) {
                    selectedName = "Magicboots";
                    selectedDescr = "Weakboots";
                } else if(rnd3 == 4) {
                    selectedName = "ChainBoots";
                    selectedDescr = "SuperiorBoots";
                } else if(rnd3 == 5) {
                    selectedName = "MageBoots";
                    selectedDescr = "MagicBoots";
                } else if(rnd3 == 6) {
                    selectedName = "KnightBoots";
                    selectedDescr = "QualityBoots";
                } else if(rnd3 == 7) {
                    selectedName = "HeavyBoots";
                    selectedDescr = "BetterBoots";
                }
                GraphicsID = 100007;
                int gen5 = srand.nextIntImproved(1,4);
                switch(gen5) {
                    case 1:
                        GraphicsX = 15;
                        GraphicsY = 15;
                        break;
                    case 2:
                        GraphicsX = 15;
                        GraphicsY = 30;
                        break;
                    case 3:
                        GraphicsX = 15;
                        GraphicsY = 45;
                        break;
                }
                break;
            case 6:
                int rnd4 = srand.nextIntImproved(0,6);
                if(rnd4 == 0) {
                    selectedName = "Gloves";
                    selectedDescr = "CommonGloves";
                } else if(rnd4 == 1) {
                    selectedName = "LeatherGloves";
                    selectedDescr = "SimpleGloves";
                } else if(rnd4 == 2) {
                    selectedName = "MageGloves";
                    selectedDescr = "MagicGloves";
                } else if(rnd4 == 3) {
                    selectedName = "FireGloves";
                    selectedDescr = "ValuableGloves";
                } else if(rnd4 == 4) {
                    selectedName = "WiseHands";
                    selectedDescr = "HolyGloves";
                } else if(rnd4 == 5) {
                    selectedName = "LightGloves";
                    selectedDescr = "PlainGloves";
                }
                GraphicsID = 100007;
                int gen6 = srand.nextIntImproved(1,4);
                switch(gen6) {
                    case 1:
                        GraphicsX = 15;
                        GraphicsY = 60;
                        break;
                    case 2:
                        GraphicsX = 15;
                        GraphicsY = 75;
                        break;
                    case 3:
                        GraphicsX = 15;
                        GraphicsY = 90;
                        break;
                }
                break;
        }
			/* <end temp code>*/

        //pring dbg info
			/*Console.WriteLine("  ===Item DBG INFO");
			Console.WriteLine("     Req.Skill:"+reqSkill);
			Console.WriteLine("     Req.Magic:"+reqMagic);
			Console.WriteLine("     Health:"+lv1_stat_heal);
			Console.WriteLine("     Mana:"+lv1_stat_mana);
			Console.WriteLine("     Damage:"+lv1_stat_dmg);
			Console.WriteLine("     Attack:"+lv1_stat_atck);
			Console.WriteLine("     Defense:"+lv1_stat_def);
			Console.WriteLine("     Skill:"+lv1_stat_skill);
			Console.WriteLine("     Magic:"+lv1_stat_magic);
			Console.WriteLine("     Health Regen:"+hregen);
			Console.WriteLine("     Mana Regen:"+mregen);*/

        gold = level * srand.nextIntImproved(1,100);

        ItemTemplate tpl = new ItemTemplate();
        tpl.type = ItemType.fromInt(type);
        tpl.set_id = 0;
        tpl.graphics_id = GraphicsID;
        tpl.graphics_x = GraphicsX;
        tpl.graphics_y = GraphicsY;
        tpl.name = selectedName;
        tpl.description = selectedDescr;
        tpl.available_status = "all";
        tpl.can_sell = 1;
        tpl.can_drop = 1;
        tpl.max_units = 1;
        tpl.price = gold;
        tpl.health_effect = lv1_stat_heal;
        tpl.mana_effect = lv1_stat_mana;
        tpl.attack_effect = lv1_stat_atck;
        tpl.defense_effect = lv1_stat_def;
        tpl.damage_effect = lv1_stat_dmg;
        tpl.skill_effect = lv1_stat_skill;
        tpl.magic_effect = lv1_stat_magic;
        tpl.healthregenerate_effect = hregen;
        tpl.manaregenerate_effect = mregen;
        tpl.action_effect_1 = 0;
        tpl.action_effect_1_data = "";
        tpl.effect_duration = 0;
        tpl.required_skill = reqSkill;
        tpl.required_magic = reqMagic;
        tpl.frequency = frequency;
        tpl.range = range;
        tpl.premium = 0;
        tpl.usage_type = UsageType.EQUIP;


        Pickupable pickupable = new Pickupable();
        pickupable.present = true;
        pickupable.respawnDelay = 0;
        pickupable.pickupTime = 0;
        pickupable.units = 1;
        pickupable.usageType = tpl.usage_type;
        pickupable.graphicsId = tpl.graphics_id;
        pickupable.graphicsX = tpl.graphics_x;
        pickupable.graphicsY = tpl.graphics_y;
        pickupable.objectId = world.getPickupableId();
        pickupable.x = coordX;
        pickupable.y = coordY;
        pickupable.itemTemplate = tpl;
        pickupable.autoRemoveSpawnTime = Time.getUnixTimeMillis();

        world.addPickupable(pickupable);
    }














    /* stats generator */
    private static Hashtable<String, Integer> item_drop(int type, int level, int rarity)
    {
        //define vars with types(yep,it's not php...)
        double rsk = 0;
        double rmg = 0;
        double hp = 0;
        double mana = 0;
        double dmg = 0;
        double atk = 0;
        double def = 0;
        double sk = 0;
        double mg = 0;
        double hf = 0;
        double mf = 0;

        level = level+1;
        //now need to us make different items stats (not only every time atk dmg etc.)
        // you can up or decrease hp mana dmg atk def sk mg hf and mf value, but warning, do not do it more. (count
        //if you want to make own best value moblevel*1.2 if you want to set maximum dmg for weapons. i think you understand.)

        double hpz = 40;
        double manaz = 20;
        double dmgz = 40;
        double atkz = 40;
        double defz = 40;
        double skz = 40;
        double mgz = 40;

        int rrarity = rarity + 1;
        int random_nr = srand.nextIntImproved(0, rrarity); //make random value ( +1 ?)
        if(type == 1) { //stats for helm
            //maximum value here can be
            hpz = 50;
            manaz = 20;
            dmgz = 40;
            atkz = 40;
            defz = 60;
            skz = 40;
            mgz = 40;
            int rand_hp = srand.nextIntImproved(0, rrarity); //make random value only for hp
            if(random_nr == rarity && rand_hp == rarity) //checking health can be added to stats or no
                hp = 0.4f; //then can be added maximum health value for 0 lvl
            else
                hp = 0; // else will be not added this stat
            int rand_mana = srand.nextIntImproved(0, rrarity); //make random value only for mana
            if(random_nr == rarity && rand_mana == rarity) //checking mana can be added to stats or no
                mana = 0.1f; //then can be added maximum mana value for 0 lvl
            else
                mana = 0; // else will be not added this stat
            int rand_dmg = srand.nextIntImproved(0, rrarity); //make random value only for dmg
            if(random_nr == rarity && rand_dmg == rarity) //checking damage can be added to stats or no
                dmg = 0.3f; //then can be added maximum damage value for 0 lvl
            else
                dmg = 0; // else will be not added this stat

            int rand_atk = srand.nextIntImproved(0, rrarity); //make random value only for atk
            if(random_nr == rarity && rand_atk == rarity) //checking attack can be added to stats or no
                atk = 0.3f; //then can be added maximum attack value for 0 lvl
            else
                atk = 0; // else will be not added this stat

            def = 0.5f; //default value, this stat will be every time
            int rand_sk = srand.nextIntImproved(0, rrarity); //make random value only for skill
            if(random_nr == rarity && rand_sk == rarity) //checking skill can be added to stats or no
                sk = 0.3f; //then can be added maximum skill value for 0 lvl
            else
                sk = 0; // else will be not added this stat

            int rand_mg = srand.nextIntImproved(0, rrarity); //make random value only for magic (stat)
            if(random_nr == rarity && rand_hp == rarity) //checking magic can be added to stats or no
                mg = 0.3f; //then can be added maximum magic value for 0 lvl
            else
                mg = 0; // else will be not added this stat
        }
        else if(type == 2)//making stats for armor
        {
            //maximum values here can be
            hpz = 60;
            manaz = 20;
            dmgz = 70;
            atkz = 50;
            defz = 80;
            skz = 50;
            mgz = 50;
            int rand_hp = srand.nextIntImproved(0, rrarity); //make random value only for hp
            if(random_nr == rarity && rand_hp == rarity) //checking health can be added to stats or no
                hp = 0.5f; //then can be added maximum health value for 0 lvl
            else
                hp = 0; // else will be not added this stat

            int rand_mana = srand.nextIntImproved(0, rrarity); //make random value only for mana
            if(random_nr == rarity && rand_mana == rarity) //checking mana can be added to stats or no
                mana = 0.1f; //then can be added maximum mana value for 0 lvl
            else
                mana = 0; // else will be not added this stat

            int rand_dmg = srand.nextIntImproved(0, rrarity); //make random value only for dmg
            if(random_nr == rarity && rand_dmg == rarity) //checking damage can be added to stats or no
                dmg = 0.6f; //then can be added maximum damage value for 0 lvl
            else
                dmg = 0; // else will be not added this stat

            int rand_atk = srand.nextIntImproved(0, rrarity); //make random value only for atk
            if(random_nr == rarity && rand_atk == rarity) //checking attack can be added to stats or no
                atk = 0.4f; //then can be added maximum attack value for 0 lvl
            else
                atk = 0; // else will be not added this stat

            def = 0.7f; //default value, this stat will be every time
            int rand_sk = srand.nextIntImproved(0, rrarity); //make random value only for skill
            if(random_nr == rarity && rand_sk == rarity) //checking skill can be added to stats or no
                sk = 0.4f; //then can be added maximum skill value for 0 lvl
            else
                sk = 0; // else will be not added this stat

            int rand_mg = srand.nextIntImproved(0, rrarity); //make random value only for magic (stat)
            if(random_nr == rarity && rand_hp == rarity) //checking magic can be added to stats or no
                mg = 0.4f; //then can be added maximum magic value for 0 lvl
            else
                mg = 0; // else will be not added this stat
        }
        else if(type == 3)//making stats for weapon
        {
            //maximum values here can be
            hpz = 50;
            manaz = 40;
            dmgz = 130;
            atkz = 130;
            defz = 40;
            skz = 50;
            mgz = 50;
            int rand_hp = srand.nextIntImproved(0, rrarity); //make random value only for hp
            if(random_nr == rarity && rand_hp == rarity) //checking health can be added to stats or no
                hp = 0.4f; //then can be added maximum health value for 0 lvl
            else
                hp = 0; // else will be not added this stat

            int rand_mana = srand.nextIntImproved(0, rrarity); //make random value only for mana
            if(random_nr == rarity && rand_mana == rarity) //checking mana can be added to stats or no
                mana = 0.3f; //then can be added maximum mana value for 0 lvl
            else
                mana = 0; // else will be not added this stat

            dmg = 1.5f; //default value, this stat will be every time
            atk = 1.5f; //default value, this stat will be every time
            int rand_def = srand.nextIntImproved(0, rrarity); //make random value only for def
            if(random_nr == rarity && rand_def == rarity) //checking defence can be added to stats or no
                def = 0.3f; //then can be added maximum defence value for 0 lvl
            else
                def = 0; // else will be not added this stat

            int rand_sk = srand.nextIntImproved(0, rrarity); //make random value only for skill
            if(random_nr == rarity && rand_sk == rarity) //checking skill can be added to stats or no
                sk = 0.4f; //then can be added maximum skill value for 0 lvl
            else
                sk = 0; // else will be not added this stat

            int rand_mg = srand.nextIntImproved(0, rrarity); //make random value only for magic (stat)
            if(random_nr == rarity && rand_hp == rarity) //checking magic can be added to stats or no
                mg = 0.4f; //then can be added maximum magic value for 0 lvl
            else
                mg = 0; // else will be not added this stat
        }
        else if(type == 4)//making stats for shield
        {
            //maximum values can be
            hpz = 30;
            manaz = 20;
            dmgz = 40;
            atkz = 50;
            defz = 120;
            skz = 40;
            mgz = 70;
            int rand_hp = srand.nextIntImproved(0, rrarity); //make random value only for hp
            if(random_nr == rarity && rand_hp == rarity) //checking health can be added to stats or no
                hp = 0.2f; //then can be added maximum health value for 0 lvl
            else
                hp = 0; // else will be not added this stat

            int rand_mana = srand.nextIntImproved(0, rrarity); //make random value only for mana
            if(random_nr == rarity && rand_mana == rarity) //checking mana can be added to stats or no
                mana = 0.1f; //then can be added maximum mana value for 0 lvl
            else
                mana = 0; // else will be not added this stat

            int rand_dmg = srand.nextIntImproved(0, rrarity); //make random value only for dmg
            if(random_nr == rarity && rand_dmg == rarity) //checking damage can be added to stats or no
                dmg = 0.3f; //then can be added maximum damage value for 0 lvl
            else
                dmg = 0; // else will be not added this stat

            int rand_atk = srand.nextIntImproved(0, rrarity); //make random value only for atk
            if(random_nr == rarity && rand_atk == rarity) //checking attack can be added to stats or no
                atk = 0.4f; //then can be added maximum attack value for 0 lvl
            else
                atk = 0; // else will be not added this stat

            def = 1.1f; //default value, this stat will be every time
            int rand_sk = srand.nextIntImproved(0, rrarity); //make random value only for skill
            if(random_nr == rarity && rand_sk == rarity) //checking skill can be added to stats or no
                sk = 0.3f; //then can be added maximum skill value for 0 lvl
            else
                sk = 0; // else will be not added this stat

            int rand_mg = srand.nextIntImproved(0, rrarity); //make random value only for magic (stat)
            if(random_nr == rarity && rand_hp == rarity) //checking magic can be added to stats or no
                mg = 0.6f; //then can be added maximum magic value for 0 lvl
            else
                mg = 0; // else will be not added this stat
        }
        else if(type == 5)//making stats for glove
        {
            //maximum values here can be
            hpz = 40;
            manaz = 20;
            dmgz = 40;
            atkz = 40;
            defz = 40;
            skz = 40;
            mgz = 40;
            int rand_hp = srand.nextIntImproved(0, rrarity); //make random value only for hp
            if(random_nr == rarity && rand_hp == rarity) //checking health can be added to stats or no
                hp = 0.3f; //then can be added maximum health value for 0 lvl
            else
                hp = 0; // else will be not added this stat

            int rand_mana = srand.nextIntImproved(0, rrarity); //make random value only for mana
            if(random_nr == rarity && rand_mana == rarity) //checking mana can be added to stats or no
                mana = 0.1f; //then can be added maximum mana value for 0 lvl
            else
                mana = 0; // else will be not added this stat

            int rand_dmg = srand.nextIntImproved(0, rrarity); //make random value only for dmg
            if(random_nr == rarity && rand_dmg == rarity) //checking damage can be added to stats or no
                dmg = 0.3f; //then can be added maximum damage value for 0 lvl
            else
                dmg = 0; // else will be not added this stat

            int rand_atk = srand.nextIntImproved(0, rrarity); //make random value only for atk
            if(random_nr == rarity && rand_atk == rarity) //checking attack can be added to stats or no
                atk = 0.3f; //then can be added maximum attack value for 0 lvl
            else
                atk = 0; // else will be not added this stat

            def = 0.3f; //default value, this stat will be every time
            int rand_sk = srand.nextIntImproved(0, rrarity); //make random value only for skill
            if(random_nr == rarity && rand_sk == rarity) //checking skill can be added to stats or no
                sk = 0.3f; //then can be added maximum skill value for 0 lvl
            else
                sk = 0; // else will be not added this stat

            int rand_mg = srand.nextIntImproved(0, rrarity); //make random value only for magic (stat)
            if(random_nr == rarity && rand_hp == rarity) //checking magic can be added to stats or no
                mg = 0.3f; //then can be added maximum magic value for 0 lvl
            else
                mg = 0; // else will be not added this stat
        }
        else if(type == 6)//making stats for boots
        {
            //maximum values here
            hpz = 40;
            manaz = 20;
            dmgz = 50;
            atkz = 50;
            defz = 40;
            skz = 30;
            mgz = 30;
            int rand_hp = srand.nextIntImproved(0, rrarity); //make random value only for hp
            if(random_nr == rarity && rand_hp == rarity) //checking health can be added to stats or no
                hp = 0.5f; //then can be added maximum health value for 0 lvl
            else
                hp = 0; // else will be not added this stat

            int rand_mana = srand.nextIntImproved(0, rrarity); //make random value only for mana
            if(random_nr == rarity && rand_mana == rarity) //checking mana can be added to stats or no
                mana = 0.1f; //then can be added maximum mana value for 0 lvl
            else
                mana = 0; // else will be not added this stat

            int rand_dmg = srand.nextIntImproved(0, rrarity); //make random value only for dmg
            if(random_nr == rarity && rand_dmg == rarity) //checking damage can be added to stats or no
                dmg = 0.4f; //then can be added maximum damage value for 0 lvl
            else
                dmg = 0; // else will be not added this stat

            int rand_atk = srand.nextIntImproved(0, rrarity); //make random value only for atk
            if(random_nr == rarity && rand_atk == rarity) //checking attack can be added to stats or no
                atk = 0.4f; //then can be added maximum attack value for 0 lvl
            else
                atk = 0; // else will be not added this stat

            def = 0.3f; //default value, this stat will be every time
            int rand_sk = srand.nextIntImproved(0, rrarity); //make random value only for skill
            if(random_nr == rarity && rand_sk == rarity) //checking skill can be added to stats or no
                sk = 0.2f; //then can be added maximum skill value for 0 lvl
            else
                sk = 0; // else will be not added this stat

            int rand_mg = srand.nextIntImproved(0, rrarity); //make random value only for magic (stat)
            if(random_nr == rarity && rand_mg == rarity) //checking magic can be added to stats or no
                mg = 0.2f; //then can be added maximum magic value for 0 lvl
            else
                mg = 0; // else will be not added this stat
        }
        else //item not exists
        {
            hp = 0;
            mana = 0;
            dmg = 0;
            atk = 0;
            def = 0;
            sk = 0;
            mg = 0;
        }
        double rand_mf = srand.nextIntImproved(0, rrarity); //make random value only for magic fill
        if(random_nr == rarity && rand_mf == rarity) //checking magic fill can be added to stats or no
            mf = 0.2f; //then can be added maximum magic fill value for 0 lvl
        else
            mf = 0; // else will be not added this stat
        double rand_hf = srand.nextIntImproved(0, rrarity); //make random value only for health fill
        if(random_nr == rarity && rand_hf == rarity) //checking health fill can be added to stats or no
            hf = 0.2f; //then can be added maximum magic fill value for 0 lvl
        else
            hf = 0; // else will be not added this stat

        // now need to make real stats
        if(hp > 0) { //checking that stats was added or not
            //then make random health number
            double hp_max = hp * level; //set maximum value of health
            double hp_min = 0.1f; //set minimum value of health
            hp_max = hp_max * 10; //making value for set max number perfectly
            hp_min = hp_min * 10; //making value for set min number perfectly
            double rand_hp = ChanceCalculator.next(hp_min * 10,hp_max * 10) / 10; //making random value of health
            hp = rand_hp / 10; // true value;
        } else
            hp = 0;

        if(mana > 0) { //checking that stats was added or not
            //then make random mana number
            double mana_max = mana * level; //set maximum value of mana
            double mana_min = 0.1f; //set minimum value of mana
            mana_max = mana_max * 10; //making value for set max number perfectly
            mana_min = mana_min * 10; //making value for set min number perfectly
            double rand_mana = ChanceCalculator.next(mana_min * 10,mana_max * 10) / 10; //making random value of mana
            mana = rand_mana / 10; // true value;
        } else
            mana = 0;

        if(dmg > 0) { //checking that stats was added or not
            //then make random damage number
            double dmg_max = dmg * level; //set maximum value of damage
            double dmg_min = 0.1f; //set minimum value of dmg
            dmg_max = dmg_max * 10; //making value for set max number perfectly
            dmg_min = dmg_min * 10; //making value for set min number perfectly
            double rand_dmg = ChanceCalculator.next(dmg_min * 10,dmg_max * 10) / 10; //making random value of damage
            dmg = rand_dmg / 10; // true value;
        } else
            dmg = 0;

        if(atk > 0) { //checking that stats was added or not
            //then make random attack number
            double atk_max = atk * level; //set maximum value of attack
            double atk_min = 0.1f; //set minimum value of atk
            atk_max = atk_max * 10; //making value for set max number perfectly
            atk_min = atk_min * 10; //making value for set min number perfectly
            double rand_atk = ChanceCalculator.next(atk_min * 10,atk_max * 10) / 10; //making random value of attack
            atk = rand_atk / 10; // true value;
        } else
            atk = 0;

        if(def > 0) { //checking that stats was added or not
            //then make random defence number
            double def_max = def * level; //set maximum value of defence
            double def_min = 0.1f; //set minimum value of def
            def_max = def_max * 10; //making value for set max number perfectly
            def_min = def_min * 10; //making value for set min number perfectly
            double rand_def = ChanceCalculator.next(def_min * 10,def_max * 10) / 10; //making random value of defence
            def = rand_def / 10; // true value;
        } else
            def = 0;

        if(sk > 0) { //checking that stats was added or not
            //then make random skill number
            double sk_max = sk * level; //set maximum value of skill
            double sk_min = 0.1f; //set minimum value of sk
            sk_max = sk_max * 10; //making value for set max number perfectly
            sk_min = sk_min * 10; //making value for set min number perfectly
            double rand_sk = ChanceCalculator.next(sk_min * 10,sk_max * 10) / 10; //making random value of skill
            sk = rand_sk / 10; // true value;
        } else
            sk = 0;

        if(mg > 0) { //checking that stats was added or not
            //then make random magic number
            double mg_max = mg * level; //set maximum value of magic
            double mg_min = 0.1f; //set minimum value of mg
            mg_max = mg_max * 10; //making value for set max number perfectly
            mg_min = mg_min * 10; //making value for set min number perfectly
            double rand_mg = ChanceCalculator.next(mg_min * 10,mg_max * 10) / 10; //making random value of magic
            mg = rand_mg / 10; // true value;
        } else
            mg = 0;

        if(hf > 0) { //checking that stats was added or not
            //then make random health fill number
            double hf_max = hf * level; //set maximum value of health fill
            double hf_min = 0.1f; //set minimum value of hf
            hf_max = hf_max * 10; //making value for set max number perfectly
            hf_min = hf_min * 10; //making value for set min number perfectly
            rand_hf = ChanceCalculator.next(hf_min * 10,hf_max * 10) / 10; //making random value of health fill
            hf = rand_hf / 10; // true value;
        } else
            hf = 0;

        if(mf > 0) { //checking that stats was added or not
            //then make random mana fill number
            double mf_max = mf * level; //set maximum value of mana fill
            double mf_min = 0.1f; //set minimum value of mf
            mf_max = mf_max * 10; //making value for set max number perfectly
            mf_min = mf_min * 10; //making value for set min number perfectly
            rand_mf = ChanceCalculator.next(mf_min * 10,mf_max * 10) / 10; //making random value of mana fill
            mf = rand_mf / 10; // true value;
        } else
            mf = 0;

        // now need to set skill and magic requements for items:

        //now we set procents how much from full value of stats is setted
        double hfz = 30; //max value in healthfill
        double mfz = 30; // max value in mana fill

        double hpps = 0;
        int hpps2 = 0;
        if(hp > 0) {
            hpps = 100 / hpz;
            hpps2 = (int)(hp * hpps);
        } else
            hpps2 = 0;

        double manaps = 0;
        int manaps2 = 0;
        if(mana > 0) {
            manaps = 100 / manaz;
            manaps2 = (int)(mana * manaps);
        } else
            manaps2 = 0;

        double dmgps = 0;
        int dmgps2 = 0;
        if(dmg > 0) {
            dmgps = 100 / dmgz;
            dmgps2 = (int)(dmg * dmgps);
        } else
            dmgps2 = 0;

        double atkps = 0;
        int atkps2 = 0;
        if(atk > 0) {
            atkps = 100 / atkz;
            atkps2 = (int)(atk * atkps);
        } else
            atkps2 = 0;

        double defps = 0;
        int defps2 = 0;
        if(def > 0) {
            defps = 100 / defz;
            defps2 = (int)(def * defps);
        } else
            defps2 = 0;

        double skps = 0;
        int skps2 = 0;
        if(sk > 0) {
            skps = 100 / skz;
            skps2 = (int)(sk * skps);
        } else
            skps2 = 0;

        double mgps = 0;
        int mgps2 = 0;
        if(mg > 0) {
            mgps = 100 / mgz;
            mgps2 = (int)(mg * mgps);
        } else
            mgps2 = 0;

        double hfps = 0;
        int hfps2 = 0 ;
        if(hf > 0) {
            hfps = 100 / hfz;
            hfps2 = (int)(hf * hfps);
        } else
            hfps2 = 0;

        double mfps = 0;
        int mfps2 = 0;
        if(mf > 0) {
            mfps = 100 / mfz;
            mfps2 = (int)(mf * mfps);
        } else
            mfps2 = 0;

        //now need to set skill and magic requements
        if(level < 5) {
            // if mob level is 0-4 then will be no requements
            rsk = 0;
            rmg = 0;
        } else {
            int min = 0;
            int vimd = 0;
            int vimd2 = 0;
            int vimd3 = 0;
            int vimd2s = 0;
            int vimd3s = 0;
            int rsks = 0;
            int rmgs = 0;

            min = 0;
            OptionalInt highest = IntStream.of(new int[]{hpps2, manaps2, dmgps2, atkps2, defps2, skps2, mgps2, hfps2, mfps2}).max();
            if(highest.isPresent())
                min = highest.getAsInt();
            if(min > 40)
                vimd = min - 40;
            else
                vimd = (min / 2);

            vimd2 = 160 * vimd;
            vimd3 = vimd2 / 100;
            vimd2s = 160 * min;
            vimd3s = vimd2s / 100;
            rsks = vimd3s;
            rmgs = vimd3s;


            if(srand.nextIntImproved(0, 2) == 1) {
                rmg = ChanceCalculator.next((double)vimd3, (double)rmgs);
                rsk = ChanceCalculator.next((double)vimd3, (double)rsks);
            } else {
                if(srand.nextIntImproved(0,4) == 3)
                    rsk = 0;
                else
                    rsk = ChanceCalculator.next((double)vimd3, (double)rsks);
                if(srand.nextIntImproved(0, 4) == 3)
                    rmg = 0;
                else
                    rmg = ChanceCalculator.next((double)vimd3, (double)rmgs);

                if(rsk == 0 && rmg == 0) {
                    int rq = srand.nextIntImproved(0, 2);
                    if(rq == 1)
                        rsk = ChanceCalculator.next((double)vimd3, (double)rsks);
                    else
                        rmg = ChanceCalculator.next((double)vimd3, (double)rmgs);
                }
            }
            if(rsk > 160)
                rsk = 160;
            if(rmg > 160)
                rmg = 160;
        }

        int _rsk = (int)(rsk * 10);
        int _rmg = (int)(rmg * 10);
        int _hp = (int)(hp * 10);
        int _mana = (int)(mana * 10);
        int _dmg = (int)(dmg * 10);
        int _atk = (int)(atk * 10);
        int _def = (int)(def * 10);
        int _sk = (int)(sk * 10);
        int _mg = (int)(mg * 10);
        int _hf = (int)(hf * 10);
        int _mf = (int)(mf * 10);

        Hashtable<String, Integer> result = new Hashtable<>();
        result.put("rskill", _rsk);
        result.put("rmagic", _rmg);
        result.put("hp", _hp);
        result.put("mana", _mana);
        result.put("dmg", _dmg);
        result.put("atk", _atk);
        result.put("def", _def);
        result.put("skill", _sk);
        result.put("magic", _mg);
        result.put("hregen", _hf);
        result.put("mregen", _mf);
        return result;
    }
}
