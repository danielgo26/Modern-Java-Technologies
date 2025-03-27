package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MJTOlympicsTest {

    private static MJTOlympics mjtOlympics;
    private static Set<Competitor> registeredCompetitors;

    @BeforeAll
    static void setup() {
        registeredCompetitors = new HashSet<>();
        registeredCompetitors.add(new Athlete("1", "A", "EN"));
        registeredCompetitors.add(new Athlete("2", "B", "BG"));

        CompetitionResultFetcher mockedResultsFetcher = mock(CompetitionResultFetcher.class);

        mjtOlympics = new MJTOlympics(registeredCompetitors, mockedResultsFetcher);
    }

    @Test
    void testUpdateMedalStatisticsShouldThrowWhenGivenNullCompetition() {
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.updateMedalStatistics(null),
            "Expected IllegalArgumentException, but nothing was thrown");
    }

    @Test
    void testUpdateMedalStatisticsShouldThrowWhenGivenAnUnregisteredCompetitor() {
        Competitor unregistered = new Athlete("unexistingId", "C", "AU");
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(unregistered);

        //act + assert
        assertThrows(IllegalArgumentException.class,
            () -> mjtOlympics.updateMedalStatistics(new Competition("Test", "Test", competitors)),
            "Expected IllegalArgumentException, but nothing was thrown");
    }

    @Test
    void testUpdateMedalTableShouldAddTwoNewCompetitorsToAnEmptyMedalTable() {
        Set<Competitor> registeredCompetitors = new HashSet<>();
        Competitor comp1 = new Athlete("1", "A", "BG");
        Competitor comp2 = new Athlete("2", "B", "EN");

        registeredCompetitors.add(comp1);
        registeredCompetitors.add(comp2);

        CompetitionResultFetcher mockedResultsFetcher = mock(CompetitionResultFetcher.class);
        TreeSet<Competitor> competitorTreeSet = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return o1.getIdentifier().compareTo(o2.getIdentifier());
            }
        });
        competitorTreeSet.add(comp1);
        competitorTreeSet.add(comp2);

        Set<Competitor> competitors = new HashSet<>();
        competitors.add(comp1);
        competitors.add(comp2);
        Competition competition = new Competition("Test", "Test", competitors);

        when(mockedResultsFetcher.getResult(competition)).thenReturn(competitorTreeSet);

        mjtOlympics = new MJTOlympics(registeredCompetitors, mockedResultsFetcher);

        mjtOlympics.updateMedalStatistics(competition);

        assertEquals(comp1.getMedals().size(), 1);
        assertEquals(comp2.getMedals().size(), 1);

        assertEquals(comp1.getMedals().toArray()[0], Medal.GOLD);
        assertEquals(comp2.getMedals().toArray()[0], Medal.SILVER);

        assertEquals(mjtOlympics.getNationsMedalTable().size(), 2);
        assertTrue(mjtOlympics.getNationsMedalTable().containsKey("BG"));
        assertTrue(mjtOlympics.getNationsMedalTable().containsKey("EN"));

        assertTrue(mjtOlympics.getNationsMedalTable().get("BG").containsKey(Medal.GOLD));
        assertEquals(mjtOlympics.getNationsMedalTable().get("BG").get(Medal.GOLD), 1);
        assertTrue(mjtOlympics.getNationsMedalTable().get("EN").containsKey(Medal.SILVER));
        assertEquals(mjtOlympics.getNationsMedalTable().get("EN").get(Medal.SILVER), 1);
    }

    @Test
    void testUpdateMedalTableShouldAddOneNewCompetitorToNonEmptyMedalTable() {
        Set<Competitor> registeredCompetitors = new HashSet<>();
        Competitor comp1 = new Athlete("1", "A", "BG");
        Competitor comp2 = new Athlete("2", "B", "EN");
        Competitor comp3 = new Athlete("3", "C", "CH");
        Competitor comp4 = new Athlete("4", "D", "AU");

        registeredCompetitors.add(comp1);
        registeredCompetitors.add(comp2);
        registeredCompetitors.add(comp3);
        registeredCompetitors.add(comp4);

        CompetitionResultFetcher mockedResultsFetcher = mock(CompetitionResultFetcher.class);
        TreeSet<Competitor> competitorTreeSet = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return o1.getIdentifier().compareTo(o2.getIdentifier());
            }
        });
        competitorTreeSet.add(comp1);
        competitorTreeSet.add(comp2);

        Set<Competitor> competitors = new HashSet<>();
        competitors.add(comp1);
        competitors.add(comp2);
        Competition competition = new Competition("Test", "Test", competitors);

        when(mockedResultsFetcher.getResult(competition)).thenReturn(competitorTreeSet);

        mjtOlympics = new MJTOlympics(registeredCompetitors, mockedResultsFetcher);
        mjtOlympics.updateMedalStatistics(competition);

        competitors.add(comp3);
        competitors.add(comp4);
        competition = new Competition("Test", "Test", competitors);

        competitorTreeSet = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return o2.getIdentifier().compareTo(o1.getIdentifier());
            }
        });

        competitorTreeSet.add(comp1);
        competitorTreeSet.add(comp2);
        competitorTreeSet.add(comp3);
        competitorTreeSet.add(comp4);
        when(mockedResultsFetcher.getResult(competition)).thenReturn(competitorTreeSet);

        //mjtOlympic = new MJTOlympics(registeredCompetitors, mockedResultsFetcher);
        mjtOlympics.updateMedalStatistics(competition);

        assertEquals(comp1.getMedals().size(), 1);
        assertEquals(comp2.getMedals().size(), 2);
        assertEquals(comp3.getMedals().size(), 1);
        assertEquals(comp4.getMedals().size(), 1);

        assertEquals(comp1.getMedals().toArray()[0], Medal.GOLD);
        assertTrue(comp2.getMedals().contains(Medal.SILVER));
        assertTrue(comp2.getMedals().contains(Medal.BRONZE));
        assertEquals(comp3.getMedals().toArray()[0], Medal.SILVER);
        assertEquals(comp4.getMedals().toArray()[0], Medal.GOLD);

        assertEquals(mjtOlympics.getNationsMedalTable().size(), 4);
        assertTrue(mjtOlympics.getNationsMedalTable().containsKey("BG"));
        assertTrue(mjtOlympics.getNationsMedalTable().containsKey("EN"));
        assertTrue(mjtOlympics.getNationsMedalTable().containsKey("CH"));
        assertTrue(mjtOlympics.getNationsMedalTable().containsKey("AU"));

        assertTrue(mjtOlympics.getNationsMedalTable().get("BG").containsKey(Medal.GOLD));
        assertEquals(mjtOlympics.getNationsMedalTable().get("BG").get(Medal.GOLD), 1);
        assertTrue(mjtOlympics.getNationsMedalTable().get("EN").containsKey(Medal.SILVER));
        assertEquals(mjtOlympics.getNationsMedalTable().get("EN").get(Medal.SILVER), 1);
        assertTrue(mjtOlympics.getNationsMedalTable().get("EN").containsKey(Medal.BRONZE));
        assertEquals(mjtOlympics.getNationsMedalTable().get("EN").get(Medal.BRONZE), 1);
        assertTrue(mjtOlympics.getNationsMedalTable().get("CH").containsKey(Medal.SILVER));
        assertEquals(mjtOlympics.getNationsMedalTable().get("CH").get(Medal.SILVER), 1);
        assertTrue(mjtOlympics.getNationsMedalTable().get("AU").containsKey(Medal.GOLD));
        assertEquals(mjtOlympics.getNationsMedalTable().get("AU").get(Medal.GOLD), 1);
    }

    @Test
    void testGetRegisteredCompetitorsShouldReturnTheMJTOlympicsRegisteredCompetitors() {
        assertIterableEquals(registeredCompetitors, mjtOlympics.getRegisteredCompetitors());
    }

    @Test
    void testGetNationsRankListShouldReturnEmptyHashSetWhenNationsMedalTableIsEmpty() {
        assertTrue(new MJTOlympics(null, null).getNationsMedalTable().equals(new HashMap<>(Collections.EMPTY_MAP)));
    }

    @Test
    void testGetNationsRankListShouldReturnEmptyTreeSetWhenTheNationsRanklistIsEmpty() {
        assertIterableEquals(new MJTOlympics(null, null).getNationsRankList(), new TreeSet<>(Collections.emptySet()));
    }

    @Test
    void testGetTotalMedalsShouldReturnOneMedalForNationalityBG() {
        Set<Competitor> registeredCompetitors = new HashSet<>();
        Competitor comp1 = new Athlete("1", "A", "BG");

        registeredCompetitors.add(comp1);

        CompetitionResultFetcher mockedResultsFetcher = mock(CompetitionResultFetcher.class);
        TreeSet<Competitor> competitorTreeSet = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return o1.getIdentifier().compareTo(o2.getIdentifier());
            }
        });
        competitorTreeSet.add(comp1);

        Set<Competitor> competitors = new HashSet<>();
        competitors.add(comp1);
        Competition competition = new Competition("Test", "Test", competitors);

        when(mockedResultsFetcher.getResult(competition)).thenReturn(competitorTreeSet);

        mjtOlympics = new MJTOlympics(registeredCompetitors, mockedResultsFetcher);

        mjtOlympics.updateMedalStatistics(competition);

        assertEquals(mjtOlympics.getTotalMedals("BG"), 1);
    }

    @Test
    void testGetTotalMedalsShouldReturnTwoMedalsForNationalityBG() {
        Set<Competitor> registeredCompetitors = new HashSet<>();
        Competitor comp1 = new Athlete("1", "A", "BG");
        Competitor comp2 = new Athlete("2", "B", "BG");

        registeredCompetitors.add(comp1);
        registeredCompetitors.add(comp2);

        CompetitionResultFetcher mockedResultsFetcher = mock(CompetitionResultFetcher.class);
        TreeSet<Competitor> competitorTreeSet = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return o1.getIdentifier().compareTo(o2.getIdentifier());
            }
        });
        competitorTreeSet.add(comp1);
        competitorTreeSet.add(comp2);

        Set<Competitor> competitors = new HashSet<>();
        competitors.add(comp1);
        competitors.add(comp2);
        Competition competition = new Competition("Test", "Test", competitors);

        when(mockedResultsFetcher.getResult(competition)).thenReturn(competitorTreeSet);

        mjtOlympics = new MJTOlympics(registeredCompetitors, mockedResultsFetcher);

        mjtOlympics.updateMedalStatistics(competition);

        assertEquals(mjtOlympics.getTotalMedals("BG"), 2);
    }

    @Test
    void testGetTotalMedalsShouldThrowWhenNationalityIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals(null));
    }

    @Test
    void testGetTotalMedalsShouldThrowWhenNationalityDoesNotExistsInTheNationsMedalTable() {
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals("unexisting"));
    }
}