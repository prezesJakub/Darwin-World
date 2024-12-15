package information;

public record GenomeSpecification(
        int minMutations,
        int maxMutations,
        int genomeLength,
        int mutationType
) {
}
