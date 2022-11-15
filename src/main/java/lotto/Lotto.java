package lotto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException();
        }
        this.isDuplicateNumberExist(numbers);
    }

    private void isDuplicateNumberExist(List<Integer> input){
        Set<Integer> set = new HashSet<>(input);
        if(input.size() != set.size())
            throw new IllegalArgumentException();
    }

    public Map<MatchInfo, Integer> calculateLottoStatistics(List<List<Integer>> userLottoList, int bonusNumber) {
        Map<MatchInfo, Integer> calculateResult = new TreeMap<>();

        for (List<Integer> current : userLottoList) {
            MatchInfo currentResult = this.calculateSingleLottoResult(current, bonusNumber);
            if (currentResult != null) {
                if (calculateResult.containsKey(currentResult)) {
                    int count = calculateResult.get(currentResult);
                    calculateResult.replace(currentResult, count + 1);
                } else {
                    calculateResult.put(currentResult, 1);
                }
            }
        }
        return calculateResult;
    }

    private MatchInfo calculateSingleLottoResult(List<Integer> input, int bonusNumber) {
        int count = 0;
        boolean bonus = false;

        for (int current : input) {
            if (numbers.contains(current)) {
                count += 1;
            }
        }

        if (count == 5 && input.contains(bonusNumber))
            bonus = true;

        return convertLottoResultToEnum(count, bonus);
    }

    private MatchInfo convertLottoResultToEnum(int count, boolean bonus) {
        if (count == 3) {
            return MatchInfo.MATCH_THREE;
        } else if (count == 4) {
            return MatchInfo.MATCH_FOUR;
        } else if (count == 5) {
            if (bonus) {
                return MatchInfo.MATCH_FIVE_WITH_BONUS;
            }
            return MatchInfo.MATCH_FIVE;
        } else if (count == 6) {
            return MatchInfo.MATCH_SIX;
        }
        return null;
    }

    public BigInteger calculateWinningAmount(Map<MatchInfo, Integer> lottoStatistics) {
        BigInteger result = new BigInteger("0");
        for (MatchInfo key : lottoStatistics.keySet()) {
            int value = lottoStatistics.get(key);
            if (key == MatchInfo.MATCH_THREE) {
                result = result.add(MatchInfo.MATCH_THREE.getWinningAmount().multiply(BigInteger.valueOf(value)));
            } else if (key == MatchInfo.MATCH_FOUR) {
                result = result.add(MatchInfo.MATCH_FOUR.getWinningAmount().multiply(BigInteger.valueOf(value)));
            } else if (key == MatchInfo.MATCH_FIVE) {
                result = result.add(MatchInfo.MATCH_FIVE.getWinningAmount().multiply(BigInteger.valueOf(value)));
            } else if (key == MatchInfo.MATCH_FIVE_WITH_BONUS) {
                result = result.add(MatchInfo.MATCH_FIVE_WITH_BONUS.getWinningAmount().multiply(BigInteger.valueOf(value)));
            } else if (key == MatchInfo.MATCH_SIX) {
                result = result.add(MatchInfo.MATCH_SIX.getWinningAmount().multiply(BigInteger.valueOf(value)));
            }
        }
        return result;
    }

    public BigDecimal calculateProfitRatio(BigInteger income, int expense){
        BigDecimal profit = BigDecimal.valueOf(expense).multiply(BigDecimal.valueOf(100)).
                divide(new BigDecimal(income),1, RoundingMode.HALF_UP);
        return profit;
    }

    public enum MatchInfo {
        MATCH_THREE(3,"5000"),
        MATCH_FOUR(4,"50000"),
        MATCH_FIVE(5,"1500000"),
        MATCH_FIVE_WITH_BONUS(5,"30000000"),
        MATCH_SIX(6,"2000000000");

        private final int matchNumber;
        private final BigInteger winningAmount;

        MatchInfo(int matchNumber, String winningAmount) {
            this.matchNumber = matchNumber;
            this.winningAmount = new BigInteger(winningAmount);
        }

        public BigInteger getWinningAmount() {
            return winningAmount;
        }

        public int getMatchNumber() {
            return matchNumber;
        }
    }
}
