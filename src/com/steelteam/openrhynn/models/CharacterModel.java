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
package com.steelteam.openrhynn.models;

public class CharacterModel {
    public int id = 0;//4
    public int classId = 0;
    public int clanId = 0;//3
    public int worldId = 0;//4
    public int graphicsId = 0;
    public int graphicsX = 0;
    public int graphicsY = 0;
    public int graphicsDim = 0;
    public int x = 0;//2
    public int y = 0;//2
    public int level = 0;//1
    public int levelPoints = 0;//2
    public int experience = 0;//4
    public int gold = 0;//4
    public int healthBase = 0;
    public int healthEffectsExtra = 0;//2
    public int healthCurrent = 0;//2
    public int manaBase = 0;
    public int manaEffectsExtra = 0;//2
    public int manaCurrent = 0;//2
    public int attackBase = 0;
    public int attackEffectsExtra = 0;
    public int defenseBase = 0;
    public int defenseEffectsExtra = 0;
    public int damageBase = 0;
    public int damageEffectsExtra = 0;
    public int skillBase = 0;
    public int skillEffectsExtra = 0;
    public int magicBase = 0;
    public int magicEffectsExtra = 0;
    public int healthregenerateBase = 0;
    public int healthregenerateEffectsExtra = 0;
    public int manaregenerateBase = 0;
    public int manaregenerateEffectsExtra = 0;
    public String displayName = null;
    public String ownStatus = null;
}
