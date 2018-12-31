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
package com.steelteam.openrhynn.enums;

public enum Attribute {
    HEALTH,
    MANA,
    DAMAGE,
    ATTACK,
    DEFENSE,
    SKILL,
    MAGIC;

    public static Attribute fromInt(int attribute) {
        switch(attribute) {
            case 0:
                return HEALTH;
            case 1:
                return MANA;
            case 2:
                return DAMAGE;
            case 3:
                return ATTACK;
            case 4:
                return DEFENSE;
            case 5:
                return SKILL;
        }
        return MAGIC;
    }

    public static int toInt(Attribute attribute) {
        switch(attribute) {
            case HEALTH:
                return 0;
            case MANA:
                return 1;
            case DAMAGE:
                return 2;
            case ATTACK:
                return 3;
            case DEFENSE:
                return 4;
            case SKILL:
                return 5;
        }
        return 6;
    }
}
