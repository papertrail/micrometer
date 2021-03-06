/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.appoptics;

import io.micrometer.core.instrument.config.MissingRequiredConfigurationException;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import io.micrometer.core.lang.Nullable;

/**
 * Configuration for {@link AppOpticsMeterRegistry}.
 *
 * @author Hunter Sherman
 */
public interface AppOpticsConfig extends StepRegistryConfig {

    //https://docs.appoptics.com/api/#create-a-measurement
    int MAX_BATCH_SIZE = 1000;
    int DEFAULT_BATCH_SIZE = 500;

    @Override
    default String prefix() {
        return "appoptics";
    }

    /**
     * @return AppOptics API token
     */
    default String token() {
        final String t = get(prefix() + ".token");
        if (null == t)
            throw new MissingRequiredConfigurationException("apiKey must be set to report metrics to AppOptics");
        return t;
    }

    /**
     * @return source identifier (usually hostname)
     */
    @Nullable
    default String source() {
        final String s = get(prefix() + ".source");
        return null == s ? "instance" : s;
    }

    /**
     * @return the URI to ship metrics to
     */
    default String uri() {

        final String uri = get(prefix() + ".uri");
        return null == uri ? "https://api.appoptics.com/v1/measurements" : uri;
    }

    /**
     * @return optional (string), prepended to metric names
     */
    default String metricPrefix() {

        final String mp = get(prefix() + ".metricPrefix");
        return null == mp ? "" : mp;
    }

    @Override
    default int batchSize() {
        final String v = get(prefix() + ".batchSize");
        return null == v ? DEFAULT_BATCH_SIZE : Math.min(Integer.parseInt(v), MAX_BATCH_SIZE);
    }
}
