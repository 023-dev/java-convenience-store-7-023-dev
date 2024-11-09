package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractFileReader<T> {

    protected static final String DELIMITER = ",";

    public List<T> load (String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines
                    .skip(1)
                    .map(this::parseLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("[ERROR] %s에서 파일을 불러오는데 실패했어요.".formatted(filePath));
            return List.of();
        }
    }

    protected abstract T parseLine(String line);
}