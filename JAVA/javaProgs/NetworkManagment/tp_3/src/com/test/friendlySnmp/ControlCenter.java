/**
* file 		: ControlCenter.java
* autor 	: JFP  
* data 		: 02-02-2017
* 
* this class generates the refresh of the table
* 
*/


package com.test.friendlySnmp;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.lang.Thread;
import java.util.*; 


// TODO : most of this methods must be synchronized

public class ControlCenter {
	private int refresh_rate;  // in hz
	private volatile int number_lines;
	private volatile int number_digits;  // number of hexadecimal digits
	private volatile String key;
	private volatile boolean stop;
	private ArrayList<String> seeds_list;
	private byte[][] matrix;

	//constructor
	// we need to receive the handler 
	ControlCenter(int rtrefresh, String cc, int n, int d, List<String> seeds){
		this.refresh_rate = rtrefresh;
		this.key = cc;
		this.seeds_list = new ArrayList<>(seeds);
		if(n == 0){
			this.number_lines = this.seeds_list.size();  
		}else{
			this.number_lines = n;
		}
		if(d == 0){
			this.number_digits = this.seeds_list.get(0).length();  
		}else{
			this.number_digits = d;
		}
		this.matrix = createMatrix(seeds_list);
		this.stop = false;
	}

	public void setCC(String cc){
		this.key = cc;
	}

	public void startRefresh(){
		this.stop = false;
		new Thread(new TableGen(this)).start();
	}

	public void stopRefresh(){
		this.stop = true;
	}

	public int getRate(){
		return this.refresh_rate;
	}

	
	public void setTable(List<String> seeds){
		this.seeds_list = new ArrayList<String>(seeds);
	}

	public boolean canStop(){
		return this.stop;
	}

	// this function creates a bi-dimensional byte array
	// this byte array contains all bytes of the seeds list

	private byte[][] createMatrix(ArrayList<String> seeds){
		int n = this.number_lines;
		int d = this.number_digits;
		byte[][] matrix = new byte[n][d];
		int i = 0; 
		int j = 0;
		for(i = 0; i < n; i++){
			for(j = 0; j < d; j++){
				matrix[i][j] = (byte)seeds.get(i).charAt(j);
			}
		}
		return matrix;
	}
	

	public String[] refresh(){
		String[] str_array = new String[number_lines];
		long seed = System.nanoTime();
		Collections.shuffle(this.seeds_list, new Random(seed));
		int i = 0;
		for(String x : this.seeds_list){
			str_array[i++] = Long.toString(0xffffffffL & Integer.parseUnsignedInt(x, 16));
		}
		return str_array;
	}
	// just for testing	
	/*
	public static void main(String[] args) {
		String[] seeds = {"f169b5f1", "44338875", "bc89273a",
			"d54d9cfd", "ac52a62f", "bc5afd73", "0baeb380", "2329318e", "cddee9d1",
			"8c73ee0b", "57368f6c", "f4bb7a59", "26aab49e", "5b9db31d", "a7cec064"};
		List<String> seedsList = Arrays.asList(seeds);
		ControlCenter crt = new ControlCenter(2, "ola", 0, 0, seedsList);
		crt.startRefresh();
		while(true){
			;
		}
	}
	*/
}

class TableGen implements Runnable{
	ControlCenter ctrc;

	TableGen(ControlCenter ctr){
		this.ctrc = ctr;
	}

	public void run(){
		try{
			while(!ctrc.canStop()){
				String[] array = ctrc.refresh();
				int time = 1000 / ctrc.getRate();
				// here we must return the array through the Handler 
				for(String x : array)
					System.out.print(""+ x +", ");
				Thread.sleep(time);
			}
		}catch(InterruptedException e){;}
	}
}