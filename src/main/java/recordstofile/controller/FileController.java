package recordstofile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recordstofile.dto.FileDto;
import recordstofile.exception.ApiError;
import recordstofile.service.FileService;

@ApiResponses(
    value = {
        @ApiResponse(
            responseCode = "204", description = "No Content",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400", description = "Bad request",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class)
                )
        ),
        @ApiResponse(
            responseCode = "500", description = "Internal error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class)
                )
//        ),
//        @ApiResponse(
//            responseCode = "200", description = "Success",
//            content = @Content(
//                mediaType = "application/octet-stream",
//                schema = @Schema(type = "String", format = "binary")
//                )
        )
    }
)
@CrossOrigin(origins = "*")
@Validated
@RestController
@RequestMapping(path = "/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(description = "Get all records from table and save to a file")
    @ApiResponses(
        value = {@ApiResponse(
            responseCode = "200", description = "Success",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FileDto.class)
            )
        )}
    )
    @PostMapping(value = "/all-to-file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> toFile() {

        Optional<FileDto> fileDtoOpt = fileService.recordsToFile();

        return returnFileDto(fileDtoOpt);
    }

    @Operation(description = "Get records with {name} from table and save to a file")
    @ApiResponses(
        value = {@ApiResponse(
            responseCode = "200", description = "Success",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FileDto.class)
            )
        )}
    )
    @PostMapping(value = "/with-name-to-file/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> withNameToFile(@PathVariable("name") @Size(min = 1, max = 30) String name) {

        Optional<FileDto> fileDtoOpt = fileService.recordsToFile(name);

        return returnFileDto(fileDtoOpt);
    }

    @Operation(description = "Get all records from table, save to a file and download")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = "application/octet-stream",
                            schema = @Schema(type = "String", format = "binary")
                    )
            )}
    )
    @PostMapping(value = "/all-to-file-and-download")
    public ResponseEntity<?> toFileAndDownload() {

        Optional<Resource> resource = fileService.toFileAndDownload();

        return returnForDownload(resource);
    }

    @Operation(description = "Get records with name {name} from table, save to a file and download")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = "application/octet-stream",
                            schema = @Schema(type = "String", format = "binary")
                    )
            )}
    )
    @PostMapping(value = "/with-name-to-file-and-download/{name}")
    public ResponseEntity<?> withNameToFileAndDownload(@PathVariable("name") @Size(min = 1, max = 30) String name) {

        Optional<Resource> resource = fileService.toFileAndDownload(name);

        return returnForDownload(resource);
    }

    @Operation(description = "Download a file with {fileName} from a directory")
    @GetMapping(value = "/download-file/{fileName}")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = "application/octet-stream",
                            schema = @Schema(type = "String", format = "binary")
                    )
            )}
    )
    public ResponseEntity<?> downloadFile(@PathVariable("fileName") @Size(min = 6) String fileName) {

        Optional<Resource> resource = fileService.getFileAsResource(fileName);

        return returnForDownload(resource);
    }

    @Operation(description = "Get list of file names from a directory")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200", description = "Success",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = String.class))
                )
            )}
    )
    @GetMapping(value = "/files-in-directory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> filesInDirectory() {

        List<String> listOfFileNames = fileService.listFiles();

        if (listOfFileNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(listOfFileNames);
    }


    ResponseEntity<?> returnFileDto(Optional<FileDto> fileDtoOpt) {

        if (fileDtoOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(fileDtoOpt.get());
    }

    ResponseEntity<?> returnForDownload(Optional<Resource> resource) {

        if (resource.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.get().getFilename() + "\"";

        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(contentType))
                             .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                             .body(resource.get());
    }

}
