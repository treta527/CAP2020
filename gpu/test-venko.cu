extern "C"

__global__ void sumar( double *a, double *b, double *sum){
    sum[0] = a[0] + b[0];
    printf ("Suma ejecutada desde gpu (%f + %f = %f  )", a[0], b[0],sum[0]);
}