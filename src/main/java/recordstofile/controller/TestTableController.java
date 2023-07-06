package recordstofile.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recordstofile.dto.TestTableDto;
import recordstofile.service.TestTableService;

@CrossOrigin(origins = "*")
@Validated
@RestController
@RequestMapping(path = "/test-table", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestTableController {

    private final TestTableService testTableService;

    @Autowired
    public TestTableController(TestTableService testTableService) {
        this.testTableService = testTableService;
    }

    @Operation(description = "Get all records from a table")
    @GetMapping("/get-all")
    public List<TestTableDto> getAll() {
        return testTableService.getAll();
    }

    @Operation(description = "Insert {number} of randomly created records into a table")
    @PostMapping("/insert/{number}")
    public String insert(@PathVariable("number") @NotNull @Min(1) Integer number) {

        int inserted = testTableService.insert(number);

        return "Successfully inserted " + inserted + " rows!";
    }

    @Operation(description = "Delete all rows from a table")
    @DeleteMapping("/delete-all")
    public String deleteAll() {

        testTableService.deleteAll();

        return "Successfully deleted all rows!";
    }

    @Operation(description = "Delete by name")
    @DeleteMapping("/delete/{name}")
    public String deleteByName(@PathVariable("name") @NotNull @NotBlank String name) {

        long deleted = testTableService.deleteByName(name);

        return "Successfully deleted " + deleted + " rows!";
    }

    @Operation(description = "Count rows in a table")
    @GetMapping("/count")
    public long count() {
        return testTableService.count();
    }

}
