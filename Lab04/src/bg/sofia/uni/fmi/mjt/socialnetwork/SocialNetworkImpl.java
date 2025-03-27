package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfileComparatorByFriendsCount;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {
    Map<UserProfile, Set<Post>> registeredUsers;

    public SocialNetworkImpl() {
        this.registeredUsers = new HashMap<>();
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("The given user is null");
        }
        if (this.registeredUsers.keySet().contains(userProfile)) {
            throw new UserRegistrationException(
                String.format("User %s is already registered in the system", userProfile.getUsername()));
        }

        this.registeredUsers.put(userProfile, new HashSet<>());
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(this.registeredUsers.keySet());
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("The given user is null");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("The given content is null or empty");
        }
        if (!this.registeredUsers.keySet().contains(userProfile)) {
            throw new UserRegistrationException("The given user is not registered in the system");
        }

        Set<Post> userPosts = this.registeredUsers.get(userProfile);
        Post newPost = new SocialFeedPost(userProfile, content);
        userPosts.add(newPost);
        this.registeredUsers.put(userProfile, userPosts);

        return newPost;
    }

    @Override
    public Collection<Post> getPosts() {
        Collection<Post> posts = new HashSet<>();
        for (Set<Post> currentPosts : this.registeredUsers.values()) {
            posts.addAll(currentPosts);
        }

        return Collections.unmodifiableCollection(posts);
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("The given post is null");
        }

        UserProfile authorOfPost = post.getAuthor();
        Set<UserProfile> friendsNetwork = new HashSet<>();
        this.getUserFriendsNetwork(authorOfPost, friendsNetwork);

        Set<UserProfile> reachedUsers = new HashSet<>();
        for (UserProfile user : friendsNetwork) {
            if (user.equals(authorOfPost)) {
                continue;
            }

            Collection<Interest> commonInterests = new HashSet<>();
            commonInterests.addAll(user.getInterests());
            commonInterests.retainAll(authorOfPost.getInterests());

            if (commonInterests.size() > 0) {
                reachedUsers.add(user);
            }
        }

        return reachedUsers;
    }

    private void getUserFriendsNetwork(UserProfile startUser, Set<UserProfile> traversedFriends) {
        traversedFriends.add(startUser);
        for (UserProfile friend : startUser.getFriends()) {
            if (!traversedFriends.contains(friend)) {
                getUserFriendsNetwork(friend, traversedFriends);
            }
        }
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
        throws UserRegistrationException {
        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("One or more of the given users are null");
        }

        if (!this.registeredUsers.keySet().contains(userProfile1)
            || !this.registeredUsers.keySet().contains(userProfile2)) {
            throw new UserRegistrationException("One or more of the given users are not registered in the system");
        }

        Set<UserProfile> intersection = new HashSet<>();
        intersection.addAll(userProfile1.getFriends());
        intersection.retainAll(userProfile2.getFriends());

        return intersection;
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> userProfilesSortedByFriendsCount =
            new TreeSet<>(new UserProfileComparatorByFriendsCount());

        userProfilesSortedByFriendsCount.addAll(this.registeredUsers.keySet());

        return userProfilesSortedByFriendsCount;
    }
}