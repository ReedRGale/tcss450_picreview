package group1.tcss450.uw.edu.picreview.util;

import android.graphics.Bitmap;
import android.location.Location;

/**
 * Created by Dema on 5/17/2017.
 * Represents a review. Contains all the information that goes with a review.
 */
public class Review {

    /* The caption. */
    private String mCaption;

    /* Review comments. */
    private String mComments;

    /* Review tag, if any. */
    private String mTag;

    /* Review image. */
    private Bitmap mImage;

    /* Location where the object of the review is located. */
    private Location mLocation;

    /* Number of likes this review has. */
    private int mLikes;

    /* Number of dislikes this review has. */
    private int mDislikes;

    public Review()
    {

    }

    // SETTERS AND GETTERS

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String theCaption) {
        this.mCaption = theCaption;
    }

    public String getComments() {
        return mComments;
    }

    public void setComments(String theComments) {
        this.mComments = theComments;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String theTag) {
        this.mTag = theTag;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap theImage) {
        this.mImage = theImage;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location theLocation) {
        this.mLocation = theLocation;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int theLikes) {
        this.mLikes = theLikes;
    }

    public int getDislikes() {
        return mDislikes;
    }

    public void setDislikes(int theDislikes) {
        this.mDislikes = theDislikes;
    }
}
