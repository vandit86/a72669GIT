public class BeepProtocol {

	public static String SOUND_PATH = "~/javaProgs/sdProject/sound.wav";  
	public static String SERVER_LIST = "serverList";
	public static long DEFAULT_DELAY = 1000;
	public static int DEFAULT_PORT = 4444;
	public static String DEFAULT_HOST = "127.0.0.1";
	
	
	public static final int TRANS_END = 3; 
	public static final int TRANS_TIME = 1;
	public static final int TRANS_DELAY = 2;
	public static final int WAIT = 100;
	public static int RECONECT_TIME = 1000 * 30; // 1 min tempo para resincronizar


}