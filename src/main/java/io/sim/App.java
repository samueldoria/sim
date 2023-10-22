package io.sim;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {

        //EnvSimulator ev = new EnvSimulator();
        //ev.start();
        // Construtores
        AlphaBank alpha = new AlphaBank();
        Company companhia = new Company();

        // Iniciando threads
        alpha.run();
        companhia.run();
        //B:\UFLA\Matérias\Matérias_2023_02_FINAL\Automação Avançada\sim\src\main\java\io\sim\App.java
    }
}