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

import com.steelteam.openrhynn.enums.ItemType;
import com.steelteam.openrhynn.enums.UsageType;

public class Item {
    public int id = 0;
    public int char_id = 0;
    public int tpl_id = 0;
    public ItemType type = ItemType.UNKNOWN;
    public int set_id = 0;
    public int graphics_id = 0;
    public int graphics_x = 0;
    public int graphics_y = 0;
    public String name = null;
    public String description = null;
    public String available_status = null;
    public int can_sell = 0;
    public int can_drop = 0;
    public int units = 0;
    public int max_units = 0;
    public int units_sell = 0;
    public int price = 0;
    public int health_effect = 0;
    public int mana_effect = 0;
    public int attack_effect = 0;
    public int defense_effect = 0;
    public int damage_effect = 0;
    public int skill_effect = 0;
    public int magic_effect = 0;
    public int healthregenerate_effect = 0;
    public int manaregenerate_effect = 0;
    public int action_effect_1 = 0;
    public String action_effect_1_data = null;
    public int effect_duration = 0;
    public int required_skill = 0;
    public int required_magic = 0;
    public int frequency = 0;
    public int range = 0;
    public int premium = 0;
    public UsageType usage_type = UsageType.UNKNOWN;
    public int equiped = 0;
    public int belt = -1;

    /*public int getFreeUnits() {
        return units - units_sell;
    }*/
}
