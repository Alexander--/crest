/*
 * Copyright 2011 CodeGist.org
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ===================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.flickr.service;

import org.codegist.crest.annotate.*;
import org.codegist.crest.flickr.handler.FlickrResponseHandler;
import org.codegist.crest.flickr.model.Comment;
import org.codegist.crest.flickr.model.Gallery;
import org.codegist.crest.flickr.model.Uploader;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * see http://www.flickr.com/services/api/
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@EndPoint("http://flickr.com")
@Path("/services")
@ResponseHandler(FlickrResponseHandler.class)
@POST
@ListSeparator(" ")
public interface Flickr {

    @Path("/rest")
    @QueryParam(value = "method", defaultValue = "flickr.galleries.create")
    Gallery newGallery(
            @FormParam("title") String title,
            @FormParam("description") String description);

    @Path("/rest")
    @QueryParam(value = "method", defaultValue = "flickr.galleries.create")
    Gallery newGallery(
            @FormParam("title") String title,
            @FormParam("description") String description,
            @FormParam("primary_photo_id") long primaryPhotoId);


    @Path("/rest")
    @QueryParam(value = "method", defaultValue = "flickr.photos.comments.addComment")
    String comment(
            @QueryParam("photo_id") long photoId,
            @QueryParam("comment_text") String comment);

    @Path("/rest")
    @QueryParam(value = "method", defaultValue = "flickr.photos.comments.editComment")
    void editComment(
            @QueryParam("comment_id") String commentId,
            @QueryParam("comment_text") String comment);

    @Path("/rest")
    @QueryParam(value = "method", defaultValue = "flickr.photos.comments.getList")
    Comment[] getComments(@QueryParam("photo_id") long photoId);

    @Path("/rest")
    @QueryParam(value = "method", defaultValue = "flickr.photos.comments.getList")
    Comment[] getComments(
            @QueryParam("photo_id") long photoId,
            @QueryParam("min_comment_date") Date from,
            @QueryParam("max_comment_date") Date to);

    @Path("/rest")
    @QueryParam(value = "method", defaultValue = "flickr.photos.comments.deleteComment")
    void deleteComment(@QueryParam("comment_id") String commentId);


    @Path("/upload")
    long uploadPhoto(@MultiPartParam(value = "photo", contentType = "image/jpeg") File photo);

    @Path("/upload")
    long uploadPhoto(@MultiPartParam(value="photo", contentType = "image/jpeg", fileName = "my-photo.jpeg") InputStream photo);

    @Path("/upload")
    long uploadPhoto(
            @MultiPartParam(value = "photo", contentType = "image/jpeg", fileName = "my-photo.jpeg") InputStream photo,
            @MultiPartParam("title") String title,
            @MultiPartParam("description") String description,
            @MultiPartParam("tags") String[] tags,
            @MultiPartParam("is_public") boolean isPublic,
            @MultiPartParam("is_friend") boolean isFriend,
            @MultiPartParam("is_family") boolean isFamily,
            @MultiPartParam("safety_level") SafetyLevel safetyLevel,
            @MultiPartParam("content_type") ContentType contentLype,
            @MultiPartParam("hidden") Visibility searchVisibility
    );


    @Path("/upload")
    @MultiPartParam(value = "async", defaultValue = "1")
    String asyncUploadPhoto(@MultiPartParam(value = "photo", contentType = "image/jpeg") File photo);

    @Path("/upload")
    @MultiPartParam(value = "async", defaultValue = "1")
    String asyncUploadPhoto(@MultiPartParam(value = "photo", contentType = "image/jpeg", fileName = "my-photo.jpeg") InputStream photo);

    @Path("/upload")
    @MultiPartParam(value = "async", defaultValue = "1")
    String asyncUploadPhoto(
            @MultiPartParam(value = "photo", contentType = "image/jpeg", fileName = "my-photo.jpeg") InputStream photo,
            @MultiPartParam("title") String title,
            @MultiPartParam("description") String description,
            @MultiPartParam("tags") String[] tags,
            @MultiPartParam("is_public") boolean isPublic,
            @MultiPartParam("is_friend") boolean isFriend,
            @MultiPartParam("is_family") boolean isFamily,
            @MultiPartParam("safety_level") SafetyLevel safetyLevel,
            @MultiPartParam("content_type") ContentType contentLype,
            @MultiPartParam("hidden") Visibility searchVisibility
    );

    @Path("/rest")
    @QueryParam(value = "method", defaultValue = "flickr.photos.upload.checkTickets")
    Uploader checkUploads(@QueryParam("tickets") String... tickets);


    @Path("/rest")
    @QueryParam(value = "method", defaultValue = "flickr.photos.delete")
    void deletePhoto(@FormParam("photo_id") long photoId);


    enum Visibility {
        Public(1), Private(2);
        private final int lvl;

        Visibility(int lvl) {
            this.lvl = lvl;
        }

        public String toString() {
            return String.valueOf(lvl);
        }
    }

    enum SafetyLevel {
        Safe(1), Moderate(2), Restricted(3);
        private final int lvl;

        SafetyLevel(int lvl) {
            this.lvl = lvl;
        }

        public String toString() {
            return String.valueOf(lvl);
        }
    }

    enum ContentType {
        Photo(1), Screenshot(2), Other(3);
        private final int lvl;

        ContentType(int lvl) {
            this.lvl = lvl;
        }

        public String toString() {
            return String.valueOf(lvl);
        }
    }
}
