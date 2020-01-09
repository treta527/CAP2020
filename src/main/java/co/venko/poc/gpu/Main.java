/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.venko.poc.gpu;

import co.venko.poc.gpu.service.TestGPU;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jcuda.CudaException;

/**
 *
 * @author Leonardo Machado Jaimes
 */
public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            double salida = TestGPU.excecute(52, 8.6);
            logger.log(Level.INFO, "Resultado suma: {0}", salida);
        } catch (IOException | CudaException ex) {
            logger.log(Level.SEVERE, "Error en calculo de suma - {0}", ex.getMessage());
        }
    }
}
