package com.uminho.pti.smartcar.Local_DB;


public class DBI {
    private static final String SERVER = "http://ec2-52-19-117-80.eu-west-1.compute.amazonaws.com/";

    public static final String LOGIN = SERVER.concat(""); //doing

    public static final String USER = SERVER.concat("user"); /** /email dados de um unico utilizador (GET) **/

    public static final String ROUTE = SERVER.concat("route");/** /email todas as rotas de um utilizador (GET)
                                                                * criar a rota (POST) mandar JSON e receber id
                                                                * terminar a rota (PUT) mandar JSON **/

    public static final String EVENT = SERVER.concat("event"); /** TODO : /email todos os eventos do utilizador (GET)
                                                                * /route/route_id
                                                                *  adicionar um evento (POST) mandar JSON com type (brake,traffic)**/

    public static final String POSITION= SERVER.concat("position"); /** TODO: /id posições da rota id (GET)
                                                                    *  insere posição (POST) mandar JSON com id da rota
                                                                    **/

    public static final String VEICULOS = SERVER.concat("vehicle"); /** TODO: /email lista de todo os veiculos de user (GET)
                                                                    **/

    //TODO: check if event comes from neighbour
    //TODO: encriptação PDKF(AES256) na password
    //TODO: esconder dados do user
    //TODO: fazer a cena dos veículos
    //TODO: apresentar dados

    /**
    tipo -> x/event/'event_type'
    utilizador -> x/event/'user_email'
    tipo e utilizador -> x/event/'event_type'/'user_email'
    rota -> x/event/route/'route_id'
    tipo e rota -> x/event/'event_type'/route/'route_id'**/

 }

