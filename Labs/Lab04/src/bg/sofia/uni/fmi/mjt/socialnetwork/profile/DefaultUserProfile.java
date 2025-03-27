package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class DefaultUserProfile implements UserProfile, Comparable<UserProfile> {
    String username;
    Collection<Interest> interests;
    Collection<UserProfile> friends;

    public DefaultUserProfile(String username) {
        //Note: the username collisions are not checked from that class, but from the SocialNetwork
        this.username = username;
        this.interests = new HashSet<>();
        this.friends = new HashSet<>();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableCollection(this.interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("The given interest is null");
        }

        return this.interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("The given interest is null");
        }

        return this.interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableCollection(this.friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("The given user is null");
        }
        if (this == userProfile) {
            throw new IllegalArgumentException("A user cannot add themselves as a friend");
        }

        boolean result = this.friends.add(userProfile);
        if (userProfile.getFriends() != null && !userProfile.getFriends().contains(this)) {
            userProfile.addFriend(this);
        }

        return result;
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("The given user is null");
        }

        boolean result = this.friends.remove(userProfile);
        if (userProfile.getFriends() != null && userProfile.getFriends().contains(this)) {
            userProfile.unfriend(this);
        }

        return result;
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("The given user is null");
        }

        return this.friends.contains(userProfile);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultUserProfile that = (DefaultUserProfile) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public int compareTo(UserProfile o) {
        if (this.equals(o)) {
            return 0;
        }

        return this.username.compareTo(o.getUsername());
    }
}