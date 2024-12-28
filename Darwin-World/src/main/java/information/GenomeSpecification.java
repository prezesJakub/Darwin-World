package information;

import model.MutationType;

public record GenomeSpecification(
        int minMutations,
        int maxMutations,
        int genomeLength,
        MutationType mutationType
) {
}
