#include <assert.h>
#include <bits/c++config.h>
#include <cuda.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include "workshop.h"

#define GRAPH_SIZE 2000

#define EDGE_COST(graph, graph_size, a, b) graph[a * graph_size + b]
#define D(a, b) EDGE_COST(output, graph_size, a, b)

#define INF 0x1fffffff

#define THREADS_PER_BLOCK 256
#define BLOCKS MIN(32, (GRAPH_SIZE + THREADS_PER_BLOCK - 1) / THREADS_PER_BLOCK)

void generate_random_graph(int *output, int graph_size) {
  int i, j;

  srand(0xdadadada);

  for (i = 0; i < graph_size; i++) {
    for (j = 0; j < graph_size; j++) {
      if (i == j) {
        D(i, j) = 0;
      } else {
        int r;
        r = rand() % 40;
        if (r > 20) {
          r = INF;
        }

        D(i, j) = r;
      }
    }
  }
}

__global__ void floyd_gpu_compute(const int *graph, int graph_size, int *output) {
  int i, j, k, index, stride;
  for (k = 0; k < graph_size; k++) {
    index = blockIdx.x * blockDim.x + threadIdx.x;
    stride = blockDim.x * gridDim.x;
    for (i = index; i < graph_size; i += stride) {
      for (j = 0; j < graph_size; j++) {
        if (D(i, k) + D(k, j) < D(i, j)) {
          D(i, j) = D(i, k) + D(k, j);
        }
      }
    }
  }
}


void floyd_warshall_gpu(const int *graph, int graph_size, int *output) {
  int *dev_output;
  int size;

  size = sizeof(int) * graph_size * graph_size;
  HANDLE_ERROR( cudaMalloc(&dev_output, size) );

  cudaMemcpy(dev_output, graph, size, cudaMemcpyHostToDevice);

  floyd_gpu_compute<<<BLOCKS, THREADS_PER_BLOCK>>>(graph, graph_size, dev_output);

  cudaMemcpy(output, dev_output, size, cudaMemcpyDeviceToHost);
  cudaFree(dev_output);
}

void floyd_warshall_cpu(const int *graph, int graph_size, int *output) {
  int i, j, k;

  memcpy(output, graph, sizeof(int) * graph_size * graph_size);

  for (k = 0; k < graph_size; k++) {
    for (i = 0; i < graph_size; i++) {
      for (j = 0; j < graph_size; j++) {
        if (D(i, k) + D(k, j) < D(i, j)) {
          D(i, j) = D(i, k) + D(k, j);
        }
      }
    }
  }
}

int main(int argc, char **argv) {
#define TIMER_START() gettimeofday(&tv1, NULL)
#define TIMER_STOP()                                                           \
  gettimeofday(&tv2, NULL);                                                    \
  timersub(&tv2, &tv1, &tv);                                                   \
  time_delta = (float)tv.tv_sec + tv.tv_usec / 1000000.0

  struct timeval tv1, tv2, tv;
  float time_delta;

  int *graph, *output_cpu, *output_gpu;
  int size;

  size = sizeof(int) * GRAPH_SIZE * GRAPH_SIZE;

  graph = (int *)malloc(size);
  assert(graph);

  output_cpu = (int *)malloc(size);
  assert(output_cpu);
  memset(output_cpu, 0, size);

  output_gpu = (int *)malloc(size);
  assert(output_gpu);

  generate_random_graph(graph, GRAPH_SIZE);

  fprintf(stderr, "running on cpu...\n");
  TIMER_START();
  floyd_warshall_cpu(graph, GRAPH_SIZE, output_cpu);
  TIMER_STOP();
  fprintf(stderr, "%f secs\n", time_delta);

  fprintf(stderr, "running on gpu...\n");
  TIMER_START();
  floyd_warshall_gpu(graph, GRAPH_SIZE, output_gpu);
  TIMER_STOP();
  fprintf(stderr, "%f secs\n", time_delta);

  if (memcmp(output_cpu, output_gpu, size) != 0) {
    fprintf(stderr, "FAIL!\n");
  }

  /* uncomment for debug
  printf("CPU - GPU\n");
  int i;
  for (i = 0; i < GRAPH_SIZE; i++) {
    printf("%2d - %2d\n", output_cpu[i], output_gpu[i]);
  }
  */

  return 0;
}
