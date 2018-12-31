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

public enum ItemType {
    UNKNOWN,
    WEAPON_1,
    SHIELD_1,
    ARMOR,
    HELMET,
    BOOTS,
    GLOVES;

    public static ItemType fromInt(int itemType) {
        switch(itemType) {
            case 1:
                return WEAPON_1;
            case 2:
                return SHIELD_1;
            case 3:
                return ARMOR;
            case 4:
                return HELMET;
            case 5:
                return BOOTS;
            case 6:
                return GLOVES;
        }
        return UNKNOWN;
    }

    public static int toInt(ItemType itemType) {
        switch(itemType) {
            case WEAPON_1:
                return 1;
            case SHIELD_1:
                return 2;
            case ARMOR:
                return 3;
            case HELMET:
                return 4;
            case BOOTS:
                return 5;
            case GLOVES:
                return 6;
        }
        return 0;
    }
}
