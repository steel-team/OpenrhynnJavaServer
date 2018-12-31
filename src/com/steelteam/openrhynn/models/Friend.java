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

import com.steelteam.openrhynn.data.DataSource;
import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.enums.UserType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Friend {
    public int friend_id = 0;
    public String frinend_name = "";
    public int initiator = 0;
    public boolean isConfirmed = false;

    public static String loadFriendName(int friendId) {
        String friend = "";
        int userFriendId = 0;

        Connection conn = null;
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT user_id, display_name FROM characters WHERE id='" + friendId + "'");
            if (resultSet.next()) {
                userFriendId = resultSet.getInt(1);
                friend = resultSet.getString(2);
            }
            state.close();
            resultSet.close();

            state = conn.createStatement();
            resultSet = state.executeQuery("SELECT type FROM users WHERE id='" + userFriendId + "'");

            if(resultSet.next()) {
                friend = Character.getDisplayName(friend, UserType.fromInt(resultSet.getInt(1)));
            }

            state.close();
            resultSet.close();

        } catch (Exception ex) { ex.printStackTrace(); } finally { try { conn.close(); } catch (Exception e) {} }

        return friend;
    }
}
