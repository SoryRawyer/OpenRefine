/*******************************************************************************
 * Copyright (C) 2018, OpenRefine contributors
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package com.google.refine.browsing;

import static org.testng.Assert.assertThrows;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.refine.browsing.Engine.Mode;
import com.google.refine.util.TestUtils;

public class EngineConfigTests {

    public static String engineConfigJson = "{\n"
            + "      \"mode\": \"row-based\",\n"
            + "      \"facets\": [\n"
            + "        {\n"
            + "          \"mode\": \"text\",\n"
            + "          \"invert\": false,\n"
            + "          \"caseSensitive\": false,\n"
            + "          \"query\": \"www\",\n"
            + "          \"name\": \"reference\",\n"
            + "          \"type\": \"text\",\n"
            + "          \"columnName\": \"reference\"\n"
            + "        }\n"
            + "      ]\n"
            + "    }";

    public static String engineConfigJsonRenamed = "{\n"
            + "      \"mode\": \"row-based\",\n"
            + "      \"facets\": [\n"
            + "        {\n"
            + "          \"mode\": \"text\",\n"
            + "          \"invert\": false,\n"
            + "          \"caseSensitive\": false,\n"
            + "          \"query\": \"www\",\n"
            + "          \"name\": \"website\",\n"
            + "          \"type\": \"text\",\n"
            + "          \"columnName\": \"website\"\n"
            + "        }\n"
            + "      ]\n"
            + "    }";

    public static String engineConfigRecordModeJson = "{"
            + "    \"mode\":\"record-based\","
            + "    \"facets\":[]"
            + "}";

    public static String noFacetProvided = "{\"mode\":\"row-based\"}";

    @Test
    public void serializeEngineConfig() {
        EngineConfig ec = EngineConfig.deserialize(engineConfigJson);
        TestUtils.isSerializedTo(ec, engineConfigJson);
    }

    @Test
    public void serializeEngineConfigRecordMode() {
        EngineConfig ec = EngineConfig.deserialize(engineConfigRecordModeJson);
        TestUtils.isSerializedTo(ec, engineConfigRecordModeJson);
    }

    @Test
    public void columnDependencies() {
        EngineConfig ec = EngineConfig.reconstruct(engineConfigJson);
        Assert.assertEquals(ec.getColumnDependencies(), Optional.of(Set.of("reference")));
    }

    @Test
    public void renameColumnDependencies() {
        EngineConfig ec = EngineConfig.reconstruct(engineConfigJson);
        EngineConfig renamed = ec.renameColumnDependencies(Map.of("reference", "website"));
        TestUtils.isSerializedTo(renamed, engineConfigJsonRenamed);
    }

    @Test
    public void reconstructNullEngineConfig() {
        EngineConfig ec = EngineConfig.reconstruct(null);
        Assert.assertEquals(ec.getMode(), Mode.RowBased);
        Assert.assertTrue(ec.getFacetConfigs().isEmpty());
    }

    @Test
    public void reconstructNoFacetsProvided() {
        EngineConfig ec = EngineConfig.reconstruct(noFacetProvided);
        Assert.assertEquals(ec.getMode(), Mode.RowBased);
        Assert.assertTrue(ec.getFacetConfigs().isEmpty());
    }

    @Test
    public void deserializeNullEngineConfig() {
        assertThrows(IllegalArgumentException.class, () -> EngineConfig.deserialize(null));
    }

    @Test
    public void deserializeNoFacetsProvided() {
        EngineConfig ec = EngineConfig.deserialize(noFacetProvided);
        Assert.assertEquals(ec.getMode(), Mode.RowBased);
        Assert.assertTrue(ec.getFacetConfigs().isEmpty());
    }

    @Test
    public void defaultRowBased() {
        EngineConfig ec = EngineConfig.defaultRowBased();
        Assert.assertEquals(ec.getMode(), Mode.RowBased);
        Assert.assertTrue(ec.getFacetConfigs().isEmpty());
    }
}
