package me.stronglift.api.entity;

import java.math.BigDecimal;
import java.time.Instant;

import me.stronglift.api.entity.annotation.Deserialize;
import me.stronglift.api.entity.annotation.Serialize;

/**
 * Lift Entity
 * 
 * @author Dusan Eremic
 *
 */
public class Lift extends BaseEntity<Lift> {
	
	private static final long serialVersionUID = 8923268886165664894L;
	
	@Deserialize
	@Serialize
	private LiftType liftType;
	
	@Deserialize
	@Serialize
	private Integer repetition;
	
	@Deserialize
	@Serialize
	private BigDecimal weight;
	
	@Deserialize
	@Serialize
	private Instant time;
	
	public Lift() {
		
	}

	public LiftType getLiftType() {
		return liftType;
	}

	public void setLiftType(LiftType liftType) {
		this.liftType = liftType;
	}

	public Integer getRepetition() {
		return repetition;
	}

	public void setRepetition(Integer repetition) {
		this.repetition = repetition;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Lift [liftType=" + liftType + ", repetition=" + repetition
				+ ", weight=" + weight + ", time=" + time + ", id=" + id
				+ ", owner=" + owner + "]";
	}
}
