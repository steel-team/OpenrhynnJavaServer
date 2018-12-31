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

import com.steelteam.openrhynn.enums.AIType;
import com.steelteam.openrhynn.enums.NpcType;

import java.util.ArrayList;

public class NpcTemplate {
    public int id = 0;
    public String name = null;
    public int clan_id = -1;
    public int graphics_id = 0;
    public int graphics_x = 0;
    public int graphics_y = 0;
    public int graphics_dim = 0;
    public int level = 0;
    public int health = 0;
    public int move_range = 0;
    public int move_speed = 0;
    public NpcType type = NpcType.TRADER;
    public AIType ai = AIType.STAY;
    public String talk_script = null;
    public String dialog_script = null;
    public ArrayList<Buyable> items = new ArrayList<>();

    public int talkDelay = 0;
}
