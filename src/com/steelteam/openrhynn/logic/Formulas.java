package com.steelteam.openrhynn.logic;

import com.steelteam.openrhynn.utilits.AdvRandom;

/*
    Some parts of code belongs to marlowe & macrolutions Ltd,
    license: https://github.com/marlowe-fw/Rhynn/blob/master/LICENSE

    Other to community (read openrhynn.net forums & other sources for formulas)
    All parts which written by me(Ivan Yurkov) or community is licensed under MIT
*/
public class Formulas {
    private static AdvRandom sharedRandom = new AdvRandom();

    public static boolean isMiss(int attacker_attack,int attacker_defense,int target_skill) {
        int pSum = attacker_attack + attacker_defense;
        double pThreshold = 2.0 * target_skill / 2.5;//3.5f
        double pRandom = (double)sharedRandom.nextInt(pSum);

        if (pRandom >= pThreshold)
            return false;//don't miss
        else
            return true;//miss
    }

    public static int calculateDamage(int attacker_damage,int target_defense) {
        double tdef = (double)(sharedRandom.nextIntImproved((int)(target_defense * 0.14f), target_defense));
        int damage = sharedRandom.nextIntImproved((int)(attacker_damage * 0.14f), attacker_damage);//* 0.14f

        tdef *= 0.8f;
        if(tdef > damage) {
            tdef = (double) damage - 0.14;//-0.14f
            tdef *= 0.9f;
        }

        damage = (int)(damage-tdef);
        if(damage < 1)
            damage = 1;

        return damage;
    }

    public static int calculateMagicPower(int magic,int how_much) {
        int magic_p_cof = magic/100;
        int one = magic_p_cof + how_much;
        if(one>=100)
            one = 99;
        return one;
    }



}
