package recordstofile.repository.jdbc;

import java.util.Optional;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import recordstofile.dto.FileDto;

public interface FileRepository {

    Optional<FileDto> recordsToFile(String sql, SqlParameterSource parameters);

}
