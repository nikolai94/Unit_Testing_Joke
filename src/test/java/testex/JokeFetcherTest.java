/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testex;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.Every;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Matchers;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.runners.MockitoJUnitRunner;
import testex.jokefetching.IFetcherFactory;
import testex.jokefetching.IJokeFetcher;

/**
 *
 * @author nikolai
 */
@RunWith(MockitoJUnitRunner.class)
public class JokeFetcherTest {

    public JokeFetcherTest() {
    }

    @Mock
    private IDateFormatter iDateFormatter;
    @Mock
    private IFetcherFactory factory;
    @Mock
    private IJokeFetcher chuckNorris;
    @Mock
    private IJokeFetcher eduJoke;
    @Mock
    private IJokeFetcher moma;
    @Mock
    private IJokeFetcher tambal;

    private JokeFetcher jokeFetcher;

    @Before
    public void setUp() {

        List<IJokeFetcher> fetcher = Arrays.asList(chuckNorris, eduJoke, moma, tambal);
        when(factory.getJokeFetchers("eduprog,chucknorris,chucknorris,moma,tambal")).thenReturn(fetcher);

        List<String> types = Arrays.asList("EduJoke", "ChuckNorris", "Moma", "Tambal");
        when(factory.getAvailableTypes()).thenReturn(types);

        Joke joke = new Joke("testJoke", "testReference");
        given(chuckNorris.getJoke()).willReturn(joke);
        given(eduJoke.getJoke()).willReturn(joke);
        given(moma.getJoke()).willReturn(joke);
        given(tambal.getJoke()).willReturn(joke);

        jokeFetcher = new JokeFetcher(iDateFormatter, factory);
    }

    @Test
    public void getJokeFetchers() {
        List<IJokeFetcher> fetcher = factory.getJokeFetchers("eduprog,chucknorris,chucknorris,moma,tambal");
        assertThat(fetcher, contains(chuckNorris, eduJoke, moma, tambal));
    }

    @Test
    public void testGetAvailableTypes() {
    List<String> availableTypes = jokeFetcher.getAvailableTypes();
        assertThat(availableTypes, hasSize(4));
        assertThat(availableTypes, contains("eduprog", "chucknorris", "moma", "tambal"));
    }


    @Test
    public void testIsStringValid() {
        assertThat(jokeFetcher.isStringValid("eduprog,chucknorris"), is(true));
        assertThat(jokeFetcher.isStringValid("Nix"), is(false));
    }
    
    @Test(expected = JokeException.class)
    public void testGetJokesJokeException() throws JokeException{
        jokeFetcher.getJokes("NotLegalJoke", "test");
    }

    @Test
    public void testGetJokes() throws JokeException {
        given(iDateFormatter.getFormattedDate(eq("Europe/Copenhagen"), anyObject())).willReturn("17 feb. 2017 10:56 AM");

        assertThat(jokeFetcher.getJokes("eduprog", "Europe/Copenhagen").getTimeZoneString(), is("17 feb. 2017 10:56 AM"));

        verify(iDateFormatter, times(1)).getFormattedDate(eq("Europe/Copenhagen"), anyObject());
        

        List<Joke> jokes = jokeFetcher.getJokes("eduprog,chucknorris,chucknorris,moma,tambal", "Europe/Copenhagen").getJokes();
        assertThat(jokes, hasSize(4));
        assertThat(jokes, everyItem(hasProperty("joke", is("testJoke"))));
        assertThat(jokes, everyItem(hasProperty("reference", is("testReference"))));
   
        verify(factory, times(1)).getJokeFetchers("eduprog,chucknorris,chucknorris,moma,tambal");
        
        verify(chuckNorris, times(1)).getJoke();
        verify(eduJoke, times(1)).getJoke();
        verify(moma, times(1)).getJoke();
        verify(tambal, times(1)).getJoke();
    }
    
   

}
