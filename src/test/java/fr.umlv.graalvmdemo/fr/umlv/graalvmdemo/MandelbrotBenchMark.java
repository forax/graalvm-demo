package fr.umlv.graalvmdemo;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


@SuppressWarnings("static-method")
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class MandelbrotBenchMark {
  static class ComplexRef {
    final double re;
    final double im;
    
    ComplexRef(double re, double im) {
      this.re = re;
      this.im = im;
    }
    
    public double squareDistance() {
      return re * re + im * im;
    }
    
    public ComplexRef square() {
      return new ComplexRef(re * re - im * im, 2 * re * im);
    }
    
    public ComplexRef add(ComplexRef c) {
      return new ComplexRef(re + c.re, im + c.im);
    }
  }
  
  private static final int WIDTH = 64;
  private static final int HEIGHT = 64;
  private static final int MAX = 1000;
  
  @Benchmark
  public void mandelbrot_ref(Blackhole blackhole) {
    for (int row = 0; row < HEIGHT; row++) {
      for (int col = 0; col < WIDTH; col++) {
        ComplexRef complex = new ComplexRef((col - WIDTH / 2) * 4.0 / WIDTH, (row - HEIGHT / 2) * 4.0 / WIDTH);
        ComplexRef point = new ComplexRef(0.0, 0.0);

        int iteration = 0;
        while (point.squareDistance() < 4 && iteration < MAX) {
          point = point.square().add(complex);
          iteration++;
        }
        if (iteration < MAX) {
          blackhole.consume(iteration);
        } else {
          blackhole.consume(0);
        }
      }
    }
  }
  
  @Benchmark
  public void mandelbrot_primitive(Blackhole blackhole) {
    for (int row = 0; row < HEIGHT; row++) {
      for (int col = 0; col < WIDTH; col++) {
        double c_re = (col - WIDTH / 2) * 4.0 / WIDTH;
        double c_im = (row - HEIGHT / 2) * 4.0 / WIDTH;
        double x = 0.0;
        double y = 0.0;

        int iteration = 0;
        while (x * x + y * y < 4 && iteration < MAX) {
          double x_new = x * x - y * y + c_re;
          double y_new = 2 * x * y + c_im;
          x = x_new;
          y = y_new;
          iteration++;
        }
        if (iteration < MAX) {
          blackhole.consume(iteration);
        } else {
          blackhole.consume(0);
        }
      }
    }
  }
  
  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(MandelbrotBenchMark.class.getName())
        .build();
    new Runner(opt).run();
  }
}
