
//Class to represent a swim team - which has four swimmers
package medleySimulation;

import medleySimulation.Swimmer.SwimStroke;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CountDownLatch;

public class SwimTeam extends Thread {
	
	public static StadiumGrid stadium; //shared 
	public Swimmer [] swimmers;
	private int teamNo; //team number

	static AtomicBoolean begin = new AtomicBoolean(false); // flag to start the team

	public CountDownLatch[] boom2 = new CountDownLatch[sizeOfTeam-1];
	public CountDownLatch[] order_strokes = new CountDownLatch[sizeOfTeam-1];

	
	public static final int sizeOfTeam=4;


	SwimTeam( int ID, FinishCounter finish,PeopleLocation [] locArr ) {
		this.teamNo=ID;
		
		swimmers= new Swimmer[sizeOfTeam];
	    SwimStroke[] strokes = SwimStroke.values();  // Get all enum constants
		stadium.returnStartingBlock(ID);

		for(int i=teamNo*sizeOfTeam,s=0;i<((teamNo+1)*sizeOfTeam); i++,s++) { //initialise swimmers in team
			locArr[i]= new PeopleLocation(i,strokes[s].getColour());
	      	int speed=(int)(Math.random() * (3)+30); //range of speeds 
			swimmers[s] = new Swimmer(i,teamNo,locArr[i],finish,speed,strokes[s]); //hardcoded speed for now
		}
	}
	

	private void startSimulation()
	{
		// thread will wait if signal (begin) has not been given to begin start simulation
		// starting simulation == starting swimmer threads
		
		synchronized(begin)
		{
			while (!begin.get())
			{
				try {
					begin.wait();
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
	public void run() {
		try {

			boom2[0] = new CountDownLatch(1);
			boom2[1] = new CountDownLatch(1);
			boom2[2] = new CountDownLatch(1);

			order_strokes[0] = new CountDownLatch(1);
			order_strokes[1] = new CountDownLatch(1);
			order_strokes[2] = new CountDownLatch(1);

			startSimulation(); //Begin simulation

			for(int s=0;s<sizeOfTeam; s++) { //start swimmer threads
				swimmers[s].start();
				
			}

			

			
			for(int s=0;s<sizeOfTeam; s++) swimmers[s].join();			//don't really need to do this;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	

