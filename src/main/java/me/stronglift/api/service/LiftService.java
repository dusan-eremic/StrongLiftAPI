package me.stronglift.api.service;

import java.util.List;

import me.stronglift.api.model.Lift;
import me.stronglift.api.model.User;

/**
 * Operacije specifične za {@link Lift} entitet.
 * 
 * @author Dusan Eremic
 *
 */
public interface LiftService extends BaseService<Lift> {
	
	/**
	 * @return Vraća listu rekorda za korisnika.
	 */
	List<Lift> getRecords(User user);
}
