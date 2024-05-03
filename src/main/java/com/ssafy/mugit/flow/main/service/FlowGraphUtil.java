package com.ssafy.mugit.flow.main.service;

import com.ssafy.mugit.flow.main.dto.FlowGraphDto;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.entity.FlowClosure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowGraphUtil {


    public FlowGraphDto makeGraph(List<FlowClosure> flowClosures) {

        Map<Long, Flow> flows = new HashMap<>();
        Map<Long, List<Long>> graph = new HashMap<>();
        Long rootFlowId = 0L;

        for (FlowClosure flowClosure : flowClosures) {

            Flow parent = flowClosure.getParentFlow();
            Flow child = flowClosure.getChildFlow();

            if (!graph.containsKey(parent.getId())) {
                graph.put(parent.getId(), new ArrayList<>());
            }

            if (!parent.equals(child)) {
                graph.get(parent.getId()).add(child.getId());
            } else {
                rootFlowId = parent.getId();
            }

            if (!flows.containsKey(parent.getId())) {
                flows.put(parent.getId(), parent);
            }
            if (!flows.containsKey(child.getId())) {
                flows.put(child.getId(), child);
            }
        }

        return dfs(rootFlowId, flows, graph);
    }

    private FlowGraphDto dfs(Long curFlowId, Map<Long, Flow> flows, Map<Long, List<Long>> graph) {
        FlowGraphDto curFlowGraphDto = new FlowGraphDto(flows.get(curFlowId));
        int edges = graph.get(curFlowId).size();
        for (int i = 0; i < edges; i++) {
            curFlowGraphDto.getChildFlows().add(dfs(graph.get(curFlowId).get(i), flows, graph));
        }
        return curFlowGraphDto;
    }
}
