package me.stronglift.api.model;

import java.math.BigDecimal;
import java.time.Instant;

import me.stronglift.api.entity.annotation.Deserialize;
import me.stronglift.api.entity.annotation.Serialize;

/**
 * Lift model
 * 
 * @author Dusan Eremic
 *
 */
public class Lift extends BaseEntity<Lift> {
	
	private static final long serialVersionUID = 8923268886165664894L;
	
	/**
	 * Tip vežbe
	 */
	@Deserialize
	@Serialize
	private LiftType liftType;
	
	/**
	 * Broj ponavljanja
	 */
	@Deserialize
	@Serialize
	private Integer repetition;
	
	/**
	 * Težina
	 */
	@Deserialize
	@Serialize
	private BigDecimal weight;
	
	/**
	 * Vreme unosa
	 */
	@Deserialize
	@Serialize
	private Instant time;
	
	/**
	 * Constructor
	 */
	public Lift() {
		
	}
	
	// GET i SET metode START
	
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
	
	// GET i SET metode END
	
	/**
	 * Računa one-repetition-maximum na osnovu unete težine i broja ponavljanja.
	 * 
	 * @return 1RM
	 */
	public BigDecimal calcOneRepMax() {
		if (weight == null || !(weight.doubleValue() > 0) || repetition == null
				|| !(repetition > 0)) {
			return new BigDecimal(0).setScale(2);
		} else {
			return new BigDecimal(weight.doubleValue()
					/ (1.0278 - (0.0278 * repetition))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
		}
	}
	
	@Override
	public String toString() {
		return "Lift [liftType=" + liftType + ", repetition=" + repetition
				+ ", weight=" + weight + ", time=" + time + ", id=" + id
				+ ", owner=" + owner + "]";
	}
}
