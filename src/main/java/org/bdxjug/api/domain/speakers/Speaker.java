/*
 * Copyright 2016 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bdxjug.api.domain.speakers;

public class Speaker {

    private final String name;
    private final String bio;
    private final String image;
    private String twitter;

    public Speaker(String name, String bio, String image) {
        this.name = name;
        this.bio = bio;
        this.image = image;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public String getTwitter() {
        return twitter;
    }
}