package com.demo.demo.services;

import com.demo.demo.entities.Award;
import com.demo.demo.entities.UserClient;
import com.demo.demo.repos.AwardRepo;
import com.demo.demo.repos.PracticeRepo;
import com.demo.demo.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AwardServiceImpl implements AwardService{
    private final AwardRepo awardRepo;
    private final PracticeRepo practiceRepo;
    private final UserRepo userRepo;

    public AwardServiceImpl(AwardRepo awardRepo, PracticeRepo practiceRepo, UserRepo userRepo) {
        this.awardRepo = awardRepo;
        this.practiceRepo = practiceRepo;
        this.userRepo = userRepo;
    }

    @Override
    public String addAward(Award award) {
        if((award == null)){
            return "Could not add award because invalid input.";
        };
        if(awardRepo.existsById(award.getId())){
            return "Could not add award because award already exists.";
        }
        if(award.getPractice() == null){
            return "Could not add award because practice was null";
        }
        if(!practiceRepo.existsById(award.getPractice().getId())){
            return "Could not add award because practice_id didn't exist.";
        }

        UUID userId = award.getPlayerId();
        if (userId != null) {
            Optional<UserClient> optionalPlayer = userRepo.findById(userId);
            if (optionalPlayer.isEmpty()) {
                return "UserClient not found";
            }
            award.setPlayer(optionalPlayer.get());
        }

        awardRepo.save(award);
        return "Successfully added award to backend-database.";
    }



    @Override
    public Optional<Award> getAward(int id) {
        if(id <= 0){
            return Optional.empty();
        }
        return awardRepo.findById(id);
    }

    @Override
    public Iterable<Award> getAllAwards() {
        return awardRepo.findAll();
    }

    @Override
    public String updateAward(Award updatedAward) {
        if (updatedAward == null || !awardRepo.existsById(updatedAward.getId())) {
            return "Could not update award because of invalid input or award doesn't exist.";
        }

        UUID userId = updatedAward.getPlayerId();
        if (userId != null) {
            Optional<UserClient> optionalPlayer = userRepo.findById(userId);
            if (optionalPlayer.isEmpty()) {
                return "UserClient not found";
            }
            updatedAward.setPlayer(optionalPlayer.get());
        }

        awardRepo.save(updatedAward);
        return "Successfully updated award";
    }

    @Override
    public String removeAward(Award award) {
        if(award == null){
            return "Failed to remove award.";
        }
        awardRepo.delete(award);
        return "Successfully removed award from backend-database.";
    }

    @Override
    public Iterable<Award> getAwardsFromPractice(int practiceId) {
        ArrayList<Award> awards = new ArrayList<>();

        if(!practiceRepo.existsById(practiceId)){
            return awards;
        }

        for (Award award : awardRepo.findAll()) {
            if(award.getPractice().getId() == practiceId){
                awards.add(award);
            }
        }
        return awards;
    }

    // VOTING
    @Override
    public boolean hasVoted(int award_id, UUID user_id) {
        Optional<Award> award = awardRepo.findById(award_id);
        if(award_id < 0){
            System.out.println("Could not find award when checking if user has voted, because award_id was invalid.");
            return false;
        }
        if(award.isEmpty()){
            System.out.println("Could not find award when checking if user has voted.");
            return false;
        }
        if(!userRepo.existsById(user_id)){
            System.out.println("Could not find user when checking if user has voted.");
            return false;
        }

        return award.get().getVotes().containsKey(user_id);
    }

    @Override
    public Iterable<UserClient> getCurrentWinners(int award_id) {
        if(award_id < 0){
            System.out.println("Could not find award because award_id was invalid.");
            return null;
        }
        Optional<Award> award = awardRepo.findById(award_id);
        if(award.isEmpty()){
            System.out.println("Could not find award when getting winners for award.");
            return null;
        }

        // Find the UUID:s with highest values.
        Map<UUID, Integer> votes = award.get().getVotes();
        List<UUID> winners = new ArrayList<>();
        int highestValue = -1;
        for(Map.Entry<UUID, Integer> vote : votes.entrySet()){
            if(winners.isEmpty()){
                winners.add(vote.getKey());
                highestValue = vote.getValue();
            }
            else{
                if(vote.getValue() == highestValue){
                    winners.add(vote.getKey());
                }
                else if (vote.getValue() > highestValue){
                    highestValue = vote.getValue();
                    winners.clear();
                    winners.add(vote.getKey());
                }
            }
        }

        // Get the winning users from database
        List<UserClient> winningUsers = new ArrayList<>();
        for(UUID u : winners){
            Optional<UserClient> user = userRepo.findById(u);
            if(user.isPresent()){
                winningUsers.add(user.get());
            }
        }

        return winningUsers;
    }

    @Override
    public Iterable<UserClient> getCurrentRank(int award_id) {
        if(award_id < 0){
            System.out.println("Could not get current Rank because award_id was invalid.");
            return null;
        }
        Optional<Award> awardOptional = awardRepo.findById(award_id);

        if(awardOptional.isEmpty()){
            System.out.println("Could not get currentRank because no award was found.");
            return null;
        }


        // Get keys sorted by their values (ascending)
        List<UUID> sortedKeys = awardOptional.get().getVotes().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        List<UserClient> result = new ArrayList<>();
        for (UUID sortedKey : sortedKeys) {
            Optional<UserClient> user = userRepo.findById(sortedKey);
            if(user.isPresent()){
                result.add(user.get());
            }
        }
        System.out.println("Successfully got the current rank of award: " + award_id);
        return result;
    }

    @Override
    public String addToAlreadyVoted(int award_id, UserClient user) {
        if(award_id < 0){
            System.out.println("Could not add user as voted because award_id was invalid.");
        }
        Optional<Award> awardOptional = awardRepo.findById(award_id);
        if(awardOptional.isEmpty()){
            return "Could not add player as voted because award was not found.";
        }

        if(user == null){
            return "Could not add player as voted because player was null.";
        }
        Optional<UserClient> userClientOptional = userRepo.findById(user.getId());
        if(userClientOptional.isEmpty()){
            return "Could not add player as voted because player was not found.";
        }
        Award award = awardOptional.get();

        Set<UUID> votes = award.getAlreadyVoted();
        votes.add(userClientOptional.get().getId());
        award.setAlreadyVoted(votes);
        awardRepo.save(award);
        return "Succesfully added player as voted for award:" + award_id;
    }

    @Override
    public String removeFromAlreadyVoted(int award_id, UserClient user) {
        if(award_id < 0){
            System.out.println("Could not remove player as voted because award_id was invalid.");
        }
        Optional<Award> awardOptional = awardRepo.findById(award_id);
        if(awardOptional.isEmpty()){
            return "Could not remove player as voted because award was not found.";
        }

        if(user == null){
            return "Could not remove player as voted because player was null.";
        }
        Optional<UserClient> userClientOptional = userRepo.findById(user.getId());
        if(userClientOptional.isEmpty()){
            return "Could not remove player as voted because player was not found.";
        }
        Award award = awardOptional.get();

        Set<UUID> votes = award.getAlreadyVoted();
        votes.remove(userClientOptional.get().getId());
        award.setAlreadyVoted(votes);
        awardRepo.save(award);
        return "Successfully removed player as voted for award: " + award_id;
    }

    // TODO: Implement check so users can't update vote multiple times.
    @Override
    public String updateVote(UserClient user, int award_id, boolean votedFor) {
        Optional<UserClient> userClientOptional = userRepo.findById(user.getId());
        if(userClientOptional.isEmpty()){
            return "Could not update vote because player to vote on was not found.";
        }
        if(award_id < 0){
            System.out.println("Could not update vote because award_id was invalid.");
        }
        Optional<Award> awardOptional = awardRepo.findById(award_id);
        if(awardOptional.isEmpty()){
            return "Could not update vote because award was not found.";
        }

        int value = votedFor ? 1 : -1;
        Map<UUID, Integer> votes = awardOptional.get().getVotes();
        int newValue = votes.get(user.getId()) == null ? value : votes.get(user.getId()) + value;
        votes.put(user.getId(), newValue);
        awardOptional.get().setVotes(votes);
        awardRepo.save(awardOptional.get());
        return "Succesfully update vote for award: " + award_id;
    }

    @Override
    public Iterable<UserClient> getTop3InTeam(long team_id, String awardType) {
        if(team_id < 0){
            return null;
        }

        Iterable<UserClient> allPlayers = userRepo.findAll();

        Map<Integer, List<UserClient>> topList = new TreeMap<>();
        for (UserClient player : allPlayers) {
            if(player.getTeamId() == team_id){
                int count = 0;
                for (Award award : player.getAwards()) {
                    if(award.getType().toString().equalsIgnoreCase(awardType)){
                        // Remove from previous index
                        if(topList.get(count) != null && topList.get(count).size()>0){
                            topList.get(count).remove(player);
                        }
                        count++;
                        if(topList.get(count) == null){
                            topList.put(count, new ArrayList<>());
                        }
                        topList.get(count).add(player);
                    }
                }
            }
        }
        List<UserClient> top3 = new ArrayList<>();
        int size = topList.size();
        while(top3.size()<3){
            if(topList.get(size) != null){
                top3.addAll(topList.get(size));
            }
            size--;
            if(size<0){
                break;
            }
        }
        if(top3.size()>3){
            top3 = top3.subList(0,2);
        }

        return top3;
    }

    @Override
    public boolean closeVote(int award_id) {
        Optional<Award> optionalAward = awardRepo.findById(award_id);
        if(optionalAward.isEmpty()){
            System.out.println("Could not close vote because award was not found.");
            return false;
        }
        Award award = optionalAward.get();

        Iterable<UserClient> winners = getCurrentWinners(award.getId());
        if(winners == null){
            System.out.println("Could not close vote because winners was not found");
            return false;
        }

        // Convert to list and get random item
        List<UserClient> winnersList = new ArrayList<>();
        winners.forEach(winnersList::add);

        if(winnersList.isEmpty()) {
            System.out.println("No winners found");
            return false;
        }

        Random random = new Random();
        UserClient randomWinner = winnersList.get(random.nextInt(winnersList.size()));
        award.setPlayer(randomWinner);
        awardRepo.save(award);
        return true;
    }
}
