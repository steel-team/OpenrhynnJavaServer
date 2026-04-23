/*
MIT License
-----------

Copyright (c) 2026 Ivan Yurkov (MB "Stylo tymas" http://steel-team.net)
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

import org.json.JSONPropertyName;

import com.steelteam.openrhynn.enums.UserType;

public class CharApiEntry {
    public CharApiEntry(String _name, int _level, UserType _userType, int _worldId, int _healthMax, int _healthCurrent,
            int _manaMax, int _manaCurrent) {
        name = _name;
        level = _level;
        userType = _userType;
        worldId = _worldId;
        healthMax = _healthMax;
        healthCurrent = _healthCurrent;
        manaMax = _manaMax;
        manaCurrent = _manaCurrent;
    }

    public CharApiEntry() {

    }

    public String name = null;
    public int level = 0;
    public UserType userType = UserType.UNKNOWN;
    public int worldId = 0;
    public int healthMax = 0;
    public int healthCurrent = 0;
    public int manaMax = 0;
    public int manaCurrent = 0;

    @JSONPropertyName("name")
    public String getName() {
        return name;
    }

    @JSONPropertyName("level")
    public int getLevel() {
        return level;
    }

    @JSONPropertyName("userType")
    public UserType getUserType() {
        return userType;
    }

    @JSONPropertyName("playfieldId")
    public int getWorldId() {
        return worldId;
    }

    @JSONPropertyName("healthMax")
    public int getHealthMax() {
        return healthMax;
    }

    @JSONPropertyName("healthCurrent")
    public int getHealthCurrent() {
        return healthCurrent;
    }

    @JSONPropertyName("manaMax")
    public int getManaMax() {
        return manaMax;
    }

    @JSONPropertyName("manaCurrent")
    public int getManaCurrent() {
        return manaCurrent;
    }
}
