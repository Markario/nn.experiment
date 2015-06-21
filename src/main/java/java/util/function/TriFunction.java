package java.util.function;

import java.util.List;

/**
 * Created by markzepeda on 6/21/15.
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R>{
    R apply(T t, U u, V v);
}
