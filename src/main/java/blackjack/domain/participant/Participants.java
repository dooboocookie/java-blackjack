package blackjack.domain.participant;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Participants {

    private static final int INIT_CARD_COUNT = 2;
    private static final int MIN_COUNT = 2;
    private static final int MAX_COUNT = 8;
    private static final int DEALER_INDEX = 0;

    private final List<Participant> participants;

    private Participants(List<Participant> participants) {
        validateCount(participants);
        validateDuplicate(participants);
        this.participants = participants;
    }

    private void validateDuplicate(List<Participant> participants) {
        long distinctCount = participants.stream()
                .distinct()
                .count();

        if (distinctCount != participants.size()) {
            throw new IllegalArgumentException("참가자는 중복을 허용하지 않습니다.");
        }
    }

    private void validateCount(List<Participant> participants) {
        int playersCount = participants.size();
        if (playersCount < MIN_COUNT || playersCount > MAX_COUNT) {
            throw new IllegalArgumentException("참가자 수는 " + MIN_COUNT + "명 이상 " + MAX_COUNT + "명 이하 입니다.");
        }
    }

    public static Participants create(List<String> names) {
        List<Player> players = names.stream()
                .map(Name::new)
                .map(Player::new)
                .collect(Collectors.toList());
        List<Participant> participants = new ArrayList<>();
        participants.add(new Dealer());
        participants.addAll(players);
        return new Participants(participants);
    }

    public void drawInitialCard(Deck deck) {
        List<Player> players = getPlayers();
        for (Player player : players) {
            drawInitialCard(player, deck);
        }
        drawInitialCard(getDealer(), deck);
    }

    private Dealer getDealer() {
        return (Dealer) participants.get(DEALER_INDEX);
    }

    private List<Player> getPlayers() {
        return IntStream
                .range(1, participants.size())
                .mapToObj(index -> (Player) participants.get(index))
                .collect(Collectors.toList());
    }

    private void drawInitialCard(Participant participant, Deck deck) {
        for (int cardCount = 0; cardCount < INIT_CARD_COUNT; cardCount++) {
            passCard(participant, deck);
        }
    }

    private void passCard(Participant participant, Deck deck) {
        if (participant.canReceive()) {
            Card card = deck.draw();
            participant.addCard(card);
        }
    }

    public List<String> findAllPlayerNames() {
        return getPlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public Player findPlayerByName(String name) {
        return getPlayers().stream()
                .filter(player -> player.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 플레이어 명입니다."));
    }

    public Dealer findDealer() {
        return getDealer();
    }
}
