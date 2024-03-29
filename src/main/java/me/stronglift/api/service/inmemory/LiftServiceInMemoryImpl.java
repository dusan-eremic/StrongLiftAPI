package me.stronglift.api.service.inmemory;

import java.util.ArrayList;
import java.util.List;

import me.stronglift.api.model.Lift;
import me.stronglift.api.model.LiftType;
import me.stronglift.api.model.User;
import me.stronglift.api.service.LiftService;

/**
 * Operacije specifične za {@link Lift} entitet.
 * 
 * @author Dusan Eremic
 *
 */
class LiftServiceInMemoryImpl extends BaseServiceInMemoryImpl<Lift> implements
		LiftService {
	
	/**
	 * @return Vraća listu rekorda za korisnika.
	 */
	@Override
	public List<Lift> getRecords(User user) {
		
		Lift benchRecord = null;
		Lift squatRecord = null;
		Lift deadliftRecord = null;
		
		for (Lift lift : data) {
			if (lift.getLiftType() == LiftType.BENCH) {
				if (benchRecord == null
						|| benchRecord.calcOneRepMax().compareTo(
								lift.calcOneRepMax()) == -1) {
					benchRecord = lift;
				}
				
			} else if (lift.getLiftType() == LiftType.SQUAT) {
				if (squatRecord == null
						|| squatRecord.calcOneRepMax().compareTo(
								lift.calcOneRepMax()) == -1) {
					squatRecord = lift;
				}
			} else if (lift.getLiftType() == LiftType.DEADLIFT) {
				if (deadliftRecord == null
						|| deadliftRecord.calcOneRepMax().compareTo(
								lift.calcOneRepMax()) == -1) {
					deadliftRecord = lift;
				}
			}
		}
		
		final List<Lift> records = new ArrayList<Lift>();
		
		if (benchRecord != null) {
			records.add(benchRecord);
		}
		
		if (squatRecord != null) {
			records.add(squatRecord);
		}
		
		if (deadliftRecord != null) {
			records.add(deadliftRecord);
		}
		
		return records;
	}
	
}