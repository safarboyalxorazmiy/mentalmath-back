package uz.mentalmath.service;

import org.springframework.stereotype.Service;
import uz.mentalmath.entity.Session.SessionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProblemGenerator {

    private final Random random = new Random();

    public record GeneratedProblem(
            String expression,
            List<String> operations,
            int answer
    ) {}

    public GeneratedProblem generate(int level, SessionType sessionType) {
        int operandCount = getOperandCount(level);
        int maxNum = getMaxNumber(level);

        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < operandCount; i++) {
            numbers.add(1 + random.nextInt(maxNum));
        }

        List<String> operators = new ArrayList<>();
        for (int i = 0; i < operandCount - 1; i++) {
            operators.add(random.nextBoolean() ? "+" : "-");
        }

        StringBuilder expression = new StringBuilder();
        List<String> operations = new ArrayList<>();
        int result = numbers.get(0);

        expression.append(numbers.get(0));
        operations.add("+" + numbers.get(0));

        for (int i = 0; i < operators.size(); i++) {
            String op = operators.get(i);
            int num = numbers.get(i + 1);

            expression.append(" ").append(op).append(" ").append(num);
            operations.add(op + num);

            if (op.equals("+")) {
                result += num;
            } else {
                result -= num;
            }
        }

        return new GeneratedProblem(expression.toString(), operations, result);
    }

    private int getOperandCount(int level) {
        if (level <= 2) return 3 + random.nextInt(2);
        if (level <= 4) return 4 + random.nextInt(2);
        if (level <= 6) return 5 + random.nextInt(2);
        return 6 + random.nextInt(2);
    }

    private int getMaxNumber(int level) {
        if (level <= 2) return 9;
        if (level <= 4) return 15;
        if (level <= 6) return 20;
        if (level <= 8) return 30;
        return 50;
    }
}