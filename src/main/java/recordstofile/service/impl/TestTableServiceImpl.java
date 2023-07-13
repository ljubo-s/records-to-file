package recordstofile.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recordstofile.dto.TestTableDto;
import recordstofile.model.TestTable;
import recordstofile.repository.jpa.TestTableRepository;
import recordstofile.service.TestTableService;

@Service
public class TestTableServiceImpl implements TestTableService {

    private static final String LOREM_STRING = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do "
        + "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud "
        + "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in "
        + "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat "
        + "cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    private static final List<String> LIST_NAMES = List.of("Alan", "Ada", "Charles", "Alice", "Martin", "Sophia",
        "John", "Lucy", "Thomas", "Linda");
    private static final List<String> LIST_SURNAMES = List.of("Tyring", "Byron", "Babbage", "Robinson", "Anderson",
        "Carter", "Smith", "Evans", "Miler", "Cooper");
    private final TestTableRepository testTableRepository;
    private final ModelMapper modelMapper;
    int colSize = 20;
    int namesCount = LIST_NAMES.size();
    int surnamesCount = LIST_SURNAMES.size();
    int loremSize = LOREM_STRING.length() - colSize;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Autowired
    public TestTableServiceImpl(TestTableRepository testTableRepository, ModelMapper modelMapper) {
        this.testTableRepository = testTableRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<TestTableDto> getAll() {

//        return testTableRepository.findAll()
//                                  .stream()
//                                  .map(post -> modelMapper.map(post, TestTableDto.class))
//                                  .collect(Collectors.toList());

        List<TestTable> listOfTestTable = testTableRepository.findAll();
        List<TestTableDto> listOfTestTableDto = new ArrayList<>();

        for (TestTable t : listOfTestTable) {
            listOfTestTableDto.add(modelMapper.map(t, TestTableDto.class));
        }

        return listOfTestTableDto;
    }

    @Override
    public int insert(int number) {
        return insertTestTableList(createTestTableList(number));
    }

    public int insertTestTableList(List<TestTable> testTableList) {

        List<TestTable> listForInsert = new ArrayList<>();
        int listForInsertCount = 0;
        int insertedCount = 0;

        for (TestTable testTable : testTableList) {

            listForInsert.add(testTable);
            listForInsertCount++;

            if (listForInsertCount == batchSize) {

                testTableRepository.saveAll(listForInsert);

                listForInsert.clear();
                listForInsertCount = 0;
                insertedCount += batchSize;
            }
        }

        testTableRepository.saveAll(listForInsert);

        insertedCount += listForInsertCount;

        return insertedCount;
    }

    List<TestTable> createTestTableList(int count) {

        List<TestTable> testTableList = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            TestTable t = randomTestTableObj();
            testTableList.add(t);
        }

        return testTableList;
    }

    @Override
    @Transactional
    public void deleteAll() {
        testTableRepository.deleteAllRows();
    }

    @Override
    @Transactional
    public long deleteByName(String name) {
        return testTableRepository.deleteByNameIgnoreCase(name);
    }

    public long count() {
        return testTableRepository.count();
    }

    public TestTable randomTestTableObj() {

        Random rand = new Random();
        TestTable randTestTableObj = new TestTable();

        randTestTableObj.setName(LIST_NAMES.get(rand.nextInt(namesCount - 1)));
        randTestTableObj.setSurname(LIST_SURNAMES.get(rand.nextInt(surnamesCount - 1)));
        randTestTableObj.setCol_1(new String(LOREM_STRING.getBytes(), rand.nextInt(this.loremSize - 1), colSize));
        randTestTableObj.setCol_2(new String(LOREM_STRING.getBytes(), rand.nextInt(this.loremSize - 1), colSize));
        randTestTableObj.setCol_3(new String(LOREM_STRING.getBytes(), rand.nextInt(this.loremSize - 1), colSize));
        randTestTableObj.setCol_4(new String(LOREM_STRING.getBytes(), rand.nextInt(this.loremSize - 1), colSize));
        randTestTableObj.setCol_5(new String(LOREM_STRING.getBytes(), rand.nextInt(this.loremSize - 1), colSize));
        randTestTableObj.setCol_6(new String(LOREM_STRING.getBytes(), rand.nextInt(this.loremSize - 1), colSize));

        return randTestTableObj;
    }

}
