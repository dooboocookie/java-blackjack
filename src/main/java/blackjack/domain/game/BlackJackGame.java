package blackjack.domain.game;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.card.Hand;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Participants;
import blackjack.domain.participant.Player;
import blackjack.dto.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlackJackGame {

    private Participants participants;
    private PlayerMoney playerMoney;
    private Deck deck;

    public BlackJackGame() {
    }

    public void registerPlayers(List<String> names) {
        participants = Participants.create(names);
        playerMoney = new PlayerMoney();
    }

    public void betMoney(String playerName, double inputMoney) {
        Player player = participants.findPlayerByName(playerName);
        Money money = new Money(inputMoney);
        playerMoney.addPlayerMoney(player, money);
    }

    public void setUp() {
        deck = Deck.create(Card.getAllCards());
        deck.shuffle();

        participants.drawInitialCard(deck);
    }

    public void passExtraCardToPlayer(String name) {
        Player player = participants.findPlayerByName(name);
        if (player.canReceive()) {
            Card card = deck.draw();
            player.addCard(card);
        }
    }

    public boolean passExtraCardToDealer() {
        Dealer dealer = participants.findDealer();
        boolean hasExtraCard = false;
        while (dealer.canReceive()) {
            Card card = deck.draw();
            dealer.addCard(card);
            hasExtraCard = true;
        }
        return hasExtraCard;
    }

    public List<String> findAllPlayerNames() {
        return participants.findAllPlayerNames();
    }

    public CardDto findDealerFirstCard() {
        Card card = participants.findDealer().getFirst();
        return new CardDto(card.getSuit(), card.getDenomination());
    }

    public boolean canPassCardToPlayer(String playerName) {
        Player player = participants.findPlayerByName(playerName);
        return player.canReceive();
    }

    public boolean canPassCardToDealer() {
        Dealer dealer = participants.findDealer();
        return dealer.canReceive();
    }

    public PlayerNameHandDto findPlayerNameHand(String playerName) {
        Player player = participants.findPlayerByName(playerName);
        return convertNameHand(player);
    }

    public List<PlayerNameHandDto> findAllPlayerNameHand() {
        List<String> allPlayerNames = participants.findAllPlayerNames();
        return allPlayerNames.stream()
                .map(playerName -> convertNameHand(participants.findPlayerByName(playerName)))
                .collect(Collectors.toList());
    }

    public DealerHandScoreDto findDealerHandScore() {
        Dealer dealer = participants.findDealer();
        return new DealerHandScoreDto(
                convertCardDTO(dealer.getHand()),
                dealer.calculateScore().getValue()
        );
    }

    public List<PlayerNameHandScoreDto> findAllPlayerNameHandScore() {
        List<String> allPlayerNames = participants.findAllPlayerNames();
        return allPlayerNames.stream()
                .map(playerName -> convertNameHandScore(participants.findPlayerByName(playerName)))
                .collect(Collectors.toList());
    }

    public ResultDto findDealerPlayerResult() {
        Map<String, Double> allPlayerResult = calculatePlayerResult();
        double dealerResult = calculateDealerResult(allPlayerResult);

        return new ResultDto(dealerResult, allPlayerResult);
    }

    private double calculateDealerResult(Map<String, Double> allPlayerResult) {
        double result = 0;
        for (String name : allPlayerResult.keySet()) {
            result -= allPlayerResult.get(name);
        }
        return result;
    }

    private PlayerNameHandDto convertNameHand(Player player) {
        return new PlayerNameHandDto(
                player.getName(),
                convertCardDTO(player.getHand())
        );
    }

    private PlayerNameHandScoreDto convertNameHandScore(Player player) {
        return new PlayerNameHandScoreDto(
                player.getName(),
                convertCardDTO(player.getHand()),
                player.calculateScore().getValue()
        );
    }

    private List<CardDto> convertCardDTO(Hand hand) {
        return hand.getHand().stream()
                .map(card -> new CardDto(card.getSuit(), card.getDenomination()))
                .collect(Collectors.toList());
    }

    private Map<String, Double> calculatePlayerResult() {
        Map<String, Double> allPlayerResult = new LinkedHashMap<>();
        Dealer dealer = participants.findDealer();
        Map<Player, Money> playerMoney = this.playerMoney.calculateYieldAllPlayer(dealer.getHand());
        for (Player player : playerMoney.keySet()) {
            String playerName = player.getName();
            Double money = playerMoney.get(player).getValue();
            allPlayerResult.put(playerName, money);
        }
        return allPlayerResult;
    }
}
