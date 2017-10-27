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

package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.ja.JapaneseTokenizer;
import org.apache.lucene.analysis.ja.SprJapaneseAnalyzer;
import org.apache.lucene.analysis.ja.dict.UserDictionary;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

import java.util.function.Supplier;

/**
 */
public class KuromojiAnalyzerProvider extends AbstractIndexAnalyzerProvider<SprJapaneseAnalyzer> {

    private final SprJapaneseAnalyzer analyzer;

    public KuromojiAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings, Supplier<KuromojiUserDictionarySyncService> dictionarySyncService) {
        super(indexSettings, name, settings);
        final JapaneseTokenizer.Mode mode = KuromojiTokenizerFactory.getMode(settings);
        final UserDictionary userDictionary = KuromojiTokenizerFactory.getUserDictionary(env, settings);
        analyzer = new SprJapaneseAnalyzer(userDictionary, mode, CharArraySet.EMPTY_SET, dictionarySyncService);
    }

    @Override
    public SprJapaneseAnalyzer get() {
        return this.analyzer;
    }


}
