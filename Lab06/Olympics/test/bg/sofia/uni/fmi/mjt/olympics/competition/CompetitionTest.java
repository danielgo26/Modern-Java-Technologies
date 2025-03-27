package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompetitionTest {

    @Test
    void testCompetitionConstructorShouldThrowWhenGivenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Competition(null, "Test", new HashSet<>()),
            "Expected IllegalArgumentException but nothing was thrown");
    }

    @Test
    void testCompetitionConstructorShouldThrowWhenGivenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Competition(" ", "Test", new HashSet<>()),
            "Expected IllegalArgumentException but nothing was thrown");
    }

    @Test
    void testCompetitionConstructorShouldThrowWhenGivenDisciplineIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("Test", null, new HashSet<>()),
            "Expected IllegalArgumentException but nothing was thrown");
    }

    @Test
    void testCompetitionConstructorShouldThrowWhenGivenDisciplineIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("Test", " ", new HashSet<>()),
            "Expected IllegalArgumentException but nothing was thrown");
    }

    @Test
    void testCompetitionConstructorShouldThrowWhenGivenCompetitorsCollectionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("Test", "Test", null),
            "Expected IllegalArgumentException but nothing was thrown");
    }

    @Test
    void testCompetitionConstructorShouldThrowWhenGivenCompetitorsCollectionIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("Test", "Test", new HashSet<>()),
            "Expected IllegalArgumentException but nothing was thrown");
    }


    @Test
    void testGetCompetitorsShouldReturnUnmodifiableCollection() {
        Competitor athlete = new Athlete("Test", "Test","Test");
        Competition comp = new Competition("Test", "Test", new HashSet<>(List.of(athlete)));

        assertThrows(UnsupportedOperationException.class, () -> comp.competitors().add(athlete),
            "Expected UnsupportedOperationException to be throw, but it was not");
    }

    @Test
    void testEqualsShouldReturnTrueWhenObjectsHaveTheSameReferences() {
        Competitor athlete = new Athlete("Test", "Test","Test");
        Competition comp = new Competition("Test", "Test", new HashSet<>(List.of(athlete)));

        assertTrue(comp.equals(comp),
            "Expected to return true, but returned false");
    }

    @Test
    void testEqualsShouldReturnFalseWhenTheRightHandSideObjectIsNull() {
        Competitor athlete = new Athlete("Test", "Test","Test");
        Competition comp = new Competition("Test", "Test", new HashSet<>(List.of(athlete)));

        assertFalse(comp.equals(null),
            "Expected to return false, but returned true");
    }

    @Test
    void testEqualsShouldReturnFalseWhenObjectsAreFromDifferentClasses() {
        Competitor athlete = new Athlete("Test", "Test","Test");
        Competition comp = new Competition("Test", "Test", new HashSet<>(List.of(athlete)));

        assertFalse(comp.equals(new String()),
            "Expected to return false, but returned true");
    }

    @Test
    void testEqualsShouldReturnFalseWhenObjectsHaveDifferentNames() {
        Competitor athlete = new Athlete("Test", "Test","Test");
        Competition compA = new Competition("TestA", "Test", new HashSet<>(List.of(athlete)));
        Competition compB = new Competition("TestB", "Test", new HashSet<>(List.of(athlete)));

        assertFalse(compA.equals(compB),
            "Expected to return false, but returned true");
    }

    @Test
    void testEqualsShouldReturnFalseWhenObjectsHaveDifferentDisciplines() {
        Competitor athlete = new Athlete("Test", "Test","Test");
        Competition compA = new Competition("Test", "TestA", new HashSet<>(List.of(athlete)));
        Competition compB = new Competition("Test", "TestB", new HashSet<>(List.of(athlete)));

        assertFalse(compA.equals(compB),
            "Expected to return false, but returned true");
    }

    @Test
    void testEqualsShouldReturnTrueWhenObjectsHaveTheSameNamesAndDisciplines() {
        Competitor athlete = new Athlete("Test", "Test","Test");
        Competition compA = new Competition("Test", "Disc", new HashSet<>(List.of(athlete)));
        Competition compB = new Competition("Test", "Disc", new HashSet<>(List.of(athlete)));

        assertTrue(compA.equals(compB),
            "Expected to return true, but returned false");
    }

}