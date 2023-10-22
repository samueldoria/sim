package io.sim;

import de.tudresden.sumo.cmd.Vehicle;

public class Car extends Vehicle implements Runnable{
    // Deve ser cliente de Company
    // Sumo deve retornar a quantidade de litros gastos e o valor de Fuel Tank deve ser alterado
    private double FuelTank;
    private boolean isAlive = false;
    private boolean abastecer ;

    public Car (){
        this.isAlive = true;
        this.FuelTank = 10;
        this.abastecer  = false;
        run();
    }
    
    public void run() {
        // Processos iniciais...
        while (isAlive){
            try {
                if (FuelTank <= 3) { // Verificando a quantidade de combustível
                    abastecer = true; // Precisa abastecer
                }
                //System.out.println("Thread botpayment");
                Thread.sleep(1000);
            } catch (Exception e) {
                // TODO: handle exception
            }
    }}

    public void atualizaCombustivel(double km, double consumo) {
        double litrosConsumidos = km*consumo;
        this.FuelTank -= litrosConsumidos;
    }

    public void infoTanqueAbastecido(double gasolina) {
        this.FuelTank = gasolina;
        this.abastecer = false;
    }

    public double getFuelTank() {
        return FuelTank;
    }

    public boolean getAbastecer() {
        return abastecer; // Sinal de que o carro foi abastecido
    }

    public void adicinaGasolina(double gasolina) {
        this.FuelTank += gasolina;
    }

    public void paraCarro() {

    }

    public void liberaCarro(){

    }
}