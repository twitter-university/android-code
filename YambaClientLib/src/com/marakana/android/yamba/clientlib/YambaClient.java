
package com.marakana.android.yamba.clientlib;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public final class YambaClient {
    private static final String TAG = "YambaClient";

    private static final int DEFAULT_TIMEOUT = 10000;

    private static final String DATE_FORMAT_PATTERN = "EEE MMM dd HH:mm:ss Z yyyy";

    private static final String DEFAULT_USER_AGENT = "YambaClient/1.0";

    public static final String DEFAULT_API_ROOT = "http://yamba.marakana.com/api";

    private String username;

    private String password;

    private String apiRoot;

    private String apiRootHost;

    private int apiRootPort;

    public YambaClient(String username, String password) {
        this(username, password, DEFAULT_API_ROOT);
    }

    public YambaClient(String username, String password, String apiRoot) {
        this.setUsername(username);
        this.setPassword(password);
        this.setApiRoot(apiRoot);
    }

    public synchronized void setUsername(String username) throws IllegalArgumentException {
        if (isBlank(username)) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        this.username = username;
    }

    public synchronized void setPassword(String password) throws IllegalArgumentException {
        if (isBlank(password)) {
            throw new IllegalArgumentException("Password must not be blank");
        }
        this.password = password;
    }

    public String getApiRoot() {
        return apiRoot;
    }

    public synchronized void setApiRoot(String apiRoot) throws IllegalArgumentException {
        if (isBlank(apiRoot)) {
            apiRoot = DEFAULT_API_ROOT;
        }
        try {
            URL url = new URL(apiRoot);
            this.apiRoot = apiRoot;
            this.apiRootHost = url.getHost();
            this.apiRootPort = url.getPort();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid API Root: " + apiRoot);
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.length() == 0;
    }

    private boolean endsWithTags(Stack<String> stack, String tag1, String tag2) {
        int s = stack.size();
        return s >= 2 && tag1.equals(stack.get(s - 2)) && tag2.equals(stack.get(s - 1));
    }

    public void fetchFriendsTimeline(TimelineProcessor timelineProcessor) {
        long t = System.currentTimeMillis();
        try {
            HttpGet get = new HttpGet(this.getUri("/statuses/friends_timeline.xml"));
            HttpClient client = this.getHttpClient();
            try {
                Log.d(TAG, "Getting " + get.getURI());
                HttpResponse response = client.execute(get);
                this.checkResponse(response);
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new YambaClientException("No friends update data");
                } else {
                    XmlPullParser xpp = this.getXmlPullParser();
                    InputStream in = entity.getContent();
                    try {
                        xpp.setInput(in, "UTF-8");
                        TimelineStatusImpl status = new TimelineStatusImpl();
                        Stack<String> stack = new Stack<String>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
                        Log.d(TAG, "Parsing timeline");
                        for (int eventType = xpp.getEventType(); eventType != XmlPullParser.END_DOCUMENT
                                && timelineProcessor.isRunnable(); eventType = xpp.next()) {
                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    timelineProcessor.onStartProcessingTimeline();
                                    break;
                                case XmlPullParser.START_TAG:
                                    stack.push(xpp.getName());
                                    break;
                                case XmlPullParser.END_TAG:
                                    if ("status".equals(stack.pop())) {
                                        timelineProcessor.onTimelineStatus(status);
                                    }
                                    break;
                                case XmlPullParser.TEXT:
                                    String text = xpp.getText();
                                    if (endsWithTags(stack, "status", "id")) {
                                        status.id = Long.parseLong(text);
                                    } else if (endsWithTags(stack, "status", "created_at")) {
                                        status.createdAt = dateFormat.parse(text);
                                    } else if (endsWithTags(stack, "status", "text")) {
                                        status.message = text;
                                    } else if (endsWithTags(stack, "user", "name")) {
                                        status.user = text;
                                    }
                                    break;
                            } // switch
                        } // for
                        timelineProcessor.onEndProcessingTimeline();
                        Log.d(TAG, "Finished parsing timeline");
                    } finally {
                        in.close();
                        entity.consumeContent();
                    }
                }
            } finally {
                client.getConnectionManager().shutdown();
            }
        } catch (Exception e) {
            throw translateException(e);
        }
        t = System.currentTimeMillis() - t;
        Log.d(TAG, "Fetched timeline in " + t + " ms");
    }

    public void updateStatus(String status) {
        updateStatus(status, Double.NaN, Double.NaN);
    }

    public void updateStatus(String status, double latitude, double longitude) {
        try {
            HttpPost post = new HttpPost(this.getUri("/statuses/update.xml"));
            List<NameValuePair> postParams = new ArrayList<NameValuePair>(3);
            postParams.add(new BasicNameValuePair("status", status));
            if (-90.00 <= latitude && latitude <= 90.00 && -180.00 <= longitude
                    && longitude <= 180.00) {
                postParams.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
                postParams.add(new BasicNameValuePair("long", String.valueOf(longitude)));
            }
            post.setEntity(new UrlEncodedFormEntity(postParams, HTTP.UTF_8));
            HttpClient client = this.getHttpClient();
            try {
                Log.d(TAG, "Submitting " + postParams + " to " + post.getURI());
                HttpResponse response = client.execute(post);
                this.checkResponse(response);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    entity.consumeContent();
                }
            } finally {
                client.getConnectionManager().shutdown();
            }
        } catch (Exception e) {
            throw translateException(e);
        }
    }

    private void checkResponse(HttpResponse response) {
        int responseCode = response.getStatusLine().getStatusCode();
        String reason = response.getStatusLine().getReasonPhrase();
        switch (responseCode) {
            case 200:
                return;
            case 401:
                throw new YambaClientUnauthorizedException(reason);
            default:
                throw new YambaClientException("Unexpected response [" + responseCode
                        + "] while posting update: " + reason);
        }
    }

    private YambaClientException translateException(Exception e) {
        if (e instanceof YambaClientException) {
            return (YambaClientException)e;
        } else if (e instanceof ConnectTimeoutException) {
            return new YambaClientTimeoutException("Timeout while communicating to" + this.apiRoot,
                    e);
        } else if (e instanceof IOException) {
            return new YambaClientIOException("I/O error while communicating to" + this.apiRoot, e);
        } else {
            return new YambaClientException("Unexpected error while communicating to"
                    + this.apiRoot, e);
        }
    }

    private HttpClient getHttpClient() {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
        HttpProtocolParams.setUserAgent(params, DEFAULT_USER_AGENT);
        httpclient.setParams(params);
        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(this.apiRootHost, this.apiRootPort),
                new UsernamePasswordCredentials(this.username, this.password));
        return httpclient;
    }

    private XmlPullParser getXmlPullParser() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            return factory.newPullParser();
        } catch (Exception e) {
            throw new YambaClientException("Failed to create parser", e);
        }
    }

    private String getUri(String relativePath) {
        return apiRoot + relativePath;
    }

    private static final class TimelineStatusImpl implements TimelineStatus {
        private long id;

        private Date createdAt;

        private String message;

        private String user;

        public long getId() {
            return id;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public String getMessage() {
            return message;
        }

        public String getUser() {
            return user;
        }
    }

    public static interface TimelineStatus {
        public long getId();

        public Date getCreatedAt();

        public String getMessage();

        public String getUser();
    }

    public static interface TimelineProcessor {
        public void onStartProcessingTimeline();

        public void onTimelineStatus(TimelineStatus timelineStatus);

        public boolean isRunnable();

        public void onEndProcessingTimeline();
    }
}
