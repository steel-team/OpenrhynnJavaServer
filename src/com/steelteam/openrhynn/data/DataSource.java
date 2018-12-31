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
package com.steelteam.openrhynn.data;

import org.apache.commons.dbcp2.BasicDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import org.json.*;

public class DataSource {

    private static DataSource datasource;
    private BasicDataSource ds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        try {
            Path path = Paths.get("./configs/database.json");
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(path)));

            ds = new BasicDataSource();
            ds.setDriverClassName(obj.getJSONObject("database").getString("driver"));
            ds.setUsername(obj.getJSONObject("database").getString("login"));
            ds.setPassword(obj.getJSONObject("database").getString("password"));
            ds.setUrl(obj.getJSONObject("database").getString("str"));

            ds.setMinIdle(obj.getJSONObject("database").getInt("min_conns"));
            ds.setMaxIdle(obj.getJSONObject("database").getInt("max_conns"));
            ds.setMaxTotal(100);
            ds.setTimeBetweenEvictionRunsMillis(300000);
            //ds.setMaxConnLifetimeMillis(20000);
            //ds.setMaxWaitMillis(20000);
            ds.setMaxOpenPreparedStatements(obj.getJSONObject("database").getInt("prepared_statements_max"));
            ds.setTestOnBorrow(true);
            ds.setValidationQuery("select 1 as dbcp_connection_test");
            ds.setTestWhileIdle(true);
            //ds.
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        Connection tmp = this.ds.getConnection();
        tmp.setAutoCommit(false);
        return tmp;
    }

}
