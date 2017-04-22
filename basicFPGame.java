import java.util.Scanner;

public class basicFPGame implements FPGame {

	
	int score=0;
	@Override
	public String getMove() {
		return (new Scanner(System.in)).nextLine();
	}

	@Override
	public void sendMove(String s) {

		score+=Integer.parseInt(s);
		System.out.println(score);

	}

}
