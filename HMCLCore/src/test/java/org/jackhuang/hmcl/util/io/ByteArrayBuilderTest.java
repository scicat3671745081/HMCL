/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2024 huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.jackhuang.hmcl.util.io;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Glavo
 */
public final class ByteArrayBuilderTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 8, 15, 16, 17, 32})
    public void testCopyFrom(int size) throws IOException {
        byte[] data = new byte[size];
        new Random(0).nextBytes(data);

        ByteArrayBuilder builder = new ByteArrayBuilder(16);
        builder.copyFrom(new ByteArrayInputStream(data));
        assertArrayEquals(data, builder.toByteArray());

        builder = new ByteArrayBuilder(16);
        builder.copyFrom(new ByteArrayInputStream(data) {
            @Override
            public int available() {
                return 0;
            }
        });
        assertArrayEquals(data, builder.toByteArray());

        builder = new ByteArrayBuilder(16);
        builder.copyFrom(new ByteArrayInputStream(data) {
            @Override
            public int available() {
                return data.length - 1;
            }
        });
        assertArrayEquals(data, builder.toByteArray());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8, 15, 16, 17, 32})
    public void testReadFromInputStream(int size) throws IOException {
        byte[] data = new byte[size];
        new Random(0).nextBytes(data);

        {
            InputStream inputStream = new ByteArrayInputStream(data);
            ByteArrayBuilder builder = ByteArrayBuilder.createFor(inputStream);
            builder.read(inputStream);
            assertArrayEquals(data, builder.getArray());
        }

        {
            InputStream inputStream = new ByteArrayInputStream(data) {
                @Override
                public int available() {
                    return 0;
                }
            };
            ByteArrayBuilder builder = new ByteArrayBuilder(10);
            //noinspection StatementWithEmptyBody
            while (builder.read(inputStream) > 0) {

            }
            assertArrayEquals(data, builder.toByteArray());
        }

        {
            InputStream inputStream = new ByteArrayInputStream(data) {
                @Override
                public int available() {
                    return 0;
                }
            };
            ByteArrayBuilder builder = new ByteArrayBuilder(10, true);
            //noinspection StatementWithEmptyBody
            while (builder.read(inputStream) > 0) {

            }
            assertArrayEquals(data, builder.toByteArray());
        }
    }
}
