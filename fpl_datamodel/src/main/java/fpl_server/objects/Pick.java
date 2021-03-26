package fpl_server.objects;

public class Pick {

	int element;
	int multiplier;
    
    boolean is_captain;
    boolean is_vice_captain;

    public int getElement() {
		return element;
	}
	public void setElement(int element) {
		this.element = element;
	}
	public int getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}
	public boolean isIs_captain() {
		return is_captain;
	}
	public void setIs_captain(boolean is_captain) {
		this.is_captain = is_captain;
	}
	public boolean isIs_vice_captain() {
		return is_vice_captain;
	}
	public void setIs_vice_captain(boolean is_vice_captain) {
		this.is_vice_captain = is_vice_captain;
	}
    
    
    
}
