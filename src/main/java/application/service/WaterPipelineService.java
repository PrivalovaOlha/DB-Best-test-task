package application.service;


import application.dao.IWaterPipelinePersistanceService;
import application.domain.WaterPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class WaterPipelineService {
    private final IWaterPipelinePersistanceService repository;

    private List<WaterPipeline> allWaterPipeline; //используется и заполняется в findWaterPiplinesByPoints. Не вызывать раньше findWaterPiplinesByPoints;
    private HashMap<Integer, Integer> roads = new HashMap<>();// заполняется в findWaterPiplinesByPoints. Не использовать в других методах. Длина путей от определенной точки ко всем остальным

    @Autowired
    public WaterPipelineService(IWaterPipelinePersistanceService repository) {
        this.repository = repository;
    }

    public void addWaterPipeline(WaterPipeline waterPipeline) {
        repository.save(waterPipeline);
    }

    public String findWaterPiplinesByPoints(int[] command) {
        allWaterPipeline = repository.findAll();
        WaterPipeline weNeed = new WaterPipeline(command[0], command[1], 0);

        for (WaterPipeline oneOfPipeline : allWaterPipeline) {
            int wPLenght = oneOfPipeline.getLength();
            if (weNeed.equals(oneOfPipeline) && roads.containsKey(weNeed.getEndPoint()) && roads.get(weNeed.getEndPoint()) > wPLenght) {
                roads.put(weNeed.getEndPoint(), wPLenght);
            } else if (oneOfPipeline.getStartPoint() == weNeed.getStartPoint()) {
                roads.put(oneOfPipeline.getEndPoint(), wPLenght);
                if (isNOTDeadEnd(oneOfPipeline.getEndPoint())) {
                    recursiveSearching(weNeed, oneOfPipeline.getEndPoint(), wPLenght);
                }

            }

        }
        if (roads.get(weNeed.getEndPoint()) == null) {
            return "FALSE;";
        } else {
            return "TRUE;" + roads.get(weNeed.getEndPoint());
        }
    }

    private void recursiveSearching(WaterPipeline weNeed, int endPoint, int roadLength) {
        for (WaterPipeline wp : allWaterPipeline) {
            int legthOfRoad = roadLength + wp.getLength();
            if (wp.getEndPoint() == weNeed.getEndPoint() && endPoint == wp.getStartPoint() && roads.get(endPoint) > legthOfRoad) {
                roads.put(endPoint, wp.getLength());
            } else if (endPoint == wp.getStartPoint()) {
                roads.put(wp.getEndPoint(), legthOfRoad);
                if (isNOTDeadEnd(wp.getEndPoint())) {
                    recursiveSearching(weNeed, wp.getEndPoint(), legthOfRoad);
                }
            }
        }
    }

    private boolean isNOTDeadEnd(int endPoint) {
        return allWaterPipeline.stream().map(a -> a.getStartPoint()).anyMatch(a -> a == endPoint);
    }
}
