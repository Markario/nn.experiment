package com.markario.nn.neural.weights;

/**
 * Created by markzepeda on 6/21/15.
 */
public interface GenericWeight<T extends GenericWeight, V> extends Comparable<T> {
    /**
     * Initialize the Weight to a "zero" value and return self.
     * @return Self
     */
    T zero();

    /**
     * Initialize the Weight to a "one" or identity value and return self. Used for bias in calculations.
     * @return Self
     */
    T identity();

    /**
     * Negate the sign of this Weight and return Self.
     * @return Self
     */
    T negate();

    /**
     * Initialize weight to a random value and return self.
     * @return Self
     */
    T random();

    /**
     * Add otherWeight into self;
     * @param otherWeight
     * @return Self
     */
    T add(T otherWeight);

    /**
     * Multiply otherWeight into Self
     * @param otherWeight
     * @return Self
     */
    T multiply(T otherWeight);

    /**
     * Copy otherWeight into Self.
     * @return Self
     */
    T copy(T otherWeight);

    /**
     * Return the underlying value of this weight.
     * @return
     */
    V value();
}