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
package com.steelteam.openrhynn.scripting;

import com.steelteam.openrhynn.entities.Entity;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.utilits.AdvRandom;

import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ScriptManager {
    private static ScriptEngineManager scriptEngineManager = null;
    private static HashMap<String, ScriptEngine> loadedScripts = new HashMap<>();

    public static void initialize() {
        scriptEngineManager = new ScriptEngineManager();
        //nashorn = scriptEngineManager.getEngineByName("nashorn");
    }

    //category = triggers, talk, quests
    public static void loadScript(String category, String key) {
        try {
            String lkey = key;
            if (!lkey.endsWith(".js"))
                lkey += ".js";
            if(!"".equals(category))
                category += "/";
            Path path = Paths.get("./data/scripts/" + category + lkey);
            String script = new String(Files.readAllBytes(path));
            ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
            nashorn.eval(script);
            loadedScripts.put(lkey, nashorn);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public static void executeTrigger(String key, Entity requester) {
        String lkey = key;
        if(!lkey.endsWith(".js"))
            lkey += ".js";
        if(!loadedScripts.containsKey(lkey))
            loadScript("triggers", lkey);
        if(!loadedScripts.containsKey(lkey))
            return;

        try {
            ScriptEngine engine = loadedScripts.get(lkey);
            Invocable invocable = (Invocable)engine;
            invocable.invokeFunction("onTriggerEnter", requester);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void executeHelp(Entity requester, int index) {
        String lkey = "help.js";
        if(!loadedScripts.containsKey(lkey))
            loadScript("", lkey);
        if(!loadedScripts.containsKey(lkey))
            return;

        try {
            ScriptEngine engine = loadedScripts.get(lkey);
            Invocable invocable = (Invocable)engine;
            invocable.invokeFunction("onHelpRequest", requester, index);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void executeRequestTalk(String key, World world, Entity requester) {
        String lkey = key;
        if(!lkey.endsWith(".js"))
            lkey += ".js";
        if(!loadedScripts.containsKey(lkey))
            loadScript("talk", lkey);
        if(!loadedScripts.containsKey(lkey))
            return;

        try {
            ScriptEngine engine = loadedScripts.get(lkey);
            Invocable invocable = (Invocable)engine;
            invocable.invokeFunction("onTalkRequest", world, requester);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static int getDelay(String key) {
        String lkey = key;
        if(!lkey.endsWith(".js"))
            lkey += ".js";
        if(!loadedScripts.containsKey(lkey))
            loadScript("talk", lkey);
        if(!loadedScripts.containsKey(lkey))
            return 0;

        try {
            ScriptEngine engine = loadedScripts.get(lkey);
            Invocable invocable = (Invocable)engine;
            return (Integer)invocable.invokeFunction("getExecutionTime");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }










    public static void executeRequestTalkNpc(String key, World world, Entity requester) {
        String lkey = key;
        if(!lkey.endsWith(".js"))
            lkey += ".js";
        if(!loadedScripts.containsKey(lkey))
            loadScript("talk_npcs", lkey);
        if(!loadedScripts.containsKey(lkey))
            return;

        try {
            ScriptEngine engine = loadedScripts.get(lkey);
            Invocable invocable = (Invocable)engine;
            invocable.invokeFunction("onTalkRequest", world, requester);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static int getDelayNpc(String key) {
        String lkey = key;
        if(!lkey.endsWith(".js"))
            lkey += ".js";
        if(!loadedScripts.containsKey(lkey))
            loadScript("talk_npcs", lkey);
        if(!loadedScripts.containsKey(lkey))
            return 0;

        try {
            ScriptEngine engine = loadedScripts.get(lkey);
            Invocable invocable = (Invocable)engine;
            return (Integer)invocable.invokeFunction("getExecutionTime");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
