package com.markmburu.interview.urlshortener.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConvertUtilsTest {
    @Test
    public void shouldConvertMaxLongToShortString() {
        String maxIdShortString = ConvertUtils.idToStr(Long.MAX_VALUE);
        Assert.assertNotNull(maxIdShortString);
        Assert.assertNotEquals(maxIdShortString, "");
    }

    @Test
    public void shouldThrowExceptionWhenShortStrLongerThanTenChars() {
        long id = ConvertUtils.strToId("sclqgMAPqi2Z");
    }

}