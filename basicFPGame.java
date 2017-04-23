import java.util.Scanner;

public class basicFPGame implements FPGame {

	int playerNum;
	int score;
	
	public basicFPGame(int pn){
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

}
