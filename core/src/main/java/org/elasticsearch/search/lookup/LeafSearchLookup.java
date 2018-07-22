/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.search.lookup;

import org.apache.lucene.index.LeafReaderContext;
import org.elasticsearch.index.mapper.MapperService;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * Per-segment version of {@link SearchLookup}.
 */
public class LeafSearchLookup {

    private final MapperService mapperService;
    final LeafReaderContext ctx;
    final LeafDocLookup docMap;
    final SourceLookup sourceLookup;
    final LeafFieldsLookup fieldsLookup;
    final LeafIndexLookup indexLookup;
    final Map<String, Object> asMap;

    public LeafSearchLookup(MapperService mapperService, LeafReaderContext ctx, LeafDocLookup docMap, SourceLookup sourceLookup,
                            LeafFieldsLookup fieldsLookup, LeafIndexLookup indexLookup, Map<String, Object> topLevelMap) {
        this.mapperService = mapperService;
        this.ctx = ctx;
        this.docMap = docMap;
        this.sourceLookup = sourceLookup;
        this.fieldsLookup = fieldsLookup;
        this.indexLookup = indexLookup;

        Map<String, Object> asMap = new HashMap<>(topLevelMap.size() + 5);
        asMap.putAll(topLevelMap);
        asMap.put("doc", docMap);
        asMap.put("_doc", docMap);
        asMap.put("_source", sourceLookup);
        asMap.put("_fields", fieldsLookup);
        asMap.put("_index", indexLookup);
        this.asMap = unmodifiableMap(asMap);
    }

    public Map<String, Object> asMap() {
        return this.asMap;
    }

    public SourceLookup source() {
        return this.sourceLookup;
    }

    public LeafIndexLookup indexLookup() {
        return this.indexLookup;
    }

    public LeafFieldsLookup fields() {
        return this.fieldsLookup;
    }

    public LeafDocLookup doc() {
        return this.docMap;
    }

    public LeafReaderContext context() {
        return this.ctx;
    }

    public void setDocument(int docId) {
        docMap.setDocument(docId);
        sourceLookup.setSegmentAndDocument(mapperService, ctx, docId);
        fieldsLookup.setDocument(docId);
        indexLookup.setDocument(docId);
    }
}
