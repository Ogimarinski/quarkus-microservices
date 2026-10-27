package bg.project;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Product(
        Long id,
        String name,
        double price
) {
}
