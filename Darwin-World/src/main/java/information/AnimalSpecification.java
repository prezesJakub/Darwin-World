package information;

public record AnimalSpecification(
        int startingEnergy,
        int energyFromEating,
        int reproductionMinEnergy,
        int reproductionCost,
        GenomeSpecification genomeSpec
) {
}
