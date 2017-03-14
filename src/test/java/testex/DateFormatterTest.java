/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testex;

import java.text.SimpleDateFormat;
import java.util.Date;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author nikolai
 */
@RunWith(MockitoJUnitRunner.class)
public class DateFormatterTest {

    @Test
    public void testGetFormattedDate() throws Exception {
        Date date = new Date(1489406061485L);
        String timeZone = "Europe/Kiev";

        DateFormatter dateFormatter = new DateFormatter();
        String result = dateFormatter.getFormattedDate(timeZone, date);

        String expResult = "13 mar. 2017 01:54 PM";
        assertThat(expResult, is(result));

    }
    
    @Test(expected = JokeException.class)
    public void testGetFormattedDateThrowsException() throws JokeException {
        Date date = new Date(1489406061485L);
        String timeZone = "NotLegal";

        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.getFormattedDate(timeZone, date);

    }

}
