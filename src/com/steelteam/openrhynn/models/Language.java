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

import com.steelteam.openrhynn.data.ServerConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Language {
    private String language = null;
    private String langCode = null;
    private HashMap<String, String> strings = new HashMap<>();

    public void prepareStrings(String lang) {
        this.language = lang;
        //load from json, path localization/<lang>.json
        try {
            Path path = Paths.get("./localization/" + this.language +".json");
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(path), Charset.forName("UTF-8")));
            JSONObject langj = obj.getJSONObject("language");
            JSONArray arr = langj.getJSONArray("data");
            langCode = langj.getString("code");
            for(int i = 0; i < arr.length(); i++) {
                //replace required symbols
                String val = arr.getJSONObject(i).getString("value");
                String id = arr.getJSONObject(i).getString("id");
                strings.put(id, val);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        ServerConfig.languages.put(this.language, this);
    }

    public String getName() {
        return language;
    }

    public String getEntry(String id) {
        return strings.get(id);
    }

    public String getCode() {
        return langCode;
    }
}
