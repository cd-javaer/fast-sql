package top.fastsql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import top.fastsql.config.DataSourceType;

import javax.sql.DataSource;
import java.sql.Driver;

/**
 * SQL类的工厂类
 */
public class SQLFactory {
    private DataSource dataSource;

    private DataSourceType dataSourceType = DataSourceType.POSTGRESQL;

    private boolean ignoreWarnings = true;

    private int fetchSize = -1;

    private int maxRows = -1;

    private int queryTimeout = -1;

    private boolean skipResultsProcessing = false;

    private boolean skipUndeclaredResults = false;

    private boolean resultsMapCaseInsensitive = false;

    private JdbcTemplate jdbcTemplate;

    /**
     * @see SQLFactory#createSQL()
     */
    @Deprecated
    public SQL createSQL() {
        return sql();
    }

    /**
     * 创建一个SQL实例
     */
    public SQL sql() {
        if (this.jdbcTemplate == null) {
            this.jdbcTemplate = new JdbcTemplate();
            this.jdbcTemplate.setIgnoreWarnings(ignoreWarnings);
            this.jdbcTemplate.setFetchSize(fetchSize);
            this.jdbcTemplate.setMaxRows(maxRows);
            this.jdbcTemplate.setQueryTimeout(queryTimeout);
            this.jdbcTemplate.setSkipResultsProcessing(skipResultsProcessing);
            this.jdbcTemplate.setSkipUndeclaredResults(skipUndeclaredResults);
            this.jdbcTemplate.setResultsMapCaseInsensitive(resultsMapCaseInsensitive);
            this.jdbcTemplate.setDataSource(this.dataSource);
        }
        return new SQL(this.jdbcTemplate, this.dataSourceType);
    }

    public static SQLFactory createUseSimpleDateSource(Driver driver, String url, String username, String password) {
        SQLFactory sqlFactory = new SQLFactory();
        if (url.contains("jdbc:mysql:")) {
            sqlFactory.setDataSourceType(DataSourceType.MY_SQL);
        }
        if (url.contains("jdbc:oracle:")) {
            sqlFactory.setDataSourceType(DataSourceType.ORACLE);
        }

        if (url.contains("jdbc:postgresql:")) {
            sqlFactory.setDataSourceType(DataSourceType.POSTGRESQL);
        }

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver, url, username, password);
        sqlFactory.setDataSource(dataSource);
        return sqlFactory;
    }

    public static SQLFactory createUseSimpleDateSource(String url, String username, String password) {
        SQLFactory sqlFactory = new SQLFactory();

        String driverClass = null;
        Driver driver = null;
        if (url.contains("jdbc:mysql:")) {
            sqlFactory.setDataSourceType(DataSourceType.MY_SQL);
            driverClass = "com.mysql.jdbc.Driver";
        }
        if (url.contains("jdbc:oracle:")) {
            sqlFactory.setDataSourceType(DataSourceType.ORACLE);
            driverClass = "oracle.jdbc.driver.OracleDriver";

        }

        if (url.contains("jdbc:postgresql:")) {
            sqlFactory.setDataSourceType(DataSourceType.POSTGRESQL);
            driverClass = "org.postgresql.Driver";

        }

        try {
            driver = (Driver) Class.forName(driverClass).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver, url, username, password);

        sqlFactory.setDataSource(dataSource);
        return sqlFactory;
    }


    public DatabaseMeta createMeta() {
        return new DatabaseMeta();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSourceType getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public boolean isIgnoreWarnings() {
        return ignoreWarnings;
    }

    public void setIgnoreWarnings(boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public boolean isSkipResultsProcessing() {
        return skipResultsProcessing;
    }

    public void setSkipResultsProcessing(boolean skipResultsProcessing) {
        this.skipResultsProcessing = skipResultsProcessing;
    }

    public boolean isSkipUndeclaredResults() {
        return skipUndeclaredResults;
    }

    public void setSkipUndeclaredResults(boolean skipUndeclaredResults) {
        this.skipUndeclaredResults = skipUndeclaredResults;
    }

    public boolean isResultsMapCaseInsensitive() {
        return resultsMapCaseInsensitive;
    }

    public void setResultsMapCaseInsensitive(boolean resultsMapCaseInsensitive) {
        this.resultsMapCaseInsensitive = resultsMapCaseInsensitive;
    }
}
