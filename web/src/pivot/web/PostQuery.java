/*
 * Copyright (c) 2008 VMware, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pivot.web;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Executes an HTTP POST operation.
 *
 * @author gbrown
 */
public class PostQuery extends Query<URL> {
    private Object value = null;

    public PostQuery(String hostname, String path) {
        this(hostname, DEFAULT_PORT, path, false);
    }

    public PostQuery(String hostname, int port, String path, boolean secure) {
        super(hostname, port, path, secure);
    }

    /**
     * Returns the value that will be POSTed to the server when the query is
     * executed.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value that will be POSTed to the server when the query is
     * executed.
     *
     * @param value
     * The value to POST to the server.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Synchronously executes the POST operation.
     *
     * @return
     * A URL that uniquely identifies the location of the resource created
     * on the server by the operation, or <tt>null</tt> if the server did
     * not return a location.
     */
    @Override
    @SuppressWarnings("unchecked")
    public URL execute() throws QueryException {
        URL valueLocation = null;

        execute(Method.POST, value);

        String location = getResponseProperties().get("Location");
        if (location != null) {
            try {
                valueLocation = new URL(getLocation(), location);
            } catch(MalformedURLException exception) {
            }
        }

        return valueLocation;
    }
}
