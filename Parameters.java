/* Stores parameters for use across simulation */
/* Start with parameters in source, implement input file later */
/* A completely static class.  */

import java.util.*;
import java.io.*;

public class Parameters {
	// global parameters
	public static double day = 0;
	public static Virus urVirus = null;
	public static Phenotype urImmunity = null;

	// simulation parameters
	public static int burnin = 0; // days to wait before logging output
	public static int endDay = 5000; // number of days to simulate
	public static double deltaT = 0.1; // number of days to move forward in a single timestep
	public static int printStep = 10; // print to out.timeseries every week
	public static double tipSamplingRate = 0.0002; // in samples per deme per day
	public static int tipSamplesPerDeme = 1000;
	public static boolean tipSamplingProportional = true; // whether to sample proportional to prevalance
	public static double treeProportion = 0.1; // proportion of tips to use in tree reconstruction
	public static int diversitySamplingCount = 1000; // how many samples to draw to calculate diversity, Ne*tau, serial
														// interval
	public static int netauWindow = 100; // window in days to calculate Ne*tau
	public static boolean repeatSim = true; // repeat simulation until endDay is reached?
	public static boolean immunityReconstruction = false; // whether to print immunity reconstruction to out.immunity
	public static boolean memoryProfiling = false; // requires -javaagent:classmexer.jar to run
	public static double yearsFromMK = 1.0;
	public static boolean pcaSamples = false; // whether to rotate and flip virus tree
	public static boolean reducedOutput = false; // whether to output only out.summary and out.timeseries
	public static boolean detailedOutput = false; // whether to output out.hosts and out.viruses files enabling
													// checkpointing
	public static boolean restartFromCheckpoint = false; // whether to load population from out.hosts
	public static String outPath = "output/"; // path to dump output files.
	public static String outPrefix = "run-"; // suffix for output files.
	public static String inPath = "input/"; // path to dump output files.
	public static int fitnessSampleSize = 10000; // number of random hosts to sample for average infection risk

	// metapopulation parameters
	public static int demeCount = 3;
	public static String[] demeNames = { "north", "tropics", "south" }; // deme names
	public static int[] initialNs = { 1000000, 1000000, 1000000 }; // inital deme population sizes

	// host parameters
	public static double birthRate = 0.000091; // in births per individual per day, 1/30 years = 0.000091
	public static double deathRate = 0.000091; // in deaths per individual per day, 1/30 years = 0.000091
	public static boolean swapDemography = true; // whether to keep overall population size constant

	// epidemiological parameters
	public static int initialI = 10; // in individuals
	public static int initialDeme = 2; // index of deme where infection starts, 1..n
	public static double initialPrR = 0.5; // as proportion of population
	public static double beta = 0.36; // 0.3 // in contacts per individual per day
	public static double nu = 0.2; // 0.2 // in recoveries per individual per day
	public static double betweenDemePro = 0.0005; // relative to within-deme beta

	// transcendental immunity
	public static boolean transcendental = false; // whether to include a general recovered class
	public static double immunityLoss = 0.01; // in R->S per individual per day
	public static double initialPrT = 0.1; // initial faction in general recovered class

	// seasonal betas
	public static double[] demeBaselines = { 1, 1, 1 }; // baseline of seasonality
	public static double[] demeAmplitudes = { 0.1, 0, 0.1 }; // amplitude of seasonality
	public static double[] demeOffsets = { 0, 0, 0.5 }; // relative to the year

	// phenotype parameters
	public static String phenotypeSpace = "geometric"; // options include: "geometric", "geometric3d", "geometric10d"
	public static double muPhenotype = 0.005; // in mutations per individual per day
	public static boolean waning = false; // whether to allow waning of host immunity
	public static double waningRate = 0.01; // rate per day of a host removing a random phenotype from their immune
											// history

	// parameters specific to GeometricPhenotype
	public static double smithConversion = 0.1; // multiplier to distance to give cross-immunity
	public static double homologousImmunity = 0.05; // immunity raised to antigenically identical virus
	public static double initialTraitA = -6; // value in dimension 1 for initial host immunity
	public static double meanStep = 0.3; // mean mutation size for non-epitopes
	public static double sdStep = 0.3; // standard deviation of mutation size for non-epitopes
	public static boolean mut2D = false; // whether to mutate in a full 360 degree arc
	public static boolean fixedStep = false; // whether to fix mutation step size

	// parameters specific to GeometricSeqPhenotype
	public static String startingSequence = "AGAGTCTAGTCC"; // default starting sequence
	public static int[] epitopeSites = {}; // epitope sites of virus (valid inputs are between 1 and
	                                       // startingSequence.length() / 3)
	public static int[] epitopeSitesLow = {};
	public static int[] epitopeSitesHigh = {};
	public static double proportionHighSites = 0.2; // proportion of epitope sites that are high in mutation step size distribution
	public static boolean predefinedVectors = true;
	public static double meanStepEpitope = 0.3; // mean mutation size for epitopes
	public static double sdStepEpitope = 0.3; // standard deviation of mutation size for epitopes
	public static double meanStepEpitopeLow = 0.3; // mean mutation size for epitopes
	public static double sdStepEpitopeLow = 0.3; // standard deviation of mutation size for epitopes
	public static double meanStepEpitopeHigh = 0.3; // mean mutation size for epitopes
	public static double sdStepEpitopeHigh = 0.3; // standard deviation of mutation size for epitopes
	public static double transitionTransversionRatio = 5.0; // transition/transversion rate ratio, k
	public static String DMSFile = null; // name of DMS csv file: must have 21 columns (site number and amino acid
	                                     // preferences ordered alphabetically) and rows must equal the number of
	                                     // amino acid sites in the virus sequence)
	public static double nonEpitopeAcceptance = 1.0; // probability of accepting a non-epitope mutation
	public static double epitopeAcceptance = 1.0; // probability of accepting an epitope mutation

	// host immunity parameters
	public static boolean sampleHostImmunity = false; // whether to sample host immunity
	public static int printHostImmunityStep = 100; // print host immunity every printHostImmunity days
	public static int[] hostImmunitySamplesPerDeme = { 100, 100, 100 }; // number of hosts to sample for immunity

	// measured in years, starting at burnin
	public static double getDate() {
		return (day - (double) burnin) / 365.0;
	}

	/**
	 * @return True if current day is an integer, false otherwise.
	 */
	public static boolean dayIsInteger() {
		return Math.ceil(day) - Math.floor(day) == 0;
	}

	//
	public static double getSeasonality(int index) {
		double baseline = demeBaselines[index];
		double amplitude = demeAmplitudes[index];
		double offset = demeOffsets[index];
		return baseline + amplitude * Math.cos(2 * Math.PI * getDate() + 2 * Math.PI * offset);
	}

	// initialize
	public static void initialize() {
		urVirus = new Virus();
		urImmunity = PhenotypeFactory.makeHostPhenotype();
	}

	// load parameters.yml
	public static void load() {

		try {

			org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
			Map map;
			InputStream input = new FileInputStream(new File("parameters.yml"));
			map = (Map) yaml.load(input);
			input.close();

			System.out.println("Loading parameters from parameters.yml");
			if (map.get("outPath") != null) {
				outPath = (String) map.get("outPath");
			}
			if (map.get("outPrefix") != null) {
				outPrefix = (String) map.get("outPrefix");
			}
			if (map.get("burnin") != null) {
				burnin = (int) map.get("burnin");
			}
			if (map.get("endDay") != null) {
				endDay = (int) map.get("endDay");
			}
			if (map.get("deltaT") != null) {
				deltaT = (double) map.get("deltaT");
			}
			if (map.get("printStep") != null) {
				printStep = (int) map.get("printStep");
			}
			if (map.get("tipSamplingRate") != null) {
				tipSamplingRate = (double) map.get("tipSamplingRate");
			}
			if (map.get("tipSamplesPerDeme") != null) {
				tipSamplesPerDeme = (int) map.get("tipSamplesPerDeme");
			}
			if (map.get("tipSamplingProportional") != null) {
				tipSamplingProportional = (boolean) map.get("tipSamplingProportional");
			}
			if (map.get("treeProportion") != null) {
				treeProportion = (double) map.get("treeProportion");
			}
			if (map.get("diversitySamplingCount") != null) {
				diversitySamplingCount = (int) map.get("diversitySamplingCount");
			}
			if (map.get("netauWindow") != null) {
				netauWindow = (int) map.get("netauWindow");
			}
			if (map.get("repeatSim") != null) {
				repeatSim = (boolean) map.get("repeatSim");
			}
			if (map.get("immunityReconstruction") != null) {
				immunityReconstruction = (boolean) map.get("immunityReconstruction");
			}
			if (map.get("memoryProfiling") != null) {
				memoryProfiling = (boolean) map.get("memoryProfiling");
			}
			if (map.get("yearsFromMK") != null) {
				yearsFromMK = (double) map.get("yearsFromMK");
			}
			if (map.get("pcaSamples") != null) {
				pcaSamples = (boolean) map.get("pcaSamples");
			}
			if (map.get("reducedOutput") != null) {
				reducedOutput = (boolean) map.get("reducedOutput");
			}
			if (map.get("detailedOutput") != null) {
				detailedOutput = (boolean) map.get("detailedOutput");
			}
			if (map.get("restartFromCheckpoint") != null) {
				restartFromCheckpoint = (boolean) map.get("restartFromCheckpoint");
			}
			if (map.get("demeCount") != null) {
				demeCount = (int) map.get("demeCount");
			}
			if (map.get("demeNames") != null) {
				demeNames = toStringArray((List<String>) map.get("demeNames"));
			}
			if (map.get("initialNs") != null) {
				initialNs = toIntArray((List<Integer>) map.get("initialNs"));
			}
			if (map.get("birthRate") != null) {
				birthRate = (double) map.get("birthRate");
			}
			if (map.get("deathRate") != null) {
				deathRate = (double) map.get("deathRate");
			}
			if (map.get("swapDemography") != null) {
				swapDemography = (boolean) map.get("swapDemography");
			}
			if (map.get("initialI") != null) {
				initialI = (int) map.get("initialI");
			}
			if (map.get("initialDeme") != null) {
				initialDeme = (int) map.get("initialDeme");
			}
			if (map.get("initialPrR") != null) {
				initialPrR = (double) map.get("initialPrR");
			}
			if (map.get("beta") != null) {
				beta = (double) map.get("beta");
			}
			if (map.get("nu") != null) {
				nu = (double) map.get("nu");
			}
			if (map.get("betweenDemePro") != null) {
				betweenDemePro = (double) map.get("betweenDemePro");
			}
			if (map.get("transcendental") != null) {
				transcendental = (boolean) map.get("transcendental");
			}
			if (map.get("immunityLoss") != null) {
				immunityLoss = (double) map.get("immunityLoss");
			}
			if (map.get("initialPrT") != null) {
				initialPrT = (double) map.get("initialPrT");
			}
			if (map.get("demeBaselines") != null) {
				demeBaselines = toDoubleArray((List<Double>) map.get("demeBaselines"));
			}
			if (map.get("demeAmplitudes") != null) {
				demeAmplitudes = toDoubleArray((List<Double>) map.get("demeAmplitudes"));
			}
			if (map.get("demeOffsets") != null) {
				demeOffsets = toDoubleArray((List<Double>) map.get("demeOffsets"));
			}
			if (map.get("phenotypeSpace") != null) {
				phenotypeSpace = (String) map.get("phenotypeSpace");
			}
			if (map.get("muPhenotype") != null) {
				muPhenotype = (double) map.get("muPhenotype");
			}
			if (map.get("waning") != null) {
				waning = (boolean) map.get("waning");
			}
			if (map.get("waningRate") != null) {
				waningRate = (double) map.get("waningRate");
			}
			if (map.get("smithConversion") != null) {
				smithConversion = (double) map.get("smithConversion");
			}
			if (map.get("homologousImmunity") != null) {
				homologousImmunity = (double) map.get("homologousImmunity");
			}
			if (map.get("initialTraitA") != null) {
				initialTraitA = (double) map.get("initialTraitA");
			}
			if (map.get("meanStep") != null) {
				meanStep = (double) map.get("meanStep");
			}
			if (map.get("sdStep") != null) {
				sdStep = (double) map.get("sdStep");
			}
			if (map.get("mut2D") != null) {
				mut2D = (boolean) map.get("mut2D");
			}
			if (map.get("fixedStep") != null) {
				fixedStep = (boolean) map.get("fixedStep");
			}
			if (map.get("startingSequence") != null) {
				String startingSequenceFile = ((String) map.get("startingSequence"));
				startingSequence = readStartingSequenceFile(inPath + startingSequenceFile);

				// Check that startingSequence is not an empty String and is a multiple of 3.
				if (phenotypeSpace.equals("geometricSeq")) {
					if (startingSequence.length() == 0 || startingSequence.length() % 3 != 0) {
						System.out.println("startingSequence length should be any multiple of 3, except for 0.");
						throw new IOException();
					}

					// Check startingSequence for stop signals.
					for (int i = 0; i < startingSequence.length(); i += 3) {
						String triplet = startingSequence.substring(i, i + 3);
						String translatedAminoAcid = Biology.CodonMap.CODONS.getAminoAcid(triplet);

						if (translatedAminoAcid.equals("STOP") && ( i != startingSequence.length() - 3)) {
							System.out.println("There should not be a stop codon at site " + (i / 3));
							throw new IOException();
						}
					}
				}
			}
			if (map.get("epitopeSites") != null) {
				String epitopeSitesFile = ((String) map.get("epitopeSites"));
				epitopeSites = readEpitopeSitesFile(inPath + epitopeSitesFile);
			}
			// Here, define low and high sites based on a user-defined parameter called "proportionHighSites"
			if (map.get("proportionHighSites") != null) {
				proportionHighSites = (double) map.get("proportionHighSites");
				int numHighSites = (int) Math.round(epitopeSites.length * proportionHighSites);
				int numLowSites = epitopeSites.length - numHighSites;
				epitopeSitesHigh = new int[numHighSites];
				epitopeSitesLow = new int[numLowSites];
				int highIdx = 0;
				int lowIdx = 0;
				for (int i = 0; i < epitopeSites.length; i++) {
					if (i < epitopeSites.length * proportionHighSites) {
						epitopeSitesHigh[highIdx] = epitopeSites[i];
						highIdx +=1;
					} else {
						epitopeSitesLow[lowIdx] = epitopeSites[i];
						lowIdx +=1;
					}
				}
			}
			if (map.get("epitopeSitesLow") != null) {
				String epitopeSitesFile = ((String) map.get("epitopeSitesLow"));
				writeEpitopeSitesFile(inPath + epitopeSitesFile, epitopeSitesLow);
			}
			if (map.get("epitopeSitesHigh") != null) {
				String epitopeSitesFile = ((String) map.get("epitopeSitesHigh"));
				writeEpitopeSitesFile(inPath + epitopeSitesFile, epitopeSitesHigh);
			}
			if (map.get("predefinedVectors") != null) {
				predefinedVectors = (boolean) map.get("predefinedVectors");
			}
			if (map.get("meanStepEpitope") != null) {
				meanStepEpitope = (double) map.get("meanStepEpitope");
			}
			if (map.get("sdStepEpitope") != null) {
				sdStepEpitope = (double) map.get("sdStepEpitope");
			}
			if (map.get("meanStepEpitopeLow") != null) {
				meanStepEpitopeLow = (double) map.get("meanStepEpitopeLow");
			}
			if (map.get("sdStepEpitopeLow") != null) {
				sdStepEpitopeLow = (double) map.get("sdStepEpitopeLow");
			}
			if (map.get("meanStepEpitopeHigh") != null) {
				meanStepEpitopeHigh = (double) map.get("meanStepEpitopeHigh");
			}
			if (map.get("sdStepEpitopeHigh") != null) {
				sdStepEpitopeHigh = (double) map.get("sdStepEpitopeHigh");
			}
			if (map.get("transitionTransversionRatio") != null) {
				transitionTransversionRatio = (double) map.get("transitionTransversionRatio");
			}
			if (map.get("nonEpitopeAcceptance") != null){
				nonEpitopeAcceptance = (double) map.get("nonEpitopeAcceptance");
			}
			if (map.get("epitopeAcceptance") != null){
				epitopeAcceptance = (double) map.get("epitopeAcceptance");
			}
			if (map.get("DMSFile") != null) {
				DMSFile = (String) map.get("DMSFile");

				// Check if the number of rows in DMSFile is equal to the length of the protein
				// sequence.
				int numberOfSites = startingSequence.length() / 3;
				int dmsDataLineCount = 0;

				Scanner DMSData = new Scanner(new File(Parameters.DMSFile));
				DMSData.nextLine(); // ignore the header
				while (DMSData.hasNextLine()) {
					dmsDataLineCount++;
					DMSData.nextLine();
				}

				if (dmsDataLineCount != numberOfSites) {
					System.out.println(
							"The DMS data provided does not have the same number of rows as the length of the protein sequence\n"
									+
									"Expected # of Sites: " + numberOfSites + "\nActual # of Sites in " + DMSFile + ": "
									+ dmsDataLineCount);
					throw new IOException();
				}
			}
      // Parameters for host sampling and fitness calculations
			if (map.get("fitnessSampleSize") != null) {
				fitnessSampleSize = (int) map.get("fitnessSampleSize");
			}
			if (map.get("sampleHostImmunity") != null) {
				sampleHostImmunity = (boolean) map.get("sampleHostImmunity");
			}
			if (map.get("printHostImmunityStep") != null) {
				printHostImmunityStep = (int) map.get("printHostImmunityStep");
			}
			if (map.get("hostImmunitySamplesPerDeme") != null) {
				hostImmunitySamplesPerDeme = toIntArray((List<Integer>) map.get("hostImmunitySamplesPerDeme"));
			}

		} catch (IOException e) {
			System.out.println("Cannot load parameters.yml, using defaults");
		}
	}

	// Returns String of the starting sequence given the fasta file
	private static String readStartingSequenceFile(String startingSequenceFile) throws FileNotFoundException {
		// Parse the fasta file to get a sequence of nucleotides
		StringBuilder startingSequenceSB = new StringBuilder();
		Scanner startingSequenceScanner = new Scanner(new File(startingSequenceFile));
		startingSequenceScanner.nextLine(); // read header >

		while (startingSequenceScanner.hasNextLine()) {
			String startingSequenceLine = startingSequenceScanner.nextLine();
			// Only read one sequence from the fasta file
			if (startingSequenceLine.indexOf(0) == '>') {
				System.out.println("Only one starting sequence can be simulated at a time.");
				break;
			}
			startingSequenceSB.append(startingSequenceLine);
		}

		return startingSequenceSB.toString().toUpperCase();
	}

	// Returns int[] of the epitope sites given the txt file
	// of site numbers seperated by commas
	private static int[] readEpitopeSitesFile(String epitopeSitesFile) throws FileNotFoundException {
		Scanner epitopeSitesScanner = new Scanner(new File(epitopeSitesFile));
		// Q for @thienktran: What if there are multiple lines? Should I make a note in the README to not have any new line characters in the file?
		String epitopeSitesLine = epitopeSitesScanner.nextLine();

		// Split epitope sites by ","
		String[] epitopeSitesString = epitopeSitesLine.split(",");

		// Convert String[] to int[]
		int[] epitopeSitesInt = new int[epitopeSitesString.length];
		for (int i = 0; i < epitopeSitesString.length; i++) {
			epitopeSitesInt[i] = Integer.parseInt(epitopeSitesString[i].trim());
		}

		return epitopeSitesInt;
	}

	private static void writeEpitopeSitesFile(String outFile, int[] epitopeSites) {
		try {
			File out = new File(outFile);
			if (!out.exists()) {
				out.createNewFile();
			}
			FileWriter writer = new FileWriter(outFile);
			for (int i = 0; i < epitopeSites.length; i++) {
				if (i == epitopeSites.length) {
					writer.write(epitopeSites[i]);
				}
				else {
					writer.write(epitopeSites[i] + ",");
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Cannot write " + outFile);
		}
	}

	private static int[] toIntArray(List<Integer> list) {
		int[] ret = new int[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i);
		}
		return ret;
	}

	private static double[] toDoubleArray(List<Double> list) {
		double[] ret = new double[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i);
		}
		return ret;
	}

	private static String[] toStringArray(List<String> list) {
		String[] ret = new String[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i);
		}
		return ret;
	}
}
