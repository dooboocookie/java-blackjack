package blackJack.view;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static blackJack.utils.ExeptionMessage.*;

public class InputView {

    private static final String ONE_MORE_CARD_MESSAGE = "%s는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)\n";
    private static final String DELIMITER = ",";
    private static final String INPUT_PLAYER_NAMES_MESSAGE = "게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)";
    private static final String ONE_MORE_CARD = "y";
    private static final String STOP_CARD = "n";

    private static String input() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        validateNull(input);
        validateEmpty(input);
        return input;
    }

    public static List<String> inputPlayerNames() {
        System.out.println(INPUT_PLAYER_NAMES_MESSAGE);
        String[] input = input().split(DELIMITER);
        return Arrays.stream(input)
                .collect(Collectors.toList());
    }

    public static boolean askOneMoreCard(String playerName) {
        System.out.printf(ONE_MORE_CARD_MESSAGE, playerName);
        String input = input();
        validateYesOrNo(input);
        return ONE_MORE_CARD.equals(input);
    }

    private static void validateYesOrNo(String input) {
        if (!input.equals(ONE_MORE_CARD) && !input.equals(STOP_CARD)) {
            throw new IllegalArgumentException(INPUT_IS_WRONG_FORMAT);
        }
    }

    private static void validateNull(String input) {
        if (input == null) {
            throw new IllegalArgumentException(INPUT_IS_NULL);
        }
    }

    private static void validateEmpty(String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException(INPUT_IS_BLANK);
        }
    }

}
