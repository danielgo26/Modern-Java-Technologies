package bg.sofia.uni.fmi.mjt.olympics.competitor;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AthleteTest {

    private static Competitor athlete;

    @BeforeAll
    static void setup() {
         athlete = new Athlete("TestId", "Test", "Test");
    }

    @Test
    void testAddMedalShouldThrowWhenMedalIsNull() {
        assertThrows(IllegalArgumentException.class, () -> athlete.addMedal(null),
            "Expected IllegalArgumentException, but nothing was thrown");
    }

    @Test
    void testAddMedalShouldAddANewMedalInTheCollection() {
        Medal medal = Medal.GOLD;
        Collection<Medal> athleteMedalsBeforeAddition = new ArrayList<>(athlete.getMedals());
        athlete.addMedal(medal);
        Collection<Medal> athleteMedalsAfterAddition = new ArrayList<>(athlete.getMedals());

        assertEquals(athleteMedalsBeforeAddition.size() + 1, athleteMedalsAfterAddition.size(),
            "After adding a new medal, the athlete's collection should increase size with one");

        for(Medal currMedal : athleteMedalsBeforeAddition) {
            assertTrue(athleteMedalsAfterAddition.contains(currMedal),
                String.format("Athlete's collection should still contain the old medals, but does not contain medal: %s", currMedal));
            athleteMedalsAfterAddition.remove(currMedal);
        }
        assertTrue(athleteMedalsAfterAddition.contains(medal),
            "Athlete's collection should contain the new medal, but does not");

    }

    @Test
    void testGetIdentifierShouldReturnTheAthleteIdentifier() {
        String returnedId = athlete.getIdentifier();
        assertEquals(returnedId, "TestId",
            String.format("Expected getIdentifier to return the athlete's valid identifier, but returned %s instead", returnedId));
    }

    @Test
    void testGetNameShouldReturnTheAthleteName() {
        String returnedName= athlete.getName();
        assertEquals(returnedName, "Test",
            String.format("Expected getName to return the athlete's valid name, but returned %s instead", returnedName));
    }

    @Test
    void testGetNationalityShouldReturnTheAthleteNationality() {
        String returnedNationality = athlete.getNationality();
        assertEquals(returnedNationality, "Test",
            String.format("Expected getNationality to return the athlete's valid nationality, but returned %s instead", returnedNationality));
    }

    @Test
    void testGetMedalsShouldReturnUnmodifiableCollection() {
        Collection<Medal> medals = athlete.getMedals();

        assertThrows(UnsupportedOperationException.class, () -> medals.add(Medal.GOLD),
            "Expected UnsupportedOperationException, but nothing was thrown");
    }

    @Test
    void testEqualsShouldReturnTrueWhenObjectsHaveTheSameReferences() {
        assertTrue(athlete.equals(athlete),
            "Expected to return true, but returned false");
    }

    @Test
    void testEqualsShouldReturnFalseWhenTheRightHandSideObjectIsNull() {
        assertFalse(athlete.equals(null),
            "Expected to return false, but returned true");
    }

    @Test
    void testEqualsShouldReturnFalseWhenObjectsAreFromDifferentClasses() {
        assertFalse(athlete.equals(new String()),
            "Expected to return false, but returned true");
    }

    @Test
    void testEqualsShouldReturnFalseWhenObjectsHaveDifferentIdentifiers() {
        Competitor otheraAthlete = new Athlete("TestId2", "Test","Test");

        assertFalse(athlete.equals(otheraAthlete),
            "Expected to return false, but returned true");
    }

    @Test
    void testEqualsShouldReturnTrueWhenObjectsHaveTheSameIdentifier() {
        Competitor otheraAthlete = new Athlete("TestId", "Test","Test");

        assertTrue(athlete.equals(otheraAthlete),
            "Expected to return true, but returned false");
    }
}