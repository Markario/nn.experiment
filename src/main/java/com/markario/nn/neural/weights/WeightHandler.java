package com.markario.nn.neural.weights;

/**
 * Created by markzepeda on 6/21/15.
 */
public interface WeightHandler<T> {
    /**
     * Return a new Weight that is "zero"
     * @return
     */
    T getZeroedWeight();

    /**
     * Return a new Weight that is "one"
     * @return
     */
    T getIdentityWeight();

    /**
     * Return a new Weight with a random value
     * @return
     */
    T getRandomWeight();

    /**
     * Add Weight2 into Weight1 and return Weight1 if it is mutable otherwise a new Weight containing the result.
     * @param weight1
     * @param weight2
     * @return
     */
    T addIntoWeight(T weight1, T weight2);

    /**
     * Copy "from" into "into" and return "into" if it is mutable otherwise just return an immutable copy of "from".
     * @param into
     * @param from
     * @return
     */
    T copyIntoWeight(T into, T from);

    /**
     * Multiply Weight2 into Weight1 and return Weight1 if it is mutable otherwise a new Weight containing the result.
     * @param weight1
     * @param weight2
     * @return
     */
    T multiplyIntoWeight(T weight1, T weight2);

    /**
     * Mutate Weight by a signed percentage.
     * @param weight
     * @param percentage
     * @return
     */
    T mutateWeight(T weight, Double percentage);

    /**
     * Negate the sign of this Weight and return it.
     * @return
     */
    T negateWeight(T weight);

    /**
     * Apply sigmoid function to Weight.
     * @param weight
     * @param activationResponse
     * @return
     */
    T sigmoid(T weight, T activationResponse);
}
