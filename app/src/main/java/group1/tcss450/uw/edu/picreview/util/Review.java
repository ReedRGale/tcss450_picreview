package group1.tcss450.uw.edu.picreview.util;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dema on 5/17/2017.
 * Represents a review. Contains all the information that goes with a review.
 */
public class Review {

    /* The caption. */
    private String mCaption;

    /* Review comments. */
    private List<String> mComments;

    /* Review tag, if any. */
    private List<String> mTags;

    /* Review image. */
    private Bitmap mImage;

    /* Location where the object of the review is located. */
    private Location mLocation;

    /* Number of likes this review has. */
    private int mLikes;

    /* Number of dislikes this review has. */
    private int mDislikes;

    /* Whether the review is positive or negative. */
    private int reviewType;

    public String getUser() {
        return mUser;
    }

    public void setUser(String theUser) {
        this.mUser = theUser;
    }

    private String mUser;

    /* No-arg constructor. */
    public Review()
    {
        // Default values
        mCaption = "";
        mImage = null;
        mLocation = null;
        mLikes = 0;
        mDislikes = 0;
        reviewType = 0;
        mComments = new ArrayList<>();
        mTags = new ArrayList<>();
    }

    /* Overloaded constructor that allows for cleaner creation of a Review.
       A caption is not required; pass in null instead. Comments & Tags must be defined with a setter.
    */
    public Review(String theCaption, Bitmap theImage, Location theLocation, int theLikes, int theDislikes, int theReviewType)
    {
        if (theImage == null || theLocation == null) throw new IllegalArgumentException("An argument is null");

        mCaption = (theCaption != null) ? theCaption : "";
        mImage = theImage;
        mLocation = theLocation;
        mLikes = theLikes;
        mDislikes = theDislikes;
        reviewType = theReviewType;
        mComments = new ArrayList<>();
        mTags = new ArrayList<>();

    }

    // SETTERS AND GETTERS

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String theCaption) {
        this.mCaption = theCaption;
    }

    public List<String> getComments() {
        return mComments;
    }

    public void setComments(List<String> theComments) {
        this.mComments = theComments;
    }

    public List<String> getTag() {
        return mTags;
    }

    public void setTags(List<String> theTags) { this.mTags = theTags; }

    public void addTag(String theTag) {
        mTags.add(theTag);
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

    public int getReviewType() { return reviewType; }

    public void setReviewType(int reviewType) { this.reviewType = reviewType; }
}
