package recordstofile.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import recordstofile.model.TestTable;

public interface TestTableRepository extends JpaRepository<TestTable, Long> {

    long deleteByNameIgnoreCase(String name);

    @Modifying
    @Query(value = "TRUNCATE TABLE TEST_TABLE", nativeQuery = true)
    void deleteAllRows();

}
