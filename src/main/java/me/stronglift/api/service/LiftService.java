package me.stronglift.api.service;

import java.util.List;

import me.stronglift.api.entity.Lift;
import me.stronglift.api.entity.User;

/**
 * Lift service interface
 * 
 * @author Dusan Eremic
 *
 */
public interface LiftService extends BaseService<Lift> {

	List<Lift> getRecords(User user);

}
