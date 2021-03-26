package fpl_server.objects;

public class Stats {
	
	int minutes;
    int goals_scored;
    int assists;
    int clean_sheets;
    int goals_conceded;
    int own_goals;
    int penalties_saved;
    int penalties_missed;
    int yellow_cards;
    int red_cards;
    int saves;
    int bonus;
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getGoals_scored() {
		return goals_scored;
	}
	public void setGoals_scored(int goals_scored) {
		this.goals_scored = goals_scored;
	}
	public int getAssists() {
		return assists;
	}
	public void setAssists(int assists) {
		this.assists = assists;
	}
	public int getClean_sheets() {
		return clean_sheets;
	}
	public void setClean_sheets(int clean_sheets) {
		this.clean_sheets = clean_sheets;
	}
	public int getGoals_conceded() {
		return goals_conceded;
	}
	public void setGoals_conceded(int goals_conceded) {
		this.goals_conceded = goals_conceded;
	}
	public int getOwn_goals() {
		return own_goals;
	}
	public void setOwn_goals(int own_goals) {
		this.own_goals = own_goals;
	}
	public int getPenalties_saved() {
		return penalties_saved;
	}
	public void setPenalties_saved(int penalties_saved) {
		this.penalties_saved = penalties_saved;
	}
	public int getPenalties_missed() {
		return penalties_missed;
	}
	public void setPenalties_missed(int penalties_missed) {
		this.penalties_missed = penalties_missed;
	}
	public int getYellow_cards() {
		return yellow_cards;
	}
	public void setYellow_cards(int yellow_cards) {
		this.yellow_cards = yellow_cards;
	}
	public int getRed_cards() {
		return red_cards;
	}
	public void setRed_cards(int red_cards) {
		this.red_cards = red_cards;
	}
	public int getSaves() {
		return saves;
	}
	public void setSaves(int saves) {
		this.saves = saves;
	}
	public int getBonus() {
		return bonus;
	}
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + assists;
		result = prime * result + bonus;
		result = prime * result + clean_sheets;
		result = prime * result + goals_conceded;
		result = prime * result + goals_scored;
		result = prime * result + own_goals;
		result = prime * result + penalties_missed;
		result = prime * result + penalties_saved;
		result = prime * result + red_cards;
		result = prime * result + saves;
		result = prime * result + yellow_cards;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stats other = (Stats) obj;
		if (assists != other.assists)
			return false;
		if (bonus != other.bonus)
			return false;
		if (clean_sheets != other.clean_sheets)
			return false;
		if (goals_conceded != other.goals_conceded)
			return false;
		if (goals_scored != other.goals_scored)
			return false;
		if (own_goals != other.own_goals)
			return false;
		if (penalties_missed != other.penalties_missed)
			return false;
		if (penalties_saved != other.penalties_saved)
			return false;
		if (red_cards != other.red_cards)
			return false;
		if (saves != other.saves)
			return false;
		if (yellow_cards != other.yellow_cards)
			return false;
		return true;
	}
	
	
    
    
}
