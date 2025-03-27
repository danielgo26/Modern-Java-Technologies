package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RuleImplTest {

    @Test
    void testRuleImplConstructorShouldThrowWhenNegativeAmountIsGiven() {
        assertThrows(IllegalArgumentException.class, () -> new LocationsRule(1, -1),
            "Expected IllegalArgumentException to be thrown!");
    }

}