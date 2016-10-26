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

package org.elasticsearch.action.admin.cluster.node.hotthreads;

import org.apache.http.HttpEntity;
import org.elasticsearch.action.support.nodes.NodesOperationRequest;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.UriBuilder;
import org.elasticsearch.rest.RestRequest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 */
public class NodesHotThreadsRequest extends NodesOperationRequest<NodesHotThreadsRequest> {

    int threads = 3;
    String type = "cpu";
    TimeValue interval = new TimeValue(500, TimeUnit.MILLISECONDS);
    int snapshots = 10;

    /**
     * Get hot threads from nodes based on the nodes ids specified. If none are passed, hot
     * threads for all nodes is used.
     */
    public NodesHotThreadsRequest(String... nodesIds) {
        super(nodesIds);
    }

    public int threads() {
        return this.threads;
    }

    public NodesHotThreadsRequest threads(int threads) {
        this.threads = threads;
        return this;
    }

    public NodesHotThreadsRequest type(String type) {
        this.type = type;
        return this;
    }

    public String type() {
        return this.type;
    }

    public NodesHotThreadsRequest interval(TimeValue interval) {
        this.interval = interval;
        return this;
    }

    public TimeValue interval() {
        return this.interval;
    }

    public int snapshots() {
        return this.snapshots;
    }

    public NodesHotThreadsRequest snapshots(int snapshots) {
        this.snapshots = snapshots;
        return this;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        threads = in.readInt();
        type = in.readString();
        interval = TimeValue.readTimeValue(in);
        snapshots = in.readInt();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeInt(threads);
        out.writeString(type);
        interval.writeTo(out);
        out.writeInt(snapshots);
    }

    @Override
    public RestRequest.Method getMethod() {
        return RestRequest.Method.GET;
    }

    @Override
    public String getEndPoint() {
        return UriBuilder.newBuilder()
                .slash("_cluster", "nodes")
                .csv(nodesIds())
                .slash("hot_threads").build();
    }

    @Override
    public Map<String, String> getParams() {
        return new MapBuilder<String, String>()
                .putIfNotNull("type", type)
                .put("threads", String.valueOf(threads))
                .put("interval", interval.toString())
                .put("snapshots", String.valueOf(snapshots)).map();
    }
}
