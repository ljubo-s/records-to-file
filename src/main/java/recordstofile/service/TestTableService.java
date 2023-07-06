package recordstofile.service;

import java.util.List;
import recordstofile.dto.TestTableDto;
import recordstofile.model.TestTable;

public interface TestTableService {

    List<TestTableDto> getAll();

    int insert(int number);

    int insertTestTableList(List<TestTable> testTableList);

    void deleteAll();

    long deleteByName(String name);

    long count();

}
