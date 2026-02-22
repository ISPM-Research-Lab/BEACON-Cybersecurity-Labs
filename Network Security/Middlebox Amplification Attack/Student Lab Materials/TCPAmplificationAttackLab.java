import java.util.*;

/**
 * Enum for reflection types:
 */
enum ReflectionEnum {destination, middlebox, destinationMiddlebox, routerLoop, victimSustained}


/**
 * Driver class
 */
public class TCPAmplificationAttackLab {
  /**
   * Instantiating outside of main method for accessibility within helper methods without the need to pass properties in parameters
   * Static in this case allows accessibility between methods and does not mean the program will continue using the same instances
   * on each run.
   */
  protected final static String CONFIRM = "Y";
  protected final static String ROUTER = "192.168.1.1";
  protected final static String MODEM = "192.168.0.1";
  protected static int numPackets = 0;
  protected static Packet pkt1;
  protected static Packet pkt2;
  protected static boolean quizSelected = false;
  protected static int numTargeted = 0;
  protected static int numWide = 0;
  protected static int numTraces = 0;
  protected static Scanner scan = new Scanner(System.in);
  protected static ArrayList<Integer> attackIPs = new ArrayList<Integer>(5);
  protected static Random rand = new Random();
  protected static Reflection[] reflections = new Reflection[5];
  protected static Middlebox[] middleboxes = new Middlebox[2];
  protected static Router[] routers;


  /**
   * Main Method
   * This method contains the operational set of instructions for executing the lab program.
   * @param args Command Line Arguments **This program does not take command line arguments.
   */
  public static void main(String[] args) {
    //print intro for student to read while program works on initializing the lab *This only runs once
    printIntroduction();


    // try-catch for packet instantiation here due to UnknownHostException
    pkt1 = new Packet();
    pkt2 = new Packet();

    //initialize the program
    initialize();
//TODO: delete these printouts once testing is complete.
//    for (Reflection reflection : reflections){
//
//      System.out.println("Reflection Type:" + reflection.getReflectionType() + "IP: " + reflection.getIPAddress());
//
//    }
    do{
      actionPrompt();
    } while (!quizSelected);

    // closes class level scanner
    scan.close();
  }


  /**
   * Support Methods for Main below:
   */


  /**
   * @return void
   * Method to initialize the lab and fill in properties
   */
  private static void initialize(){
    configurePacketPrompt();
    generateAttackIPs();
    generateMiddleboxes();
    generateRouters();
  }

  private static void generateRouters() {

  }

  private static void generateMiddleboxes() {

  }

  /**
   * generateAttackIPs
   * @return void
   * This method randomly generates 5 unique attack IPs between 0 and 255.
   * The 5 IPs are then stored for future reference.
   */
  private static void generateAttackIPs(){
    //loop generate unique IPs
	  Random rand = new Random();
	  int genRand = rand.nextInt(50);
    for(int i = 0; i < 5 ; i++){
      int newIP;
      //while the generated IP is contained in the ArrayList keep generating until we find a unique newIP
      do {
        newIP = Math.abs(rand.nextInt() % 255);
      } while (attackIPs.contains(newIP));
      //Once a unique IP is generated add it to the ArrayList for reference and create the corresponding Reflection (attack)

      attackIPs.add(newIP);
      reflections[i] = new Reflection(newIP, i);
    }
    reflections[1].setResponseSize(1, genRand);
    reflections[2].setResponseSize(2, genRand);
    reflections[3].setRandRouters();
  }

/**
 * prints the introduction to the assignment to the console
 */
  private static void printIntroduction() {
    System.out.println("\nWelcome to the TCP Reflected Aplification Lab");
    System.out.println("/////////////////////////////////////////////");
    System.out.println("\nIn this lab you will need to use the tools provided to reveal and identify each of the 5 types of TCP amplifaction attacks.\n"
                      + "You will be performing actions as an \"attacker\" and observing the feedback as the \"victim\". If you get stuck, the article \n"
                      + "may provide the information you need.\tBe sure to save and submit the results from your terminal.");
  }


  private static void actionPrompt(){
    System.out.println("Please select an action from the following list:");
    System.out.println("\t0) Configure your packet(s)\n"
                    +  "\t1) View your current packet configuration(s)\n"
                    +  "\t2) Perform an IPV4 wide attack\n"
                    +  "\t3) Perform an targeted attack\n"
                    +  "\t4) Perform a Traceroute\n"
                    +  "\t5) Complete the quiz?"
                    );
    ActionSelection(getNumericSelection(0,5));
  }


  /**
   * @param selection
   */
  private static void ActionSelection(int selection){
    switch (selection) {
      case (0):
        configurePacketPrompt();
        break;
      case (1):
        printPacketConfig();
        break;
      case (2):
        System.out.println("\nYou are about to perform a wide attack with " + (numPackets == 1 ? "one (" : "two (")+ numPackets + ") packet(s).\n");
        numWide++;
        wideAttack();
        break;
      case (3):
        System.out.println("\nYou are about to perform a targeted attack with " + (numPackets == 1 ? "one (" : "two (")+ numPackets + ") packet(s).\n"
                          +"Please enter a value for the fourth segment of your target IP. (0-255):"
                          );
        numTargeted++;
        targetedAttack(getNumericSelection(0,255));
        break;
      case (4):
        System.out.println("\nYou are about to perform a traceroute operation with your packet(s).\n"
                          +"Please enter a value for the fourth segment of your target IP. (0-255):"
                          );
        //make selection and update target for both packets
        int target = getNumericSelection(0,255);
        numTraces++;
        pkt1.setDestinationIP(Integer.toString(target));
        pkt2.setDestinationIP(Integer.toString(target));
        //run traceroute
        traceroute(target,pkt1);


        break;
      case (5):
        System.out.println("\nYou are about to start the quiz. You should be prepared to:\n"
                          +"\t1) Declare the IPs at which a successful attack takes place,                                          | 40 points\n"
                          +"\t2) Declare which type of attack is at each IP address,                                                | 25 points\n"
                          +"\t3) Declare the response size of destination, middlebox, and destination middlebox reflection,         | 5 points each\n"
                          +"\t4) Declare the number of routers in the routing loop,                                                 | 10 points\n"
                          +"\t5) Declare the number of packets the victim sent in the victim-sustained attack,                      | 10 points\n"
                          +"\n\t*NOTE: You will only be allowed to take this quiz one time. Are you sure you want to prodeed?"
                          );
        if (getYesNoResponse()){quiz();}
        break;
    }
  }


  private static void configurePacketPrompt() {
    System.out.println("\nWelcome to the packet configuration system!");
    getNumPackets();
    int selection;
    do{
      System.out.println("\nWhat packet value(s) you like to set?\n"
                        +"\t0)Set HTTP Header\n"
                        +"\t1)Set Flag(s)\n"
                        +"\t2)Set TTL (time to live)\n"
                        +"\t3)Adjust Packet Optional Size\n"
                        +"\t4)Adjust Packet Payload Size\n"
                        +"\t5)View Current Packet Configuration\n"
                        +"\t6)Learn More About Packet Configuration\n"
                        +"\t7)Close Packet Configuration (will print the resultant configuration)\n"
      );
      selection = getNumericSelection(0,7);
      if (selection != 7){PacketConfigurationSelection(selection);}
    } while(selection != 7);
    printPacketConfig();
  }

  private static void PacketConfigurationSelection(int selection){
    switch (selection) {
      case (0):
        configureHTTPHeader();
        break;
      case (1):
        System.out.println("Current Packet Flags: ");
        System.out.println("Packet 1: " + pkt1.getFlagStatus());
        System.out.println("Packet 2: " + pkt2.getFlagStatus());
        configurePacketFlags();
        break;
      case (2):
        configurePacketTTL();
        break;
      case (3):
        configurePacketOptionalSize();
        break;
      case (4):
        configurePacketPayload();
        break;
      case (5):
        printPacketConfig();
        break;
      case (6):
        showPacketConstruction();
        break;
      case (7):
        break;
    }
  }

  private static void configurePacketPayload() {
    System.out.println("Adjust Packet Payload Tool\n"
                      +"The payload of a packet is in addition to its standard size. When optionals are used the\n"
                      +"Packet is padded so that the payload begins on a new 32 bit segment. If you wish to adjust\n"
                      +"the payload of packet one, you may enter an integer between 0 (default) and " + pkt1.getMaxDataSize() + "."
                      );
    pkt1.setOptionalSize(getNumericSelection(0, pkt1.getMaxDataSize()));
    if (numPackets == 2) {
      System.out.println("Your options indicate you are using 2 packets. Please enter a size for packet 2's payload from 0 (default) to " + pkt2.getMaxDataSize() + ".");
      pkt2.setOptionalSize(getNumericSelection(0,pkt2.getMaxDataSize()));
    }
  }


  private static void configurePacketOptionalSize() {
    System.out.println("Adjust Packet Size Tool\n"
                      +"NOTE: The default packet size with an options size of zero (0), is 160bits.\n"
                      +"If you wish to adjust the size of your packet one, you may enter an integer\n"
                      +"between 0 (default) and 40 "
                      );
    pkt1.setOptionalSize(getNumericSelection(0, 40));
    if (numPackets == 2) {
      System.out.println("Your options indicate you are using 2 packets. Please enter an optional size for packet 2 from 0 (default) to 40.");
      pkt2.setOptionalSize(getNumericSelection(0,40));
    }
  }

  private static void showPacketConstruction(){
    System.out.println("\nThis page is for informational purposes only. It should help to give you an idea of\n"
                      +"how memory is allocated in a TCP packet.");
    System.out.println(" 0                   1                   2                   3\n"
                    +  " 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1\n"
                    +  "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n"
                    +  "|     Source Port (16 bits)     |   Destination Port (16 bits)  |\n"
                    +  "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n"
                    +  "|                  Sequence Number (32 bits)                    |\n"
                    +  "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n"
                    +  "|               Acknowledgment Number  (32 bits)                |\n"
                    +  "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n"
                    +  "|  Data |           |U|A|P|R|S|F|                               |\n"
                    +  "| Offset| Reserved  |R|C|S|S|Y|I|            Window             |\n"
                    +  "|       |           |G|K|H|T|N|N|                               |\n"
                    +  "|(4bits)| (6 bits)  | (6 bits)  |          (16 bits)            |\n"
                    +  "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n"
                    +  "|      Checksum (16 bits)       |    Urgent Pointer (16 bits)   |\n"
                    +  "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n"
                    +  "|             Options (0-40 bits)               |    Padding    |\n"
                    +  "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n"
                    +  "|                             data                              |\n"
                    +  "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n"
                    +  "                            TCP Header Format\n"
                    +  "          Note that one tick mark represents one bit position.\n"
                    +  "\n"
                    +  "   Flags :  6 bits (from left to right):\n"
                    +  "\tURG:  Urgent Pointer field significant\n"
                    +  "\tACK:  Acknowledgment field significant\n"
                    +  "\tPSH:  Push Function\n"
                    +  "\tRST:  Reset the connection\n"
                    +  "\tSYN:  Synchronize sequence numbers\n"
                    +  "\tFIN:  No more data from sender\n\n"
                    +  "   Destination Port:  16 bits (i.e., The destination port number"
                    );
  }


  private static void configureHTTPHeader() {
    System.out.println("For simplification the header entered here will be saved to both packets. Please enter a valid URL header of the\n"
                      + "form: <Domain.Top-Level-Domain>\tFor example, the default HTTP header is: example.com\n"
                      +"\tNOTE: This system will assume that the subdomain is 'www.' unless one is specified. This system sill only accept\n"
                      +"\tone specific subdomain other than 'www'. If you are not sure what to put here, review the research paper."
                    );
    do {
      String newHeader = scan.next();
      pkt1.setHeader(newHeader);
      pkt2.setHeader(pkt1.getHTTPHeader());
      System.out.println("Packet headers changed to <" + newHeader + ">. Is this correct?");
    } while (!getYesNoResponse());
  }

  private static void configurePacketFlags() {
    setFlagLoop(pkt1, 1);
    if (numPackets == 2){
      setFlagLoop(pkt2, 2);
    }
  }


  /**
   * @param pktNum
   */
  private static void setFlagLoop(Packet pkt, int pktNum){
    int selection;
    do{
      System.out.println("You may set one (1) flag at a time. Which flag to set on Packet " + pktNum + "?\n"
                                +"\t0) URG\n"
                                +"\t1) ACK\n"
                                +"\t2) PSH\n"
                                +"\t3) RST\n"
                                +"\t4) SYN\n"
                                +"\t5) FIN\n"
                                +"\t6) Continue.\n"
      );
      selection = getNumericSelection(0,6);
      if(selection!=6){
        pkt.toggleFlag(selection);
      }
    } while (selection != 6 );
  }

  private static void configurePacketTTL() {
    pkt1.setTTL(assignTTL(1));
    System.out.println("Packet 1 TTL set to: " + pkt1.getTTL());
    if (numPackets == 2){
      pkt2.setTTL(assignTTL(2));
      System.out.println("Packet 2 TTL set to: " + pkt2.getTTL());
    }
  }

  private static int assignTTL(int pktNum){
    System.out.println("Please enter your desired TTL (1-255) value for packet " + pktNum + ":");
    int retVal = getNumericSelection(1, 255);
    System.out.println("you entered: " + retVal);
    return retVal;
  }

  private static void printPacketConfig() {
    System.out.println("Packet 1 Configuration:");
    System.out.println(pkt1);
    if (numPackets == 2){
      System.out.println("Packet 2 Configuration:");
      System.out.println(pkt2);
    }
  }

  private static void getNumPackets(){
    System.out.println("Would you like to use one (1) or two (2) packets in your next attack?");
    try {
      numPackets = Integer.parseInt(scan.next());
      if (numPackets == 1 || numPackets == 2) {return;}
      else {
        numPackets = 0;
        throw new Exception();
      }
    } catch (Exception e) {
      System.out.println("Sorry, try again. This program only allows one (1) or two (2) packets to be sent at a time.");
      getNumPackets();
    }
  }

  private static void wideAttack(){

    System.out.println("You performed a wide attack");

    System.out.println("\n IP: PACKETS");

    double responsePercent = 0;






    //2 PACKETS SYN, PSH+ACK
    if(numPackets == 2 && (pkt1.getSYN() && (pkt2.getPSH() && pkt2.getACK()))   ||  (pkt2.getSYN() && (pkt1.getPSH() && pkt1.getACK())))
    {
    	responsePercent = 1;
    }
 // syn with get
    //I SEE NOTHIGN IN THE PACKET CLASS ABOUT GET REQUESTS. THUS I AM ASSUMING ALL PACKETS ARE SENT WITH GETS AS THEY HAVE HTTP HEADERS
    else if(numPackets == 1 && pkt1.getSYN())
   	{
    	responsePercent = 0.8;
   	}
  //2 packets SYN, PSH
    else if(numPackets == 2 && (pkt1.getPSH() || pkt1.getSYN()) &&  (pkt2.getPSH() || pkt2.getSYN()))
   	{
    	responsePercent = 0.6;
   	}
  //psh+ACK
    else if(numPackets == 1 && pkt1.getPSH() && pkt1.getACK())
	{
    	responsePercent = 0.4;
	}
  //based on the flags set, the number of packets returned for each amplification will change
    else if(numPackets == 1 && pkt1.getPSH())
	{
		responsePercent = 0.2;
	}


    //based on the table the number of ips that will produce amplification changes

    //the if statements are in decending order

    //all respond
    if(pkt1.getHTTPHeader().contains("youporn")|| pkt2.getHTTPHeader().contains("youporn"))
    {
    	for(int i = 0; i < 256; i++)
    	{
    		//destination reflection
    		if(reflections[0].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[0].getResponseSize()*responsePercent));
    		}
    		else if(reflections[1].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[1].getResponseSize()*responsePercent));
    		}
    		else if(reflections[2].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[2].getResponseSize()*responsePercent));
    		}
    		else if(reflections[3].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[3].getResponseSize()*responsePercent));
    		}
    		else if(reflections[4].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[4].getResponseSize()*responsePercent));
    		}
    		else System.out.println("183.76.15." + i + ": 1");
    	}
    }
    // 4/5
    else if(pkt1.getHTTPHeader().contains("roxypalace") || pkt2.getHTTPHeader().contains("roxypalace"))
    {
    	for(int i = 0; i < 256; i++)
    	{
    		if(reflections[0].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[0].getResponseSize()*responsePercent));
    		}
    		else if(reflections[1].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[1].getResponseSize()*responsePercent));
    		}
    		else if(reflections[2].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[2].getResponseSize()*responsePercent));
    		}
    		else if(reflections[3].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[3].getResponseSize()*responsePercent));
    		}else System.out.println("183.76.15." + i + ": 1");
    	}
    }
    // 3/5
    else if(pkt1.getHTTPHeader().contains("google") || pkt2.getHTTPHeader().contains("google"))
    {
    	for(int i = 0; i < 256; i++)
    	{
    		if(reflections[0].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[0].getResponseSize()*responsePercent));
    		}
    		else if(reflections[1].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[1].getResponseSize()*responsePercent));
    		}
    		else if(reflections[2].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[2].getResponseSize()*responsePercent));
    		}else System.out.println("183.76.15." + i + ": 1");
    	}
    }
    // 2/5
    else if(pkt1.getHTTPHeader().contains("bittorrent") || pkt2.getHTTPHeader().contains("bittorrent"))
    {
    	for(int i = 0; i < 256; i++)
    	{
    		if(reflections[0].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[0].getResponseSize()*responsePercent));
    		}
    		else if(reflections[1].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[1].getResponseSize()*responsePercent));
    		}else System.out.println("183.76.15." + i + ": 1");
    	}
    }
    // 1/5
    else if(pkt1.getHTTPHeader().contains("survive") || pkt2.getHTTPHeader().contains("survive"))
    {
    	for(int i = 0; i < 256; i++)
    	{
    		if(reflections[0].getIPAddress() == i)
    		{
    			System.out.println("183.76.15." + i + ": " + (int)(reflections[0].getResponseSize()*responsePercent));
    		}else System.out.println("183.76.15." + i + ": 1");
    	}
    }
    // none. standard response from all ips
    else
    {
    	for(int i = 0; i < 256; i++)
    	{
    		System.out.println("183.76.15." + i + ": 1");
    	}
    }


  }


  /**
   * @param attackIP
   */
  private static void targetedAttack(int attackIP) {

System.out.println("You performed a targeted attack");
//TODO: fill in per image in github .png



double responsePercent = 0;

//2 PACKETS SYN, PSH+ACK
if(numPackets == 2 && (pkt1.getSYN() && (pkt2.getPSH() && pkt2.getACK()))   ||  (pkt2.getSYN() && (pkt1.getPSH() && pkt1.getACK())))
{
	responsePercent = 1;
}
// syn with get
//I SEE NOTHIGN IN THE PACKET CLASS ABOUT GET REQUESTS. THUS I AM ASSUMING ALL PACKETS ARE SENT WITH GETS AS THEY HAVE HTTP HEADERS
else if(numPackets == 1 && pkt1.getSYN())
	{
	responsePercent = 0.8;
	}
//2 packets SYN, PSH
else if(numPackets == 2 && (pkt1.getPSH() || pkt1.getSYN()) &&  (pkt2.getPSH() || pkt2.getSYN()))
	{
	responsePercent = 0.6;
	}
//psh+ACK
else if(numPackets == 1 && pkt1.getPSH() && pkt1.getACK())
{
	responsePercent = 0.4;
}
//based on the flags set, the number of packets returned for each amplification will change
else if(numPackets == 1 && pkt1.getPSH())
{
	responsePercent = 0.2;
}

//check if the attackIP has a reflection
boolean reflection = false;
int index = 6;
for(int i = 0; i < 5; i++)
{
	if(reflections[i].getIPAddress() == attackIP)
	{
		index = i;
	}
}

if(index < 5 && pkt1.getHTTPHeader().contains("youporn"))
{
	reflection = true;
}
else if(index < 4 && pkt1.getHTTPHeader().contains("roxypalace"))
{
	reflection = true;
}
else if(index < 3 && pkt1.getHTTPHeader().contains("google"))
{
	reflection = true;
}
else if(index < 2 && pkt1.getHTTPHeader().contains("bittorrent"))
{
	reflection = true;
}
else if(index < 1 && pkt1.getHTTPHeader().contains("survive"))
{
	reflection = true;
}



if(reflection)
{
	System.out.println("183.76.15." + attackIP + ": " + (int)(reflections[index].getResponseSize()*responsePercent));
}
else
{
	System.out.println("183.76.15." + attackIP + ": 1");
}


}


  //prints the routing loop depending on how many routers were generated
  private static void printRoutingLoop() {
	  int occilate = 0;
	  if(reflections[3].getRouters() == 5)
	  {
		 for(int i = 0; i < 30; i++)
  		{
  			//variable to have it go back and forth between the routers without repeating

  			if(occilate == 0)
  			{
  				//random ip taken from china
  				System.out.println("Router 4.14.239.107");
  				occilate++;
  			}
  			else if(occilate == 1)
  			{
  				//random ip taken from china
  				System.out.println("Router 38.124.43.226");
  				occilate++;
  			}
  			else if(occilate == 2)
  			{
  				//random ip taken from china
  				System.out.println("Router 203.119.169.80");
  				occilate++;
  			}
  			else if(occilate == 3)
  			{
  			//random ip taken from china
  				System.out.println("Router 43.128.0.196");
  				occilate++;
  			}
  			else if(occilate == 4)
  			{
  				//random ip taken from china
  				System.out.println("Router 8.25.82.225");
  				occilate = 0;
  			}
      		System.out.println("Middlebox: 223.6.6.6 ");
      		//two hops are done so i incriments twice, once by the loop and once here.
      		i++;
  		}
  		System.out.println("Traceroute terminated after 30 hops");
	  }
	  else if(reflections[3].getRouters() == 4)
	  {
		  for(int i = 0; i < 30; i++)
	  		{
	  			//variable to have it go back and forth between the routers without repeating

	  			if(occilate == 0)
	  			{
	  				//random ip taken from china
	  				System.out.println("Router 4.14.239.107");
	  				occilate++;
	  			}
	  			else if(occilate == 1)
	  			{
	  				//random ip taken from china
	  				System.out.println("Router 38.124.43.226");
	  				occilate++;
	  			}
	  			else if(occilate == 2)
	  			{
	  				//random ip taken from china
	  				System.out.println("Router 203.119.169.80");
	  				occilate++;
	  			}
	  			else if(occilate == 3)
	  			{
	  			//random ip taken from china
	  				System.out.println("Router 43.128.0.196");
	  				occilate = 0;
	  			}

	      		System.out.println("Middlebox: 223.6.6.6 ");
	      		//two hops are done so i incriments twice, once by the loop and once here.
	      		i++;
	  		}
	  		System.out.println("Traceroute terminated after 30 hops");
	  }
	  else if(reflections[3].getRouters() == 3)
	  {
		  for(int i = 0; i < 30; i++)
	  		{
	  			//variable to have it go back and forth between the routers without repeating

	  			if(occilate == 0)
	  			{
	  				//random ip taken from china
	  				System.out.println("Router 4.14.239.107");
	  				occilate++;
	  			}
	  			else if(occilate == 1)
	  			{
	  				//random ip taken from china
	  				System.out.println("Router 38.124.43.226");
	  				occilate++;
	  			}
	  			else if(occilate == 2)
	  			{
	  				//random ip taken from china
	  				System.out.println("Router 203.119.169.80");
	  				occilate = 0;
	  			}


	      		System.out.println("Middlebox: 223.6.6.6 ");
	      		//two hops are done so i incriments twice, once by the loop and once here.
	      		i++;
	  		}
	  		System.out.println("Traceroute terminated after 30 hops");
	  }
	  else if(reflections[3].getRouters() == 2)
	  {
		  for(int i = 0; i < 30; i++)
	  		{
	  			//variable to have it go back and forth between the routers without repeating

	  			if(occilate == 0)
	  			{
	  				//random ip taken from china
	  				System.out.println("Router 4.14.239.107");
	  				occilate++;
	  			}
	  			else if(occilate == 1)
	  			{
	  				//random ip taken from china
	  				System.out.println("Router 38.124.43.226");
	  				occilate = 0;
	  			}


	      		System.out.println("Middlebox: 223.6.6.6 ");
	      		//two hops are done so i incriments twice, once by the loop and once here.
	      		i++;
	  		}
	  		System.out.println("Traceroute terminated after 30 hops");
	  }

  }


  /**
   * @param targetIPAdress
   *
   * depending on whether the packet header allows for a middlebox response or not, the traceroute will produce a response according to the reflection diagrams
   */
  private static void traceroute(int targetIPAddress, Packet pkt){
    System.out.println("\n\nTracing route to " + pkt.getHTTPHeader() + " [" + targetIPAddress + "]");
    System.out.println("over a maximum of 30 hops");

    if(pkt.getHTTPHeader().contains("youporn"))
    {
    	//destination reflection
    	if(reflections[0].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	//middlebox reflection
    	else if(reflections[1].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Middlebox: 223.6.6.6 ");
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	//destination and middlebox reflection
    	//user should be able to identify by the number of packets that this is the destination and middlebox reflection
    	else if(reflections[2].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Middlebox: 223.6.6.6 ");
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	//routing loop
    	else if(reflections[3].getIPAddress() == targetIPAddress)
    	{

    		System.out.println("Start location: " + ROUTER);
    		//for loop to demonstrate the routing loop then being cut off after a certain point
    		printRoutingLoop();

    	}
    	//victim sustained reflection
    	//while this is just the destination and start location, the large number of packets in response as seen by the wide attack should hint that it is the victim sustained reflection.
    	else if(reflections[4].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}

    }
    else if(pkt.getHTTPHeader().contains("roxypalace"))
    {
    	//destination reflection
    	if(reflections[0].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	//middlebox reflection
    	else if(reflections[1].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Middlebox: 223.6.6.6 ");
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	//destination and middlebox reflection
    	//user should be able to identify by the number of packets that this is the destination and middlebox reflection
    	else if(reflections[2].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Middlebox: 223.6.6.6 ");
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	//routing loop
    	else if(reflections[3].getIPAddress() == targetIPAddress)
    	{

    		System.out.println("Start location: " + ROUTER);
    		//for loop to demonstrate the routing loop then being cut off after a certain point
    		printRoutingLoop();

    	}
    	//if they have the correct header but the incorrect ip
    	else
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    }
    else if(pkt.getHTTPHeader().contains("google"))
    {
    	//destination reflection
    	if(reflections[0].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	//middlebox reflection
    	else if(reflections[1].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Middlebox: 223.6.6.6 ");
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	//destination and middlebox reflection
    	//user should be able to identify by the number of packets that this is the destination and middlebox reflection
    	else if(reflections[2].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Middlebox: 223.6.6.6 ");
    		System.out.println("Destination reached: " + targetIPAddress);
    	}
    	//if they have the correct header but the incorrect ip
    	else
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    }
    else if(pkt.getHTTPHeader().contains("bittorrent"))
    {
    	//destination reflection
    	if(reflections[0].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	//middlebox reflection
    	else if(reflections[1].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Middlebox: 223.6.6.6 ");
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	else
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    }
    else if(pkt.getHTTPHeader().contains("survive"))
    {
    	//destination reflection
    	if(reflections[0].getIPAddress() == targetIPAddress)
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    	else
    	{
    		System.out.println("Start location: " + ROUTER);
    		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    	}
    }
    //no correct header so standard response is produced for all ips
    else
    {
    	System.out.println("Start location: " + ROUTER);
		System.out.println("Destination reached: 186.76.15." + targetIPAddress);
    }

    System.out.println("\n");

  }



  /**
   * getNumericSelection
   * @param max The maximum value allowed to be entered ** should be inclusive
   * @return int ** If a valid number is entered, then this number
   */
  private static int getNumericSelection(int min, int max) {
		int retVal = 0;
    try {
      retVal = Integer.parseInt(scan.next());
      if (retVal < min || retVal > max){
        throw new IndexOutOfBoundsException();
      }
    } catch (Exception e) {
      System.out.println("Sorry, please check the selection range and try again:");
      retVal = getNumericSelection(min, max);
    }
		return retVal;
	}

  private static boolean getYesNoResponse() {
    System.out.println("\tPlease enter (Y)es or (N)o:");
    boolean proceed = false;
    try{
      String response = scan.next();
      if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")){
        proceed = true;
      } else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")){
        proceed = false;
      } else {
        throw new InputMismatchException();
      }
    } catch (Exception e){
      System.out.println("Sorry, invalid input. Please enter a valid response: \"Yes\" or \"Y\" or \"No\" or \"N\"");
      proceed = getYesNoResponse();
    }
		return proceed;
	}

  /**
   * @return int
   */
  // private static int attackVisibility(){
  //   if (numPackets == 1) {
  //       return attackingWithOnePacket(pkt1);
  //   } else {
  //     return attackingWithTwoPackets();
  //   }
  // }


  /**
   * @param pkt1
   * @return int
   */
  // private static int attackingWithOnePacket(Packet pkt1) {
  //   if (pkt1.getIsPSH() || pkt1.getIsACK() || pkt1.getIsSYN()){
  //     if(pkt1.getIsSYN()){
  //       return 1;
  //     } else if(pkt1.getIsPSH() && pkt1.getIsACK()){
  //       return 2;
  //     } else if (pkt1.getIsPSH()){
  //       return 3;
  //     }
  //   }
  //   return 0;
  // }


  /**
   * @return int
   */
  // private static int attackingWithTwoPackets() {
  //   if (pkt1.getIsSYN()){
  //     if(pkt2.getIsPSH() && pkt2.getIsACK()){
  //       return 5;
  //     } else if (pkt2.getIsPSH()){
  //       return 4;
  //     }
  //   } else if (!pkt1.getIsSYN()) {
  //     attackingWithOnePacket(pkt1);
  //     attackingWithOnePacket(pkt2);
  //   }

  //   return 0;
  // }


  /**
   * Quiz
   * @return void
   * entering the quiz method will break the program loop and segue the prgram to shutdown
   */
  private static void quiz() {
    int response;
    double score = 0.0;
    //TODO: Include grading metric for portions of the problems they get correct.
    // quiz for if they found the correct IPs associated with each reflection
    int ipGuesses = 1;



    //keep track of what has been finished so points are not added twice
    boolean r0 = false, r2 = false, r3 = false, r4 = false, r1 = false, r0type = false, r2type = false, r3type = false, r4type = false, r1type = false;
    boolean destRes = false, midRes = false, destMidRes = false, routRes = false, vicRes = false;


    do {
      System.out.println("Attempts Remaining: " + (9-ipGuesses) + "\t\t\tCurrent Score: " + score + "/100");
      System.out.println("\nPlease enter the host portion of an address you believe is a successful attack: (0,255)");
      response = getNumericSelection(0,255);


      /**
       * Things to identify for quiz:
       * 5x IPs 8 8 8 8 8                               = 40
       * 5x attack types 5 5 5 5 5 = 25                 = 65
       * 1 destination response size 5                  = 70
       * 2x middlebox response size 5 5 10 10 = 20      = 90
       * # of routers in routing loop 5                 = 95
       * How long did the victim sustain the attack? 5  = 100
       */
      //destination reflection
      if (response == reflections[0].getIPAddress()){
    	  //add score if this is the first time
    	  if(!r0){score += 8; r0 = true;}

        if (quizAttackTypeSelection() == reflections[0].getReflectionType().ordinal()){
        	//add score if this is the first time
        	if(!r0type) {score += 5; r0type = true;}

        	System.out.println("What was the size of the response sent to the victim? Enter an integer:");
        	response = getNumericSelection(0,1000000);
        	if(response == reflections[0].getResponseSize())
        	{
        		System.out.println("Well done!");
        		//add score if this is the first time
        		if(!destRes) { score += 5; destRes = true;}
        	}
        	else
        	{
        		System.out.println("Incorrect!");
            	ipGuesses++;
            	continue;
        	}
        }
        else {
        	System.out.println("Incorrect!");
        	ipGuesses++;
        	continue;
        }
      }
      //middlebox reflection
      else if (response == reflections[1].getIPAddress()){
    	  //add score if this is the first time
    	  if(!r1){score += 8; r1 = true;}

        if (quizAttackTypeSelection() == reflections[1].getReflectionType().ordinal()){
        	//add score if this is the first time
        	if(!r1type) {score += 5; r1type = true;}

        	System.out.println("What was the size of the response sent to the victim? Enter an integer:");
        	response = getNumericSelection(0,1000000);
        	if(response == reflections[1].getResponseSize())
        	{
        		System.out.println("Well done!");
        		//add score if this is the first time
        		if(!midRes) { score += 5; midRes = true;}
        	}
        	else
        	{
        		System.out.println("Incorrect!");
            	ipGuesses++;
            	continue;
        	}
        }
        else {
        	System.out.println("Incorrect!");
        	ipGuesses++;
        	continue;
        }
      }
      //dest and middlebox reflection
      else if (response == reflections[2].getIPAddress()){
    	  //add score if this is the first time
    	  if(!r2){score += 8; r2 = true;}

        if (quizAttackTypeSelection() == reflections[2].getReflectionType().ordinal()){
        	//add score if this is the first time
        	if(!r2type) {score += 5; r2type = true;}

        	System.out.println("What was the size of the response sent to the victim? Enter an integer:");
        	response = getNumericSelection(0,1000000);
        	if(response == reflections[2].getResponseSize())
        	{
        		System.out.println("Well done!");
        		//add score if this is the first time
        		if(!destMidRes) { score += 5; destMidRes = true;}
        	}
        	else
        	{
        		System.out.println("Incorrect!");
            	ipGuesses++;
            	continue;
        	}
        }
        else {
        	System.out.println("Incorrect!");
        	ipGuesses++;
        	continue;
        }
      }
      //routing loop
      else if (response == reflections[3].getIPAddress()){
    	  //add score if this is the first time
    	  if(!r3){score += 8; r3 = true;}

        if (quizAttackTypeSelection() == reflections[3].getReflectionType().ordinal()){
        	//add score if this is the first time
        	if(!r3type) {score += 5; r3type = true;}

        	System.out.println("How many routers were in the routing loop?");
        	response = getNumericSelection(0,10);
        	if(response == reflections[3].getRouters())
        	{
        		System.out.println("Well done!");
        		//add score if this is the first time
        		if(!routRes) { score += 10; routRes = true;}
        	}
        	else
        	{
        		System.out.println("Incorrect!");
            	ipGuesses++;
            	continue;
        	}
        }
        else {
        	System.out.println("Incorrect!");
        	ipGuesses++;
        	continue;
        }
      }
      //victim-sustained attack
      else if (response == reflections[4].getIPAddress()){
    	  //add score if this is the first time
    	  if(!r4){score += 8; r4 = true;}

        if (quizAttackTypeSelection() == reflections[4].getReflectionType().ordinal()){
        	//add score if this is the first time
        	if(!r4type) {score += 10; r4type = true;}

        	System.out.println("How many packets did the victim send in the victim-sustained attack? (Check the hint on the documentation if you need help)");
        	response = getNumericSelection(0,1000000);
        	if(response == reflections[4].getResponseSize()/2)
        	{
        		System.out.println("Well done!");
        		//add score if this is the first time
        		if(!vicRes) { score += 5; vicRes = true;}
        	}
        	else
        	{
        		System.out.println("Incorrect!");
            	ipGuesses++;
            	continue;
        	}
        }
        else {
        	System.out.println("Incorrect!");
        	ipGuesses++;
        	continue;
        }
      }
      else ipGuesses++;


      //if all questions were answered correctly exit the program
      if(r0 && r1 && r2 && r3 && r4 && r0type && r1type && r2type && r3type && r4type && destRes && midRes && destMidRes && routRes && vicRes)
      {
    	  break;
      }


    } while (ipGuesses < 9);

    // TODO add a more elequent ending message & Grade
    System.out.println("\n Thank you for using this lab program! Your final score is " + score);

    System.out.println("Number of wide attacks done: " + numWide);
    System.out.println("Number of targeted attacks done: " + numTargeted);
    System.out.println("Number of traceroutes done: " + numTraces);

    //changing the quizSelected boolean makes the program exit
    quizSelected = true;
  }


  private static int quizAttackTypeSelection(){
    System.out.println("Congratulations! You have correctly identified the address of a successful attack. What type of attack is this?");
    System.out.println("Please enter the number corresponding to the attack.\n"
                      +"\t0) Destination Reflection\n"
                      +"\t1) Middlebox Reflection\n"
                      +"\t2) Destination and Middlebox Reflection\n"
                      +"\t3) Routing Loop Reflection\n"
                      +"\t4) Victim Sustained Reflection\n"
                  );
    return getNumericSelection(0, 4);
  }

/**
 * End of TCPAmplificationAttackLab.java
 */
}

/**
 * Reflection Class
 */

class Reflection{

  private int ip, responseSize, routers;
  private ReflectionEnum attackType;

  public Reflection(int ip, int typeOrdinal){
    this.ip = ip;
    setTypeProperties(typeOrdinal);
  }

  public ReflectionEnum getReflectionType(){
    return this.attackType;
  }

  public int getResponseSize(){
    return this.responseSize;
  }

  public int getIPAddress(){
    return this.ip;
  }

  private void setTypeProperties(int typeOrdinal){
    Random rand = new Random();
    switch (typeOrdinal) {
      case 0:
        attackType = ReflectionEnum.destination;
        this.responseSize = 50;
        break;
      case 1:
        attackType = ReflectionEnum.middlebox;
        this.responseSize = rand.nextInt(50) + 100;
        break;
      case 2:
        attackType = ReflectionEnum.destinationMiddlebox;
        this.responseSize = rand.nextInt(100) + 320;
        break;
      case 3:
        attackType = ReflectionEnum.routerLoop;
        this.responseSize = rand.nextInt(500) + 10000;
        break;
      case 4:
        attackType = ReflectionEnum.victimSustained;
        this.responseSize = rand.nextInt(5000) + 100000;
        break;
    }
  }

  //used for middlebox and middlebox + destination reflection so that middlebox + destination reflection is equivolent to middlebox's random
  public void setResponseSize(int type, int rand)
  {
	  //destination + middlebox
	  if(type == 2)
	  {
		  this.responseSize = rand + 100 + 50;
	  }
	  //middlebox
	  else if(type == 1)
	  {
		  this.responseSize = rand + 100;
	  }
  }

  public void setRandRouters()
  {
	  Random rand = new Random();
	  routers = rand.nextInt(4) +2;
  }
  public int getRouters()
  {
	  return routers;
  }


  public void attack(){
    switch (attackType) {
      case destination:
        //TODO: Attack behavior
        break;
      case middlebox:
        //TODO: Attack behavior
        break;
      case destinationMiddlebox:
        //TODO: Attack behavior
        break;
      case routerLoop:
        //TODO: Attack behavior
        break;
      case victimSustained:
        //TODO: Attack behavior
        break;
    }
  }
    /**
     * End of Reflection Class
     */
}

/**
 * Router Class
 */

class Router{
  private int ipAddress;
  private boolean misconfigured;
  public Router(int ipAddress){
    this.ipAddress = ipAddress;
  }

  public void handlePacket(Packet packet, int destinationIP){
    if (misconfigured){
      //TODO:
    }
  }

  public String toString(){
    String retVal = "Router: " + this.ipAddress;
    return retVal;
  }
}

/**
 * Middlebox Class
 */

class Middlebox{
  private int responseSize;
  private String[] forbiddenHeaders = {"plus.google", "bittorrent", "example", "survive.org.uk", "youporn", "roxypalace"};
  public Middlebox(int responseSize){
    this.responseSize = responseSize;
  }

// 7x YouP---
// 3x +.google.com
// 4x Bittorrent
// 8x Roxypalace
// 6x Survive_org
// 5x example_com

  public void respond(Packet packet){

  }

  private void getResponseProperties(Packet packet){
    if (packet.getHTTPHeader().toLowerCase().contains(forbiddenHeaders[0])) {

    }
    if (packet.getHTTPHeader().toLowerCase().contains(forbiddenHeaders[1])) {

    }
    if (packet.getHTTPHeader().toLowerCase().contains(forbiddenHeaders[2])) {

    }
    if (packet.getHTTPHeader().toLowerCase().contains(forbiddenHeaders[3])) {

    }
    if (packet.getHTTPHeader().toLowerCase().contains(forbiddenHeaders[4])) {

    }
    if (packet.getHTTPHeader().toLowerCase().contains(forbiddenHeaders[5])) {

    }

  }

  /**
   * End of Middlebox Class
   */
}

/**
 * Packet Class
 */

class Packet{

  //variables
  private String httpHeader;
  private final int MIN_SIZE = 160;
  private final int MAX_SIZE = 4288;
  private int declaredTTL, activeTTL, numFlags, bitSize, padSize , dataSize, optionalSize;
  private boolean isURG, isACK, isPSH, isRST, isSYN, isFIN;
  private String sourceIP, destinationIP;


//constructors
  public Packet() {
    this.httpHeader = "www.example.com";
    this.declaredTTL = this.activeTTL = 255;
    this.isURG = this.isACK = this.isPSH = this.isSYN = this.isFIN = false;
    this.isRST = true;
    this.numFlags = 1;
    this.optionalSize = 0;
    this.bitSize = MIN_SIZE;
    this.updatePadSize();
    this.sourceIP = "192.168.1.32";
    this.destinationIP  = "183.76.15.0";
  }

  public int getMaxDataSize(){
    return this.MAX_SIZE - this.MIN_SIZE - this.optionalSize - this.padSize;
  }

  public void setOptionalSize(int optionalSize) {
    if (optionalSize < 0 || optionalSize > 40){
      System.out.println("Packet: Invalid optional size declared. Optional size unchanged.");
      return;
    } else {
      this.optionalSize = optionalSize;
      updatePadSize();
      this.bitSize = this.MIN_SIZE + this.padSize + optionalSize;
    }
  }

  private void updatePadSize(){
    this.padSize = (this.MIN_SIZE + this.optionalSize) % 32;
  }

  public void setDataSize(int dataSize){
    if (dataSize < 0 || dataSize > getMaxDataSize()){
      System.out.println("Packet: Invalid data size declared. Data size unchanged.");
      return;
    } else {
      this.dataSize = dataSize;
    }
  }

  public int getDataSize(){
    return this.dataSize;
  }

  public void toggleFlag(int flag){
    if (flag > 5 || flag < 0){
      System.out.println("Packet: Invalid flag selection.");
      return;
    }
    switch(flag){
      case (0):
        this.isURG = !this.isURG;
        if (this.isURG) {
          this.numFlags++;}
        else {this.numFlags--;}
        System.out.println("URG flag: "+this.isURG);
        break;
      case (1):
        this.isACK = !this.isACK;
        if (this.isACK) {this.numFlags++;}
        else {this.numFlags--;}
        System.out.println("ACK flag: "+this.isACK);
        break;
      case (2):
        this.isPSH = !this.isPSH;
        if (this.isPSH) {this.numFlags++;}
        else {this.numFlags--;}
        System.out.println("PSH flag: "+this.isPSH);
        break;
      case (3):
        this.isRST = !this.isRST;
        if (this.isRST) {this.numFlags++;}
        else {this.numFlags--;}
        System.out.println("RST flag: "+this.isRST);
        break;
      case (4):
        this.isSYN = !this.isSYN;
        if (this.isSYN) {this.numFlags++;}
        else {this.numFlags--;}
        System.out.println("SYN flag: "+this.isSYN);
        break;
      case (5):
        this.isFIN = !this.isFIN;
        if (this.isFIN) {this.numFlags++;}
        else {this.numFlags--;}
        System.out.println("FIN flag: "+this.isFIN);
        break;
    }
  }

  public void decrementTTL(){
    this.activeTTL--;
  }

  public int getModifiedTTL(){
    return this.activeTTL;
  }

  public void resetPacketTTL(){
    this.activeTTL = this.declaredTTL;
  }

  public String toString(){
    String retVal = ""
    + "TTL: " + this.declaredTTL
    + " | Source: " + this.sourceIP
    + " | HTTP Header: " + this.httpHeader
    + " | Destination: " + this.destinationIP
    + " | Size: " + this.bitSize
    + " | Flags: " + this.numFlags + " [ ";
            if (isURG){retVal += "URG ";}
            if (isSYN){retVal += "SYN ";}
            if (isPSH){retVal += "PSH ";}
            if (isACK){retVal += "ACK ";}
            if (isRST){retVal += "RST ";}
            if (isFIN){retVal += "FIN ";}
    retVal += "]";
    return retVal;
  }

  //getters
  public void setHeader(String newHeader){
    this.httpHeader = newHeader;
  }

  public String getHTTPHeader() {
    return this.httpHeader;
  }

  public int getTTL() {
    return this.declaredTTL;
  }

  //flag booleans
  public boolean getURG(){
    return this.isURG;
  }

  public boolean getACK(){
    return this.isACK;
  }

  public boolean getPSH(){
    return this.isPSH;
  }

  public boolean getRST(){
    return this.isRST;
  }

  public boolean getSYN(){
    return this.isSYN;
  }

  public boolean getFIN(){
    return this.isFIN;
  }

  public String getFlagStatus(){
    String retVal = "[ ";
    if (isURG){retVal += "URG(" + getURG() + ")";}
    if (isSYN){retVal += "SYN(" + getSYN() + ")";}
    if (isPSH){retVal += "PSH(" + getPSH() + ")";}
    if (isACK){retVal += "ACK(" + getACK() + ")";}
    if (isRST){retVal += "RST(" + getRST() + ")";}
    if (isFIN){retVal += "FIN(" + getFIN() + ")";}
    retVal += " ]";
    return retVal;
  }

  //IPs
  public String getSourceIP(){
    return this.sourceIP;
  }

  public String getDestinationIP(){
    return this.destinationIP;
  }

  public void setTTL(int declaredTTL) {
    this.declaredTTL = declaredTTL;
  }

  //IPs
  public void setSourceIP(String s){
    this.sourceIP = "192.168.1." +s;
  }

  public void setDestinationIP(String destinationIP){
    this.destinationIP = "183.76.15." + destinationIP;
  }

  /**
   * End of Packet Class
   */

}

