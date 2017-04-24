package com.test.dtnprotocol ; 

/* File name : DTNInterface.java 
	interface que tem de implementar class superior do 
*/
//import java.lang.*;
// Any number of import statements

public interface DTNInterface {
   // Any number of final, static fields
   // Any number of abstract method declarations\

	/*
		pedido para armazenar os dados que passam por este host, 
		tem que verificar sé os dados (alguma parte do filme) já esta armazenada
		e sé esta imformação tiver em base dados, acrecentar um distinatario 
		ao lista dos distinatarios 
	*/
	public void storeData (byte [] data, DTNHost dtnHost);

	/* 
		vem algum pedido dos dados armazenados neste host
		devolve dados se tiver, ou null se nao tem dados pretendidos 
		os dados podem ser pedido duma parte do filme, ou a play-List  

	*/
	public byte[] getedRequest (byte [] data);


	/*
		quando recebida a resposta ao um pedido deste host, DTN protocol chama 
		esta função, para avizar a aplicação que resposta já chegou.
		Cada pedido tem um ID, devolvido por DTN.sendRequest() metodo na altura 
		em que host efectuou o pedido 
	*/

	public void getedResponse (byte [] data  , int idREquest ); 

}