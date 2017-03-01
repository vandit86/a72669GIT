package com.uminho.pti.smartcar.Data;



public class Veiculo {

    private String matricula;
    private String marca;
    private String modelo;
    private long kms;
    private double consumo;

    public Veiculo(String matricula,String marca, String modelo, long kms, double consumo){
        this.matricula=matricula;
        this.marca=marca;
        this.modelo=modelo;
        this.kms= kms;
        this.consumo = consumo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public long getKms() {
        return kms;
    }

    public void setKms(long kms) {
        this.kms = kms;
    }

    public double getConsumo() {
        return consumo;
    }

    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }
}
