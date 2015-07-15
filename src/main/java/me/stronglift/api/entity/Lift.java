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
	
	@Deserialize
	@Serialize
	private EntityReference<User> owner;
	
	public Lift() {
		
	}
	
}
