package application.dao;


import application.domain.WaterPipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWaterPipelinePersistanceService extends JpaRepository<WaterPipeline, Long> {

}


