/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package musicplayer.src;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import musicplayer.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Print a list of videos matching a search term.
 *
 * @author Jeremy Walker
 */
public class YoutubeSearch {

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 5;

    private ArrayList<String> titles; //an arraylist of the titles of the videos to output to the user

    private ArrayList<String> videoIds; // an arraylist of video ids to output to the user

    private ArrayList<String> thumbnailURLs; // an arraylist of thumbnail urls (in String) to output to the user

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     */
    public void search(String queryTerm) {
        // Read the developer key from the properties file.
        Properties properties = new Properties();
        try {
            InputStream in = YoutubeSearch.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        try {
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) {
                }
            }).setApplicationName("Music-Player").build();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey(""); //Put key here
            search.setQ(queryTerm);

            search.setType("video");

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), queryTerm);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, and thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    private void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        titles = new ArrayList<>();
        videoIds = new ArrayList<>();
        thumbnailURLs = new ArrayList<>();

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                addTitles(singleVideo.getSnippet().getTitle());
                addVideoIds(rId.getVideoId());
                addThumbnailURLs(thumbnail.getUrl());

            }
        }
    }

    /**
     * addTitles
     * adds a title to an arraylist of titles
     * @param title String object of the title
     */
    public void addTitles(String title){ this.titles.add(title); }

    /**
     * getTitles
     * gets the arraylist of titles
     * @return ArrayList of titles
     */
    public ArrayList<String> getTitles(){ return this.titles; }

    /**
     * addVideoIds
     * adds a videoID to an arraylist of videoIDs
     * @param videoId String object of the videoID
     */
    public void addVideoIds(String videoId){ this.videoIds.add(videoId); }

    /**
     * getVideoIDs
     * gets the arraylist of videoIDs
     * @return ArrayList of videoIDs
     */
    public ArrayList<String>  getVideoIds(){ return this.videoIds; }

    /**
     * addThumbnailURLs
     * adds a thumbnailURL to an arraylist of thumbnailURLs
     * @param thumbnailURL String object of the thumbnailURL
     */
    public void addThumbnailURLs(String thumbnailURL){ this.thumbnailURLs.add(thumbnailURL); }

    /**
     * getThumbnailURLs
     * gets the arraylist of thumbnailURLs
     * @return ArrayList of thumbnailURLs
     */
    public ArrayList<String>  getThumbnailURLs(){ return this.thumbnailURLs; }
}

