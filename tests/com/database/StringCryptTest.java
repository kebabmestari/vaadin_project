package com.database;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for encryption
 * Created by samlinz on 17.10.2016.
 */
public class StringCryptTest {

    @Test
    public void encrypt() throws Exception {
        System.out.println("Testing SHA256 encryption");
        assertEquals("D5579C46DFCC7F18207013E65B44E4CB4E2C2298F4AC457BA8F82743F31E930B",
                StringCrypt.encrypt("test string"));
    }

}