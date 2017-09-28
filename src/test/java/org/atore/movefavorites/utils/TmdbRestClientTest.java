package org.atore.movefavorites.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TmdbRestClientTest {

    private TmdbRestClient tmdbClient;

    @Before
    public void setUp() {
        tmdbClient = new TmdbRestClient();
        tmdbClient.setImdbItemsPerPage(20);
    }

    @Test
    public void testPageOne() {
        int page = tmdbClient.findFirstTmdbPage(1, 5);
        Assert.assertEquals(1, page);
    }

    @Test
    public void testForSmallerPage1() {
        int page = tmdbClient.findFirstTmdbPage(3, 6);
        Assert.assertEquals(1, page);
    }

    @Test
    public void testForSmallerPage2() {
        int page = tmdbClient.findFirstTmdbPage(5, 6);
        Assert.assertEquals(2, page);
    }

    @Test
    public void testForBigerPage1() {
        int page = tmdbClient.findFirstTmdbPage(1, 21);
        Assert.assertEquals(1, page);
    }

    @Test
    public void testForBigerPage2() {
        int page = tmdbClient.findFirstTmdbPage(2, 41);
        Assert.assertEquals(3, page);
    }

    @Test
    public void testOffsetForBigerTmdb() {
        int offset = tmdbClient.getPageOffset(2, 3);
        Assert.assertEquals(3, offset);
    }

    @Test
    public void testOffsetForSmallerTmdb() {
        int offset = tmdbClient.getPageOffset(2, 41);
        Assert.assertEquals(1, offset);
    }
}
