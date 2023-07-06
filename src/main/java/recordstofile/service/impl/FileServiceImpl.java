package recordstofile.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import recordstofile.dto.FileDto;
import recordstofile.query.Query;
import recordstofile.repository.jdbc.FileRepository;
import recordstofile.service.FileHandlerService;
import recordstofile.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileHandlerService fileHandlerService;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, FileHandlerService fileHandlerService) {
        this.fileRepository = fileRepository;
        this.fileHandlerService = fileHandlerService;
    }

    @Override
    public Optional<FileDto> recordsToFile() {

        Query query = new Query();
        query.buildQueryAll();

        Optional<FileDto> fileDtoOpt = fileRepository.recordsToFile(query.getSql(), query.getSqlParameterSource());

        fileDtoOpt.ifPresent(fileDto -> fileDto.setFileSize(fileHandlerService.fileSize(fileDtoOpt.get().getFileName())));

        return fileDtoOpt;
    }

    @Override
    public Optional<FileDto> recordsToFile(String name) {

        Query query = new Query();
        query.buildQueryWithName(name);

        Optional<FileDto> fileDtoOpt = fileRepository.recordsToFile(query.getSql(), query.getSqlParameterSource());

        fileDtoOpt.ifPresent(fileDto -> fileDto.setFileSize(fileHandlerService.fileSize(fileDtoOpt.get().getFileName())));

        return fileDtoOpt;
    }

    @Override
    public Optional<Resource> toFileAndDownload() {

        Optional<FileDto> fileDtoOpt = recordsToFile();

        return getFileAsResource(fileDtoOpt);
    }

    @Override
    public Optional<Resource> toFileAndDownload(String name) {

        Optional<FileDto> fileDtoOpt = recordsToFile(name);

        return getFileAsResource(fileDtoOpt);
    }

    public Optional<Resource> getFileAsResource(Optional<FileDto> fileDtoOpt) {

        if (fileDtoOpt.isPresent()) {
            return fileHandlerService.getFileAsResource(fileDtoOpt.get().getFileName());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Resource> getFileAsResource(String fileName) {
        return fileHandlerService.getFileAsResource(fileName);
    }

    @Override
    public List<String> listFiles() {
        return fileHandlerService.getFileNamesFromDirectory();
    }

}
