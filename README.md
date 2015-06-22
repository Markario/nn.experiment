# nn.experiment
General purpose feed-forward neural network.

Still buggy and doesn't save the results or reload previous results.

Based on the tutorials at http://www.ai-junkie.com/ann/evolved/nnt1.html

Example of use:

  
    public static void main(String[] args){
        ExperimentConfig config = FileUtil.readTFromTextFile(new File("config.json"), ExperimentConfig.class);
        GenericWeightExperiment<DoubleWeight> experiment = new GenericWeightExperiment<>(config, () -> new DoubleWeight().zero());
        
        List<DoubleWeight> inputs = new ArrayList<>(config.numInputs);
        DoubleWeight weight = new DoubleWeight();
        inputs.add(weight);
        
        for(int gen = 0; gen < 10000; gen++){
            double input = 0.0d;
            while(input <= 2 * Math.PI){
                for(int i = 0; i < config.populationSize; i++){
                    double target = Math.sin(input);
                    weight.value = input;
                    List<DoubleWeight> outputs = experiment.update(i, inputs);
                    double output = outputs.get(0).value;
                    double fitness = 1 - Math.abs(target-output);
                    experiment.addFitness(i, fitness);
                }
                input += .01;
            }
            experiment.nextGeneration();
            Trace.v(String.format("Gen: %d, stats: %s",experiment.genetics.getGenerationNum(), experiment.genetics.getStats().toString()));
        }
    }
