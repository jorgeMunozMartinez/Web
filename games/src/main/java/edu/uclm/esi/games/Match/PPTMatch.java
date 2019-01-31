
import edu.uclm.esi.games.ppt;
import edu.uclm.esi.games.Match;

public class PPTMatch extends Match {
	
	public void PPTMatch () {
		super();
		this.board=new PPTBoard(this);
	}
	
	public void calcularFirstPlayer() {
		
	}

}
