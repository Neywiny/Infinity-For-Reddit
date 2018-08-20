package ml.docilealligator.infinityforreddit;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

class FetchComment {
    interface FetchCommentListener {
        void onFetchCommentSuccess(String response);
        void onFetchCommentFail();
    }

    private RequestQueue requestQueue;
    private String subredditName;
    private String article;
    private FetchCommentListener mFetchCommentListener;

    FetchComment(RequestQueue requestQueue, String subredditName, String article) {
        this.requestQueue = requestQueue;
        this.subredditName = subredditName;
        this.article = article;
    }

    void queryComment(FetchCommentListener fetchCommentListener) {
        mFetchCommentListener = fetchCommentListener;

        Uri uri = Uri.parse(RedditUtils.getQueryCommentUrl(subredditName, article))
                .buildUpon().appendQueryParameter(RedditUtils.RAW_JSON_KEY, RedditUtils.RAW_JSON_VALUE)
                .build();

        StringRequest commentRequest = new StringRequest(Request.Method.GET, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mFetchCommentListener.onFetchCommentSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mFetchCommentListener.onFetchCommentFail();
            }
        }) {};
        commentRequest.setTag(FetchComment.class);
        requestQueue.add(commentRequest);
    }
}
