/*
 * Copyright (C) 2011 10gen Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// UUIDRepresentation.java

package org.bson;

import org.bson.io.Bits;

import java.util.UUID;

/**
 *
 */
public enum UUIDRepresentation {
    /**
     * Use the new standard representation for Guids (binary subtype 4 with bytes in network byte order).
     */
    STANDARD {
        @Override
        public byte[] toBytes(UUID uuid) {
            byte[] bytes = new byte[16];

            writeLongToArrayBigEndian(bytes, 0, uuid.getMostSignificantBits());
            writeLongToArrayBigEndian(bytes, 8, uuid.getLeastSignificantBits());

            return bytes;
        }

        @Override
        public UUID fromBytes(byte[] bytes) {
            return new UUID(readLongFromArrayBigEndian(bytes, 0), readLongFromArrayBigEndian(bytes, 8));

        }

        @Override
        public int getSubType() {
            return BSON.B_UUID_STANDARD;
        }
    },
    /**
     * Use the representation used by older versions of the Java driver.
     */
    JAVA_LEGACY {
        @Override
        public byte[] toBytes(UUID uuid) {
            byte[] bytes = new byte[16];

            writeLongToArrayLittleEndian(bytes, 0, uuid.getMostSignificantBits());
            writeLongToArrayLittleEndian(bytes, 8, uuid.getLeastSignificantBits());

            return bytes;
        }

        @Override
        public UUID fromBytes(byte[] bytes) {
            return new UUID(readLongFromArrayLittleEndian(bytes, 0), readLongFromArrayLittleEndian(bytes, 8));
        }

        @Override
        public int getSubType() {
            return BSON.B_UUID_LEGACY;
        }
    },
    /**
     * Use the representation used by older versions of the Python driver.
     */
    PYTHON_LEGACY {
        @Override
        public byte[] toBytes(UUID uuid) {
            return STANDARD.toBytes(uuid);
        }

        @Override
        public UUID fromBytes(byte[] bytes) {
            return STANDARD.fromBytes(bytes);
        }

        @Override
        public int getSubType() {
            return BSON.B_UUID_LEGACY;
        }
    },
    /**
     * Use the representation used by older versions of the C# driver (including most community provided C# drivers).
     */
    C_SHARP_LEGACY {
        @Override
        public byte[] toBytes(UUID uuid) {
            byte[] bytes = new byte[16];

            writeLongToArrayCSharpLegacy( bytes, 0, uuid.getMostSignificantBits() );
            writeLongToArrayBigEndian(bytes, 8, uuid.getLeastSignificantBits());

            return bytes;
        }

        @Override
        public UUID fromBytes(byte[] bytes) {
            return new UUID(readLongFromArrayCSharpLegacy( bytes, 0 ), readLongFromArrayBigEndian(bytes, 8));
        }

        @Override
        public int getSubType() {
            return BSON.B_UUID_LEGACY;
        }

        private void writeLongToArrayCSharpLegacy(byte[] bytes, int offset, long x) {
            bytes[offset + 0] = (byte)(0xFFL & ( x >> 32 ) );
            bytes[offset + 1] = (byte)(0xFFL & ( x >> 40 ) );
            bytes[offset + 2] = (byte)(0xFFL & ( x >> 48 ) );
            bytes[offset + 3] = (byte)(0xFFL & ( x >> 56 ) );
            bytes[offset + 4] = (byte)(0xFFL & ( x >> 16 ) );
            bytes[offset + 5] = (byte)(0xFFL & ( x >> 24 ) );
            bytes[offset + 6] = (byte)(0xFFL & ( x >> 0 ) );
            bytes[offset + 7] = (byte)(0xFFL & ( x >> 8 ) );
        }

        private long readLongFromArrayCSharpLegacy(byte[] bytes, int offset) {
            long x = 0;
            x |= ( 0xFFL & bytes[offset+0] ) << 32;
            x |= ( 0xFFL & bytes[offset+1] ) << 40;
            x |= ( 0xFFL & bytes[offset+2] ) << 48;
            x |= ( 0xFFL & bytes[offset+3] ) << 56;
            x |= ( 0xFFL & bytes[offset+4] ) << 16;
            x |= ( 0xFFL & bytes[offset+5] ) << 24;
            x |= ( 0xFFL & bytes[offset+6] ) << 0;
            x |= ( 0xFFL & bytes[offset+7] ) << 8;
            return x;
        }

    };

    private static void writeLongToArrayLittleEndian(byte[] bytes, int offset, long x) {
        bytes[offset + 0] = (byte)(0xFFL & ( x >> 0 ) );
        bytes[offset + 1] = (byte)(0xFFL & ( x >> 8 ) );
        bytes[offset + 2] = (byte)(0xFFL & ( x >> 16 ) );
        bytes[offset + 3] = (byte)(0xFFL & ( x >> 24 ) );
        bytes[offset + 4] = (byte)(0xFFL & ( x >> 32 ) );
        bytes[offset + 5] = (byte)(0xFFL & ( x >> 40 ) );
        bytes[offset + 6] = (byte)(0xFFL & ( x >> 48 ) );
        bytes[offset + 7] = (byte)(0xFFL & ( x >> 56 ) );
    }

    private static void writeLongToArrayBigEndian(byte[] bytes, int offset, long x) {
        bytes[offset + 0] = (byte)(0xFFL & ( x >> 56 ) );
        bytes[offset + 1] = (byte)(0xFFL & ( x >> 48 ) );
        bytes[offset + 2] = (byte)(0xFFL & ( x >> 40 ) );
        bytes[offset + 3] = (byte)(0xFFL & ( x >> 32 ) );
        bytes[offset + 4] = (byte)(0xFFL & ( x >> 24 ) );
        bytes[offset + 5] = (byte)(0xFFL & ( x >> 16 ) );
        bytes[offset + 6] = (byte)(0xFFL & ( x >> 8 ) );
        bytes[offset + 7] = (byte)(0xFFL & ( x >> 0 ) );
    }

    private static long readLongFromArrayLittleEndian(byte[] bytes, int offset) {
        return Bits.readLong(bytes, offset);
    }

    private static long readLongFromArrayBigEndian(byte[] bytes, int offset) {
        long x = 0;
        x |= ( 0xFFL & bytes[offset+0] ) << 56;
        x |= ( 0xFFL & bytes[offset+1] ) << 48;
        x |= ( 0xFFL & bytes[offset+2] ) << 40;
        x |= ( 0xFFL & bytes[offset+3] ) << 32;
        x |= ( 0xFFL & bytes[offset+4] ) << 24;
        x |= ( 0xFFL & bytes[offset+5] ) << 16;
        x |= ( 0xFFL & bytes[offset+6] ) << 8;
        x |= ( 0xFFL & bytes[offset+7] ) << 0;
        return x;
    }

    public abstract byte[] toBytes(UUID uuid);
    public abstract UUID fromBytes(byte[] bytes);
    public abstract int getSubType();

    private static UUIDRepresentation _defaultRepresentation = UUIDRepresentation.JAVA_LEGACY;

    public static synchronized void setDefault( UUIDRepresentation defaultRepresentation ) {
        _defaultRepresentation = defaultRepresentation;
    }
    public static synchronized UUIDRepresentation getDefault() {
        return _defaultRepresentation;
    }
}
