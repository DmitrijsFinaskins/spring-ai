package dev.finashkin.springai;

import java.util.List;

public record CodeReview (
        String summary,
        List<String> issues,
        List<String> suggestions,
        int qualityScore
){}

