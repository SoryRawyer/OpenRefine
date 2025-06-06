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

package com.google.refine.sorting;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.refine.util.ParsingUtilities;

/**
 * Stores the configuration of a row/record sorting setup.
 * 
 * @author Antonin Delpeuch
 *
 */
public final class SortingConfig {

    protected Criterion[] _criteria;

    @JsonCreator
    public SortingConfig(
            @JsonProperty("criteria") Criterion[] criteria) {
        _criteria = criteria;
    }

    @JsonProperty("criteria")
    public Criterion[] getCriteria() {
        return _criteria;
    }

    public static SortingConfig reconstruct(String obj) throws IOException {
        return ParsingUtilities.mapper.readValue(obj, SortingConfig.class);
    }

    /**
     * Adapt the sorting configuration to column renames, without modifying this instance.
     * 
     * @param newColumnNames
     *            a map from old to new column names
     * @return an adapted sorting config
     */
    public SortingConfig renameColumns(Map<String, String> newColumnNames) {
        List<Criterion> renamedCriteria = List.<Criterion> of(_criteria).stream()
                .map(criterion -> criterion.renameColumns(newColumnNames))
                .collect(Collectors.toList());
        return new SortingConfig(renamedCriteria.toArray(new Criterion[renamedCriteria.size()]));
    }
}
