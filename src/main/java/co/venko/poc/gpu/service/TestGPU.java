/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.venko.poc.gpu.service;

import co.venko.poc.gpu.service.util.Utilidades;
import java.io.IOException;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

/**
 *
 * @author Leonardo Machado Jaimes
 */
public class TestGPU {

//    public static String PATH_PTX = "/opt/venko/gpu/untitled.cu";
//    public static String PATH_PTX = "/opt/venko/gpu/vec_add.cu";
//    public static String PATH_PTX = "/opt/venko/gpu/test-venko.cu";
    public static String PATH_PTX = "gpu/test-venko.cu";

    static {
        JCudaDriver.setExceptionsEnabled(true);
        JCudaDriver.cuInit(0);
        CUdevice udevice = new CUdevice();
        JCudaDriver.cuDeviceGet(udevice, 0);
        CUcontext ucontext = new CUcontext();
        JCudaDriver.cuCtxCreate(ucontext, 0, udevice);
    }

    public static double excecute(double a, double b) throws IOException {

        //Aquí se carga el archivo ptx compilado con el proceso de suma
        CUmodule umodule = new CUmodule();
        JCudaDriver.cuModuleLoad(umodule, Utilidades.preparePtxFile(PATH_PTX));
        //Aquí se carga la función sumar
        CUfunction ufunction = new CUfunction();
        JCudaDriver.cuModuleGetFunction(ufunction, umodule, "sumar");

        int numElements = 1;

        // Aquí se asigna los valores de entrada de la suma
        double hostInputA[] = {a};
        double hostInputB[] = {b};

        // Aquí se asigna los datos de entrada al dispositivo (gpu) y le asigna memoria
        CUdeviceptr deviceInputA = new CUdeviceptr();
        JCudaDriver.cuMemAlloc(deviceInputA, numElements * Sizeof.DOUBLE);
        JCudaDriver.cuMemcpyHtoD(deviceInputA, Pointer.to(hostInputA), numElements * Sizeof.DOUBLE);
        CUdeviceptr deviceInputB = new CUdeviceptr();
        JCudaDriver.cuMemAlloc(deviceInputB, numElements * Sizeof.DOUBLE);
        JCudaDriver.cuMemcpyHtoD(deviceInputB, Pointer.to(hostInputB), numElements * Sizeof.DOUBLE);

        // Aquí se asigna memoria al dispositivo de salida
        CUdeviceptr deviceOutput = new CUdeviceptr();
        JCudaDriver.cuMemAlloc(deviceOutput, numElements * Sizeof.DOUBLE);

        // Aqui se asigna los datos a los atributos de la función sumar
        Pointer kernelParameters = Pointer.to(
                Pointer.to(deviceInputA),
                Pointer.to(deviceInputB),
                Pointer.to(deviceOutput)
        );

        // Aquí de ejecuta el llamado a la función sumar
       // int blockSize = 256;
        int blockSize = 1;
        int gridSize = (int) Math.ceil((double) numElements / blockSize);
        JCudaDriver.cuLaunchKernel(ufunction,
                gridSize, 1, 1, 
                blockSize, 1, 1,
                0, null, // Shared memory size and stream
                kernelParameters, null // parámetros de la función
        );
        JCudaDriver.cuCtxSynchronize();

        // Aquí se asigna la respuesta al array de salida
        double hostOutput[] = new double[numElements];
        JCudaDriver.cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput, numElements * Sizeof.DOUBLE);

        // Libera memoria.
        JCudaDriver.cuMemFree(deviceInputA);
        JCudaDriver.cuMemFree(deviceInputB);
        JCudaDriver.cuMemFree(deviceOutput);
        return hostOutput[0];
    }
}
