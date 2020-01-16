# CAP2020
Uso de librería jcuda con un que llama una función de suma desde java.

Es importante que el equipo donde se harán las pruebas cuente con una tarjeta gráfica nvidia y tenga instalado los driver.

Comando para ver el uso de la trajeta gráfica
# nvidia-smi


Url de descarga del toolkit de nvidia: https://developer.nvidia.com/cuda-toolkit

Comando para compilar el archivo .cu
# nvcc -m64 -ptx [DIRECTORIO_PROYECTO]/gpu/test-venko.cu -o [DIRECTORIO_PROYECTO]/gpu/test-venko.ptx


