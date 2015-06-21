package com.markario.nn.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by markzepeda on 6/20/15.
 */
public abstract class NeuralNetwork<T> {
    private final ArrayList<NeuronLayer<T>> layers;
    protected final NeuralNetworkConfig<T> config;

    public NeuralNetwork(NeuralNetworkConfig<T> config){
        this.config = config;

        layers = new ArrayList<>(config.numHiddenLayers+1);
        if(config.numHiddenLayers > 0){
            layers.add(new NeuronLayer<>(config.numNeuronsPerHiddenLayer, config.numInputs, config.weightFactory));
            for(int i = 0; i < config.numHiddenLayers - 1; i++){
                layers.add(new NeuronLayer<>(config.numNeuronsPerHiddenLayer, config.numNeuronsPerHiddenLayer, config.weightFactory));
            }
            layers.add(new NeuronLayer<>(config.numOutputs, config.numNeuronsPerHiddenLayer, config.weightFactory));
        }else{
            layers.add(new NeuronLayer<>(config.numOutputs, config.numInputs, config.weightFactory));
        }
    }

    public void setWeights(ArrayList<T> newWeights){
        int weightIndex = 0;
        List<Neuron<T>> neurons = getNeurons();
        for(int i = 0; i < neurons.size() && weightIndex < newWeights.size(); i++){
            List<T> neuronWeights = neurons.get(i).getWeights();
            for(int j = 0; j <neuronWeights.size(); j++){
                neuronWeights.set(j, newWeights.get(weightIndex));
                weightIndex++;
            }
        }
    }

    public List<T> update(List<T> inputs){
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

    public List<T> calcNeuronLayerOutputs(List<Neuron<T>> neurons, List<T> inputs){
        List<T> outputs = new ArrayList<>(neurons.size());

        for (Neuron<T> neuron : neurons) {
            T output = calcNeuronOutput(neuron, inputs);
            outputs.add(config.weightFilter.apply(output, config.activationResponse));
        }

        return outputs;
    }

    public T calcNeuronOutput(Neuron<T> neuron, List<T> inputs){
        NeuralNetworkConfig<T> config = getConfig();

        T output = getZeroedWeight();

        List<T> weights = neuron.getWeights();
        T copyHolder = getZeroedWeight();
        for (int k = 0; k < inputs.size(); k++) {
            //Copies a neuron weight into a holder value which is multiplied by the corresponding input weight and added into the output.
            addWeights(output, multiplyWeight(copyWeight(copyHolder, weights.get(k)), inputs.get(k)));
        }
        //Copies the last weight (aka bias/threshold) and multiplies it by the bias weight.
        addWeights(output, multiplyWeight(copyWeight(copyHolder, weights.get(weights.size() - 1)), config.bias));
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

    public abstract T getZeroedWeight();
    public abstract T addWeights(T weight1, T weight2);
    public abstract T copyWeight(T into, T from);
    public abstract T multiplyWeight(T weight1, T weight2);

    public NeuralNetworkConfig<T> getConfig() {
        return config;
    }

    public int getNumWeights(){
        return weights().mapToInt(value -> 1).sum();
    }

    public List<T> getWeights(){
        return weights().collect(Collectors.toList());
    }

    public List<Neuron<T>> getNeurons(){
        return neurons().collect(Collectors.toList());
    }

    public List<NeuronLayer<T>> getLayers(){
        return layers;
    }

    public Stream<NeuronLayer<T>> layers(){
        return layers(this);
    }

    public Stream<Neuron<T>> neurons(){
        return neurons(this);
    }

    public Stream<T> weights(){
        return weights(this);
    }

    public static <T> Stream<NeuronLayer<T>> layers(NeuralNetwork<T> neuralNetwork){
        return neuralNetwork.layers.stream();
    }

    public static <T> Stream<Neuron<T>> neurons(NeuralNetwork<T> neuralNetwork){
        return layers(neuralNetwork).flatMap(NeuronLayer::neurons);
    }

    public static <T> Stream<T> weights(NeuralNetwork<T> neuralNetwork){
        return neurons(neuralNetwork).flatMap(Neuron::weights);
    }
}