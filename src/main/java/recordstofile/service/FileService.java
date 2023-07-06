package recordstofile.service;

import java.util.List;
import java.util.Optional;
import org.springframework.core.io.Resource;
import recordstofile.dto.FileDto;

public interface FileService {

    Optional<FileDto> recordsToFile();

    Optional<FileDto> recordsToFile(String name);

    Optional<Resource> toFileAndDownload();

    Optional<Resource> toFileAndDownload(String name);

    Optional<Resource> getFileAsResource(String fileName);

    List<String> listFiles();

}
