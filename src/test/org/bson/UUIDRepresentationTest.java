// UUIDRepresentationTest.java

/**
 *      Copyright (C) 2008 10gen Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.bson;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.bson.UUIDRepresentation.*;

public class UUIDRepresentationTest extends Assert {
    @Test
    public void testJavaLegacyEncoding() {
        UUID uuid = UUID.fromString("01020304-0506-0708-090a-0b0c0d0e0f10");
        byte[] bytes = JAVA_LEGACY.toBytes(uuid);
        byte[] expected = new byte[] { 8, 7, 6, 5, 4, 3, 2, 1, 16, 15, 14, 13, 12, 11, 10, 9 };
        Assert.assertEquals(bytes, expected);
    }

    @Test
    public void testJavaLegacyDecoding() {
        byte[] bytes = new byte[] { 8, 7, 6, 5, 4, 3, 2, 1, 16, 15, 14, 13, 12, 11, 10, 9 };
        UUID uuid = JAVA_LEGACY.fromBytes(bytes);
        UUID expected = UUID.fromString("01020304-0506-0708-090a-0b0c0d0e0f10");
        Assert.assertEquals(uuid, expected);
    }

    @Test
    public void testStandardEncoding() {
        UUID uuid = UUID.fromString("01020304-0506-0708-090a-0b0c0d0e0f10");
        byte[] bytes = STANDARD.toBytes(uuid);
        byte[] expected = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
        Assert.assertEquals(bytes, expected);
    }

    @Test
    public void testStandardDecoding() {
        byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
        UUID uuid = STANDARD.fromBytes(bytes);
        UUID expected = UUID.fromString("01020304-0506-0708-090a-0b0c0d0e0f10");
        Assert.assertEquals(uuid, expected);
    }

}
