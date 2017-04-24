
public abstract class NPGame {
	
	int n;
	int playerNum;
	
	public NPGame(int pnum){
		playerNum = pnum;
	}
	
	public abstract NPGame getInstance(int newPlayerNum);
	public abstract boolean isOver();				
	public abstract String getMove();
	public abstract void sendMove(String s);
	
}
