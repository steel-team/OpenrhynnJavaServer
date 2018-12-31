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
package com.steelteam.openrhynn;

import com.steelteam.openrhynn.data.DataSource;
import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.data.SharedVariables;
import com.steelteam.openrhynn.logic.PostInitializator;
import com.steelteam.openrhynn.logic.ServerConsole;
import com.steelteam.openrhynn.models.ServerEntry;
import com.steelteam.openrhynn.network.ORServer;
import com.steelteam.openrhynn.scripting.ScriptManager;
import com.sun.security.ntlm.Server;

import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        try {

            String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
            System.out.println("Working directory: " + currentDir);
            DataSource.getInstance().getConnection().close();
            /* create script manager */
            ScriptManager.initialize();
            ServerConfig.loadConfig();
            SharedVariables.serverInstance = new ORServer(ServerConfig.port);
            SharedVariables.serverInstance.run();
            PostInitializator.initializeServer();

            System.gc();

            ServerConsole.bind();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
