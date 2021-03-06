/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.transaction.xa.jta.connection.dialect;

import com.zaxxer.hikari.HikariDataSource;
import io.shardingsphere.core.constant.DatabaseType;
import io.shardingsphere.transaction.xa.fixture.DataSourceUtils;
import io.shardingsphere.transaction.xa.jta.datasource.XADataSourceFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAResource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class MySQLXAConnectionWrapperTest {
    
    private XADataSource xaDataSource;
    
    @Mock
    private Connection connection;
    
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws SQLException, ClassNotFoundException {
        Connection connection = (Connection) mock(Class.forName("com.mysql.jdbc.Connection"));
        DataSource dataSource = DataSourceUtils.build(HikariDataSource.class, DatabaseType.MySQL, "ds1");
        xaDataSource = XADataSourceFactory.build(DatabaseType.MySQL, dataSource);
        when(this.connection.unwrap((Class<Object>) any())).thenReturn(connection);
    }
    
    @Test
    public void assertCreateMySQLConnection() throws SQLException {
        XAConnection actual = new MySQLXAConnectionWrapper().wrap(xaDataSource, connection);
        assertThat(actual.getXAResource(), instanceOf(XAResource.class));
        assertThat(actual.getConnection(), instanceOf(Connection.class));
    }
}
