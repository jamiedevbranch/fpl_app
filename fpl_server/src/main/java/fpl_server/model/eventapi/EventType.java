package fpl_server.model.eventapi;

import java.io.Serializable;

public enum EventType implements Serializable{

	GOAL,
	ASSIST,
	YELLOW,
	RED,
	PENALTY_MISS,
	PENALTY_SAVE,
	SAVE_POINT,
	CLEAN_SHEET_GAINED,
	CLEAN_SHEET_LOST,
	CLEAN_SHEET_LOCKED_IN
}
