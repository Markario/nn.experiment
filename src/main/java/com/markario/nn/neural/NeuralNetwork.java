package com.markario.nn.neural;

import com.markario.nn.neural.weights.WeightHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by markzepeda on 6/20/15.
 */
public final class NeuralNetwork<T> {
    private final ArrayList<NeuronLayer<T>> layers;
    protected final NeuralNetworkConfig<T> config;
    private final WeightHandler<T> weightHandler;
    private final Supplier<T> weightFactory;
    /**
     * Create a new NeuralNetwork based on the provided Config.
     * @param config
     */
    public NeuralNetwork(NeuralNetworkConfig<T> config){
        this.config = config;
        this.weightHandler = config.weightHandler;
        this.weightFactory = weightHandler::getIdentityWeight;

        layers = new ArrayList<>(config.numHiddenLayers+1);
        if(config.numHiddenLayers > 0){
            layers.add(new NeuronLayer<>(config.numNeuronsPerHiddenLayer, config.numInputs, weightFactory));
            for(int i = 0; i < config.numHiddenLayers - 1; i++){
                layers.add(new NeuronLayer<>(config.numNeuronsPerHiddenLayer, config.numNeuronsPerHiddenLayer, weightFactory));
            }
            layers.add(new NeuronLayer<>(config.numOutputs, config.numNeuronsPerHiddenLayer, weightFactory));
        }else{
            layers.add(new NeuronLayer<>(config.numOutputs, config.numInputs, weightFactory));
        }
    }

    public static int calculateNumWeights(NeuralNetworkConfig<?> config){
        int numWeights = 0;
        if(config.numHiddenLayers > 0){
            numWeights += config.numNeuronsPerHiddenLayer * config.numInputs;
            for(int i = 0; i < config.numHiddenLayers - 1; i++){
                numWeights += config.numNeuronsPerHiddenLayer * config.numNeuronsPerHiddenLayer;
            }
            numWeights += config.numOutputs * config.numNeuronsPerHiddenLayer;
        }else{
            numWeights += config.numOutputs * config.numInputs;
        }
        return numWeights;
    }

    public final void setWeights(List<T> newWeights){
        int weightIndex = 0;
        List<Neuron<T>> neurons = getNeurons();
        for(int i = 0; i < neurons.size() && weightIndex < newWeights.size(); i++){
            List<T> neuronWeights = neurons.get(i).getWeights();
            for(int j = 0; j <neuronWeights.size(); j++){
                neuronWeights.set(j, weightHandler.copyIntoWeight(neuronWeights.get(j), newWeights.get(weightIndex)));
                weightIndex++;
            }
        }
    }

    public final List<T> update(List<T> inputs){
        if(inputs == null){
            throw new IllegalArgumentException("Inputs are null");
        }
        if(inputs.size() != config.numInputs){
            throw new IllegalArgumentException(String.format("Inputs size is incorrect. expected: %d observed: %d", inputs.size(), config.numInputs));
        }

        List<T> outputs = new ArrayList<>();

        for(int i = 0; i < config.numHiddenLayers + 1; i++){
            if(i > 0){
                inputs = outputs;
                //Clear the outputs from the last layer as they are now the inputs.
                outputs.clear();
            }

            if( inputs.size() != layers.get(i).getNumInputs()){
                throw new IllegalStateException("Inputs(or Outputs from last layer) do not match inputs for this layer");
            }

            outputs = calcNeuronLayerOutputs(layers.get(i).getNeurons(), inputs);
        }

        if(outputs.size() != config.numOutputs){
            throw new IllegalStateException(String.format("Outputs size is incorrect. expected: %d observed: %d", outputs.size(), config.numOutputs));
        }

        return outputs;
    }

    /**
     * Calculate a list of outputs from a set of Neurons given a list of inputs.
     * @param neurons
     * @param inputs
     * @return
     */
    private List<T> calcNeuronLayerOutputs(List<Neuron<T>> neurons, List<T> inputs){
        List<T> outputs = new ArrayList<>(neurons.size());

        for (Neuron<T> neuron : neurons) {
            T output = calcNeuronOutput(neuron, inputs);
            outputs.add(weightHandler.sigmoid(output, config.activationResponse));
        }

        return outputs;
    }

    /**
     * Calculate the output for a Neuron given a list of inputs.
     * @param neuron
     * @param inputs
     * @return
     */
    private T calcNeuronOutput(Neuron<T> neuron, List<T> inputs){
        NeuralNetworkConfig<T> config = getConfig();

        T output = weightHandler.getZeroedWeight();

        List<T> weights = neuron.getWeights();
        T copyHolder = weightHandler.getZeroedWeight();
        for (int k = 0; k < inputs.size(); k++) {
            //Copies a neuron weight into a holder value which is multiplied by the corresponding input weight and added into the output.
            weightHandler.addIntoWeight(output, weightHandler.multiplyIntoWeight(weightHandler.copyIntoWeight(copyHolder, weights.get(k)), inputs.get(k)));
        }
        //Copies the last weight (aka bias/threshold) and multiplies it by the bias weight.
        weightHandler.addIntoWeight(output, weightHandler.multiplyIntoWeight(weightHandler.copyIntoWeight(copyHolder, weights.get(weights.size() - 1)), config.bias));
        return output;
    }

//    @Override
//    public T calcNeuronOutput(Neuron<T> neuron, List<T> inputs) {
//        NeuralNetworkConfig<T> config = getConfig();
//
//        T output = config.weightFactory.get().zero();
//
//        List<T> weights = neuron.getWeights();
//        T copyHolder = config.weightFactory.get().zero();
//        for (int k = 0; k < inputs.size(); k++) {
//            //Copies a neuron weight into a holder value which is multiplied by the corresponding input weight and added into the output.
//            output.add(copyHolder.copy(weights.get(k)).multiply(inputs.get(k)));
//        }
//        //Copies the last weight (aka bias/threshold) and multiplies it by the bias weight.
//        output.add(copyHolder.copy(weights.get(weights.size() - 1)).multiply(config.bias));
//        return output;
//    }

    protected NeuralNetworkConfig<T> getConfig() {
        return config;
    }

    public int getNumWeights(){
        return weights().mapToInt(value -> 1).sum();
    }

    public List<T> getWeights(){
        return weights().collect(Collectors.toList());
    }

    protected List<Neuron<T>> getNeurons(){
        return neurons().collect(Collectors.toList());
    }

    protected List<NeuronLayer<T>> getLayers(){
        return layers;
    }

    protected Stream<NeuronLayer<T>> layers(){
        return layers(this);
    }

    protected Stream<Neuron<T>> neurons(){
        return neurons(this);
    }

    public Stream<T> weights(){
        return weights(this);
    }

    protected static <T> Stream<NeuronLayer<T>> layers(NeuralNetwork<T> neuralNetwork){
        return neuralNetwork.layers.stream();
    }

    protected static <T> Stream<Neuron<T>> neurons(NeuralNetwork<T> neuralNetwork){
        return layers(neuralNetwork).flatMap(NeuronLayer::neurons);
    }

    protected static <T> Stream<T> weights(NeuralNetwork<T> neuralNetwork){
        return neurons(neuralNetwork).flatMap(Neuron::weights);
    }
}