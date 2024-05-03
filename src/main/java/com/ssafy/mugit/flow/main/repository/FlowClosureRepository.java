package com.ssafy.mugit.flow.main.repository;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.entity.FlowClosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowClosureRepository extends JpaRepository<FlowClosure, Long> {
    @Query("SELECT fc FROM flow_closure fc " +
            "WHERE fc.childFlow.id = :childFlowId")
    Optional<FlowClosure> findFlowClosureByChildFlowId(Long childFlowId);

    @Query("SELECT fc FROM flow_closure fc " +
            "LEFT JOIN flow pf ON fc.parentFlow.id = pf.id " +
            "LEFT JOIN flow cf ON fc.childFlow.id = cf.id " +
            "WHERE fc.rootFlow = :rootFlow")
    List<FlowClosure> findFlowClosuresByRootFlow(Flow rootFlow);
}
