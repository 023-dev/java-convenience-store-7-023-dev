package store.util.reader;

import java.time.LocalDate;
import store.model.dto.PromotionDto;

public class PromotionFileReader extends AbstractFileReader<PromotionDto> {

    private static final int NAME_INDEX = 0;
    private static final int BUY_INDEX = 1;
    private static final int GET_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    @Override
    protected PromotionDto parseLine(String line) {
        String[] fields = line.split(DELIMITER);

        String name = fields[NAME_INDEX];
        int buyQuantity = Integer.parseInt(fields[BUY_INDEX]);
        int getQuantity = Integer.parseInt(fields[GET_INDEX]);
        LocalDate startDate = LocalDate.parse(fields[START_DATE_INDEX]);
        LocalDate endDate = LocalDate.parse(fields[END_DATE_INDEX]);

        return new PromotionDto(name, buyQuantity, getQuantity, startDate, endDate);
    }
}
