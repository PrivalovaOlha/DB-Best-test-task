package application.domain;

import javax.persistence.*;


@Entity
@Table(name = "WATER_PIPELINE")
public class WaterPipeline implements Comparable<WaterPipeline> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WP_ID")
    private Long id;
    @Column(name = "START_POINT")
    private int startPoint;
    @Column(name = "END_POINT")
    private int endPoint;
    @Column(name = "LENGTH")
    private int length;

    public WaterPipeline() {
    }

    public WaterPipeline(int startPoint, int endPoint, int length) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaterPipeline)) return false;
        WaterPipeline that = (WaterPipeline) o;
        return getStartPoint() == that.getStartPoint() &&
                getEndPoint() == that.getEndPoint();
    }


    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(int endPoint) {
        this.endPoint = endPoint;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int compareTo(WaterPipeline o) {
        return getEndPoint() - o.getEndPoint();
    }
}
