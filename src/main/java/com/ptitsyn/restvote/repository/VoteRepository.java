package com.ptitsyn.restvote.repository;

import com.ptitsyn.restvote.model.User;
import com.ptitsyn.restvote.model.Vote;
import com.ptitsyn.restvote.to.VoteCountTo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    Vote findFirstByUser_IdAndCastedBetweenOrderByCastedDesc(@NonNull Integer id, @NonNull LocalDateTime castedStart,
                                                             @NonNull LocalDateTime castedEnd);

    @EntityGraph(attributePaths = {"restaurant", "user"})
    @Query("""
            select v from Vote v where v.id = (select max(vv.id) from Vote vv where vv.casted between :startDate and :endDate group by vv.restaurant)
            """)
    List<Vote> findLastVotesForDate(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    @EntityGraph(attributePaths = {"restaurant"})
    @Query("""
            select v from Vote v where v.id =
            (SELECT MAX(vv.id) FROM Vote vv WHERE vv.user = :user AND vv.casted BETWEEN :startDate AND :endDate)
            """)
    Vote findLastVoteForUserForDate(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param(
            "endDate") LocalDateTime endDate);

    @Query("""
            select new com.ptitsyn.restvote.to.VoteCountTo(v.restaurant.id, v.restaurant.name, CAST(count(v) as int) as voteCount)
            from Vote v
            where v.id = (SELECT MAX(vv.id) FROM Vote vv WHERE vv.user = v.user AND vv.casted between :startDate and :endDate)
            group by v.restaurant
            order by voteCount desc
            """)
    List<VoteCountTo> findAllResultByLastVote(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @NonNull
    Vote save(@NonNull Vote vote);
}
