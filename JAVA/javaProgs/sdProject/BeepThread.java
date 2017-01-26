//

public class BeepThread implements Runnable {
  
    private String soundPath = BeepProtocol.SOUND_PATH;
    private String[] cmd = {"sh", "-c", "aplay " + soundPath};     // command play to sound

    private long delay;  // delay for beep interval between beeps 
    private long atraso = 0;
    private long  startTime; 
    private volatile boolean delayFlag = false ; 
    private volatile boolean atrasoFlag = false;                              
    
    BeepThread (long delay){
        if (delay <= 0) this.delay = BeepProtocol.DEFAULT_DELAY;
        else this.delay = delay;
    }

        
    public void run() {
        try {
            while(true) {
                    // Pause for time msec
                if (atraso != 0) atrasoFlag = true;
                startTime = System.currentTimeMillis();     // get time 
                System.out.println (startTime);
                Runtime.getRuntime().exec(cmd);               // make beep
                Thread.sleep(delay - atraso); 
                if (atrasoFlag == true) { atraso = 0; atrasoFlag = false; }
                if (delayFlag == true) delayFlag = false; 

            }
        } catch(Exception ie){

         }
   }


    // tempo que falta para fazer o beep 
   public long getRestTime (){ 
        return this.startTime + delay - System.currentTimeMillis();
    }

    public long getDelay (){ 
        return this.delay;
    }

    public void setDelay (long d){
        if (d > 0)
        this.delay = d;
        delayFlag = true;
    }

    // recebe o valor que falta para beep no servidor 
    public void setAtraso (long d){
        long t = getRestTime();
        atraso = t - d ;
    }

    public boolean getDelayFlag (){
        return this.delayFlag;
    }
}