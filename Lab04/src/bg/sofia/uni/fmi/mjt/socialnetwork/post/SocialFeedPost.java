package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SocialFeedPost implements Post, Comparable<Post> {
    private String uniqueId;
    private UserProfile author;
    private LocalDateTime publishedOn;
    private String content;
    private Map<ReactionType, Set<UserProfile>> usersReactions;

    public SocialFeedPost(UserProfile author, String content) {
        this.uniqueId = UUID.randomUUID().toString();
        this.author = author;
        this.publishedOn = LocalDateTime.now();
        this.content = content;
        this.usersReactions = new HashMap<>();
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public UserProfile getAuthor() {
        return this.author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return this.publishedOn;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null || reactionType == null) {
            throw new IllegalArgumentException("The given user profile or reaction type is null");
        }

        boolean userAlreadyReacted = false;
        for (ReactionType reaction : this.usersReactions.keySet()) {
            Set<UserProfile> usersWithCurrentReaction =
                this.usersReactions.get(reaction);
            if (usersWithCurrentReaction.contains(userProfile)) {
                if (reactionType == reaction) {
                    return false;
                }
                userAlreadyReacted = true;
                usersWithCurrentReaction.remove(userProfile);
                if (this.usersReactions.get(reaction).size() == 0) {
                    this.usersReactions.remove(reaction);
                }
                break;
            }
        }

        if (!this.usersReactions.containsKey(reactionType)) {
            this.usersReactions.put(reactionType, new HashSet<>());
        }
        this.usersReactions.get(reactionType).add(userProfile);

        return !userAlreadyReacted;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("The given user profile is null");
        }
        for (ReactionType reaction : this.usersReactions.keySet())     {
            Set<UserProfile> usersWithCurrentReaction = this.usersReactions.get(reaction);
            if (usersWithCurrentReaction.contains(userProfile)) {
                usersWithCurrentReaction.remove(userProfile);
                if (this.usersReactions.get(reaction).size() == 0) {
                    this.usersReactions.remove(reaction);
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        return Collections.unmodifiableMap(this.usersReactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("The given reaction type is null");
        }

        if (!this.usersReactions.containsKey(reactionType)) {
            return 0;
        }

        return this.usersReactions.get(reactionType).size();
    }

    @Override
    public int totalReactionsCount() {
        int totalReactionsCount = 0;
        for (Set<UserProfile> userProfilesPerCurrentReaction : this.usersReactions.values()) {
            totalReactionsCount += userProfilesPerCurrentReaction.size();
        }

        return totalReactionsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SocialFeedPost that = (SocialFeedPost) o;
        return uniqueId.equals(that.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId);
    }

    @Override
    public int compareTo(Post o) {
        if (this.equals(o)) {
            return 0;
        }

        return this.uniqueId.compareTo(o.getUniqueId());
    }
}