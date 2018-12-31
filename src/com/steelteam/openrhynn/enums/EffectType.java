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

public enum EffectType {
    NO_EFFECT,
    RESTORE_HEALTH,
    RESTORE_MANA,
    SKILL_TARGET,
    SKILL_AOE;

    public static EffectType fromInt(int clientType) {
        switch(clientType) {
            case 0:
                return NO_EFFECT;
            case 1:
                return RESTORE_HEALTH;
            case 2:
                return RESTORE_MANA;
            case 3:
                return SKILL_TARGET;
            case 4:
                return SKILL_AOE;
        }
        return NO_EFFECT;
    }

    public static int toInt(EffectType clientType) {
        switch(clientType) {
            case NO_EFFECT:
                return 0;
            case RESTORE_HEALTH:
                return 1;
            case RESTORE_MANA:
                return 2;
            case SKILL_TARGET:
                return 3;
            case SKILL_AOE:
                return 4;
        }
        return 6;
    }
}
