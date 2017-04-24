import java.util.Scanner;

public class BasicMPGame extends NPGame {

	int playerNum;
	int score;
	
	public BasicMPGame(int pn, int nPlayers){
		super(pn);
		n = nPlayers;
		System.out.println("Player "+pn+" created");
		playerNum = pn;
		score=0;
	}
	
	
	@Override
	public String getMove() {
		if(isOver())
			return "0";
		System.out.println(playerNum+" your turn");
		return (new Scanner(System.in)).nextLine();
	}

	@Override
	public void sendMove(String s) {

		score+=Integer.parseInt(s);
		System.out.println(playerNum+" "+score);

	}

	@Override
	public boolean isOver() {
		return score>100;
	}


	@Override
	public NPGame getInstance(int newPlayerNum) {
		return new BasicMPGame(newPlayerNum, n);
	}

}
