package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Comparator;

public class UserProfileComparatorByFriendsCount implements Comparator<UserProfile> {

    @Override
    public int compare(UserProfile o1, UserProfile o2) {
        int o1FriendsSize = o1.getFriends().size();
        int o2FriendsSize = o2.getFriends().size();
        if (o1FriendsSize == o2FriendsSize) {
            return 0;
        } else if (o1FriendsSize > o2FriendsSize) {
            return -1;
        } else {
            return 1;
        }
    }
}