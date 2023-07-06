package recordstofile.query;

import lombok.Data;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@Data
public class Query {

    private String sql;
    private SqlParameterSource sqlParameterSource;

    public void buildQueryAll() {
        this.sql = "SELECT * FROM TEST_TABLE ORDER BY ID";
    }

    public void buildQueryWithName(String name) {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", name, OracleTypes.VARCHAR);

        this.sql = "SELECT * FROM TEST_TABLE WHERE lower(name) = lower(:name) ORDER BY ID";
        this.sqlParameterSource = mapSqlParameterSource;
    }

}
