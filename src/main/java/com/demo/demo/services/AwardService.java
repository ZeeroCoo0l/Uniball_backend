package com.demo.demo.services;

import com.demo.demo.entities.Award;
import com.demo.demo.entities.UserClient;
import java.util.Optional;
import java.util.UUID;

public interface AwardService {
    String addAward(Award award);
    Optional<Award> getAward(int id);
    Iterable<Award> getAllAwards();
    String updateAward(Award updatedAward);
    String removeAward(Award award);
    Iterable<Award> getAwardsFromPractice(int practiceId);

    // VOTING
    boolean hasVoted(int award_id, UUID user_id);
    Iterable<UserClient> getCurrentWinners(int award_id);
    Iterable<UserClient> getCurrentRank(int award_id);
    String addToAlreadyVoted(int award_id, UserClient user);
    String removeFromAlreadyVoted(int award_id, UserClient user);
    String updateVote(UserClient user, int award_id, boolean votedFor);
    Iterable<UserClient> getTop3InTeam(long team_id, String awardType);
    boolean closeVote(int award_id);
}
